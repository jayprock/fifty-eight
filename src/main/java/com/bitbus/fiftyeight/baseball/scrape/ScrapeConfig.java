package com.bitbus.fiftyeight.baseball.scrape;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bitbus.fiftyeight.common.scrape.ScrapeProperties;

@Configuration
public class ScrapeConfig {

    @Autowired
    private ScrapeProperties scrapeProperties;

    @Bean(destroyMethod = "quit")
    public WebDriver getWebDriver() {
        System.setProperty(scrapeProperties.getChromeDriverLocationProperty(),
                scrapeProperties.getChromeDriverLocation());
        return new ChromeDriver();
    }

    @Bean
    public WebDriverWait getWebDriverWait() {
        return new WebDriverWait(getWebDriver(), 30);
    }

    @Bean
    public Actions getActions() {
        return new Actions(getWebDriver());
    }
}
