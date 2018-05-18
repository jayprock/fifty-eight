package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;
import com.bitbus.fiftyeight.common.scrape.ex.ScrapeException;
import com.bitbus.fiftyeight.common.scrape.ex.WarningScrapeException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class WalkResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public WalkResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Walk");
        startingWords.add("Intentional Walk");
        startingWords.add("Hit By Pitch");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) throws ScrapeException {
        log.trace("Assessing the walk type");
        PlateAppearanceResult result;
        if (resultDescription.startsWith(startingWords.get(0))) {
            result = PlateAppearanceResult.WALK;
        } else if (resultDescription.startsWith(startingWords.get(1))) {
            result = PlateAppearanceResult.INTENTIONAL_WALK;
        } else if (resultDescription.startsWith(startingWords.get(2))) {
            result = PlateAppearanceResult.HIT_BY_PITCH;
        } else {
            throw new ScrapeException(
                    "Result description [" + resultDescription + "] cannot be mapped to a walk starting word");
        }
        log.trace("Walk type: " + result);

        log.trace("Determining if the walk resulted in an RBI");
        int runsScored = StringUtils.countMatches(resultDescription, "Scores");
        if (runsScored > 1) {
            log.warn("A walk resulted in more than 1 RBI. Review, something is probably wrong!");
            throw new WarningScrapeException(
                    "A walk resulted in more than 1 RBI. Review, something is probably wrong! Descrption: "
                            + resultDescription);
        }
        log.trace("RBIs assessed: " + runsScored);

        return PlateAppearanceResultDTO.builder() //
                .result(result) //
                .runsBattedIn(runsScored) //
                .build();
    }

}
