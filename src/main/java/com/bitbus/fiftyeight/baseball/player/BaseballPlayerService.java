package com.bitbus.fiftyeight.baseball.player;

import java.util.Optional;

public interface BaseballPlayerService {

    Optional<BaseballPlayer> findByBaseballReferenceId(String baseballReferenceId);

    BaseballPlayer save(BaseballPlayer player);

}
