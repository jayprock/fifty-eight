package com.bitbus.fiftyeight.baseball.player.plateappearance;

public enum HitType {

    GROUND_BALL("Ground Ball"), LINE_DRIVE("Line Drive"), FLYBALL("Fly Ball"), POPFLY("Popfly"), BUNT("Bunt");

    private String displayName;

    private HitType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static HitType findByDisplayName(String displayName) {
        String displayNameNoWhitespace = displayName.replaceAll("\\s", "");
        for (HitType hitType : values()) {
            if (hitType.getDisplayName().replaceAll("\\s", "").equalsIgnoreCase(displayNameNoWhitespace)) {
                return hitType;
            }
        }
        throw new IllegalArgumentException("Cannot find HitType associated with display name: [" + displayName + "]");
    }
}
