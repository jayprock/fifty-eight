package com.bitbus.fiftyeight.common.player;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import lombok.Data;

@MappedSuperclass
@Data
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playerId;
    private String firstName;
    private String lastName;
    private String ambiguousName;
    private double height;
    @Enumerated(EnumType.STRING)
    private HeightUnit heightUnit;
    private double weight;
    @Enumerated(EnumType.STRING)
    private WeightUnit weightUnit;

    @Transient
    public String getFullName() {
        if (ambiguousName != null) {
            return firstName + " " + ambiguousName + " " + lastName;
        } else {
            return firstName + " " + lastName;
        }
    }

}
