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
public class FlyBallResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public FlyBallResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Flyball");
        startingWords.add("Foul Flyball");
        startingWords.add("Double Play: Flyball");
        startingWords.add("Double Play: Foul Flyball");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) throws ScrapeException {
        log.trace("Determining the location of the fly ball");
        HitLocation hitLocation;
        String[] flyBallDescriptionParts = resultDescription.split(";")[0].split("\\(|\\)");
        if (flyBallDescriptionParts.length == 1) {
            hitLocation = HitLocation
                    .findByDisplayName(resultDescription.replace("Double Play: ", "").split(":\\s|;|/|-")[1]);
        } else {
            hitLocation = HitLocation.findByDisplayName(flyBallDescriptionParts[1].replace(" Hole", ""));
        }
        log.trace("Fly ball location: {}", hitLocation);

        log.trace("Assessing the number of runs scored");
        boolean qualifiedAB = true;
        int rbis = 0;
        int runsScored = StringUtils.countMatches(resultDescription, "Scores");
        if (runsScored > 0) {
            if (resultDescription.contains("Sacrifice Fly")) {
                qualifiedAB = false;
            } else {
                log.warn("A run scored on a flyball [{}], but did not find \"Sacrifice Fly\", this is unusual",
                        resultDescription);
            }
            int runsScoreDiscounted = Math.max(StringUtils.countMatches(resultDescription, "No RBI"),
                    Math.max(StringUtils.countMatches(resultDescription, "Scores/Adv on E"),
                            StringUtils.countMatches(resultDescription, "Scores/unER/Adv on E")));
            rbis = Math.max(0, runsScored - runsScoreDiscounted);
            if (rbis > 1) {
                log.warn("Flyball scenario with {} RBIs. How is there more than 1 RBI?", runsScored);
                throw new WarningScrapeException(
                        "Flyball scenario with {} RBIs. Why are there more than 1 RBIs? Review description: "
                                + resultDescription);
            }
        }
        PlateAppearanceResult result =
                qualifiedAB ? PlateAppearanceResult.BALL_IN_PLAY_OUT : PlateAppearanceResult.SAC_FLY;
        log.trace("Flyball parsed result type: " + result);

        log.trace("Successfully parsed the flyball result description");
        return PlateAppearanceResultDTO.builder() //
                .result(result) //
                .hitLocation(hitLocation) //
                .hitType(HitType.FLYBALL) //
                .qualifiedAtBat(qualifiedAB) //
                .ballHitInPlay(true) //
                .runsBattedIn(rbis) //
                .build();
    }

}
