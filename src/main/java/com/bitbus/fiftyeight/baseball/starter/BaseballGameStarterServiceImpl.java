package com.bitbus.fiftyeight.baseball.starter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitbus.fiftyeight.baseball.matchup.BaseballMatchup;

@Service
public class BaseballGameStarterServiceImpl implements BaseballGameStarterService {

    @Autowired
    private BaseballGameStarterRepository baseballGameStarterRepo;

    @Override
    public List<BaseballGameStarter> save(List<BaseballPlayerStarterDTO> starterDTOs, BaseballMatchup matchup) {
        List<BaseballGameStarter> baseballGameStarters = starterDTOs.stream()
                .map(dto -> new BaseballGameStarter(matchup, dto.getPlayer(), dto.getFieldingPosition(),
                        dto.getBattingOrderPosition()))
                .collect(Collectors.toList());
        return baseballGameStarterRepo.save(baseballGameStarters);
    }

}
