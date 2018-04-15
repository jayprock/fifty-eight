package com.bitbus.fiftyeight.baseball.player.plateappearance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlateAppearanceServiceImpl implements PlateAppearanceService {

    @Autowired
    private PlateAppearanceRepository plateAppearanceRepo;


    @Override
    public List<PlateAppearance> save(List<PlateAppearance> plateAppearances) {
        return plateAppearanceRepo.saveAll(plateAppearances);
    }

}
