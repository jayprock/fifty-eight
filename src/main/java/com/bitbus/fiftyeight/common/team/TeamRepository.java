package com.bitbus.fiftyeight.common.team;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitbus.fiftyeight.common.Sport;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    List<Team> findBySport(Sport sport);
}
