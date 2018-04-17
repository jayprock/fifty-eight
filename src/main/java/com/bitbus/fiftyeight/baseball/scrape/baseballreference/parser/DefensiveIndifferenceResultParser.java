package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

@Component
public class DefensiveIndifferenceResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public DefensiveIndifferenceResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Defensive Indifference");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) {
        return PlateAppearanceResultDTO.builder().notPlateAppearanceResult(true).build();
    }

}
