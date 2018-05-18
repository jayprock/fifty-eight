package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;
import com.bitbus.fiftyeight.common.scrape.ex.ScrapeException;

public class WalkResultParserTest {

    private WalkResultParser parser = new WalkResultParser();

    @Test
    public void testParserFor() {
        assertTrue(parser.isParserFor("Walk"));
        assertTrue(parser.isParserFor("Walk; Judge to 2B"));
        assertTrue(parser.isParserFor("Walk; Grandal Scores; Puig to 3B; Hernandez to 2B"));
        assertTrue(parser.isParserFor("Intentional Walk"));
        assertTrue(parser.isParserFor("Hit By Pitch"));
        assertFalse(parser.isParserFor("Popfly: 3B"));
        assertFalse(parser.isParserFor("Popfly: 2B (Deep SS-2B)"));
        assertFalse(parser.isParserFor("Foul Popfly: C (Behind Home)"));
        assertFalse(parser.isParserFor("Popfly: 3B (Weak SS)"));
        assertFalse(parser.isParserFor("Foul Popfly: 1B (1B into Foul Terr.)"));
        assertFalse(parser.isParserFor("Lineout: SS"));
        assertFalse(parser.isParserFor("Lineout: LF (Deep LF-CF)"));
        assertFalse(parser.isParserFor("Lineout: CF/Sacrifice Fly (Deep CF-RF); Panik Scores"));
        assertFalse(parser.isParserFor("Double Play: Lineout: SS; Bregman out at 2B/SS-2B"));
        assertFalse(parser.isParserFor("Flyball: LF/Sacrifice Fly (Deep LF); Gordon Scores/unER"));
        assertFalse(parser.isParserFor("Foul Flyball: LF (LF into Foul Terr.)"));
        assertFalse(parser.isParserFor("Flyball: LF"));
        assertFalse(parser.isParserFor("Groundout: 3B-1B (Weak 3B)"));
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
        PlateAppearanceResultDTO dto = parser.parse("Walk");
        assertEquals(PlateAppearanceResult.WALK, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertFalse(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertNull(dto.getHitType());
        assertNull(dto.getHitLocation());

        dto = parser.parse("Walk; Judge to 2B");
        assertEquals(PlateAppearanceResult.WALK, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertFalse(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertNull(dto.getHitType());
        assertNull(dto.getHitLocation());

        dto = parser.parse("Intentional Walk");
        assertEquals(PlateAppearanceResult.INTENTIONAL_WALK, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertFalse(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertNull(dto.getHitType());
        assertNull(dto.getHitLocation());

        dto = parser.parse("Walk; Grandal Scores; Puig to 3B; Hernandez to 2B");
        assertEquals(PlateAppearanceResult.WALK, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertFalse(dto.isBallHitInPlay());
        assertEquals(1, dto.getRunsBattedIn());
        assertNull(dto.getHitType());
        assertNull(dto.getHitLocation());

        dto = parser.parse("Hit By Pitch");
        assertEquals(PlateAppearanceResult.HIT_BY_PITCH, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertFalse(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertNull(dto.getHitType());
        assertNull(dto.getHitLocation());
    }

}
