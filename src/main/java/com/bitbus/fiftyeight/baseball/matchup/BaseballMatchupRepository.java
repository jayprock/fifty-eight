package com.bitbus.fiftyeight.baseball.matchup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseballMatchupRepository extends JpaRepository<BaseballMatchup, Integer> {

    BaseballMatchup findByBaseballReferenceId(String baseballReferenceId);

}
