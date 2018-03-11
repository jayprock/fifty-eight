package com.bitbus.fiftyeight.baseball.team;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.bitbus.fiftyeight.baseball.matchup.BaseballMatchup;
import com.bitbus.fiftyeight.common.team.Team;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"awayMatchups", "homeMatchups"})
@ToString(callSuper = true, exclude = {"awayMatchups", "homeMatchups"})
public class BaseballTeam extends Team {

    private String conference;
    private String division;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "homeTeam")
    private List<BaseballMatchup> homeMatchups;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "awayTeam")
    private List<BaseballMatchup> awayMatchups;

}
