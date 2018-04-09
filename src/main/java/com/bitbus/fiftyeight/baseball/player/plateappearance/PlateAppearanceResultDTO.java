package com.bitbus.fiftyeight.baseball.player.plateappearance;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlateAppearanceResultDTO {

    private PlateAppearanceResult result;
    private boolean hit;
    private boolean qualifiedAtBat;
    // TODO - What constitutes a ball in play? What do I want to constitute this? Consider
    // HitLocation property...
    private boolean ballHitInPlay;
    private int runsBattedIn;
    private HitType hitType;
    private HitLocation hitLocation;

    private static PlateAppearanceResultDTOBuilder builder() {
        return new PlateAppearanceResultDTOBuilder();
    }

    public static PlateAppearanceResultDTOBuilder builder(PlateAppearanceResult result) {
        return builder().result(result);
    }

}
