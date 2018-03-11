package com.bitbus.fiftyeight.baseball.matchup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseballMatchupServiceImpl implements BaseballMatchupService {

    @Autowired
    private BaseballMatchupRepository baseballMatchupRepo;


    @Override
    public BaseballMatchup save(BaseballMatchup matchup) {
        return baseballMatchupRepo.save(matchup);
    }

    @Override
    public BaseballMatchup findByBaseballReferenceId(String baseballReferenceId) {
        return baseballMatchupRepo.findByBaseballReferenceId(baseballReferenceId);
    }

    @Override
    public boolean matchupExistsForBaseballReferenceId(String baseballReferenceId) {
        return findByBaseballReferenceId(baseballReferenceId) != null;
    }
}
