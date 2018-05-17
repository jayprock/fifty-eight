package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

@Component
public class BalkResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public BalkResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Balk");
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
