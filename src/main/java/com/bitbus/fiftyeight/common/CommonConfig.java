package com.bitbus.fiftyeight.common;

import java.text.Collator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    public Collator nameCollator() {
        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.PRIMARY);
        return collator;
    }
}
