package com.bitbus.fiftyeight.baseball.starter;

import com.bitbus.fiftyeight.baseball.player.BaseballPlayer;
import com.bitbus.fiftyeight.baseball.player.BaseballPosition;

import lombok.Data;

@Data
public class BaseballPlayerStarterDTO {

    private BaseballPlayer player;
    private BaseballPosition fieldingPosition;
    private Integer battingOrderPosition;

}
