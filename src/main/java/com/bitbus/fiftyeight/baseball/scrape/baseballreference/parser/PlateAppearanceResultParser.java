package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.List;
import java.util.Optional;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;
import com.bitbus.fiftyeight.common.scrape.ex.ScrapeException;

public interface PlateAppearanceResultParser {

    List<String> getStartingWords();

    default Optional<List<String>> getExcludedStartingWords() {
        return Optional.empty();
    }

    default boolean isParserFor(String resultDescription) {
        if (getExcludedStartingWords().isPresent()) {
            for (String excludedWord : getExcludedStartingWords().get()) {
                if (resultDescription.startsWith(excludedWord)) {
                    return false;
                }
            }
        }
        for (String startingWord : getStartingWords()) {
            if (resultDescription.startsWith(startingWord)) {
                return true;
            }
        }
        return false;
    }

    PlateAppearanceResultDTO parse(String resultDescription) throws ScrapeException;
}
