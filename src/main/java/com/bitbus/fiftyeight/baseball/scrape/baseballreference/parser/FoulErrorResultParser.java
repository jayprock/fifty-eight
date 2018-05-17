package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

@Component
public class FoulErrorResultParser implements PlateAppearanceResultParser {

    @Override
    public List<String> getStartingWords() {
        return null;
    }

    @Override
    public boolean isParserFor(String resultDescription) {
        return resultDescription.startsWith("E") && resultDescription.contains(" on Foul Ball");
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) {
        return PlateAppearanceResultDTO.builder().notPlateAppearanceResult(true).build();
    }

}
