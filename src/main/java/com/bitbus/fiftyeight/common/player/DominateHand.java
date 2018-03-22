package com.bitbus.fiftyeight.common.player;

public enum DominateHand {

    LEFT("Left"), RIGHT("Right");

    private String lookupName;

    private DominateHand(String lookupName) {
        this.lookupName = lookupName;
    }

    public String getLookupName() {
        return lookupName;
    }

    public static DominateHand findByLookupName(String lookupName) {
        for (DominateHand dominateHand : values()) {
            if (dominateHand.getLookupName().equals(lookupName)) {
                return dominateHand;
            }
        }
        throw new IllegalArgumentException("Cannot find DominateHand by lookup name: " + lookupName);
    }

}
