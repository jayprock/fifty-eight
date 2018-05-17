package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import org.junit.Assert;
import org.junit.Test;

public class BaseballReferenceIdParserImplTest {

    private BaseballReferenceIdParserImpl parser = new BaseballReferenceIdParserImpl();

    @Test
    public void testParse() {
        Assert.assertEquals("suzukku01", parser.parse("https://www.baseball-reference.com/players/s/suzukku01.shtml"));
        Assert.assertEquals("dicker.01", parser.parse("https://www.baseball-reference.com/players/d/dicker.01.shtml"));
        Assert.assertEquals("sabatc.01", parser.parse("https://www.baseball-reference.com/players/s/sabatc.01.shtml"));
        Assert.assertEquals("BAL201704080",
                parser.parse("https://www.baseball-reference.com/boxes/BAL/BAL201704080.shtml"));
    }
}
