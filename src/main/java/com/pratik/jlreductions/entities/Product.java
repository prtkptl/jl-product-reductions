package com.pratik.jlreductions.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.math.DoubleMath;
import com.pratik.jlreductions.PriceLabelFormat;

import java.util.ArrayList;
import java.util.List;

public class Product {
    @JsonProperty
    public final String productId;
    @JsonProperty
    public final String title;
    @JsonProperty
    public final List<ColorSwatch> colorSwatches;

    private final Double nowPrice;
    private final PriceLabelFormat format;
    private final Double wasPrice;
    private final Double then1Price;
    private final Double then2Price;

    public Product(Builder builder) {
        this.productId     = builder.productId;
        this.title         = builder.title;
        this.nowPrice      = builder.nowPrice;
        this.wasPrice      = builder.wasPrice;
        this.colorSwatches = builder.colorSwatches;
        this.then1Price    = builder.then1Price;
        this.then2Price    = builder.then2Price;
        this.format        = builder.format;
    }

    @JsonProperty("nowPrice")
    public String getNowPrice() {
        return getFormattedPrice(nowPrice);
    }

    @JsonIgnore
    public Double priceReduction() {
        // if either is absent then return 0 as we it doesn't make sense to calculate a reduction
        if (wasPrice == null || nowPrice == null)
            return 0D;

        return wasPrice - nowPrice;
    }

    @JsonProperty
    public String priceLabel() {
        switch (format) {
            case SHOW_WAS_THEN_NOW:
                return showWasThenNow();
            case SHOW_PERC_DISCOUNT:
                return showPercDscount();
            default:
                return showWasNow();
        }
    }

    private String showWasNow() {
        return String.format("Was %s, now %s",
                getFormattedPrice(wasPrice),
                getFormattedPrice(nowPrice));
    }

    private String showWasThenNow() {
        // don't show then if neither is available
        if (then1Price == null && then2Price == null)
            return showWasNow();

        return String.format("Was %s, then %s, now %s",
                getFormattedPrice(wasPrice),
                getFormattedPrice((then2Price != null) ? then2Price : then1Price),
                getFormattedPrice(nowPrice));
    }

    private String showPercDscount() {
        double percOff = 100 - (nowPrice * 100 / wasPrice);
        return String.format("%.0f%% off - now %s",
                percOff,
                getFormattedPrice(nowPrice));
    }

    private static String getFormattedPrice(Double price) {
        return (DoubleMath.isMathematicalInteger(price) && price >= 10D)
                ? String.format("£%.0f", price)
                : String.format("£%.2f", price);
    }

    @JsonIgnore
    @VisibleForTesting
    public static Builder builder() {
        return new Builder(PriceLabelFormat.SHOW_WAS_NOW);
    }

    @JsonIgnore
    public static Builder builder(PriceLabelFormat labelFormat) {
        return new Builder(labelFormat);
    }

    public static class Builder {
        private PriceLabelFormat format;
        private String productId;
        private String title;
        private Double nowPrice;
        private Double wasPrice;
        private Double then1Price;
        private Double then2Price;
        private List<ColorSwatch> colorSwatches = new ArrayList<>();

        public Builder(PriceLabelFormat format) {
            this.format = format;
        }

        public Builder productId(final String productId) {
            this.productId = productId;
            return this;
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder nowPrice(final String nowPrice) {
            this.nowPrice = Double.parseDouble(nowPrice);
            return this;
        }

        public Builder wasPrice(final String wasPrice) {
            this.wasPrice = Double.parseDouble(wasPrice);
            return this;
        }

        public Builder then1Price(final String then1Price) {
            this.then1Price = Double.parseDouble(then1Price);
            return this;
        }

        public Builder then2Price(final String then2Price) {
            this.then2Price = Double.parseDouble(then2Price);
            return this;
        }

        public Builder colorSwatches(final List<ColorSwatch> colorSwatches) {
            this.colorSwatches = colorSwatches;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
