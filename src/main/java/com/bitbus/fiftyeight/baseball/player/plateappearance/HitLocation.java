package com.bitbus.fiftyeight.baseball.player.plateappearance;

public enum HitLocation {

    FRONT_OF_HOME("Front of Home", false),
    FIRST_BASE("1B", false),
    DEEP_FIRST_BASE("Deep 1B", false),
    SECOND_BASE_FIRST_BASE("2B-1B", false),
    DEEP_SECOND_BASE_FIRST_BASE("Deep 2B-1B", false),
    SECOND_BASE("2B", false),
    DEEP_SECOND_BASE("Deep 2B", false),
    SHORT_STOP_SECOND_BASE("SS-2B", false),
    DEEP_SHORT_STOP_SECOND_BASE("Deep SS-2B", false),
    SHORT_STOP("SS", false),
    DEEP_SHORT_STOP("Deep SS", false),
    THIRD_BASE_SHORT_STOP("3B-SS", false),
    DEEP_THIRD_BASE_SHORT_STOP("Deep 3B-SS", false),
    THIRD_BASE("3B", false),
    DEEP_THIRD_BASE("Deep 3B", false),
    PITCHER("P", false),
    SHORT_LEFT_FIELD_LINE("Short LF Line", true),
    LEFT_FIELD_LINE("LF Line", true),
    DEEP_LEFT_FIELD_LINE("Deep LF Line", true),
    SHORT_LEFT_FIELD("Short LF", true),
    LEFT_FIELD("LF", true),
    DEEP_LEFT_FIELD("Deep LF", true),
    SHORT_LEFT_FIELD_CENTER_FIELD("Short LF-CF", true),
    LEFT_FIELD_CENTER_FIELD("LF-CF", true),
    DEEP_LEFT_FIELD_CENTER_FIELD("Deep LF-CF", true),
    SHORT_CENTER_FIELD("Short CF", true),
    CENTER_FIELD("CF", true),
    DEEP_CENTER_FIELD("Deep CF", true),
    SHORT_CENTER_FIELD_RIGHT_FIELD("Short CF-RF", true),
    CENTER_FIELD_RIGHT_FIELD("CF-RF", true),
    DEEP_CENTER_FIELD_RIGHT_FIELD("Deep CF-RF", true),
    SHORT_RIGHT_FIELD("Short RF", true),
    RIGHT_FIELD("RF", true),
    SHORT_RIGHT_FIELD_LINE("Short RF Line", true),
    RIGHT_FIELD_LINE("RF Line", true),
    DEEP_RIGHT_FIELD_LINE("Deep RF Line", true);

    private String displayName;
    private boolean outfield;

    private HitLocation(String displayName, boolean outfield) {
        this.displayName = displayName;
        this.outfield = outfield;
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
            if (hitLocation.getDisplayName().equalsIgnoreCase(trimmedDisplayName)) {
                return hitLocation;
            }
        }
        throw new IllegalArgumentException("Cannot find HitLocation from display name: [" + displayName + "]");
    }
}
