package com.bitbus.fiftyeight.baseball.dfs;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.bitbus.fiftyeight.baseball.player.BaseballPlayer;

import lombok.Data;

@Entity
@Data
public class BaseballPlayerDfsSalary {

    @ManyToOne
    @JoinColumn(name = "player_id")
    private BaseballPlayer player;

}
