package com.bitbus.fiftyeight.baseball.player;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.bitbus.fiftyeight.baseball.dfs.BaseballPlayerDfsSalary;
import com.bitbus.fiftyeight.baseball.matchup.BaseballPlayerPosition;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearance;
import com.bitbus.fiftyeight.baseball.starter.BaseballGameStarter;
import com.bitbus.fiftyeight.common.player.DominateHand;
import com.bitbus.fiftyeight.common.player.Player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"playerPositions", "gameStarts", "plateAppearances", "dfsSalaries"})
@ToString(callSuper = true, exclude = {"playerPositions", "gameStarts", "plateAppearances", "dfsSalaries"})
public class BaseballPlayer extends Player {

    @Column(name = "bats")
    private BatterType batsFrom;

    @Column(name = "throws")
    private DominateHand throwsFrom;

    private String baseballReferenceId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "player")
    @Fetch(FetchMode.SUBSELECT)
    private List<BaseballPlayerPosition> playerPositions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "player")
    @Fetch(FetchMode.SUBSELECT)
    private List<BaseballGameStarter> gameStarts;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "batter")
    @Fetch(FetchMode.SUBSELECT)
    private List<PlateAppearance> plateAppearances;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "player")
    @Fetch(FetchMode.SUBSELECT)
    private List<BaseballPlayerDfsSalary> dfsSalaries;

}
