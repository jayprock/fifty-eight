package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.ArrayList;
import java.util.List;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

public class WildPitchResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public WildPitchResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Wild Pitch");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) {
        return PlateAppearanceResultDTO.builder(null).notPlateAppearanceResult(true).build();
    }

}
