package com.bitbus.fiftyeight.baseball.player.plateappearance;

public enum PitchResultType {

    STRIKE_CALLED('C'),
    STRIKE_SWINGING('S'),
    STRIKE_UNKNOWN_TYPE('K'),
    FOUL('F'),
    FOUL_TIP('T'),
    FOUL_BUNT('L'),
    MISSED_BUNT_ATTEMPT('M'),
    BALL('B'),
    BALL_PUT_IN_PLAY('X'),
    BALL_INTENTIONAL('I'),
    HIT_BATTER('H'),
    NO_PITCH('N'),
    FOUL_TIP_BUNT('O'),
    PITCHOUT('P'),
    SWINGING_ON_PITCHOUT('Q'),
    FOUL_ON_PITCHOUT('R'),
    UNKNOWN_PITCH('U');

    private char lookupId;

    private PitchResultType(char lookupId) {
        this.lookupId = lookupId;
    }

    public char getLookupId() {
        return lookupId;
    }

    public static PitchResultType findByLookupId(char lookupId) {
        for (PitchResultType pitchResultType : values()) {
            if (pitchResultType.getLookupId() == lookupId) {
                return pitchResultType;
            }
        }
        throw new IllegalArgumentException("Cannot find a pitch result type associated with lookup ID " + lookupId);
    }

}
