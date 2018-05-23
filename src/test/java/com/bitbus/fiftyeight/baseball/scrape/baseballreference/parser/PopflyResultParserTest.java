package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bitbus.fiftyeight.baseball.player.plateappearance.HitLocation;
import com.bitbus.fiftyeight.baseball.player.plateappearance.HitType;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;
import com.bitbus.fiftyeight.common.scrape.ex.ScrapeException;

public class PopflyResultParserTest {

    private PopflyResultParser parser = new PopflyResultParser();

    @Test
    public void testParserFor() {
        assertTrue(parser.isParserFor("Popfly: 3B"));
        assertTrue(parser.isParserFor("Popfly: 2B (Deep SS-2B)"));
        assertTrue(parser.isParserFor("Foul Popfly: C (Behind Home)"));
        assertTrue(parser.isParserFor("Popfly: 3B (Weak SS)"));
        assertTrue(parser.isParserFor("Foul Popfly: 1B (1B into Foul Terr.)"));
        assertTrue(parser.isParserFor("Popfly: SS (Deep SS-3B Hole)"));
        assertTrue(parser.isParserFor("Popfly: 1B/Interference by Runner/Forceout at 2B; Travis to 1B"));
        assertTrue(parser.isParserFor("Double Play: Popfly: SS (Short CF); Gomez out at 1B/SS-1B"));
        assertTrue(parser.isParserFor("Double Play: Popfly: 1B; Panik out at 2B/1B-SS"));
        assertTrue(parser.isParserFor("Popfly: 1B-SS/Forceout at 2B"));
        assertTrue(parser.isParserFor("Double Play: Foul Popfly: 2B (1B into Foul Terr.); Blackmon out at 1B/2B-P"));
        assertTrue(parser.isParserFor("Foul Popfly: 2B/Sacrifice Fly (RF into Foul Terr.); Perez Scores"));
        assertTrue(parser.isParserFor("Popfly: 2B-SS/Forceout at 2B (Deep 2B-1B); Hechavarria Scores; Sucre to 3B"));

        assertFalse(parser.isParserFor("Lineout: SS"));
        assertFalse(parser.isParserFor("Lineout: LF (Deep LF-CF)"));
        assertFalse(parser.isParserFor("Lineout: CF/Sacrifice Fly (Deep CF-RF); Panik Scores"));
        assertFalse(parser.isParserFor("Double Play: Lineout: SS; Bregman out at 2B/SS-2B"));
        assertFalse(parser.isParserFor("Flyball: LF/Sacrifice Fly (Deep LF); Gordon Scores/unER"));
        assertFalse(parser.isParserFor("Foul Flyball: LF (LF into Foul Terr.)"));
        assertFalse(parser.isParserFor("Flyball: LF"));
        assertFalse(parser.isParserFor("Groundout: 3B-1B (Weak 3B)"));
        assertFalse(parser.isParserFor("Walk"));
        assertFalse(parser.isParserFor("Wild Pitch; Cespedes to 2B"));
        assertFalse(parser.isParserFor("Single to 3B (Ground Ball to SS-3B Hole); Ellis to 2B"));
        assertFalse(parser.isParserFor("Double to LF (Line Drive to Deep LF Line); Ellis Scores/unER; Yelich to 3B"));
        assertFalse(parser.isParserFor("Triple to RF (Ground Ball); Flores Scores; Bruce Scores; Walker Scores"));
        assertFalse(parser
                .isParserFor("Home Run (Line Drive to Deep LF Line); Ellis Scores; Yelich Scores; Stanton Scores"));
        assertFalse(parser.isParserFor("Ground-rule Double (Line Drive to Deep CF)"));
        assertFalse(parser.isParserFor("Strikeout Swinging"));
        assertFalse(parser.isParserFor("Strikeout Looking"));
    }

    @Test
    public void testParse() throws ScrapeException {
        PlateAppearanceResultDTO dto = parser.parse("Popfly: 3B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.POPFLY, dto.getHitType());
        assertEquals(HitLocation.THIRD_BASE, dto.getHitLocation());

        dto = parser.parse("Popfly: 2B (Deep SS-2B)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.POPFLY, dto.getHitType());
        assertEquals(HitLocation.DEEP_SHORT_STOP_SECOND_BASE, dto.getHitLocation());

        dto = parser.parse("Foul Popfly: C (Behind Home)\"");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.POPFLY, dto.getHitType());
        assertEquals(HitLocation.BEHIND_HOME, dto.getHitLocation());

        dto = parser.parse("Popfly: 3B (Weak SS)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.POPFLY, dto.getHitType());
        assertEquals(HitLocation.WEAK_SHORT_STOP, dto.getHitLocation());

        dto = parser.parse("Foul Popfly: 1B (1B into Foul Terr.)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.POPFLY, dto.getHitType());
        assertEquals(HitLocation.FIRST_BASE_FOUL_TERRITORY, dto.getHitLocation());

        dto = parser.parse("Popfly: SS (Deep SS-3B Hole)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.POPFLY, dto.getHitType());
        assertEquals(HitLocation.DEEP_THIRD_BASE_SHORT_STOP, dto.getHitLocation());

        dto = parser.parse("Double Play: Popfly: SS (Short CF); Gomez out at 1B/SS-1B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.POPFLY, dto.getHitType());
        assertEquals(HitLocation.SHORT_CENTER_FIELD, dto.getHitLocation());

        dto = parser.parse("Popfly: 1B/Interference by Runner/Forceout at 2B; Travis to 1B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.POPFLY, dto.getHitType());
        assertEquals(HitLocation.FIRST_BASE, dto.getHitLocation());

        dto = parser.parse("Double Play: Popfly: 1B; Panik out at 2B/1B-SS");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.POPFLY, dto.getHitType());
        assertEquals(HitLocation.FIRST_BASE, dto.getHitLocation());

        dto = parser.parse("Popfly: 1B-SS/Forceout at 2B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.POPFLY, dto.getHitType());
        assertEquals(HitLocation.FIRST_BASE, dto.getHitLocation());

        dto = parser.parse("Double Play: Foul Popfly: 2B (1B into Foul Terr.); Blackmon out at 1B/2B-P");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.POPFLY, dto.getHitType());
        assertEquals(HitLocation.FIRST_BASE_FOUL_TERRITORY, dto.getHitLocation());

        dto = parser.parse("Foul Popfly: 2B/Sacrifice Fly (RF into Foul Terr.); Perez Scores");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(1, dto.getRunsBattedIn());
        assertEquals(HitType.POPFLY, dto.getHitType());
        assertEquals(HitLocation.RIGHT_FIELD_FOUL_TERRITORY, dto.getHitLocation());

        dto = parser.parse("Popfly: 2B-SS/Forceout at 2B (Deep 2B-1B); Hechavarria Scores; Sucre to 3B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(1, dto.getRunsBattedIn());
        assertEquals(HitType.POPFLY, dto.getHitType());
        assertEquals(HitLocation.DEEP_SECOND_BASE_FIRST_BASE, dto.getHitLocation());
    }
}
