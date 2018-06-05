package com.bitbus.fiftyeight.baseball.dfs;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.bitbus.fiftyeight.baseball.player.BaseballPlayer;
import com.bitbus.fiftyeight.common.dfs.PlayerDfsSalary;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BaseballPlayerDfsSalary extends PlayerDfsSalary {

    @ManyToOne
    @JoinColumn(name = "player_id")
    private BaseballPlayer player;

}
