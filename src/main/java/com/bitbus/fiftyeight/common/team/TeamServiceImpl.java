package com.bitbus.fiftyeight.common.team;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitbus.fiftyeight.common.Sport;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepo;

    @Override
    public List<Team> findBySport(Sport sport) {
        return teamRepo.findBySport(sport);
    }

}
