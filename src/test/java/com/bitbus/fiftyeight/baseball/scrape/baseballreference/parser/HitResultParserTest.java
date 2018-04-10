package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bitbus.fiftyeight.baseball.player.plateappearance.HitLocation;
import com.bitbus.fiftyeight.baseball.player.plateappearance.HitType;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

public class HitResultParserTest {

    HitResultParser parser = new HitResultParser();

    @Test
    public void testNotParsableBy() {
        assertFalse(parser.isParserFor("Groundout: 3B-1B (Weak 3B)"));
        assertFalse(parser.isParserFor("Walk"));
        assertFalse(parser.isParserFor("Strikeout Swinging"));
        assertFalse(parser.isParserFor("Flyball: LF/Sacrifice Fly (Deep LF); Gordon Scores/unER"));
        assertFalse(parser.isParserFor("Lineout: LF (Deep LF-CF)"));
        assertFalse(parser.isParserFor("Popfly: 2B (Deep SS-2B)"));
        assertFalse(parser.isParserFor("Foul Flyball: LF (LF into Foul Terr.)"));
        assertFalse(parser.isParserFor("Wild Pitch; Cespedes to 2B"));
    }

    @Test
    public void testParsableBy() {
        assertTrue(parser.isParserFor("Single to 3B (Ground Ball to SS-3B Hole); Ellis to 2B"));
        assertTrue(parser.isParserFor("Double to LF (Line Drive to Deep LF Line); Ellis Scores/unER; Yelich to 3B"));
        assertTrue(parser.isParserFor("Triple to RF (Ground Ball); Flores Scores; Bruce Scores; Walker Scores"));
        assertTrue(parser
                .isParserFor("Home Run (Line Drive to Deep LF Line); Ellis Scores; Yelich Scores; Stanton Scores"));
        assertTrue(parser.isParserFor("Ground-rule Double (Line Drive to Deep CF)"));
        assertTrue(parser.isParserFor("Single to 3B/Bunt (Bunt to Weak 3B)"));
    }

    @Test
    public void testParseSingle() {
        PlateAppearanceResultDTO dto = parser.parse("Single to LF (Ground Ball)");
        assertTrue(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(PlateAppearanceResult.SINGLE, dto.getResult());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.LEFT_FIELD, dto.getHitLocation());
        assertEquals(0, dto.getRunsBattedIn());

        dto = parser.parse("Single to CF (Ground Ball thru SS-2B); Owings Scores; Goldschmidt Scores");
        assertTrue(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(PlateAppearanceResult.SINGLE, dto.getResult());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.SHORT_STOP_SECOND_BASE, dto.getHitLocation());
        assertEquals(2, dto.getRunsBattedIn());

        dto = parser.parse("Single to LF (Line Drive to Short LF)");
        assertTrue(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(PlateAppearanceResult.SINGLE, dto.getResult());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.SHORT_LEFT_FIELD, dto.getHitLocation());
        assertEquals(0, dto.getRunsBattedIn());

        dto = parser.parse("Single to 3B/Bunt (Bunt to Weak 3B)");
        assertTrue(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(PlateAppearanceResult.SINGLE, dto.getResult());
        assertEquals(HitType.BUNT, dto.getHitType());
        assertEquals(HitLocation.WEAK_THIRD_BASE, dto.getHitLocation());
        assertEquals(0, dto.getRunsBattedIn());
    }

    @Test
    public void testParseDouble() {
        PlateAppearanceResultDTO dto = parser.parse("Double to RF (Fly Ball to Deep CF-RF)");
        assertTrue(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(PlateAppearanceResult.DOUBLE, dto.getResult());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.DEEP_CENTER_FIELD_RIGHT_FIELD, dto.getHitLocation());
        assertEquals(0, dto.getRunsBattedIn());

        dto = parser.parse("Double to RF (Line Drive to RF Line)");
        assertTrue(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(PlateAppearanceResult.DOUBLE, dto.getResult());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.RIGHT_FIELD_LINE, dto.getHitLocation());
        assertEquals(0, dto.getRunsBattedIn());

        dto = parser.parse("Double to LF (Line Drive); Owings Scores; Goldschmidt Scores; Lamb to 3B");
        assertTrue(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(PlateAppearanceResult.DOUBLE, dto.getResult());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.LEFT_FIELD, dto.getHitLocation());
        assertEquals(2, dto.getRunsBattedIn());
    }

    @Test
    public void testParseTriple() {
        PlateAppearanceResultDTO dto =
                parser.parse("Triple to RF (Ground Ball); Flores Scores; Bruce Scores; Walker Scores");
        assertTrue(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(PlateAppearanceResult.TRIPLE, dto.getResult());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.RIGHT_FIELD, dto.getHitLocation());
        assertEquals(3, dto.getRunsBattedIn());
    }

    @Test
    public void testParseHomeRun() {
        PlateAppearanceResultDTO dto = parser.parse("Home Run (Fly Ball to Deep CF); Pederson Scores");
        assertTrue(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(PlateAppearanceResult.HOMERUN, dto.getResult());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.DEEP_CENTER_FIELD, dto.getHitLocation());
        assertEquals(2, dto.getRunsBattedIn());

        dto = parser.parse("Home Run (Line Drive to Deep LF Line); Ellis Scores; Yelich Scores; Stanton Scores");
        assertTrue(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(PlateAppearanceResult.HOMERUN, dto.getResult());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.DEEP_LEFT_FIELD_LINE, dto.getHitLocation());
        assertEquals(4, dto.getRunsBattedIn());

        dto = parser.parse("Home Run (Fly Ball to Deep LF)");
        assertTrue(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(PlateAppearanceResult.HOMERUN, dto.getResult());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.DEEP_LEFT_FIELD, dto.getHitLocation());
        assertEquals(1, dto.getRunsBattedIn());
    }

    @Test
    public void testParseGroundRuleDouble() {
        PlateAppearanceResultDTO dto = parser.parse("Ground-rule Double (Line Drive to Deep CF)");
        assertTrue(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(PlateAppearanceResult.DOUBLE, dto.getResult());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.DEEP_CENTER_FIELD, dto.getHitLocation());
        assertEquals(0, dto.getRunsBattedIn());
    }

}
