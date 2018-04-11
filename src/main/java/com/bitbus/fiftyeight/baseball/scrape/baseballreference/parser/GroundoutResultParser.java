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
public class GroundoutResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public GroundoutResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Groundout");
        startingWords.add("Ground Ball Double Play");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) {
        log.trace("Determining the location of the groundout");
        HitLocation hitLocation;
        String[] descriptionParts = resultDescription.split("\\(|\\)");
        boolean disallowRBI = false;
        if (descriptionParts.length == 1) {
            String hitLocationValue = resultDescription.split(":\\s")[1].split("\\s|-")[0];
            if (resultDescription.contains("Double Play:")) {
                disallowRBI = true;
            }
            hitLocation = HitLocation.findByDisplayName(hitLocationValue);
        } else {
            hitLocation = HitLocation.findByDisplayName(descriptionParts[1].replace(" Hole", ""));
        }
        log.trace("Location of groundout: " + hitLocation);

        log.trace("Checking for an RBI");
        int runsScored = StringUtils.countMatches(resultDescription, "Scores");
        if (runsScored > 1) {
            log.warn("More than 1 run scored on a groundout. This probably is not handled correctly!");
        }
        int rbis = disallowRBI ? 0 : runsScored;
        log.trace("RBIs assessed: " + rbis);

        return PlateAppearanceResultDTO.builder(PlateAppearanceResult.BALL_IN_PLAY_OUT) //
                .hitLocation(hitLocation) //
                .hitType(HitType.GROUND_BALL) //
                .qualifiedAtBat(true) //
                .ballHitInPlay(true) //
                .runsBattedIn(rbis) //
                .build();
    }

}
