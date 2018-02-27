package com.bitbus.fiftyeight.common.scrape;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource("classpath:common/scrape/scrape.properties")
@ConfigurationProperties
@Data
public class ScrapeProperties {

    private String chromeDriverLocationProperty;
    private String chromeDriverLocation;
}
