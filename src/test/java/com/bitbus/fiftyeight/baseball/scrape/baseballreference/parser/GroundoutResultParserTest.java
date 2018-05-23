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
import com.bitbus.fiftyeight.common.scrape.ex.WarningScrapeException;

public class GroundoutResultParserTest {

    private GroundoutResultParser parser = new GroundoutResultParser();

    @Test
    public void testParserFor() {
        assertTrue(parser.isParserFor("Groundout: SS-1B"));
        assertTrue(parser.isParserFor("Groundout: SS-1B (SS-3B Hole)"));
        assertTrue(parser.isParserFor("Groundout: SS-2B/Forceout at 2B"));
        assertTrue(parser.isParserFor("Groundout: 1B unassisted"));
        assertTrue(parser.isParserFor("Groundout: P unassisted (Short 1B Line)"));
        assertTrue(parser.isParserFor("Groundout: 3B unassisted/Forceout at 3B; Seager to 2B"));
        assertTrue(parser.isParserFor("Groundout: P-1B (P's Left)"));
        assertTrue(parser.isParserFor("Groundout: 3B-1B (Weak 3B)"));
        assertTrue(parser.isParserFor("Groundout: 2B-SS/Forceout at 2B (Weak 2B); Gamel Scores; Haniger to 3B"));
        assertTrue(parser.isParserFor("Ground Ball Double Play: 3B-2B-1B"));
        assertTrue(parser.isParserFor("Double Play: Groundout: P-1B (Weak 2B-1B); Kemp out at 3B/1B-SS"));
        assertTrue(parser.isParserFor("Double Play: Groundout: 1B; Cardullo out at Hm/1B-C; Freeland to 2B"));
        assertTrue(parser.isParserFor("Groundout: 1B-SS/Forceout at 2B; Romine to 2B/Adv on E6 (throw)"));
        assertTrue(parser.isParserFor("Ground Ball Triple Play: SS-2B-1B (Deep SS-3B Hole)"));
        assertTrue(parser.isParserFor("Ground Ball Double Play: SS-2B-1B; Dietrich Scores/No RBI"));
        assertTrue(parser.isParserFor("Ground Ball Double Play: 2B-SS-1B (2B-1B); Cozart Scores/No RBI; Votto to 3B"));
        assertTrue(parser.isParserFor(
                "Groundout: SS-2B/Forceout at 2B; Bautista Scores; Donaldson Scores/unER/Adv on E4 (throw)/No RBI"));
        assertTrue(parser.isParserFor("Groundout: SS-2B/Forceout at 2B; DeJong Scores/unER; Fowler Scores/unER"));

        assertFalse(parser.isParserFor("Ground Ball Double Play: Bunt 1B-2B-SS"));
        assertFalse(parser.isParserFor("Popfly: 3B"));
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
        PlateAppearanceResultDTO dto = parser.parse("Groundout: SS-1B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.SHORT_STOP, dto.getHitLocation());

        dto = parser.parse("Groundout: SS-1B (SS-3B Hole)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.THIRD_BASE_SHORT_STOP, dto.getHitLocation());

        dto = parser.parse("Groundout: SS-2B/Forceout at 2B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.SHORT_STOP, dto.getHitLocation());

        dto = parser.parse("Groundout: 1B unassisted");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.FIRST_BASE, dto.getHitLocation());

        dto = parser.parse("Groundout: P unassisted (Short 1B Line)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.SHORT_FIRST_BASE_LINE, dto.getHitLocation());

        dto = parser.parse("Groundout: 3B unassisted/Forceout at 3B; Seager to 2B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.THIRD_BASE, dto.getHitLocation());

        dto = parser.parse("Groundout: P-1B (P's Left)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.PITCHERS_LEFT, dto.getHitLocation());

        dto = parser.parse("Groundout: 3B-1B (Weak 3B)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.WEAK_THIRD_BASE, dto.getHitLocation());

        dto = parser.parse("Groundout: 2B-SS/Forceout at 2B (Weak 2B); Gamel Scores; Haniger to 3B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(1, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.WEAK_SECOND_BASE, dto.getHitLocation());

        dto = parser.parse("Ground Ball Double Play: 3B-2B-1B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.THIRD_BASE, dto.getHitLocation());

        dto = parser.parse("Double Play: Groundout: P-1B (Weak 2B-1B); Kemp out at 3B/1B-SS");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.WEAK_SECOND_BASE_FIRST_BASE, dto.getHitLocation());

        dto = parser.parse("Double Play: Groundout: 1B; Cardullo out at Hm/1B-C; Freeland to 2B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.FIRST_BASE, dto.getHitLocation());

        dto = parser.parse("Groundout: 1B-SS/Forceout at 2B; Romine to 2B/Adv on E6 (throw)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.FIRST_BASE, dto.getHitLocation());

        dto = parser.parse("Ground Ball Triple Play: SS-2B-1B (Deep SS-3B Hole)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.DEEP_THIRD_BASE_SHORT_STOP, dto.getHitLocation());

        dto = parser.parse("Ground Ball Double Play: SS-2B-1B; Dietrich Scores/No RBI");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.SHORT_STOP, dto.getHitLocation());

        dto = parser.parse("Ground Ball Double Play: 2B-SS-1B (2B-1B); Cozart Scores/No RBI; Votto to 3B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.SECOND_BASE_FIRST_BASE, dto.getHitLocation());

        dto = parser.parse(
                "Groundout: SS-2B/Forceout at 2B; Bautista Scores; Donaldson Scores/unER/Adv on E4 (throw)/No RBI");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(1, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.SHORT_STOP, dto.getHitLocation());

        try {
            // TODO - drop warning exception
            dto = parser.parse("Groundout: SS-2B/Forceout at 2B; DeJong Scores/unER; Fowler Scores/unER");
            assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
            assertFalse(dto.isHit());
            assertTrue(dto.isQualifiedAtBat());
            assertTrue(dto.isBallHitInPlay());
            assertEquals(2, dto.getRunsBattedIn());
            assertEquals(HitType.GROUND_BALL, dto.getHitType());
            assertEquals(HitLocation.SHORT_STOP, dto.getHitLocation());
        } catch (WarningScrapeException e) {

        }
    }

}
