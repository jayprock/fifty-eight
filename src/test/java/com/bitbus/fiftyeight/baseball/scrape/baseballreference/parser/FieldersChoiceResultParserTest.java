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

public class FieldersChoiceResultParserTest {

    private FieldersChoiceResultParser parser = new FieldersChoiceResultParser();

    @Test
    public void testParserFor() {
        assertTrue(parser.isParserFor("Fielder's Choice SS; Reynolds out at 3B/SS-3B-SS"));
        assertTrue(parser.isParserFor("Fielder's Choice 3B; Souza out at Hm/3B-C; Morrison to 3B"));
        assertTrue(parser.isParserFor("Fielder's Choice P/Sacrifice Bunt; Cozart to 3B; Barnhart to 2B"));
        assertTrue(parser.isParserFor("Fielder's Choice C; Grichuk out at 3B/C-3B"));
        assertTrue(parser
                .isParserFor("Double Play: Fielder's Choice P unassisted; Revere out at Hm/P; Pennington out at 3B/P"));
        assertTrue(parser.isParserFor("Fielder's Choice 1B; Pillar Scores; Barney to 2B"));
        assertTrue(parser.isParserFor(
                "Fielder's Choice 2B; Crawford Scores; Tomlinson Scores/Adv on E4 (throw); Moncrief to 2B"));
        assertTrue(parser.isParserFor(
                "Fielder's Choice P; Crawford Scores; Panik Scores/Adv on E1 (throw); Parker Scores/unER/Adv on E2 (throw); Moore to 2B/Adv on throw"));

        assertFalse(parser.isParserFor("Bunt Groundout: 3B-1B (Short 3B Line)"));
        assertFalse(parser.isParserFor("Bunt Groundout: C-2B/Sacrifice (Front of Home); Descalso to 2B"));
        assertFalse(parser.isParserFor("Bunt Popfly: 1B (Short 1B Line)"));
        assertFalse(parser.isParserFor("Bunt Lineout: P"));
        assertFalse(parser.isParserFor("Foul Bunt Popfly: 3B (Behind Home)"));
        assertFalse(parser.isParserFor("Bunt Ground Ball Double Play: Bunt 1B-SS-2B"));
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
        PlateAppearanceResultDTO dto = parser.parse("Fielder's Choice SS; Reynolds out at 3B/SS-3B-SS");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.SHORT_STOP, dto.getHitLocation());

        dto = parser.parse("Fielder's Choice 3B; Souza out at Hm/3B-C; Morrison to 3B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.THIRD_BASE, dto.getHitLocation());

        dto = parser.parse("Fielder's Choice P/Sacrifice Bunt; Cozart to 3B; Barnhart to 2B");
        assertEquals(PlateAppearanceResult.SAC_BUNT, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.BUNT, dto.getHitType());
        assertEquals(HitLocation.PITCHER, dto.getHitLocation());

        dto = parser.parse("Fielder's Choice C; Grichuk out at 3B/C-3B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.FRONT_OF_HOME, dto.getHitLocation());

        dto = parser.parse("Double Play: Fielder's Choice P unassisted; Revere out at Hm/P; Pennington out at 3B/P");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.PITCHER, dto.getHitLocation());

        dto = parser.parse("Fielder's Choice 1B; Pillar Scores; Barney to 2B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(1, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.FIRST_BASE, dto.getHitLocation());

        dto = parser.parse("Fielder's Choice 2B; Crawford Scores; Tomlinson Scores/Adv on E4 (throw); Moncrief to 2B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(1, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.SECOND_BASE, dto.getHitLocation());

        dto = parser.parse(
                "Fielder's Choice P; Crawford Scores; Panik Scores/Adv on E1 (throw); Parker Scores/unER/Adv on E2 (throw); Moore to 2B/Adv on throw");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(1, dto.getRunsBattedIn());
        assertEquals(HitType.GROUND_BALL, dto.getHitType());
        assertEquals(HitLocation.PITCHER, dto.getHitLocation());
    }

}
