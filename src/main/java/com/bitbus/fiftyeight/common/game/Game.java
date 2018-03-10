package com.bitbus.fiftyeight.common.game;

import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int gameId;

    private LocalDateTime gameDateTime;

    @Enumerated(EnumType.STRING)
    private GameLocation location;

    private int teamScore;

    private int opponentScore;

    @Enumerated(EnumType.STRING)
    private GameResult result;

}
