package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bitbus.fiftyeight.baseball.player.plateappearance.HitLocation;
import com.bitbus.fiftyeight.baseball.player.plateappearance.HitType;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;
import com.bitbus.fiftyeight.common.scrape.ex.ScrapeException;

public class ErrorResultParserTest {

    private ErrorResultParser parser = new ErrorResultParser();

    @Test
    public void testParserFor() {
        assertTrue(parser.isParserFor("Reached on E5 (Ground Ball to Weak 3B)"));
        assertTrue(parser.isParserFor("Reached on E5 (throw) (Ground Ball to Weak 3B); Pederson to 3B; Puig to 2B"));
        assertTrue(parser.isParserFor("Reached on E4/attempted forceout (Ground Ball to Deep 2B); Pennington to 3B"));
        assertTrue(parser.isParserFor("Reached on E6 (Ground Ball); Kendrick to 3B; Herrera to 2B"));
        assertTrue(parser.isParserFor("Reached on E5 (Ground Ball to SS-3B Hole)"));
        assertTrue(parser.isParserFor("Reached on E9/Sacrifice Fly; Donaldson Scores"));
        assertTrue(parser.isParserFor(
                "Reached on E5/attempted forceout (Ground Ball to Weak 3B); Votto Scores/unER/Adv on E5 (throw)/No RBI; Duvall to 3B"));
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
    public void testParse() throws ScrapeException {
        PlateAppearanceResultDTO dto = parser.parse("Reached on E5 (Ground Ball to Weak 3B)");
        assertEquals(PlateAppearanceResult.REACH_ON_ERROR, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.WEAK_THIRD_BASE, dto.getHitLocation());

        dto = parser.parse("Reached on E5 (throw) (Ground Ball to Weak 3B); Pederson to 3B; Puig to 2B");
        assertEquals(PlateAppearanceResult.REACH_ON_ERROR, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.WEAK_THIRD_BASE, dto.getHitLocation());

        dto = parser.parse("Reached on E4/attempted forceout (Ground Ball to Deep 2B); Pennington to 3B");
        assertEquals(PlateAppearanceResult.REACH_ON_ERROR, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.DEEP_SECOND_BASE, dto.getHitLocation());

        dto = parser.parse("Reached on E6 (Ground Ball); Kendrick to 3B; Herrera to 2B");
        assertEquals(PlateAppearanceResult.REACH_ON_ERROR, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertNull(dto.getHitLocation());

        dto = parser.parse("Reached on E5 (Ground Ball to SS-3B Hole)");
        assertEquals(PlateAppearanceResult.REACH_ON_ERROR, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.THIRD_BASE_SHORT_STOP, dto.getHitLocation());

        dto = parser.parse("Reached on E9/Sacrifice Fly; Donaldson Scores");
        assertEquals(PlateAppearanceResult.SAC_FLY, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(1, dto.getRunsBattedIn());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.RIGHT_FIELD, dto.getHitLocation());

        dto = parser.parse(
                "Reached on E5/attempted forceout (Ground Ball to Weak 3B); Votto Scores/unER/Adv on E5 (throw)/No RBI; Duvall to 3B");
        assertEquals(PlateAppearanceResult.REACH_ON_ERROR, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.WEAK_THIRD_BASE, dto.getHitLocation());

    }

}
