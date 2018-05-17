package com.bitbus.fiftyeight.baseball.player.plateappearance;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlateAppearanceResultDTO {

    private PlateAppearanceResult result;
    private boolean hit;
    private boolean qualifiedAtBat;
    private boolean ballHitInPlay;
    private int runsBattedIn;
    private HitType hitType;
    private HitLocation hitLocation;
    private boolean notPlateAppearanceResult;

}
