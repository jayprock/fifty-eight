package com.bitbus.fiftyeight.baseball.game;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.bitbus.fiftyeight.baseball.team.BaseballTeam;
import com.bitbus.fiftyeight.common.game.Game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BaseballGame extends Game {

    @ManyToOne
    @JoinColumn(name = "team_id")
    private BaseballTeam team;

    @ManyToOne
    @JoinColumn(name = "opponent_id")
    private BaseballTeam opponent;
}
