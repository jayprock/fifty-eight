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

@Component
@Log4j2
public class FieldersChoiceResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public FieldersChoiceResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Fielder's Choice");
        startingWords.add("Double Play: Fielder's Choice");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) throws ScrapeException {
        log.trace("Assessing Fielders Choice result, hitType, and if its a qualified at bat");
        PlateAppearanceResult result;
        HitType hitType;
        boolean atBat;
        // Fielder's choice can be bunt or groundout
        if (resultDescription.contains("Sacrifice Bunt")) {
            result = PlateAppearanceResult.SAC_BUNT;
            hitType = HitType.BUNT;
            atBat = false;
        } else if (resultDescription.contains("Bunt")) {
            result = PlateAppearanceResult.BALL_IN_PLAY_OUT;
            hitType = HitType.BUNT;
            atBat = true;
        } else {
            result = PlateAppearanceResult.BALL_IN_PLAY_OUT;
            hitType = HitType.GROUND_BALL;
            atBat = true;
        }
        log.trace("Result: {}, Hit Type: {}, At Bat: {}", result, hitType, atBat);

        log.trace("Checking for runs scored on a fielder's choice");
        int rbis = 0;
        if (resultDescription.contains("Scores")) {
            int runsScored = StringUtils.countMatches(resultDescription, "Scores");
            int runsScoredDiscounted = StringUtils.countMatches(resultDescription, "Scores/Adv on E");
            rbis = Math.max(0, runsScored - runsScoredDiscounted);
            if (rbis > 1) {
                log.warn("Fielder's Choice with {} run(s) scored. This is unusual. Review", runsScored);
                throw new WarningScrapeException("Fielder's Choice with " + runsScored
                        + " run(s) scored. This is unusual. Review description: " + resultDescription);
            }
        } else {
            log.trace("No runs scored, this is normal");
        }

        log.trace("Finding the hit for the fielder's choice");
        String[] descriptionParts = resultDescription.replace("Double Play: ", "").split("\\s|;|/");
        HitLocation hitLocation = HitLocation.findByDisplayName(descriptionParts[2]);
        log.trace("Hit location: " + hitLocation);

        return PlateAppearanceResultDTO.builder() //
                .result(result) //
                .ballHitInPlay(true) //
                .hitLocation(hitLocation) //
                .hitType(hitType) //
                .qualifiedAtBat(atBat) //
                .runsBattedIn(rbis) //
                .build();

    }

}
