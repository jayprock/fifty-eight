package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

public class CatchersInterferenceResultParserTest {

    private CatchersInterferenceResultParser parser = new CatchersInterferenceResultParser();

    @Test
    public void testParserFor() {
        assertTrue(parser.isParserFor("Reached on Interference on C; Aoki to 2B"));
        assertFalse(parser.isParserFor("Reached on E5 (Ground Ball to Weak 3B)"));
        assertFalse(parser.isParserFor("Reached on E5 (throw) (Ground Ball to Weak 3B); Pederson to 3B; Puig to 2B"));
        assertFalse(parser.isParserFor("Reached on E4/attempted forceout (Ground Ball to Deep 2B); Pennington to 3B"));
        assertFalse(parser.isParserFor("Reached on E6 (Ground Ball); Kendrick to 3B; Herrera to 2B"));
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
        PlateAppearanceResultDTO dto = parser.parse("Reached on Interference on C; Aoki to 2B");
        assertEquals(PlateAppearanceResult.CATCHERS_INTERFERENCE, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertFalse(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertNull(dto.getHitType());
        assertNull(dto.getHitLocation());
    }
}
