package com.bitbus.fiftyeight.baseball.game;

import java.util.Collection;
import java.util.List;

public interface BaseballGameService {

    BaseballGame save(BaseballGame game);

    List<BaseballGame> save(BaseballGame... games);

    List<BaseballGame> save(Collection<BaseballGame> games);
}
