package com.bitbus.fiftyeight.common.matchup;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class Matchup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int matchupId;

    private LocalDateTime gameDateTime;

    private int homeTeamScore;

    private int awayTeamScore;


}
