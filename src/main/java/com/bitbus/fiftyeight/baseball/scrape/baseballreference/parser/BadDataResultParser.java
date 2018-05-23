package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;
import com.bitbus.fiftyeight.common.scrape.ex.ScrapeException;

@Component
public class BadDataResultParser implements PlateAppearanceResultParser {

    @Override
    public List<String> getStartingWords() {
        return null;
    }

    @Override
    public boolean isParserFor(String resultDescription) {
        return "Double".equals(resultDescription);
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) throws ScrapeException {
        if ("Double".equals(resultDescription)) {
            return PlateAppearanceResultDTO.builder()
                    .ballHitInPlay(true)
                    .hit(true)
                    .qualifiedAtBat(true)
                    .result(PlateAppearanceResult.DOUBLE)
                    .build();
        }
        return null;
    }

}
