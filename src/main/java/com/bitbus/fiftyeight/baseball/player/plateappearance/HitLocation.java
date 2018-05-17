package com.bitbus.fiftyeight.baseball.player.plateappearance;

import java.util.Arrays;
import java.util.List;

public enum HitLocation {

    FRONT_OF_HOME("Front of Home", false, Arrays.asList("C")),
    BEHIND_HOME("Behind Home", false, null),
    FIRST_BASE("1B", false, null),
    WEAK_FIRST_BASE("Weak 1B", false, null),
    DEEP_FIRST_BASE("Deep 1B", false, null),
    SHORT_FIRST_BASE_LINE("Short 1B Line", false, null),
    FIRST_BASE_FOUL_TERRITORY("1B into Foul Terr.", false, null),
    SECOND_BASE_FIRST_BASE("2B-1B", false, null),
    WEAK_SECOND_BASE_FIRST_BASE("Weak 2B-1B", false, null),
    DEEP_SECOND_BASE_FIRST_BASE("Deep 2B-1B", false, null),
    SECOND_BASE("2B", false, null),
    WEAK_SECOND_BASE("Weak 2B", false, null),
    DEEP_SECOND_BASE("Deep 2B", false, null),
    SHORT_STOP_SECOND_BASE("SS-2B", false, null),
    WEAK_SHORT_STOP_SECOND("Weak SS-2B", false, null),
    DEEP_SHORT_STOP_SECOND_BASE("Deep SS-2B", false, null),
    SHORT_STOP("SS", false, null),
    WEAK_SHORT_STOP("Weak SS", false, null),
    DEEP_SHORT_STOP("Deep SS", false, null),
    THIRD_BASE_SHORT_STOP("SS-3B", false, null),
    WEAK_THIRD_BASE_SHORT_STOP("Weak SS-3B", false, null),
    DEEP_THIRD_BASE_SHORT_STOP("Deep SS-3B", false, null),
    THIRD_BASE("3B", false, null),
    WEAK_THIRD_BASE("Weak 3B", false, null),
    DEEP_THIRD_BASE("Deep 3B", false, null),
    SHORT_THIRD_BASE_LINE("Short 3B Line", false, null),
    THIRD_BASE_FOUL_TERRITORY("3B into Foul Terr.", true, null),
    PITCHER("P", false, null),
    PITCHERS_RIGHT("P's Right", false, null),
    PITCHERS_LEFT("P's Left", false, null),
    SHORT_LEFT_FIELD_LINE("Short LF Line", true, null),
    LEFT_FIELD_FOUL_TERRITORY("LF into Foul Terr.", true, null),
    LEFT_FIELD_LINE("LF Line", true, null),
    DEEP_LEFT_FIELD_LINE("Deep LF Line", true, null),
    SHORT_LEFT_FIELD("Short LF", true, null),
    LEFT_FIELD("LF", true, null),
    DEEP_LEFT_FIELD("Deep LF", true, null),
    SHORT_LEFT_FIELD_CENTER_FIELD("Short LF-CF", true, null),
    LEFT_FIELD_CENTER_FIELD("LF-CF", true, null),
    DEEP_LEFT_FIELD_CENTER_FIELD("Deep LF-CF", true, null),
    SHORT_CENTER_FIELD("Short CF", true, null),
    CENTER_FIELD("CF", true, null),
    DEEP_CENTER_FIELD("Deep CF", true, null),
    SHORT_CENTER_FIELD_RIGHT_FIELD("Short CF-RF", true, null),
    CENTER_FIELD_RIGHT_FIELD("CF-RF", true, null),
    DEEP_CENTER_FIELD_RIGHT_FIELD("Deep CF-RF", true, null),
    SHORT_RIGHT_FIELD("Short RF", true, null),
    RIGHT_FIELD("RF", true, null),
    DEEP_RIGHT_FIELD("Deep RF", true, null),
    SHORT_RIGHT_FIELD_LINE("Short RF Line", true, null),
    RIGHT_FIELD_LINE("RF Line", true, null),
    DEEP_RIGHT_FIELD_LINE("Deep RF Line", true, null),
    RIGHT_FIELD_FOUL_TERRITORY("RF into Foul Terr.", true, null);

    private String displayName;
    private boolean outfield;
    private List<String> alternateLookupValues;

    private HitLocation(String displayName, boolean outfield, List<String> alternateLookupValues) {
        this.displayName = displayName;
        this.outfield = outfield;
        this.alternateLookupValues = alternateLookupValues;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isOutfield() {
        return outfield;
    }

    public static HitLocation findByDisplayName(String displayName) {
        String trimmedDisplayName = displayName.trim();
        for (HitLocation hitLocation : values()) {
            if (hitLocation.getDisplayName().equalsIgnoreCase(trimmedDisplayName)
                    || (hitLocation.alternateLookupValues != null
                            && hitLocation.alternateLookupValues.contains(displayName))) {
                return hitLocation;
            }
        }
        throw new IllegalArgumentException("Cannot find HitLocation from display name: [" + displayName + "]");
    }
}
