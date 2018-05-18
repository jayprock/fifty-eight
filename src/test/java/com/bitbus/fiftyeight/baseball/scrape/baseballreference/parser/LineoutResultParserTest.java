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

public class LineoutResultParserTest {

    private LineoutResultParser parser = new LineoutResultParser();

    @Test
    public void testParserFor() {
        assertTrue(parser.isParserFor("Lineout: SS"));
        assertTrue(parser.isParserFor("Lineout: 3B (SS-3B Hole)"));
        assertTrue(parser.isParserFor("Lineout: LF (Deep LF-CF)"));
        assertTrue(parser.isParserFor("Lineout: CF/Sacrifice Fly (Deep CF-RF); Panik Scores"));
        assertTrue(parser.isParserFor("Double Play: Lineout: SS; Bregman out at 2B/SS-2B"));
        assertTrue(parser.isParserFor("Double Play: Lineout: 1B unassisted; Barnhart out at 1B/1B"));
        assertTrue(parser.isParserFor("Foul Lineout: RF (RF into Foul Terr."));
        assertTrue(parser.isParserFor("Lineout: RF/Sacrifice Fly; Hernandez Scores; Kendrick to 3B"));
        assertTrue(parser.isParserFor("Lineout: P-1B; Polanco to 3B"));
        assertTrue(parser
                .isParserFor("Double Play: Foul Lineout: 1B unassisted (1B into Foul Terr.); Hicks out at 1B/1B"));
        assertTrue(parser.isParserFor("Lineout: LF; Goldschmidt to 3B/Adv on E7 (throw)"));
        assertTrue(parser.isParserFor("Line Drive Double Play: P; Hosmer out at 2B/P-2B"));


        assertFalse(parser.isParserFor("Flyball: LF/Sacrifice Fly (Deep LF); Gordon Scores/unER"));
        assertFalse(parser.isParserFor("Foul Flyball: LF (LF into Foul Terr.)"));
        assertFalse(parser.isParserFor("Flyball: LF"));
        assertFalse(parser.isParserFor("Groundout: 3B-1B (Weak 3B)"));
        assertFalse(parser.isParserFor("Walk"));
        assertFalse(parser.isParserFor("Popfly: 2B (Deep SS-2B)"));
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
        PlateAppearanceResultDTO dto = parser.parse("Lineout: SS");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.SHORT_STOP, dto.getHitLocation());

        dto = parser.parse("Lineout: 3B (SS-3B Hole)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.THIRD_BASE_SHORT_STOP, dto.getHitLocation());

        dto = parser.parse("Lineout: LF (Deep LF-CF)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.DEEP_LEFT_FIELD_CENTER_FIELD, dto.getHitLocation());

        dto = parser.parse("Lineout: CF/Sacrifice Fly (Deep CF-RF); Panik Scores");
        assertEquals(PlateAppearanceResult.SAC_FLY, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(1, dto.getRunsBattedIn());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.DEEP_CENTER_FIELD_RIGHT_FIELD, dto.getHitLocation());

        dto = parser.parse("Double Play: Lineout: SS; Bregman out at 2B/SS-2B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.SHORT_STOP, dto.getHitLocation());

        dto = parser.parse("Double Play: Lineout: 1B unassisted; Barnhart out at 1B/1B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.FIRST_BASE, dto.getHitLocation());

        dto = parser.parse("Foul Lineout: RF (RF into Foul Terr.");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.RIGHT_FIELD_FOUL_TERRITORY, dto.getHitLocation());

        dto = parser.parse("Lineout: RF/Sacrifice Fly; Hernandez Scores; Kendrick to 3B");
        assertEquals(PlateAppearanceResult.SAC_FLY, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(1, dto.getRunsBattedIn());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.RIGHT_FIELD, dto.getHitLocation());

        dto = parser.parse("Lineout: P-1B; Polanco to 3B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.PITCHER, dto.getHitLocation());

        dto = parser.parse("Double Play: Foul Lineout: 1B unassisted (1B into Foul Terr.); Hicks out at 1B/1B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.FIRST_BASE_FOUL_TERRITORY, dto.getHitLocation());

        dto = parser.parse("Lineout: LF; Goldschmidt to 3B/Adv on E7 (throw)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.LEFT_FIELD, dto.getHitLocation());

        dto = parser.parse("Line Drive Double Play: P; Hosmer out at 2B/P-2B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.LINE_DRIVE, dto.getHitType());
        assertEquals(HitLocation.PITCHER, dto.getHitLocation());
    }
}
