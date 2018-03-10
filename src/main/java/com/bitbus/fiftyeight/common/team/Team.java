package com.bitbus.fiftyeight.common.team;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class Team {

    @Id
    private int teamId;
    private String name;
    private String shortName;
    private String city;
    private String timezone;
    private String homeGameVenue;

    @Transient
    public String getFullName() {
        return city + " " + name;
    }

}
