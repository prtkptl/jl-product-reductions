package com.pratik.jlreductions.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;

import java.awt.Color;
import java.util.Map;
import java.util.Optional;

public class ColorSwatch {
    private static final Map<String, Color> COLOUR_MAP = ImmutableMap.<String, Color>builder()
            .put("blue", Color.BLUE)
            .put("green", Color.GREEN)
            .put("black", Color.BLACK)
            .put("grey", Color.GRAY)
            .put("red", Color.RED)
            .put("yellow", Color.YELLOW)
            .put("orange", Color.ORANGE)
            .put("purple", new Color(128, 0, 128))
            .build();

    @JsonProperty
    public final String color;
    @JsonProperty
    public final String skuid;
    private final Color rgbColor;

    public ColorSwatch(Builder builder) {
        this.color    = builder.color;
        this.rgbColor = builder.rgbColor;
        this.skuid    = builder.skuid;
    }

    @JsonProperty("rgbColor")
    public String getRgbColor() {
        return Optional.ofNullable(rgbColor)
                .map(c -> Integer.toHexString(c.getRGB()).substring(2))
                .map(String::toUpperCase)
                .orElse("");
    }

    @JsonIgnore
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String color;
        private Color rgbColor;
        private String skuid;

        public Builder color(final String color) {
            this.color = color;
            return this;
        }

        public Builder rgbColor(final String rgbColor) {
            this.rgbColor = COLOUR_MAP.get(rgbColor.toLowerCase());
            return this;
        }

        public Builder skuid(final String skuid) {
            this.skuid = skuid;
            return this;
        }

        public ColorSwatch build() {
            return new ColorSwatch(this);
        }
    }
}
