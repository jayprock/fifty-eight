package com.bitbus.fiftyeight.baseball.player.plateappearance;

public enum RunnersOnBase {

    NONE("---", 0, 0),
    FIRST("1--", 0, 1),
    SECOND("-2-", 1, 1),
    THIRD("--3", 1, 1),
    FIRST_AND_SECOND("12-", 1, 2),
    FIRST_AND_THIRD("1-3", 1, 2),
    SECOND_AND_THIRD("-23", 2, 2),
    BASES_LOADED("123", 2, 3);

    private String code;
    private int runnersInScoringPosition;
    private int runnersOnBaseCount;

    private RunnersOnBase(String code, int runnersInScoringPosition, int runnersOnBaseCount) {
        this.code = code;
        this.runnersInScoringPosition = runnersInScoringPosition;
        this.runnersOnBaseCount = runnersOnBaseCount;
    }

    public String getCode() {
        return code;
    }

    public int getRunnersInScoringPosition() {
        return runnersInScoringPosition;
    }

    public int getRunnersOnBaseCount() {
        return runnersOnBaseCount;
    }

    public static RunnersOnBase findByCode(String code) {
        for (RunnersOnBase runnersOnBase : values()) {
            if (runnersOnBase.getCode().equals(code)) {
                return runnersOnBase;
            }
        }
        throw new IllegalArgumentException("No RunnersOnBase could be associated with code: " + code);
    }
}
