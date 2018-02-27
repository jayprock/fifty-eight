package com.bitbus.fiftyeight.common.team;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.bitbus.fiftyeight.common.Sport;

import lombok.Data;

@Entity
@Data
public class Team {

    @Id
    private int teamId;
    @Enumerated(EnumType.STRING)
    private Sport sport;
    private String name;
    private String shortName;
    private String city;
    private String timezone;

}
