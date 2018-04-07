package com.bitbus.fiftyeight.baseball.player.plateappearance;

public enum InningType {

    TOP('t'), BOTTOM('b');

    private char lookupId;

    private InningType(char lookupId) {
        this.lookupId = lookupId;
    }

    public char getLookupId() {
        return lookupId;
    }

    public static InningType findByLookupId(char lookupId) {
        for (InningType inningType : values()) {
            if (inningType.getLookupId() == lookupId) {
                return inningType;
            }
        }
        throw new IllegalArgumentException("Could not find InningType by lookupId: " + lookupId);
    }
}
