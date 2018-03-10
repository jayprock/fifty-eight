package com.bitbus.fiftyeight.baseball.team;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseballTeamServiceImpl implements BaseballTeamService {

    @Autowired
    private BaseballTeamRepository baseballTeamRepo;

    @Override
    public List<BaseballTeam> findAll() {
        return baseballTeamRepo.findAll();
    }
}
