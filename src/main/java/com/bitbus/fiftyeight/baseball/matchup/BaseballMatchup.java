package com.bitbus.fiftyeight.baseball.matchup;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearance;
import com.bitbus.fiftyeight.baseball.starter.BaseballGameStarter;
import com.bitbus.fiftyeight.baseball.team.BaseballTeam;
import com.bitbus.fiftyeight.common.matchup.Matchup;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"starters", "plateAppearances"})
@ToString(callSuper = true, exclude = {"starters", "plateAppearances"})
public class BaseballMatchup extends Matchup {

    private String baseballReferenceId;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private BaseballTeam homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private BaseballTeam awayTeam;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "matchup")
    @Fetch(FetchMode.SUBSELECT)
    private List<BaseballGameStarter> starters;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "matchup")
    @Fetch(FetchMode.SUBSELECT)
    private List<PlateAppearance> plateAppearances;

}
