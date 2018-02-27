package com.bitbus.fiftyeight.common.team;

import java.util.List;

import com.bitbus.fiftyeight.common.Sport;

public interface TeamService {

    List<Team> findBySport(Sport sport);

}
