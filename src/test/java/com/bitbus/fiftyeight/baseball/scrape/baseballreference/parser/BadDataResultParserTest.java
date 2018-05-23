package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;
import com.bitbus.fiftyeight.common.scrape.ex.ScrapeException;

public class BadDataResultParserTest {

    private BadDataResultParser parser = new BadDataResultParser();

    @Test
    public void testIsParserFor() {
        assertTrue(parser.isParserFor("Double"));

        assertFalse(parser.isParserFor("Groundout: 3B-1B (Weak 3B)"));
        assertFalse(parser.isParserFor("Walk"));
        assertFalse(parser.isParserFor("Strikeout Swinging"));
        assertFalse(parser.isParserFor("Flyball: LF/Sacrifice Fly (Deep LF); Gordon Scores/unER"));
        assertFalse(parser.isParserFor("Lineout: LF (Deep LF-CF)"));
        assertFalse(parser.isParserFor("Popfly: 2B (Deep SS-2B)"));
        assertFalse(parser.isParserFor("Foul Flyball: LF (LF into Foul Terr.)"));
        assertFalse(parser.isParserFor("Wild Pitch; Cespedes to 2B"));
        assertFalse(parser.isParserFor("Single to 3B (Ground Ball to SS-3B Hole); Ellis to 2B"));
        assertFalse(parser.isParserFor("Double to LF (Line Drive to Deep LF Line); Ellis Scores/unER; Yelich to 3B"));
        assertFalse(parser.isParserFor("Triple to RF (Ground Ball); Flores Scores; Bruce Scores; Walker Scores"));
        assertFalse(parser
                .isParserFor("Home Run (Line Drive to Deep LF Line); Ellis Scores; Yelich Scores; Stanton Scores"));
        assertFalse(parser.isParserFor("Single to 3B/Bunt (Bunt to Weak 3B)"));
        assertFalse(parser.isParserFor("Single to 2B"));
        assertFalse(parser.isParserFor("Double/Fan Interference (Line Drive to Deep LF Line)"));
        assertFalse(parser.isParserFor("Single (Deep 2B-1B); Davis stays at 2B"));
        assertFalse(parser.isParserFor("Ground-rule Double (Line Drive to Deep CF)"));
        assertFalse(parser.isParserFor("Ground-rule Double/Fan Interference; Escobar Scores"));
        assertFalse(parser.isParserFor("Ground-rule Double (Ground Ball); Napoli Scores"));
        assertFalse(parser.isParserFor("Ground-rule Double (SS-3B Hole)"));
        assertFalse(parser.isParserFor("Single to P (SS-2B)"));
        assertFalse(parser.isParserFor("Single to SS/Bunt"));
        assertFalse(parser
                .isParserFor("Single/runner struck by batted ball and is out (Line Drive); Stanton out at 2B/2B"));
        assertFalse(
                parser.isParserFor("Single/runner struck by batted ball and is out (Line Drive); De Aza out at 2B/2B"));
        assertFalse(parser.isParserFor("Inside-the-park Home Run to CF (Fly Ball to Deep LF-CF)"));
        assertFalse(parser.isParserFor("Double/Fan Interference (Ground Ball); Miller Scores/unER"));
        assertFalse(parser
                .isParserFor("Single to LF (Line Drive to LF-CF); Castro Scores/unER/Adv on E7/No RBI; Judge to 3B"));
        assertFalse(parser.isParserFor("Home Run (Fly Ball)"));
        assertFalse(parser.isParserFor("Double to LF (SS-3B Hole)"));
        assertFalse(parser.isParserFor("Double (Line Drive to Deep LF); Maybin Scores"));
        assertFalse(parser.isParserFor("Single to C (Bunt Popup to Short 1B Line); Villar to 3B; Franklin to 2B"));
        assertFalse(parser.isParserFor("Single to 2B/Bunt; Wong to 2B"));
        assertFalse(parser.isParserFor(
                "Single to RF (Line Drive); Zimmer Scores/unER; Brantley Scores/Adv on E9 (throw)/unER/No RBI; Ramirez to 3B/Adv on throw"));
        assertFalse(parser.isParserFor("Double to LF/Fan Interference (Ground Ball)"));
        assertFalse(parser.isParserFor("Double to RF/Fan Interference; Urshela Scores"));
    }

    @Test
    public void testParse() throws ScrapeException {
        PlateAppearanceResultDTO dto = parser.parse("Double");
        assertTrue(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(PlateAppearanceResult.DOUBLE, dto.getResult());
        assertNull(dto.getHitType());
        assertNull(dto.getHitLocation());
        assertEquals(0, dto.getRunsBattedIn());
    }
}
