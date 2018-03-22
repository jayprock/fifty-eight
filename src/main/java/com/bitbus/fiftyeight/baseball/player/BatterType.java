package com.bitbus.fiftyeight.baseball.player;

public enum BatterType {

    LEFT("Left"), RIGHT("Right"), SWITCH("Both");

    private String lookupName;

    private BatterType(String lookupName) {
        this.lookupName = lookupName;
    }

    public String getLookupName() {
        return lookupName;
    }

    public static BatterType findByLookupName(String lookupName) {
        for (BatterType batterType : values()) {
            if (batterType.getLookupName().equals(lookupName)) {
                return batterType;
            }
        }
        throw new IllegalArgumentException("Cannot find batter type by lookup name: " + lookupName);
    }

}
