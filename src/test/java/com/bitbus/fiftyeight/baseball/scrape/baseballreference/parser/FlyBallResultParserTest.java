package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bitbus.fiftyeight.baseball.player.plateappearance.HitLocation;
import com.bitbus.fiftyeight.baseball.player.plateappearance.HitType;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResult;
import com.bitbus.fiftyeight.baseball.player.plateappearance.PlateAppearanceResultDTO;

public class FlyBallResultParserTest {

    private FlyBallResultParser parser = new FlyBallResultParser();

    @Test
    public void testParserFor() {
        assertTrue(parser.isParserFor("Flyball: LF/Sacrifice Fly (Deep LF); Gordon Scores/unER"));
        assertTrue(parser.isParserFor("Foul Flyball: LF (LF into Foul Terr.)"));
        assertTrue(parser.isParserFor("Flyball: LF"));
        assertTrue(parser.isParserFor("Flyball: RF; Toles to 3B"));
        assertTrue(parser.isParserFor("Double Play: Flyball: RF (Deep CF-RF); Aoki out at 1B/RF-1B"));
        assertTrue(parser.isParserFor("Flyball: LF/Sacrifice Fly; Marisnick Scores"));
        assertTrue(parser.isParserFor("Flyball: LF (Deep SS-3B Hole)"));
        assertTrue(
                parser.isParserFor("Flyball: RF; Mercer Scores/unER/Adv on E9 (throw); Marte to 3B; McCutchen to 2B"));
        assertTrue(parser.isParserFor("Double Play: Flyball: LF; Altherr out at Hm/LF-C"));
        assertTrue(
                parser.isParserFor("Double Play: Foul Flyball: LF (LF into Foul Terr.); Benintendi out at 2B/LF-2B"));
    }

    @Test
    public void testNotParserFor() {
        assertFalse(parser.isParserFor("Groundout: 3B-1B (Weak 3B)"));
        assertFalse(parser.isParserFor("Walk"));
        assertFalse(parser.isParserFor("Strikeout Swinging"));
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
        PlateAppearanceResultDTO dto = parser.parse("Flyball: LF");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.LEFT_FIELD, dto.getHitLocation());

        dto = parser.parse("Flyball: LF/Sacrifice Fly (Deep LF); Gordon Scores/unER");
        assertEquals(PlateAppearanceResult.SAC_FLY, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(1, dto.getRunsBattedIn());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.DEEP_LEFT_FIELD, dto.getHitLocation());

        dto = parser.parse("Foul Flyball: LF (LF into Foul Terr.)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.LEFT_FIELD_FOUL_TERRITORY, dto.getHitLocation());

        dto = parser.parse("Flyball: RF; Toles to 3B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.RIGHT_FIELD, dto.getHitLocation());

        dto = parser.parse("Double Play: Flyball: RF (Deep CF-RF); Aoki out at 1B/RF-1B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.DEEP_CENTER_FIELD_RIGHT_FIELD, dto.getHitLocation());

        dto = parser.parse("Flyball: LF/Sacrifice Fly; Marisnick Scores");
        assertEquals(PlateAppearanceResult.SAC_FLY, dto.getResult());
        assertFalse(dto.isHit());
        assertFalse(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(1, dto.getRunsBattedIn());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.LEFT_FIELD, dto.getHitLocation());

        dto = parser.parse("Flyball: LF (Deep SS-3B Hole)");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.DEEP_THIRD_BASE_SHORT_STOP, dto.getHitLocation());

        dto = parser.parse("Flyball: RF; Mercer Scores/unER/Adv on E9 (throw); Marte to 3B; McCutchen to 2B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.RIGHT_FIELD, dto.getHitLocation());

        dto = parser.parse("Double Play: Flyball: LF; Altherr out at Hm/LF-C");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.LEFT_FIELD, dto.getHitLocation());

        dto = parser.parse("Double Play: Foul Flyball: LF (LF into Foul Terr.); Benintendi out at 2B/LF-2B");
        assertEquals(PlateAppearanceResult.BALL_IN_PLAY_OUT, dto.getResult());
        assertFalse(dto.isHit());
        assertTrue(dto.isQualifiedAtBat());
        assertTrue(dto.isBallHitInPlay());
        assertEquals(0, dto.getRunsBattedIn());
        assertEquals(HitType.FLYBALL, dto.getHitType());
        assertEquals(HitLocation.LEFT_FIELD_FOUL_TERRITORY, dto.getHitLocation());

    }
}
