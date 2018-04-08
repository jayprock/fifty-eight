package com.bitbus.fiftyeight.baseball.player.plateappearance;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.bitbus.fiftyeight.baseball.matchup.BaseballMatchup;
import com.bitbus.fiftyeight.baseball.player.BaseballPlayer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {"batter", "pitcher", "matchup", "pitchResults"})
@ToString(exclude = {"batter", "pitcher", "matchup", "pitchResults"})
public class PlateAppearance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int plateAppearanceId;

    @ManyToOne
    @JoinColumn(name = "batter_id")
    private BaseballPlayer batter;

    @OneToOne
    @JoinColumn(name = "pitcher_id")
    private BaseballPlayer pitcher;

    @ManyToOne
    @JoinColumn(name = "matchup_id")
    private BaseballMatchup matchup;

    private int inning;

    @Enumerated(EnumType.ORDINAL)
    private InningType inningType;

    private int pitchTotal;
    private int outsExisting;
    private int outsResult;
    private int runsResult;

    private String runnersOnBaseCode;
    private boolean resultsInHit;
    private int basesEventuallyStolen;
    private boolean runEventuallyScored;
    private boolean qualifiedAtBat;
    private int runsBattedIn;

    @Enumerated(EnumType.STRING)
    private PlateAppearanceResult resultType;

    @Enumerated(EnumType.STRING)
    private HitType hitType;

    @Enumerated(EnumType.STRING)
    private HitLocation hitLocation;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plateAppearance")
    private List<PitchResult> pitchResults;

    @Transient
    public RunnersOnBase getRunnersOnBase() {
        return RunnersOnBase.findByCode(getRunnersOnBaseCode());
    }

    public void setRunnersOnBase(RunnersOnBase runnersOnBase) {
        this.runnersOnBaseCode = runnersOnBase.getCode();
    }
}
