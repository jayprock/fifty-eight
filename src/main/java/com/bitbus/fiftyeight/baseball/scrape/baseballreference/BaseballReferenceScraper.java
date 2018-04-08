package com.bitbus.fiftyeight.baseball.scrape.baseballreference;

import static com.bitbus.fiftyeight.common.scrape.ScrapeSleeper.randomSleep;
import static com.bitbus.fiftyeight.common.scrape.ScrapeSleeper.sleep;

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
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitbus.fiftyeight.baseball.matchup.BaseballMatchup;
import com.bitbus.fiftyeight.baseball.matchup.BaseballMatchupService;
import com.bitbus.fiftyeight.baseball.matchup.BaseballPlayerPosition;
import com.bitbus.fiftyeight.baseball.player.BaseballPlayer;
import com.bitbus.fiftyeight.baseball.player.BaseballPlayerService;
import com.bitbus.fiftyeight.baseball.player.BaseballPosition;
import com.bitbus.fiftyeight.baseball.player.BatterType;
import com.bitbus.fiftyeight.baseball.player.plateappearance.InningType;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PitchResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearance;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;
import com.bitbus.fiftyeight.baseball.player.plateappearance.RunnersOnBase;
import com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser.PlateAppearanceResultParser;
import com.bitbus.fiftyeight.baseball.starter.BaseballGameStarterService;
import com.bitbus.fiftyeight.baseball.starter.BaseballPlayerStarterDTO;
import com.bitbus.fiftyeight.baseball.team.BaseballTeam;
import com.bitbus.fiftyeight.baseball.team.BaseballTeamService;
import com.bitbus.fiftyeight.common.player.DominateHand;
import com.bitbus.fiftyeight.common.player.HeightUnit;
import com.bitbus.fiftyeight.common.player.WeightUnit;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class BaseballReferenceScraper {

    private static final String OPEN_LINK_IN_NEW_TAB = Keys.chord(Keys.CONTROL, Keys.RETURN);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy h:m a", Locale.US);

    @Autowired
    private WebDriver driver;
    @Autowired
    private WebDriverWait wait;
    @Autowired
    private Actions actions;
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
    @Autowired
    private List<PlateAppearanceResultParser> plateAppearanceResultParsers;

    private Map<String, BaseballTeam> teamNameMap = new HashMap<>();

    @PostConstruct
    private void init() {
        log.debug("Getting all baseball teams.");
        List<BaseballTeam> baseballTeams = teamService.findAll();
        System.out.println(baseballTeams);
        log.debug("Storing teams in map by name.");
        for (BaseballTeam baseballTeam : baseballTeams) {
            teamNameMap.put(baseballTeam.getFullName(), baseballTeam);
        }
    }

    /**
     * @return tab handle for boxscore page
     */
    public String openPageWithBoxscores() {
        log.info("Opening the baseball reference page for the 2017 schedule.");
        driver.get(baseballProperties.getScheduleUrl2017());
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("game")));
        return driver.getWindowHandle();
    }

    public List<WebElement> getDailyBoxscoreBlocks() {
        log.info("Looking up the per day game blocks");
        return driver.findElements(By.xpath("//div[@class='section_content']/div"));
    }

    public List<WebElement> getBoxscoreLinks(WebElement dailyBoxscoreBlock) {
        String gameBlockDate = dailyBoxscoreBlock.findElement(By.tagName("h3")).getText();
        log.info("Getting boxscore links for games on {}", gameBlockDate);
        return dailyBoxscoreBlock.findElements(By.linkText("Boxscore"));
    }

    @Transactional
    public void processBoxScore(WebElement boxscoreLink, String mainTabHandle, String matchupBaseballReferenceId) {
        log.debug("Opening boxscore in a new tab, then scraping that page.");
        boxscoreLink.sendKeys(OPEN_LINK_IN_NEW_TAB);
        String boxscoreTabHandle = driver.getWindowHandles() //
                .stream() //
                .filter(handle -> !handle.equals(mainTabHandle)) //
                .findFirst() //
                .get();
        driver.switchTo().window(boxscoreTabHandle);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("top_inning")));


        String description = driver.findElement(By.xpath("//div[@id='content']/h1")).getText();
        log.info("Processing game details for {}", description);

        String[] descriptionComponents = description.split("\\sat\\s|\\sBox\\sScore,\\s");
        if (descriptionComponents.length != 3) {
            log.error(
                    "The Box Score description could not be parsed as expected.  Any scraping could lead to unexpected results.");
            throw new RuntimeException("Unexpected box score description format");
        }
        BaseballTeam awayTeam = teamNameMap.get(descriptionComponents[0]);
        BaseballTeam homeTeam = teamNameMap.get(descriptionComponents[1]);
        if (awayTeam == null || homeTeam == null) {
            log.error("Could not map {} or {} to a persisted team.", descriptionComponents[0],
                    descriptionComponents[1]);
            throw new RuntimeException("Unable to identify team.");
        }
        createPlateAppearances(null);

        BaseballMatchup matchup =
                createMatchup(homeTeam, awayTeam, descriptionComponents[2], matchupBaseballReferenceId);

        List<BaseballPlayer> playersInMatchup = createPlayers(matchup, mainTabHandle, boxscoreTabHandle);

        createStarters(playersInMatchup, matchup);

        // TODO - Get game play-by-play

        log.debug("Done scraping game data, closing tab.");
        randomSleep();
        driver.close();
        driver.switchTo().window(mainTabHandle);
    }

    private BaseballMatchup createMatchup(BaseballTeam homeTeam, BaseballTeam awayTeam, String date,
            String baseballReferenceId) {

        log.info("Create a new matchup with id {}.", baseballReferenceId);
        BaseballMatchup matchup = new BaseballMatchup();
        matchup.setBaseballReferenceId(baseballReferenceId);
        matchup.setHomeTeam(homeTeam);
        matchup.setAwayTeam(awayTeam);

        log.debug("Parsing the game date/time data");
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
        log.debug("Game time at default timezone is {}", gameDateTime);

        log.debug("Getting game result data");
        List<WebElement> scoreElements = driver.findElements(By.className("score"));
        if (scoreElements.size() != 2) {
            log.error("Unexpected results searching for score data. Found {} elements", scoreElements.size());
            throw new RuntimeException("Unexpected score data.");
        }
        int awayTeamScore = Integer.parseInt(scoreElements.get(0).getText());
        int homeTeamScore = Integer.parseInt(scoreElements.get(1).getText());
        matchup.setAwayTeamScore(awayTeamScore);
        matchup.setHomeTeamScore(homeTeamScore);

        log.debug("Saving the matchup data");
        return matchupService.save(matchup);
    }

    private List<BaseballPlayer> createPlayers(BaseballMatchup matchup, String mainTabHandle,
            String boxscoreTabHandle) {
        List<BaseballPlayer> players = new ArrayList<>();

        log.debug("Attempting to create away team players.");
        List<BaseballPlayer> awayTeamPlayers =
                createPlayersForTeam(matchup.getAwayTeam(), mainTabHandle, boxscoreTabHandle);
        players.addAll(awayTeamPlayers);

        log.debug("Attempting to create home team players.");
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
                    log.debug(
                            "Player already found with ID {} but new position(s) exist. Only creating these positions: {}",
                            playerBaseballReferenceId, newPositions);
                    player.getPlayerPositions().addAll(newPositions);
                } else {
                    log.debug("Player already found with ID {}. Will not attempt to create.",
                            playerBaseballReferenceId);
                }
                continue;
            }
            randomSleep();
            log.debug("Need to get player data for {} with ID {}. Opening player page in a new tab.",
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

            log.debug("Getting player metadata");
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
        log.debug("Saving game starters");
        baseballGameStarterService.save(startingPlayerDTOs, matchup);
    }

    private void createPlateAppearances(List<BaseballPlayer> players) {
        log.debug("Processing the matchup play by play data");
        WebElement viewPitchesButton = driver.findElement(By.xpath("//span[text()='View Pitches']/.."));
        actions.moveToElement(viewPitchesButton).perform();
        sleep(500);
        viewPitchesButton.click();
        List<WebElement> plateAppearanceRows = driver.findElements(By.xpath(
                "//table[@id='play_by_play']/descendant::tr[contains(@class,'top_inning') or contains(@class,'bottom_inning')]"));
        for (WebElement plateAppearanceRow : plateAppearanceRows) {
            createPlateAppearance(plateAppearanceRow, players);
        }

    }

    private void createPlateAppearance(WebElement plateAppearanceRow, List<BaseballPlayer> players) {
        PlateAppearance plateAppearance = new PlateAppearance();
        log.trace("Getting the inning data");
        String inningText = plateAppearanceRow.findElement(By.tagName("th")).getText();
        plateAppearance.setInning(Character.getNumericValue(inningText.charAt(1)));
        plateAppearance.setInningType(InningType.findByLookupId(inningText.charAt(0)));

        log.trace("Getting all other table columns for this plate appearance");
        List<WebElement> columns = plateAppearanceRow.findElements(By.tagName("td"));

        log.trace("Attempting to parse the outs total");
        plateAppearance.setOutsExisting(Integer.parseInt(columns.get(1).getText()));

        log.trace("Determining the runners on base");
        plateAppearance.setRunnersOnBase(RunnersOnBase.findByCode(columns.get(2).getText()));

        log.trace("Parsing pitch count");
        String pitchCountDetails = columns.get(3).getText();
        plateAppearance.setPitchTotal(Integer.parseInt(pitchCountDetails.split(",")[0]));

        log.trace("Determining the entire pitch sequence");
        String pitchSequenceCode = pitchCountDetails.split("\\s")[1] //
                .replaceAll("V", "B") //
                .replaceAll("Y", "X") //
                .replaceAll("\\*|>|\\+|\\.|[123]", "");
        if (pitchSequenceCode.length() != plateAppearance.getPitchTotal()) {
            throw new RuntimeException(
                    "Inconsistent pitch information found in row with text: " + plateAppearanceRow.getText());
        }
        List<PitchResult> pitchResults = pitchSequenceCode.chars() //
                .mapToObj(c -> new PitchResult((char) c, plateAppearance)) //
                .collect(Collectors.toList());
        plateAppearance.setPitchResults(pitchResults);

        log.trace("Get the resulting outs and runs scored from the plate appearance");
        String runsScoredOutsMadeCode = columns.get(4).getText();
        int outsResult = runsScoredOutsMadeCode.replaceAll("R", "").length();
        plateAppearance.setOutsResult(outsResult);
        int runsResult = runsScoredOutsMadeCode.replaceAll("O", "").length();
        plateAppearance.setRunsResult(runsResult);

        log.trace("Assessing the batter");
        String batterName = columns.get(6).getText();
        BaseballPlayer batter = players.stream() //
                .filter(player -> player.getFullName().equals(batterName)) //
                .findFirst() //
                .get();
        plateAppearance.setBatter(batter);

        log.trace("Assessing the pitcher");
        String pitcherName = columns.get(7).getText();
        BaseballPlayer pitcher = players.stream() //
                .filter(player -> player.getFullName().equals(pitcherName)) //
                .findFirst() //
                .get();
        plateAppearance.setPitcher(pitcher);

        log.trace("Looking up a parser for the plate appearance description");
        String plateAppearanceDescription = columns.get(10).getText();
        PlateAppearanceResultParser resultParser = plateAppearanceResultParsers.stream() //
                .filter(parser -> parser.isParserFor(plateAppearanceDescription)) //
                .findFirst() //
                .orElseThrow(() -> new RuntimeException(
                        "Could not find a parser for plate appearance description: " + plateAppearanceDescription));
        log.trace("Found parser [{}] for plate appearance description [{}]", resultParser.getClass().getName(),
                plateAppearanceDescription);
        PlateAppearanceResultDTO resultDTO = resultParser.parse(plateAppearanceDescription);
        plateAppearance.setResultType(resultDTO.getResult());
        plateAppearance.setQualifiedAtBat(resultDTO.isQualifiedAtBat());
        plateAppearance.setResultsInHit(resultDTO.isHit());
        plateAppearance.setHitType(resultDTO.getHitType());
        plateAppearance.setHitLocation(resultDTO.getHitLocation());
        plateAppearance.setRunsBattedIn(resultDTO.getRunsBattedIn());
        // TODO - handle stolen base scenario (2 rows) -- When assessing the result, just skip if a
        // stolen base
        // TODO - track post-hit activities (e.g. did the runner score, stolen bases, etc)
    }


    public String getBaseballReferenceId(WebElement baseballReferenceLink) {
        String[] linkComponents = baseballReferenceLink.getAttribute("href").split("\\/|\\.");
        return linkComponents[linkComponents.length - 2];
    }

}
