package com.bitbus.fiftyeight.baseball.starter;

import java.util.List;

import com.bitbus.fiftyeight.baseball.matchup.BaseballMatchup;

public interface BaseballGameStarterService {

    List<BaseballGameStarter> save(List<BaseballPlayerStarterDTO> starterDTOs, BaseballMatchup matchup);

}
