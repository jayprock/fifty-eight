package com.bitbus.fiftyeight.common.scrape;

import com.bitbus.fiftyeight.common.Result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrapeResult {

    private Result result;
    private String message;

    public ScrapeResult(Result result) {
        this.result = result;
    }
}
