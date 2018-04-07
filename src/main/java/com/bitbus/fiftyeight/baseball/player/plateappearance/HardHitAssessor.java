package com.bitbus.fiftyeight.baseball.player.plateappearance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HardHitAssessor {

    private static final Map<HitType, List<HitLocation>> HARD_HITS_BY_TYPE;

    static {
        HARD_HITS_BY_TYPE = new HashMap<>();
        HARD_HITS_BY_TYPE.put(HitType.LINE_DRIVE, Arrays.stream(HitLocation.values()) //
                .filter(hitType -> hitType.isOutfield()) //
                .collect(Collectors.toList()));
    }


    public static boolean isHardHit(HitType hitType, HitLocation hitLocation) {
        boolean hardHit = false;
        return hardHit;
    }
}
