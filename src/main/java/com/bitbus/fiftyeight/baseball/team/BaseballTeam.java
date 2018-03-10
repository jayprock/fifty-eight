package com.bitbus.fiftyeight.baseball.team;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.bitbus.fiftyeight.baseball.game.BaseballGame;
import com.bitbus.fiftyeight.common.team.Team;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"games"})
@ToString(callSuper = true, exclude = {"games"})
public class BaseballTeam extends Team {

    private String conference;
    private String division;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "team")
    private List<BaseballGame> games;

}
