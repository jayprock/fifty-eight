package com.bitbus.fiftyeight.baseball.game;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseballGameServiceImpl implements BaseballGameService {

    @Autowired
    private BaseballGameRepository baseballGameRepo;


    @Override
    public BaseballGame save(BaseballGame game) {
        return baseballGameRepo.save(game);
    }

    @Override
    public List<BaseballGame> save(BaseballGame... games) {
        return save(Arrays.asList(games));
    }

    @Override
    public List<BaseballGame> save(Collection<BaseballGame> games) {
        return baseballGameRepo.save(games);
    }

}
