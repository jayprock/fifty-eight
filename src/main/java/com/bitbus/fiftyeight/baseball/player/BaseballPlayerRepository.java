package com.bitbus.fiftyeight.baseball.player;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseballPlayerRepository extends JpaRepository<BaseballPlayer, Integer> {

    Optional<BaseballPlayer> findByBaseballReferenceId(String baseballReferenceId);

}
