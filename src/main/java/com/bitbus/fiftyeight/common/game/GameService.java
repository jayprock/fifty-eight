package com.bitbus.fiftyeight.common.game;

import java.util.List;

public interface GameService {

    Game save(Game game);

    List<Game> save(Game... games);

    List<Game> save(List<Game> games);

}
