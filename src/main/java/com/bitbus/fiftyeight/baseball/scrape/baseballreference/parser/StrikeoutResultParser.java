package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class StrikeoutResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public StrikeoutResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Strikeout");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) {
        PlateAppearanceResult result;
        log.trace("Determining if the batter struck out swinging or looking");
        if (resultDescription.startsWith("Strikeout Swinging")) {
            result = PlateAppearanceResult.STRIKEOUT_SWINGING;
        } else if (resultDescription.startsWith("Strikeout Looking")) {
            result = PlateAppearanceResult.STRIKEOUT_LOOKING;
        } else {
            throw new RuntimeException(
                    "Could not determine if strikeout was looking or swinging for description: " + resultDescription);
        }
        return PlateAppearanceResultDTO.builder(result) //
                .qualifiedAtBat(true) //
                .build();
    }

}
