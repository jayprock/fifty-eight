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
public class ErrorResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public ErrorResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Reached on E");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) throws ScrapeException {
        PlateAppearanceResult result = PlateAppearanceResult.REACH_ON_ERROR;
        int runsBattedIn = 0;
        boolean qualifiedAtBat = true;
        HitType hitType;
        HitLocation hitLocation;
        if (resultDescription.contains("Sacrifice Fly")) {
            log.info("Identified a sacrifice fly in a reached on error scenario, this is unusual");
            result = PlateAppearanceResult.SAC_FLY;
            runsBattedIn = 1;
            qualifiedAtBat = false;
            hitType = HitType.FLYBALL;
            if (resultDescription.startsWith("Reached on E7")) {
                hitLocation = HitLocation.LEFT_FIELD;
            } else if (resultDescription.startsWith("Reached on E8")) {
                hitLocation = HitLocation.CENTER_FIELD;
            } else if (resultDescription.startsWith("Reached on E9")) {
                hitLocation = HitLocation.RIGHT_FIELD;
            } else {
                throw new ScrapeException(
                        "Don't know how to assess hit location in odd sac fly scenario, with play description: "
                                + resultDescription);
            }
        } else {
            log.trace("Determining the hit details: location and hit type");
            int hitDetailsIndex = 1;
            if (StringUtils.countMatches(resultDescription.split(";")[0], '(') > 1) {
                hitDetailsIndex = 3;
            }
            String[] hitDescriptionParts = resultDescription.split("\\(|\\)");
            String[] hitDetailParts = hitDescriptionParts[hitDetailsIndex].split("\\sto\\s|\\sthru\\s");
            hitType = HitType.findByDisplayName(hitDetailParts[0]);
            log.trace("Hit type: {}", hitType);
            if (hitDetailParts.length > 1) {
                hitLocation = HitLocation.findByDisplayName(hitDetailParts[1].replace(" Hole", ""));
            } else {
                hitLocation = null;
            }
            log.trace("Hit location: {}", hitLocation);
        }

        return PlateAppearanceResultDTO.builder() //
                .result(result) //
                .qualifiedAtBat(qualifiedAtBat) //
                .runsBattedIn(runsBattedIn) //
                .ballHitInPlay(true) //
                .hitType(hitType) //
                .hitLocation(hitLocation) //
                .build();
    }

}
