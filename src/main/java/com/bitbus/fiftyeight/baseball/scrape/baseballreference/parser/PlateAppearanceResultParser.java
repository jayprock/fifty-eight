package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.List;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

public interface PlateAppearanceResultParser {

    List<String> getStartingWords();

    default boolean isParserFor(String resultDescription) {
        for (String startingWord : getStartingWords()) {
            if (resultDescription.startsWith(startingWord)) {
                return true;
            }
        }
        return false;
    }

    PlateAppearanceResultDTO parse(String resultDescription);
}
