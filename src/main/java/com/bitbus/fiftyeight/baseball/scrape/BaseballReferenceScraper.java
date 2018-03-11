package com.bitbus.fiftyeight.baseball.scrape;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.bitbus.fiftyeight.FiftyEightApplication;
import com.bitbus.fiftyeight.baseball.matchup.BaseballMatchup;
import com.bitbus.fiftyeight.baseball.matchup.BaseballMatchupService;
import com.bitbus.fiftyeight.baseball.team.BaseballTeam;
import com.bitbus.fiftyeight.baseball.team.BaseballTeamService;
import com.bitbus.fiftyeight.common.scrape.ScrapeProperties;

@SpringBootApplication
@ComponentScan(basePackageClasses = FiftyEightApplication.class)
public class BaseballReferenceScraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseballReferenceScraper.class);

    private static final String OPEN_LINK_IN_NEW_TAB = Keys.chord(Keys.CONTROL, Keys.RETURN);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy h:m a", Locale.US);

    @Autowired
    private ScrapeProperties generalProperties;
    @Autowired
    private BaseballScrapeProperties baseballProperties;
    @Autowired
    private BaseballTeamService teamService;
    @Autowired
    private BaseballMatchupService matchupService;

    private WebDriver driver;
    private WebDriverWait wait;
    private Random random = new Random();
    private Map<String, BaseballTeam> teamNameMap = new HashMap<>();

    @PostConstruct
    private void init() {
        LOGGER.debug("Setting up Selenium dependencies.");
        System.setProperty(generalProperties.getChromeDriverLocationProperty(),
                generalProperties.getChromeDriverLocation());

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 30);

        LOGGER.debug("Getting all baseball teams.");
        List<BaseballTeam> baseballTeams = teamService.findAll();
        System.out.println(baseballTeams);
        LOGGER.debug("Storing teams in map by name.");
        for (BaseballTeam baseballTeam : baseballTeams) {
            teamNameMap.put(baseballTeam.getFullName(), baseballTeam);
        }
    }

    private void performScrape() {
        LOGGER.info("Opening the baseball reference page for the 2017 schedule.");
        driver.get(baseballProperties.getScheduleUrl2017());
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("game")));
        randomWaitTime();

        LOGGER.info("Looking up the per day game blocks");
        List<WebElement> dailyGameBlocks = driver.findElements(By.xpath("//div[@class='section_content']/div"));
        for (WebElement dailyGameBlock : dailyGameBlocks) {

            String gameBlockDate = dailyGameBlock.findElement(By.tagName("h3")).getText();
            LOGGER.info("Processing the games on {}", gameBlockDate);

            String mainTabHandle = driver.getWindowHandle();
            List<WebElement> boxscoreLinks = dailyGameBlock.findElements(By.linkText("Boxscore"));
            for (WebElement boxscoreLink : boxscoreLinks) {

                String matchupBaseballReferenceId = getBaseballReferenceIdForMatchup(boxscoreLink);
                if (matchupService.matchupExistsForBaseballReferenceId(matchupBaseballReferenceId)) {
                    LOGGER.info("Matchup with ID {} has already been processed. Skipping.", matchupBaseballReferenceId);
                    continue;
                }

                processBoxScore(boxscoreLink, mainTabHandle, matchupBaseballReferenceId);
            }
        }

    }

    @Transactional
    private void processBoxScore(WebElement boxscoreLink, String mainTabHandle, String matchupBaseballReferenceId) {
        LOGGER.debug("Opening boxscore in a new tab, then scraping that page.");
        boxscoreLink.sendKeys(OPEN_LINK_IN_NEW_TAB);
        String newTabHandle = driver.getWindowHandles() //
                .stream() //
                .filter(handle -> !handle.equals(mainTabHandle)) //
                .findFirst() //
                .get();
        driver.switchTo().window(newTabHandle);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("top_inning")));


        String description = driver.findElement(By.xpath("//div[@id='content']/h1")).getText();
        LOGGER.info("Processing game details for {}", description);

        String[] descriptionComponents = description.split("\\sat\\s|\\sBox\\sScore,\\s");
        if (descriptionComponents.length != 3) {
            LOGGER.error(
                    "The Box Score description could not be parsed as expected.  Any scraping could lead to unexpected results.");
            throw new RuntimeException("Unexpected box score description format");
        }

        BaseballTeam awayTeam = teamNameMap.get(descriptionComponents[0]);
        BaseballTeam homeTeam = teamNameMap.get(descriptionComponents[1]);
        if (awayTeam == null || homeTeam == null) {
            LOGGER.error("Could not map {} or {} to a persisted team.", descriptionComponents[0],
                    descriptionComponents[1]);
            throw new RuntimeException("Unable to identify team.");
        }

        createMatchup(homeTeam, awayTeam, descriptionComponents[2], matchupBaseballReferenceId);

        createPlayersInMatchup();


        // TODO - Get player data

        // TODO - Get starting lineup

        // TODO - Get game play-by-play

        LOGGER.debug("Done scraping game data, closing tab.");
        randomWaitTime();
        driver.close();
        driver.switchTo().window(mainTabHandle);
    }

    private BaseballMatchup createMatchup(BaseballTeam homeTeam, BaseballTeam awayTeam, String date,
            String baseballReferenceId) {

        LOGGER.info("Create a new matchup with id {}.", baseballReferenceId);
        BaseballMatchup matchup = new BaseballMatchup();
        matchup.setBaseballReferenceId(baseballReferenceId);
        matchup.setHomeTeam(homeTeam);
        matchup.setAwayTeam(awayTeam);

        LOGGER.debug("Parsing the game date/time data");
        String[] timeComponents = driver.findElement(By.xpath("//div[@class='scorebox_meta']/div[2]"))
                .getText() //
                .split("\\s");
        String time = timeComponents[2];
        String ampm = timeComponents[3].equals("a.m.") ? "AM" : "PM";
        LocalDateTime gameDateTime = LocalDateTime.parse(date + " " + time + " " + ampm, FORMATTER) //
                .atZone(ZoneId.of(homeTeam.getTimezone())) //
                .withZoneSameInstant(ZoneId.systemDefault()) //
                .toLocalDateTime();
        matchup.setGameDateTime(gameDateTime);
        LOGGER.debug("Game time at default timezone is {}", gameDateTime);

        LOGGER.debug("Getting game result data");
        List<WebElement> scoreElements = driver.findElements(By.className("score"));
        if (scoreElements.size() != 2) {
            LOGGER.error("Unexpected results searching for score data. Found {} elements", scoreElements.size());
            throw new RuntimeException("Unexpected score data.");
        }
        int awayTeamScore = Integer.parseInt(scoreElements.get(0).getText());
        int homeTeamScore = Integer.parseInt(scoreElements.get(1).getText());
        matchup.setAwayTeamScore(awayTeamScore);
        matchup.setHomeTeamScore(homeTeamScore);

        LOGGER.debug("Saving the matchup data");
        return matchupService.save(matchup);
    }

    /**
     * Create the players in the matchup, if they do not already exist
     */
    private void createPlayersInMatchup() {

        List<WebElement> starters = driver.findElements(By.xpath("//div[@id='div_lineups']//a"));
        for (WebElement starter : starters) {

        }

    }

    private String getBaseballReferenceIdForMatchup(WebElement boxscoreLink) {
        String[] boxscoreLinkComponents = boxscoreLink.getAttribute("href").split("\\/|\\.");
        return boxscoreLinkComponents[boxscoreLinkComponents.length - 2];
    }

    /**
     * @return random wait time between 1 and 5 seconds
     */
    private void randomWaitTime() {
        try {
            int waitMillis = 1000 + random.nextInt(2000);
            LOGGER.debug("Adding random wait time of {} millis", waitMillis);
            Thread.sleep(waitMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(BaseballReferenceScraper.class, args);
        BaseballReferenceScraper baseballReferenceScrape = ctx.getBean(BaseballReferenceScraper.class);
        baseballReferenceScrape.performScrape();
    }

}
