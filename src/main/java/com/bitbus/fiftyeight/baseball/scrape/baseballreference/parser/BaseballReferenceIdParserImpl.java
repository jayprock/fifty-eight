package com.bitbus.fiftyeight.baseball.scrape.baseballreference.parser;

import org.springframework.stereotype.Component;

@Component
public class BaseballReferenceIdParserImpl implements BaseballReferenceIdParser {

    @Override
    public String parse(String baseballReferenceLinkText) {
        String[] splitLink = baseballReferenceLinkText.split("/|.shtml");
        return splitLink[splitLink.length - 1];
    }

}
