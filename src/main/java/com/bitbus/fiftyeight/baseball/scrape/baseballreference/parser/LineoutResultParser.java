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
public class LineoutResultParser implements PlateAppearanceResultParser {

    private List<String> startingWords;

    public LineoutResultParser() {
        startingWords = new ArrayList<>();
        startingWords.add("Lineout");
        startingWords.add("Double Play: Lineout");
        startingWords.add("Foul Lineout");
        startingWords.add("Double Play: Foul Lineout");
        startingWords.add("Line Drive Double Play");
    }

    @Override
    public List<String> getStartingWords() {
        return startingWords;
    }

    @Override
    public PlateAppearanceResultDTO parse(String resultDescription) {
        log.trace("Determining the location of the lineout");
        HitLocation hitLocation;
        String[] lineoutDescriptionParts = resultDescription.split(";")[0].split("\\(|\\)");
        if (lineoutDescriptionParts.length == 1) {
            String[] hitLocationParts = resultDescription.split(":\\s|;|/|-");
            if (resultDescription.startsWith("Double Play")) {
                hitLocation = HitLocation.findByDisplayName(hitLocationParts[2].replace(" unassisted", ""));
            } else {
                hitLocation = HitLocation.findByDisplayName(hitLocationParts[1]);
            }
        } else {
            String hitLocationLookupVal = lineoutDescriptionParts[1].replace(" Hole", "");
            hitLocation = HitLocation.findByDisplayName(hitLocationLookupVal);
        }
        log.trace("Lineout location: {}", hitLocation);

        log.trace("Determining if the lineout was a sacrifice");
        boolean sacrifice = false;
        int runsScored = StringUtils.countMatches(resultDescription, "Scores");
        if (runsScored > 0) {
            if (resultDescription.contains("Sacrifice Fly")) {
                sacrifice = true;
            } else {
                log.warn("A run scored on a lineout, but did not find \"Sacrifice Fly\", is something wrong?");
            }
            if (runsScored > 1) {
                log.warn(
                        "Lineout scenario with {} runs scored. Why are there more than 1 runs? More than 1 RBI never granted on sacrifice.",
                        runsScored);
            }
        }
        log.trace("Lineout sacrifice assessment: " + sacrifice);

        log.trace("Successfully parsed the lineout result description");
        if (sacrifice) {
            return PlateAppearanceResultDTO.builder() //
                    .result(PlateAppearanceResult.SAC_FLY) //
                    .hitLocation(hitLocation) //
                    .hitType(HitType.LINE_DRIVE) //
                    .qualifiedAtBat(false) //
                    .ballHitInPlay(true) //
                    .runsBattedIn(1) //
                    .build();
        } else {
            return PlateAppearanceResultDTO.builder() //
                    .result(PlateAppearanceResult.BALL_IN_PLAY_OUT) //
                    .hitLocation(hitLocation) //
                    .hitType(HitType.LINE_DRIVE) //
                    .qualifiedAtBat(true) //
                    .ballHitInPlay(true) //
                    .runsBattedIn(0) //
                    .build();
        }
    }

}
