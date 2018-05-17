package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

@Component
public class CatchersInterferenceResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public CatchersInterferenceResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Reached on Interference on C");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) {
        return PlateAppearanceResultDTO.builder() //
                .result(PlateAppearanceResult.CATCHERS_INTERFERENCE) //
                .ballHitInPlay(false) //
                .qualifiedAtBat(false) //
                .build();
    }

}
