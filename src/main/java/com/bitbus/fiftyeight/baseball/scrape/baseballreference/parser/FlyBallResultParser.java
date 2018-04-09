package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bitbus.fiftyeight.baseball.player.plateappearance.HitLocation;
import com.bitbus.fiftyeight.baseball.player.plateappearance.HitType;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class FlyBallResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public FlyBallResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Flyball");
        startingWords.add("Foul Flyball");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) {
        log.trace("Determining the location of the fly ball");
        HitLocation hitLocation;
        String[] flyBallDescriptionParts = resultDescription.split("\\(|\\)");
        if (flyBallDescriptionParts.length == 1) {
            hitLocation = HitLocation.findByDisplayName(resultDescription.split(":\\s")[1]);
        } else {
            hitLocation = HitLocation.findByDisplayName(flyBallDescriptionParts[1]);
        }
        log.trace("Fly ball location: {}", hitLocation);

        log.trace("Assessing whether the fly ball was a sacrifice");
        boolean sacrifice = false;
        int runsScored = StringUtils.countMatches(resultDescription, "Scores");
        if (runsScored > 0) {
            if (resultDescription.contains("Sacrifice Fly")) {
                sacrifice = true;
            } else {
                log.error("A run scored on a flyball, but did not find \"Sacrifice Fly\", is something wrong?");
            }
            if (runsScored > 1) {
                log.warn(
                        "Flyball scenario with {} runs scored. Why are there more than 1 runs? More than 1 RBI never granted on sacrifice.",
                        runsScored);
            }
        }
        log.trace("Flyball is a sacrifice finding: " + sacrifice);

        log.trace("Successfully parsed the flyball result description");
        if (sacrifice) {
            return PlateAppearanceResultDTO.builder(PlateAppearanceResult.SAC_FLY) //
                    .hitLocation(hitLocation) //
                    .hitType(HitType.FLYBALL) //
                    .qualifiedAtBat(false) //
                    .ballHitInPlay(true) //
                    .runsBattedIn(1) //
                    .build();
        } else {
            return PlateAppearanceResultDTO.builder(PlateAppearanceResult.BALL_IN_PLAY_OUT) //
                    .hitLocation(hitLocation) //
                    .hitType(HitType.FLYBALL) //
                    .qualifiedAtBat(true) //
                    .ballHitInPlay(true) //
                    .runsBattedIn(0) //
                    .build();
        }

    }

}
