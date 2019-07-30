package com.pratik.jlreductions.entities;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

public class ColorSwatchTest {
    @Test
    public void testBuildColorSwatch() {
        final String customColour = "salmon";
        final String skuId = "12345";
        ColorSwatch swatch = ColorSwatch.builder()
                .color(customColour)
                .skuid(skuId)
                .build();

        assertThat(swatch.color, is(customColour));
        assertThat(swatch.skuid, is(skuId));
    }

    @Test
    public void testGetRgbColorNoColor() {
        ColorSwatch swatch = ColorSwatch.builder().build();
        assertThat(swatch.getRgbColor(), is(isEmptyString()));
    }

    @Test
    public void testGetRgbColorValidColor() {
        ColorSwatch swatch = ColorSwatch.builder()
                .rgbColor("blue")
                .build();
        assertThat(swatch.getRgbColor(), is("0000FF"));

        // try a different case
        swatch = ColorSwatch.builder()
                .rgbColor("GREEN")
                .build();
        assertThat(swatch.getRgbColor(), is("00FF00"));
    }

    @Test
    public void testGetRgbColorInvalidColor() {
        ColorSwatch swatch = ColorSwatch.builder()
                .rgbColor("salmon")
                .build();
        assertThat(swatch.getRgbColor(), is(isEmptyString()));
    }
}
