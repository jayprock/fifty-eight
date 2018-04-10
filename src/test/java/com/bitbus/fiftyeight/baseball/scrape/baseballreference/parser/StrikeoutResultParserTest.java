package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

public class StrikeoutResultParserTest {

    private StrikeoutResultParser parser = new StrikeoutResultParser();

    @Test
    public void testParserFor() {
        assertTrue(parser.isParserFor("Strikeout Swinging"));
        assertTrue(parser.isParserFor("Strikeout Looking"));
        assertTrue(parser.isParserFor("Strikeout (foul bunt)"));
    }

    @Test
    public void testNotParserFor() {
        assertFalse(parser.isParserFor("Flyball: LF/Sacrifice Fly (Deep LF); Gordon Scores/unER"));
        assertFalse(parser.isParserFor("Foul Flyball: LF (LF into Foul Terr.)"));
        assertFalse(parser.isParserFor("Flyball: LF"));
        assertFalse(parser.isParserFor("Groundout: 3B-1B (Weak 3B)"));
        assertFalse(parser.isParserFor("Walk"));
        assertFalse(parser.isParserFor("Lineout: LF (Deep LF-CF)"));
        assertFalse(parser.isParserFor("Popfly: 2B (Deep SS-2B)"));
        assertFalse(parser.isParserFor("Wild Pitch; Cespedes to 2B"));
        assertFalse(parser.isParserFor("Single to 3B (Ground Ball to SS-3B Hole); Ellis to 2B"));
        assertFalse(parser.isParserFor("Double to LF (Line Drive to Deep LF Line); Ellis Scores/unER; Yelich to 3B"));
        assertFalse(parser.isParserFor("Triple to RF (Ground Ball); Flores Scores; Bruce Scores; Walker Scores"));
        assertFalse(parser
                .isParserFor("Home Run (Line Drive to Deep LF Line); Ellis Scores; Yelich Scores; Stanton Scores"));
        assertFalse(parser.isParserFor("Ground-rule Double (Line Drive to Deep CF)"));
    }

    @Test
    public void testParse() {
        PlateAppearanceResultDTO dto = parser.parse("Strikeout Swinging");
        assertEquals(PlateAppearanceResult.STRIKEOUT_SWINGING, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertFalse(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertNull(dto.getHitType());
        assertNull(dto.getHitLocation());

        dto = parser.parse("Strikeout Looking");
        assertEquals(PlateAppearanceResult.STRIKEOUT_LOOKING, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertFalse(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertNull(dto.getHitType());
        assertNull(dto.getHitLocation());

        dto = parser.parse("Strikeout (foul bunt)");
        assertEquals(PlateAppearanceResult.STRIKEOUT_BUNTING, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertFalse(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertNull(dto.getHitType());
        assertNull(dto.getHitLocation());
    }
}
