package com.bitbus.fiftyeight.baseball.player;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseballPlayerServiceImpl implements BaseballPlayerService {

    @Autowired
    private BaseballPlayerRepository baseballPlayerRepo;


    @Override
    public Optional<BaseballPlayer> findByBaseballReferenceId(String baseballReferenceId) {
        return baseballPlayerRepo.findByBaseballReferenceId(baseballReferenceId);
    }

    @Override
    public BaseballPlayer save(BaseballPlayer player) {
        return baseballPlayerRepo.save(player);
    }

}
