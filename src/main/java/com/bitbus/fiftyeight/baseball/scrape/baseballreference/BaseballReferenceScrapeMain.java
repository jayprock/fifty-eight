package com.bitbus.fiftyeight.baseball.scrape.baseballreference;

import static com.bitbus.fiftyeight.common.scrape.ScrapeSleeper.sleep;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.bitbus.fiftyeight.FiftyEightApplication;
import com.bitbus.fiftyeight.baseball.matchup.BaseballMatchupService;
import com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser.BaseballReferenceIdParser;
import com.bitbus.fiftyeight.common.Result;
import com.bitbus.fiftyeight.common.scrape.ScrapeResult;
import com.bitbus.fiftyeight.common.scrape.ex.ScrapeException;
import com.bitbus.fiftyeight.common.scrape.ex.WarningScrapeException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
@ComponentScan(basePackageClasses = FiftyEightApplication.class)
public class BaseballReferenceScrapeMain {

    @Autowired
    private BaseballReferenceScraper scraper;
    @Autowired
    private BaseballReferenceIdParser baseballReferenceIdParser;
    @Autowired
    private BaseballMatchupService matchupService;

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(BaseballReferenceScrapeMain.class, args);
        ctx.getBean(BaseballReferenceScrapeMain.class).performScrape();
    }

    private void performScrape() {
        List<ScrapeResult> errorResults = new ArrayList<>();
        int seleniumErrorCount = 0;
        boolean done = false;
        String mainWindowHandle = scraper.getMainWindowHandle();
        while (!done && seleniumErrorCount < 5) {
            try {
                scraper.closeExtraWindowHandles(mainWindowHandle);
                scraper.openPageWithBoxscores();
                log.info("Looking up the per day game blocks");
                List<WebElement> dailyBoxscoreBlocks = scraper.getDailyBoxscoreBlocks();
                for (WebElement dailyBoxscoreBlock : dailyBoxscoreBlocks) {
                    List<WebElement> boxscoreLinks = scraper.getBoxscoreLinks(dailyBoxscoreBlock);
                    for (WebElement boxscoreLink : boxscoreLinks) {
                        String matchupBaseballReferenceId =
                                baseballReferenceIdParser.parse(boxscoreLink.getAttribute("href"));
                        if (matchupService.matchupExistsForBaseballReferenceId(matchupBaseballReferenceId)) {
                            log.info("Matchup with ID {} has already been processed. Skipping.",
                                    matchupBaseballReferenceId);
                            continue;
                        }
                        try {
                            scraper.processBoxScore(boxscoreLink, mainWindowHandle, matchupBaseballReferenceId);
                        } catch (IllegalArgumentException | ScrapeException e) {
                            Result result;
                            if (e instanceof WarningScrapeException) {
                                result = Result.WARNING;
                            } else {
                                result = Result.FAILURE;
                            }
                            String message = result + ": " + e.getMessage();
                            errorResults.add(new ScrapeResult(result, message));
                        }
                    }
                }
                done = true;
            } catch (WebDriverException e) {
                log.error("Selenium WebDriverException during parsing", e);
                seleniumErrorCount++;
                log.info("Sleeping for 2 minutes to hopefully recover from Selenium error");
                sleep(2 * 60 * 1000);
            } catch (Exception e) {
                log.error("An error occurred during the baseball reference scrape", e);
                done = true;
            }
        }
        log.warn("Identified {} warnings or errors during the scrape process", errorResults.size());
        for (ScrapeResult errorResult : errorResults) {
            System.out.println(errorResult.getMessage());
        }
    }

}
