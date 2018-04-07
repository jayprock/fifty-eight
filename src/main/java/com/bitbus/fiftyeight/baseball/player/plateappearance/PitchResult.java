package com.bitbus.fiftyeight.baseball.player.plateappearance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = "plateAppearance")
@ToString(exclude = "plateAppearance")
@NoArgsConstructor
public class PitchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pitchResultId;

    @ManyToOne
    @JoinColumn(name = "plate_appearance_id")
    private PlateAppearance plateAppearance;

    @Column(name = "result")
    private char pitchResultLookupId;

    public PitchResult(char pitchResultLookupId, PlateAppearance plateAppearance) {
        this.pitchResultLookupId = pitchResultLookupId;
        this.plateAppearance = plateAppearance;
    }

    @Transient
    public PitchResultType getPitchResultType() {
        return PitchResultType.findByLookupId(pitchResultLookupId);
    }

    public void setPitchResultType(PitchResultType pitchResultType) {
        this.pitchResultLookupId = pitchResultType.getLookupId();
    }
}
