package com.bitbus.fiftyeight.common.team;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.bitbus.fiftyeight.common.Sport;
import com.bitbus.fiftyeight.common.game.Game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {"games"})
@ToString(exclude = {"games"})
public class Team {

    @Id
    private int teamId;
    @Enumerated(EnumType.STRING)
    private Sport sport;
    private String name;
    private String shortName;
    private String city;
    private String timezone;
    private String homeGameVenue;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "team")
    private List<Game> games;

    @Transient
    public String getFullName() {
        return city + " " + name;
    }

}
