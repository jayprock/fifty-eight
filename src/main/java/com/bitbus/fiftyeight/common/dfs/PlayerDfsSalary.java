package com.bitbus.fiftyeight.common.dfs;

import java.time.LocalDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data
public class PlayerDfsSalary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dfsSalaryId;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private DailyFantasySite site;

    private int salary;
}
