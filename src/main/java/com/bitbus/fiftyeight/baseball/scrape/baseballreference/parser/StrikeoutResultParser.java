package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;
import com.bitbus.fiftyeight.common.scrape.ex.ScrapeException;
import com.bitbus.fiftyeight.common.scrape.ex.WarningScrapeException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class StrikeoutResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public StrikeoutResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Strikeout");
        startingWords.add("Double Play: Strikeout");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) throws ScrapeException {
        PlateAppearanceResult result;
        log.trace("Determining if the batter struck out swinging or looking");
        if (resultDescription.contains("Strikeout Swinging")) {
            result = PlateAppearanceResult.STRIKEOUT_SWINGING;
        } else if (resultDescription.contains("Strikeout Looking")) {
            result = PlateAppearanceResult.STRIKEOUT_LOOKING;
        } else if (resultDescription.contains("bunt")) {
            result = PlateAppearanceResult.STRIKEOUT_BUNTING;
        } else {
            result = PlateAppearanceResult.STRIKEOUT_SWINGING;
            log.warn("Cannot determine type of strikeout. Defaulting to a strikeout swinging");
            throw new WarningScrapeException(
                    "Cannot determine type of strikeout. Defaulting to a strikeout swinging. Review description: "
                            + resultDescription);
        }
        return PlateAppearanceResultDTO.builder() //
                .result(result) //
                .qualifiedAtBat(true) //
                .build();
    }

}
