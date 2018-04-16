package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

@Component
public class StealResultParser implements PlateAppearanceResultParser {

    @Override
    public List<String> getStartingWords() {
        return null;
    }

    @Override
    public boolean isParserFor(String resultDescription) {
        return resultDescription.contains("Steals") || resultDescription.contains("Caught Stealing");
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) {
        return PlateAppearanceResultDTO.builder(null).notPlateAppearanceResult(true).build();
    }

}
