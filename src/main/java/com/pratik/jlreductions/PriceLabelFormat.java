package com.pratik.jlreductions;

import java.util.Arrays;

public enum PriceLabelFormat {
    SHOW_WAS_NOW("ShowWasNow"),
    SHOW_WAS_THEN_NOW("ShowWasThenNow"),
    // typo in spec
    SHOW_PERC_DISCOUNT("ShowPercDscount");

    public String paramName;

    PriceLabelFormat(String paramName) {
        this.paramName = paramName;
    }

    public static PriceLabelFormat getByParam(String param) {
        // if the param can't be found, default to ShowWasNow
        return Arrays.stream(values())
                .filter(label -> label.paramName.equals(param))
                .findFirst()
                .orElse(SHOW_WAS_NOW);
    }
}
