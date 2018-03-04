package com.bitbus.fiftyeight.common.game;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    @Getter
    @Setter
    private GameRepository gameRepo;


    @Override
    public Game save(Game game) {
        return gameRepo.save(game);
    }

    @Override
    public List<Game> save(Game... games) {
        return gameRepo.save(Arrays.asList(games));
    }

    @Override
    public List<Game> save(List<Game> games) {
        return gameRepo.save(games);
    }

}
