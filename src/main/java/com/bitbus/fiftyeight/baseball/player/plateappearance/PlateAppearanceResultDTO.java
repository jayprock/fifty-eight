package com.bitbus.fiftyeight.baseball.player.plateappearance;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlateAppearanceResultDTO {

    private PlateAppearanceResult result;
    private boolean hit;
    private boolean qualifiedAtBat;
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
