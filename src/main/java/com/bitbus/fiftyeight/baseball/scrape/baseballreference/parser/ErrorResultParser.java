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
    public PlateAppearanceResultDTO parse(String resultDescription) {
        PlateAppearanceResult result = PlateAppearanceResult.REACH_ON_ERROR;
        int runsBattedIn = 0;
        boolean qualifiedAtBat = true;
        if (resultDescription.contains("Sacrifice Fly")) {
            result = PlateAppearanceResult.SAC_FLY;
            runsBattedIn = 1;
            qualifiedAtBat = false;
            log.debug("Identified a sacrifice fly in a reached on error scenario");
        }

        log.trace("Determining the hit details: location and hit type");
        int hitDetailsIndex = 1;
        if (StringUtils.countMatches(resultDescription, '(') > 1) {
            hitDetailsIndex = 3;
        }
        String[] hitDescriptionParts = resultDescription.split("\\(|\\)");
        String[] hitDetailParts = hitDescriptionParts[hitDetailsIndex].split("\\sto\\s|\\sthru\\s");
        HitType hitType = HitType.findByDisplayName(hitDetailParts[0]);
        log.trace("Hit type: {}", hitType);
        HitLocation hitLocation = HitLocation.findByDisplayName(hitDetailParts[1]);
        log.trace("Hit location: {}", hitLocation);

        return PlateAppearanceResultDTO.builder(result) //
                .qualifiedAtBat(qualifiedAtBat) //
                .runsBattedIn(runsBattedIn) //
                .ballHitInPlay(true) //
                .hitType(hitType) //
                .hitLocation(hitLocation) //
                .build();
    }

}
