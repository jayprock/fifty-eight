package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bitbus.fiftyeight.baseball.player.plateappearance.HitLocation;
import com.bitbus.fiftyeight.baseball.player.plateappearance.HitType;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

public class BuntResultParserTest {

    private BuntResultParser parser = new BuntResultParser();

    @Test
    public void testParserFor() {
        assertTrue(parser.isParserFor("Bunt Groundout: 3B-1B (Short 3B Line)"));
        assertTrue(parser.isParserFor("Bunt Groundout: C-2B/Sacrifice (Front of Home); Descalso to 2B"));
        assertTrue(parser.isParserFor("Bunt Popfly: 1B (Short 1B Line)"));
        assertTrue(parser.isParserFor("Bunt Lineout: P"));
        assertTrue(parser.isParserFor("Foul Bunt Popfly: 3B (Behind Home)"));
        assertTrue(parser.isParserFor("Bunt Ground Ball Double Play: Bunt 1B-SS-2B"));
        assertFalse(parser.isParserFor("Popfly: 3B"));
        assertFalse(parser.isParserFor("Popfly: 2B (Deep SS-2B)"));
        assertFalse(parser.isParserFor("Foul Popfly: C (Behind Home)"));
        assertFalse(parser.isParserFor("Popfly: 3B (Weak SS)"));
        assertFalse(parser.isParserFor("Foul Popfly: 1B (1B into Foul Terr.)"));
        assertFalse(parser.isParserFor("Popfly: SS (Deep SS-3B Hole)"));
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
    public void testParse() {
        PlateAppearanceResultDTO dto = parser.parse("Bunt Groundout: 3B-1B (Short 3B Line)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.BUNT, dto.getHitType());
        assertEquals(HitLocation.SHORT_THIRD_BASE_LINE, dto.getHitLocation());

        dto = parser.parse("Bunt Groundout: C-2B/Sacrifice (Front of Home); Descalso to 2B");
        assertEquals(PlateAppearanceResult.SAC_BUNT, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.BUNT, dto.getHitType());
        assertEquals(HitLocation.FRONT_OF_HOME, dto.getHitLocation());

        dto = parser.parse("Bunt Popfly: 1B (Short 1B Line)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.BUNT, dto.getHitType());
        assertEquals(HitLocation.SHORT_FIRST_BASE_LINE, dto.getHitLocation());

        dto = parser.parse("Bunt Lineout: P");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.BUNT, dto.getHitType());
        assertEquals(HitLocation.PITCHER, dto.getHitLocation());

        dto = parser.parse("Foul Bunt Popfly: 3B (Behind Home)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.BUNT, dto.getHitType());
        assertEquals(HitLocation.BEHIND_HOME, dto.getHitLocation());

        dto = parser.parse("Bunt Ground Ball Double Play: Bunt 1B-SS-2B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.BUNT, dto.getHitType());
        assertEquals(HitLocation.FIRST_BASE, dto.getHitLocation());

    }
}
