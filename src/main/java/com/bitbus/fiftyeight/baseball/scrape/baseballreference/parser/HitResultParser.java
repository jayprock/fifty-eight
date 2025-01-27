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
public class HitResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public HitResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Single");
        startingWords.add("Double to");
        startingWords.add("Triple");
        startingWords.add("Home Run");
        startingWords.add("Ground-rule Double");
        startingWords.add("Double/Fan");
        startingWords.add("Inside-the-park Home Run");
        startingWords.add("Double (");
        startingWords.add("Double Play: Single");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) throws ScrapeException {
        PlateAppearanceResult result;
        boolean groundRuleDouble = false;
        log.trace("Determining the type of hit");
        if (resultDescription.startsWith(startingWords.get(0))) {
            result = PlateAppearanceResult.SINGLE;
        } else if (resultDescription.startsWith(startingWords.get(1))
                || resultDescription.startsWith(startingWords.get(5))
                || resultDescription.startsWith(startingWords.get(7))) {
            result = PlateAppearanceResult.DOUBLE;
        } else if (resultDescription.startsWith(startingWords.get(4))) {
            result = PlateAppearanceResult.DOUBLE;
            groundRuleDouble = true;
        } else if (resultDescription.startsWith(startingWords.get(2))) {
            result = PlateAppearanceResult.TRIPLE;
        } else if (resultDescription.startsWith(startingWords.get(3))
                || resultDescription.startsWith(startingWords.get(6))) {
            result = PlateAppearanceResult.HOMERUN;
        } else if (resultDescription.startsWith(startingWords.get(8))) {
            result = PlateAppearanceResult.SINGLE;
        } else {
            throw new ScrapeException("The result description [" + resultDescription
                    + "] is not a hit and should not be parsed by the HitResultParser");
        }
        log.trace("Found hit result {}", result);

        log.trace("Determining the hit details: location and hit type");
        String[] hitDescriptionParts = resultDescription.split("\\(|\\)");
        String[] summaryParts = hitDescriptionParts[0].split("\\sto\\s");
        HitType hitType;
        HitLocation hitLocation = null;
        if (hitDescriptionParts.length > 1) {
            String[] hitDetailParts = hitDescriptionParts[1].split("\\sto\\s|\\sthru\\s");
            boolean skipHitLocation = false;
            try {
                if (hitDetailParts.length == 1 && summaryParts.length == 1) {
                    if (hitDescriptionParts[0].contains("runner struck")) {
                        hitType = HitType.findByDisplayName(hitDetailParts[0]);
                        skipHitLocation = true;
                        log.warn(
                                "A runner was struck by the ball. This is very unusual and the format is not certain. Review!");
                    } else if (groundRuleDouble) {
                        hitType = null;
                        skipHitLocation = true;
                        log.warn(
                                "Found ground-rule double with an unusual format. Review to make sure this was handled correctly. [{}]",
                                resultDescription);
                    } else if (resultDescription.contains("Fan Interference")
                            || result == PlateAppearanceResult.HOMERUN) {
                        hitType = HitType.findByDisplayName(hitDetailParts[0]);
                        skipHitLocation = true;
                    } else {
                        hitType = null;
                    }
                } else {
                    hitType = HitType.findByDisplayName(hitDetailParts[0].replace(" Popup", ""));
                }
            } catch (IllegalArgumentException e) {
                hitLocation = HitLocation.findByDisplayName(hitDetailParts[0].replace(" Hole", ""));
                hitType = null;
            }
            if (hitLocation == null && !skipHitLocation) {
                if (hitDetailParts.length > 1) {
                    hitLocation = HitLocation.findByDisplayName(hitDetailParts[1].replace(" Hole", ""));
                } else if (summaryParts.length > 1) {
                    hitLocation = HitLocation.findByDisplayName(summaryParts[1].split("/")[0].replace(" Hole", ""));
                } else {
                    hitLocation = HitLocation.findByDisplayName(hitDetailParts[0].replace(" Hole", ""));
                }
            }
        } else if (summaryParts.length > 1) {
            String hitDetailsParts = summaryParts[1].split(";")[0];
            if (hitDetailsParts.contains("Bunt")) {
                hitType = HitType.BUNT;
                hitLocation = HitLocation.findByDisplayName(hitDetailsParts.replace("/Bunt", ""));
            } else {
                hitType = null;
                hitLocation = HitLocation.findByDisplayName(hitDetailsParts.split("/")[0]);
            }
        } else {
            hitLocation = null;
            hitType = null;
        }
        log.trace("Hit type: {}", hitType);
        log.trace("Hit location: {}", hitLocation);

        log.trace("Determining RBIs");
        int runnersScored = StringUtils.countMatches(resultDescription, "Scores");
        if (runnersScored > 0) {
            int runsScoredDiscounted = Math.max(StringUtils.countMatches(resultDescription, "No RBI"),
                    StringUtils.countMatches(resultDescription, "Scores/Adv on E"));
            if (runsScoredDiscounted > 0) {
                log.debug("Found runs scored that do not count as RBIs, removing them from the RBI count");
                runnersScored = Math.max(runnersScored - runsScoredDiscounted, 0);
            }

        }
        int runsBattedIn = (result == PlateAppearanceResult.HOMERUN) ? runnersScored + 1 : runnersScored;
        log.trace("RBIs: {}", runsBattedIn);

        log.debug("Successfully parsed the hit result data");
        return PlateAppearanceResultDTO.builder() //
                .result(result) //
                .hit(true) //
                .qualifiedAtBat(true) //
                .ballHitInPlay(true) //
                .hitType(hitType) //
                .hitLocation(hitLocation) //
                .runsBattedIn(runsBattedIn) //
                .build();
    }

}
