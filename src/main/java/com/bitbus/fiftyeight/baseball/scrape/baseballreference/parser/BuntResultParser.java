package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bitbus.fiftyeight.baseball.player.plateappearance.HitLocation;
import com.bitbus.fiftyeight.baseball.player.plateappearance.HitType;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;
import com.bitbus.fiftyeight.common.scrape.ex.ScrapeException;
import com.bitbus.fiftyeight.common.scrape.ex.WarningScrapeException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class BuntResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public BuntResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Bunt");
        startingWords.add("Foul Bunt");
        startingWords.add("Double Play: Bunt");
        startingWords.add("Ground Ball Double Play: Bunt");
        startingWords.add("Double Play: Foul Bunt");
    }


    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) throws ScrapeException {
        log.trace("Assessing bunt result");
        // Only bunt outs are handled, a bunt for a hit is just treated as a hit
        PlateAppearanceResult result = resultDescription.contains("Sacrifice") //
                ? PlateAppearanceResult.SAC_BUNT
                : PlateAppearanceResult.BALL_IN_PLAY_OUT;
        log.trace("Bunt result determined to be: " + result);

        log.trace("Determining the location of the bunt");
        HitLocation hitLocation;
        String[] descriptionParts = resultDescription.split("\\(|\\)");
        boolean disallowRBI = false;
        if (descriptionParts.length == 1) {
            if (resultDescription.contains("Double Play:")) {
                hitLocation = HitLocation.findByDisplayName(resultDescription.split(":\\sBunt\\s|-")[1]);
                disallowRBI = true;
            } else {
                hitLocation = HitLocation.findByDisplayName(resultDescription.split("\\s|-")[2]);
            }
        } else {
            hitLocation = HitLocation.findByDisplayName(descriptionParts[1].replace(" Hole", ""));
        }
        log.trace("Location of bunt: " + hitLocation);

        log.trace("Checking for an RBI");
        int runsScored = StringUtils.countMatches(resultDescription, "Scores");
        if (runsScored > 1) {
            log.warn("More than 1 run scored on a bunt. This probably is not handled correctly!");
            throw new WarningScrapeException(
                    "More than 1 run scored on a bunt. This probably is not handled correctly! Description: "
                            + resultDescription);
        }
        int rbis = disallowRBI ? 0 : runsScored;
        log.trace("RBIs assessed: " + rbis);

        return PlateAppearanceResultDTO.builder() //
                .result(result) //
                .hitLocation(hitLocation) //
                .hitType(HitType.BUNT) //
                .qualifiedAtBat(result == PlateAppearanceResult.BALL_IN_PLAY_OUT)
                .ballHitInPlay(true) //
                .runsBattedIn(rbis) //
                .build();
    }

}
