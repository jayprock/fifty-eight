package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

@Component
public class BaserunnerAdvanceParser implements PlateAppearanceResultParser {

    List<String> startingWords;

    public BaserunnerAdvanceParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Baserunner Advance");
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
