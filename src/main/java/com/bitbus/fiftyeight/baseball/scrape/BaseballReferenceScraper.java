package com.bitbus.fiftyeight.baseball.scrape;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
import com.bitbus.fiftyeight.baseball.matchup.BaseballPlayerPosition;
import com.bitbus.fiftyeight.baseball.player.BaseballPlayer;
import com.bitbus.fiftyeight.baseball.player.BaseballPlayerService;
import com.bitbus.fiftyeight.baseball.player.BaseballPosition;
import com.bitbus.fiftyeight.baseball.player.BatterType;
import com.bitbus.fiftyeight.baseball.starter.BaseballGameStarterService;
import com.bitbus.fiftyeight.baseball.starter.BaseballPlayerStarterDTO;
import com.bitbus.fiftyeight.baseball.team.BaseballTeam;
import com.bitbus.fiftyeight.baseball.team.BaseballTeamService;
import com.bitbus.fiftyeight.common.player.DominateHand;
import com.bitbus.fiftyeight.common.player.HeightUnit;
import com.bitbus.fiftyeight.common.player.WeightUnit;
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
    @Autowired
    private BaseballPlayerService playerService;
    @Autowired
    private BaseballGameStarterService baseballGameStarterService;

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

                String matchupBaseballReferenceId = getBaseballReferenceId(boxscoreLink);
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
        String boxscoreTabHandle = driver.getWindowHandles() //
                .stream() //
                .filter(handle -> !handle.equals(mainTabHandle)) //
                .findFirst() //
                .get();
        driver.switchTo().window(boxscoreTabHandle);
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

        BaseballMatchup matchup =
                createMatchup(homeTeam, awayTeam, descriptionComponents[2], matchupBaseballReferenceId);

        List<BaseballPlayer> playersInMatchup = createPlayers(matchup, mainTabHandle, boxscoreTabHandle);

        createStarters(playersInMatchup, matchup);

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

    private List<BaseballPlayer> createPlayers(BaseballMatchup matchup, String mainTabHandle,
            String boxscoreTabHandle) {
        List<BaseballPlayer> players = new ArrayList<>();

        LOGGER.debug("Attempting to create away team players.");
        List<BaseballPlayer> awayTeamPlayers =
                createPlayersForTeam(matchup.getAwayTeam(), mainTabHandle, boxscoreTabHandle);
        players.addAll(awayTeamPlayers);

        LOGGER.debug("Attempting to create home team players.");
        List<BaseballPlayer> homeTeamPlayers =
                createPlayersForTeam(matchup.getHomeTeam(), mainTabHandle, boxscoreTabHandle);
        players.addAll(homeTeamPlayers);

        return players;
    }

    private List<BaseballPlayer> createPlayersForTeam(BaseballTeam team, String mainTabHandle,
            String boxscoreTabHandle) {
        List<BaseballPlayer> players = new ArrayList<>();
        String teamBattingTableId = team.getFullName().replaceAll("\\s|\\.", "") + "batting";
        List<WebElement> playerLinks = driver.findElement(By.id(teamBattingTableId)).findElements(By.tagName("a"));
        for (WebElement playerLink : playerLinks) {
            String[] playerTextComponents = playerLink.findElement(By.xpath("..")).getText().split("\\s");
            List<BaseballPosition> positions =
                    Arrays.asList(playerTextComponents[playerTextComponents.length - 1].split("-")) //
                            .stream() //
                            .filter(positionText -> !positionText.equals("PH"))
                            .filter(positionText -> !positionText.equals("PR"))
                            .map(positionText -> BaseballPosition.findByPositionId(positionText))
                            .collect(Collectors.toList());
            String playerBaseballReferenceId = getBaseballReferenceId(playerLink);
            Optional<BaseballPlayer> playerOpt = playerService.findByBaseballReferenceId(playerBaseballReferenceId);
            if (playerOpt.isPresent()) {
                BaseballPlayer player = playerOpt.get();
                List<BaseballPosition> existingPositions = player.getPlayerPositions() //
                        .stream() //
                        .map(position -> position.getPosition()) //
                        .collect(Collectors.toList());
                List<BaseballPlayerPosition> newPositions = positions.stream() //
                        .filter(position -> !existingPositions.contains(position)) //
                        .map(position -> new BaseballPlayerPosition(position, playerOpt.get()))
                        .collect(Collectors.toList());
                if (newPositions.size() > 0) {
                    LOGGER.debug(
                            "Player already found with ID {} but new position(s) exist. Only creating these positions: {}",
                            playerBaseballReferenceId, newPositions);
                    player.getPlayerPositions().addAll(newPositions);
                } else {
                    LOGGER.debug("Player already found with ID {}. Will not attempt to create.",
                            playerBaseballReferenceId);
                }
                continue;
            }
            randomWaitTime();
            LOGGER.debug("Need to get player data for {} with ID {}. Opening player page in a new tab.",
                    playerLink.getText(), playerBaseballReferenceId);
            playerLink.sendKeys(OPEN_LINK_IN_NEW_TAB);
            String playerTabHandle = driver.getWindowHandles()
                    .stream() //
                    .filter(handleName -> !handleName.equals(mainTabHandle) && !handleName.equals(boxscoreTabHandle)) //
                    .findFirst() //
                    .get();
            driver.switchTo().window(playerTabHandle);
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("section_heading")));

            BaseballPlayer player = new BaseballPlayer();
            player.setBaseballReferenceId(playerBaseballReferenceId);
            List<BaseballPlayerPosition> playerPositions = positions.stream() //
                    .map(position -> new BaseballPlayerPosition(position, player)) //
                    .collect(Collectors.toList());
            player.getPlayerPositions().addAll(playerPositions);

            LOGGER.debug("Getting player metadata");
            WebElement playerMetaElement = driver.findElement(By.id("meta"));
            String fullName = playerMetaElement.findElement(By.tagName("h1")).getText();
            String[] names = fullName.split("\\s");
            player.setFirstName(names[0]);
            player.setLastName(names[names.length - 1]);
            if (names.length > 2) {
                String[] ambiguousNames = Arrays.copyOfRange(names, 1, names.length - 1);
                player.setAmbiguousName(String.join(" ", ambiguousNames));
            }
            List<WebElement> metaBlocks = playerMetaElement.findElements(By.tagName("p"));
            for (WebElement metaBlock : metaBlocks) {
                String label = metaBlock.findElement(By.tagName("strong")).getText().trim();
                if (label.equals("Bats:")) {
                    String[] metaComponents = metaBlock.getText().split("\\s");
                    player.setBatsFrom(BatterType.findByLookupName(metaComponents[1]));
                    player.setThrowsFrom(DominateHand.findByLookupName(metaComponents[6]));
                    String size = metaBlock.findElement(By.xpath("following-sibling::p")).getText();
                    String[] sizeElements = size.split("-|,\\s|lb");
                    int heightInches = Integer.valueOf(sizeElements[0]) * 12 + Integer.valueOf(sizeElements[1]);
                    player.setHeight(heightInches);
                    player.setHeightUnit(HeightUnit.IN);
                    player.setWeight(Double.valueOf(sizeElements[2]));
                    player.setWeightUnit(WeightUnit.LB);
                    break;
                }
            }
            playerService.save(player);
            players.add(player);
            driver.close();
            driver.switchTo().window(boxscoreTabHandle);
        }
        return players;
    }

    private void createStarters(List<BaseballPlayer> players, BaseballMatchup matchup) {
        List<BaseballPlayerStarterDTO> startingPlayerDTOs = new ArrayList<>();
        List<WebElement> starterLinks = driver.findElements(By.xpath("//div[@id='div_lineups']//a"));
        for (WebElement starterLink : starterLinks) {
            BaseballPlayerStarterDTO startingPlayerDTO = new BaseballPlayerStarterDTO();
            Integer battingOrderPosition;
            try {
                battingOrderPosition =
                        Integer.parseInt(starterLink.findElement(By.xpath("../preceding-sibling::td")).getText());
            } catch (NumberFormatException e) {
                battingOrderPosition = null;
            }
            startingPlayerDTO.setBattingOrderPosition(battingOrderPosition);
            BaseballPosition fieldingPosition = BaseballPosition
                    .findByPositionId(starterLink.findElement(By.xpath("../following-sibling::td")).getText());
            startingPlayerDTO.setFieldingPosition(fieldingPosition);
            String playerBaseballReferenceId = getBaseballReferenceId(starterLink);
            BaseballPlayer startingPlayer = players.stream() //
                    .filter(player -> player.getBaseballReferenceId().equals(playerBaseballReferenceId)) //
                    .findFirst() //
                    .get();
            startingPlayerDTO.setPlayer(startingPlayer);
            startingPlayerDTOs.add(startingPlayerDTO);
        }
        LOGGER.debug("Saving game starters");
        baseballGameStarterService.save(startingPlayerDTOs, matchup);
    }

    private String getBaseballReferenceId(WebElement baseballReferenceLink) {
        String[] linkComponents = baseballReferenceLink.getAttribute("href").split("\\/|\\.");
        return linkComponents[linkComponents.length - 2];
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


    @PreDestroy
    private void destroy() {
        driver.quit();
    }

}
