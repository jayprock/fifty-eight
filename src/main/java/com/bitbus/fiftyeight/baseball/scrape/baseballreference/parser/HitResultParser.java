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
public class HitResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public HitResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Single");
        startingWords.add("Double");
        startingWords.add("Triple");
        startingWords.add("Home Run");
        startingWords.add("Ground-rule Double");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) {
        PlateAppearanceResult result;
        log.trace("Determining the type of hit");
        if (resultDescription.startsWith(startingWords.get(0))) {
            result = PlateAppearanceResult.SINGLE;
        } else if (resultDescription.startsWith(startingWords.get(1))
                || resultDescription.startsWith(startingWords.get(4))) {
            result = PlateAppearanceResult.DOUBLE;
        } else if (resultDescription.startsWith(startingWords.get(2))) {
            result = PlateAppearanceResult.TRIPLE;
        } else if (resultDescription.startsWith(startingWords.get(3))) {
            result = PlateAppearanceResult.HOMERUN;
        } else {
            throw new RuntimeException(
                    "The result description is not a hit and should not be parsed by the HitResultParser");
        }
        log.trace("Found hit type {}", result);

        log.trace("Determining the hit details: location and hit type");
        String[] hitDescriptionParts = resultDescription.split("\\(|\\)");
        String[] summaryParts = hitDescriptionParts[0].split("\\sto\\s");
        String[] hitDetailParts = hitDescriptionParts[1].split("\\sto\\s|\\sthru\\s");
        HitType hitType = HitType.findByDisplayName(hitDetailParts[0]);
        log.trace("Hit type: {}", hitType);
        HitLocation hitLocation;
        if (hitDetailParts.length > 1) {
            hitLocation = HitLocation.findByDisplayName(hitDetailParts[1].replace(" Hole", ""));
        } else {
            hitLocation = HitLocation.findByDisplayName(summaryParts[1]);
        }
        log.trace("Hit location: {}", hitLocation);

        // TODO - Are there any error scenarios that need to be accounted for? E.g. runner scores on
        // bad throw?
        log.trace("Determining RBIs");
        int runnersScored = StringUtils.countMatches(resultDescription, "Scores");
        int runsBattedIn = (result == PlateAppearanceResult.HOMERUN) ? runnersScored + 1 : runnersScored;
        log.trace("RBIs: {}", runsBattedIn);

        log.debug("Successfully parsed the hit result data");
        return PlateAppearanceResultDTO.builder(result) //
                .hit(true) //
                .qualifiedAtBat(true) //
                .ballHitInPlay(true) //
                .hitType(hitType) //
                .hitLocation(hitLocation) //
                .runsBattedIn(runsBattedIn) //
                .build();
    }

}
