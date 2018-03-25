package com.bitbus.fiftyeight.common.scrape;

import java.util.Random;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScrapeSleeper {

    private static final Random RANDOM = new Random();

    public static void randomSleep() {
        try {
            int waitMillis = 1000 + RANDOM.nextInt(2000);
            log.debug("Adding random wait time of {} millis", waitMillis);
            Thread.sleep(waitMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
