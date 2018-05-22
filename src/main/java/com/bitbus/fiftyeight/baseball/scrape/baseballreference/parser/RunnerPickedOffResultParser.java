package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

@Component
public class RunnerPickedOffResultParser implements PlateAppearanceResultParser {

    @Override
    public List<String> getStartingWords() {
        return null;
    }

    @Override
    public boolean isParserFor(String resultDescription) {
        return resultDescription.contains("Picked off");
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) {
        return PlateAppearanceResultDTO.builder().notPlateAppearanceResult(true).build();
    }

}