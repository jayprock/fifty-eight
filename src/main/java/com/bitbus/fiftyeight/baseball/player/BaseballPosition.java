package com.bitbus.fiftyeight.baseball.player;

public enum BaseballPosition {

    CATCHER("C"),
    FIRST_BASE("1B"),
    SECOND_BASE("2B"),
    SHORT_STOP("SS"),
    THIRD_BASE("3B"),
    LEFT_FIELD("LF"),
    CENTER_FIELD("CF"),
    RIGHT_FIELD("RF"),
    PITCHER("P"),
    DESIGNATED_HITTER("DH");

    private String positionId;

    private BaseballPosition(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionId() {
        return positionId;
    }

    public static BaseballPosition findByPositionId(String positionId) {
        for (BaseballPosition position : values()) {
            if (position.getPositionId().equals(positionId)) {
                return position;
            }
        }
        throw new IllegalArgumentException("There is no position that maps to " + positionId);
    }
}
