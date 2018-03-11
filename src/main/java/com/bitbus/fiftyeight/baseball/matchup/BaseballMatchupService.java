package com.bitbus.fiftyeight.baseball.matchup;

public interface BaseballMatchupService {

    BaseballMatchup save(BaseballMatchup matchup);

    BaseballMatchup findByBaseballReferenceId(String baseballReferenceId);

    boolean matchupExistsForBaseballReferenceId(String baseballReferenceId);
}
