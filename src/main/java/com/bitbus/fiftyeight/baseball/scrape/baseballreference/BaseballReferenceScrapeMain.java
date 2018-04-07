package com.bitbus.fiftyeight.baseball.scrape.baseballreference;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.bitbus.fiftyeight.FiftyEightApplication;
import com.bitbus.fiftyeight.baseball.matchup.BaseballMatchupService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
@ComponentScan(basePackageClasses = FiftyEightApplication.class)
public class BaseballReferenceScrapeMain {

    @Autowired
    private BaseballReferenceScraper scraper;
    @Autowired
    private BaseballMatchupService matchupService;

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(BaseballReferenceScrapeMain.class, args);
        ctx.getBean(BaseballReferenceScrapeMain.class).performScrape();
    }

    private void performScrape() {
        String mainTabHandle = scraper.openPageWithBoxscores();
        log.info("Looking up the per day game blocks");
        List<WebElement> dailyBoxscoreBlocks = scraper.getDailyBoxscoreBlocks();
        for (WebElement dailyBoxscoreBlock : dailyBoxscoreBlocks) {
            List<WebElement> boxscoreLinks = scraper.getBoxscoreLinks(dailyBoxscoreBlock);
            for (WebElement boxscoreLink : boxscoreLinks) {
                String matchupBaseballReferenceId = scraper.getBaseballReferenceId(boxscoreLink);
                if (matchupService.matchupExistsForBaseballReferenceId(matchupBaseballReferenceId)) {
                    log.info("Matchup with ID {} has already been processed. Skipping.", matchupBaseballReferenceId);
                    continue;
                }
                scraper.processBoxScore(boxscoreLink, mainTabHandle, matchupBaseballReferenceId);
            }
        }
    }

}
