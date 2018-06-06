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

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class PopflyResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public PopflyResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Popfly");
        startingWords.add("Foul Popfly");
        startingWords.add("Double Play: Popfly");
        startingWords.add("Double Play: Foul Popfly");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) throws ScrapeException {
        log.trace("Determining the location of the Popfly");
        HitLocation hitLocation;
        String[] popflyDescriptionParts = resultDescription.split("\\(|\\)");
        if (popflyDescriptionParts.length == 1) {
            hitLocation = HitLocation
                    .findByDisplayName(resultDescription.replace("Double Play: ", "").split(":\\s|/|;|-")[1]);
        } else {
            String hitLocationLookupVal = popflyDescriptionParts[1].replace(" Hole", "");
            hitLocation = HitLocation.findByDisplayName(hitLocationLookupVal);
        }
        log.trace("Popfly location: {}", hitLocation);

        PlateAppearanceResult result = PlateAppearanceResult.BALL_IN_PLAY_OUT;
        int rbis = 0;
        log.trace("Determining if the popfly included runs scored. This would be an unexpected situation");
        int runsScored = StringUtils.countMatches(resultDescription, "Scores");
        if (runsScored > 0) {
            log.warn("Run(s) scored during a popfly. This is unexpected and potentially not handled. Review!");
            if (resultDescription.contains("Sacrifice Fly")) {
                result = PlateAppearanceResult.SAC_FLY;
            }
            int runsScoredDiscounted = Math.max(StringUtils.countMatches(resultDescription, "No RBI"),
                    StringUtils.countMatches(resultDescription, "Scores/Adv on E"));
            rbis = Math.max(0, runsScored - runsScoredDiscounted);
        } else {
            log.trace("No runs were scored, this is the expected result");
        }

        log.trace("Successfully parsed the popfly result description");
        return PlateAppearanceResultDTO.builder() //
                .result(result) //
                .hitLocation(hitLocation) //
                .hitType(HitType.POPFLY) //
                .qualifiedAtBat(result == PlateAppearanceResult.BALL_IN_PLAY_OUT) //
                .ballHitInPlay(true) //
                .runsBattedIn(rbis) //
                .build();
    }

}
