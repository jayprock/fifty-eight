package com.bitbus.fiftyeight.baseball.scrape.baseballreference;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource("classpath:baseball/scrape/baseball-scrape.properties")
@ConfigurationProperties
@Data
public class BaseballScrapeProperties {

    private String scheduleUrl2017;
}
