package com.pratik.jlreductions;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PriceLabelFormatTest {
    @Test
    public void testGetByParam() {
        assertThat(PriceLabelFormat.getByParam("ShowWasNow"), is(PriceLabelFormat.SHOW_WAS_NOW));
        assertThat(PriceLabelFormat.getByParam("ShowWasThenNow"), is(PriceLabelFormat.SHOW_WAS_THEN_NOW));
        assertThat(PriceLabelFormat.getByParam("ShowPercDscount"), is(PriceLabelFormat.SHOW_PERC_DISCOUNT));
        // test default
        assertThat(PriceLabelFormat.getByParam("ShowInvalid"), is(PriceLabelFormat.SHOW_WAS_NOW));
    }
}
