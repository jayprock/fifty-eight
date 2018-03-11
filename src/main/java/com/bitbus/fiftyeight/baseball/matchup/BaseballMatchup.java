package com.bitbus.fiftyeight.baseball.matchup;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.bitbus.fiftyeight.baseball.team.BaseballTeam;
import com.bitbus.fiftyeight.common.matchup.Matchup;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BaseballMatchup extends Matchup {

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private BaseballTeam homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private BaseballTeam awayTeam;

    private String baseballReferenceId;
}
