package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class GroundoutResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;
    private List<String> excludedStartingWords;

    public GroundoutResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Groundout");
        startingWords.add("Ground Ball Double Play");
        startingWords.add("Double Play: Groundout");
        startingWords.add("Ground Ball Triple Play");

        excludedStartingWords = new ArrayList<>();
        excludedStartingWords.add("Ground Ball Double Play: Bunt");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public Optional<List<String>> getExcludedStartingWords() {
        return Optional.of(excludedStartingWords);
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) throws ScrapeException {
        log.trace("Determining the location of the groundout");
        HitLocation hitLocation;
        String[] descriptionParts = resultDescription.split(";")[0].split("\\(|\\)");
        boolean disallowRBI = false;
        if (descriptionParts.length == 1) {
            int indexOffset = 0;
            if (resultDescription.contains("Ground Ball Double Play:")) {
                disallowRBI = true;
            } else if (resultDescription.startsWith("Double Play:")) {
                disallowRBI = true;
                indexOffset++;
            }
            String hitLocationValue = resultDescription.split(":\\s")[1 + indexOffset].split("\\s|-|;")[0];
            hitLocation = HitLocation.findByDisplayName(hitLocationValue);
        } else {
            hitLocation = HitLocation.findByDisplayName(descriptionParts[1].replace(" Hole", ""));
        }
        log.trace("Location of groundout: " + hitLocation);

        log.trace("Checking for an RBI");
        int rbis = 0;
        int runsScored = StringUtils.countMatches(resultDescription, "Scores");
        if (runsScored > 0) {
            int runsScoredNotRBIs = Math.max(StringUtils.countMatches(resultDescription, "No RBI"),
                    StringUtils.countMatches(resultDescription, "Scores/Adv on E"));
            rbis = Math.max(0, runsScored - runsScoredNotRBIs);
        }
        if (rbis > 1) {
            log.warn("More than 1 run scored on a groundout [{}]. This might not be handled correctly!",
                    resultDescription);
        }
        rbis = disallowRBI ? 0 : rbis;
        log.trace("RBIs assessed: " + rbis);

        return PlateAppearanceResultDTO.builder() //
                .result(PlateAppearanceResult.BALL_IN_PLAY_OUT) //
                .hitLocation(hitLocation) //
                .hitType(HitType.GROUND_BALL) //
                .qualifiedAtBat(true) //
                .ballHitInPlay(true) //
                .runsBattedIn(rbis) //
                .build();
    }

}
