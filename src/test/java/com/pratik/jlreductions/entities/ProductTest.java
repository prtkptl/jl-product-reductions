package com.pratik.jlreductions.entities;

import com.google.common.collect.ImmutableList;
import com.pratik.jlreductions.PriceLabelFormat;
import com.pratik.jlreductions.PriceLabelFormatTest;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ProductTest {
    @Test
    public void testProductBasic() {
        final String productId = "12345";
        final String title = "Test Title";
        final ColorSwatch swatch = ColorSwatch.builder().build();

        Product product = Product.builder()
                .productId(productId)
                .title(title)
                .colorSwatches(ImmutableList.of(swatch))
                .build();

        assertThat(product.productId, is(productId));
        assertThat(product.title, is(title));
        assertThat(product.colorSwatches, hasSize(1));
        assertThat(product.colorSwatches, contains(swatch));
    }

    @Test
    public void testPriceReduction() {
        Product product = Product.builder()
                .wasPrice("15")
                .nowPrice("10")
                .build();

        assertThat(product.priceReduction(), is(5D));

        // no prices given
        product = Product.builder().build();
        assertThat(product.priceReduction(), is(0D));

        product = Product.builder().nowPrice("10").build();
        assertThat(product.priceReduction(), is(0D));

        product = Product.builder().wasPrice("10").build();
        assertThat(product.priceReduction(), is(0D));
    }

    @Test
    public void testPriceFormatting() {
        // to 2 decimal places
        Product product = Product.builder().nowPrice("10.966").build();
        assertThat(product.getNowPrice(), is("£10.97"));

        // integer of 10 or above should show as integer price
        product = Product.builder().nowPrice("10").build();
        assertThat(product.getNowPrice(), is("£10"));

        // integer below 10 should show as decimal price
        product = Product.builder().nowPrice("2").build();
        assertThat(product.getNowPrice(), is("£2.00"));
    }

    @Test
    public void testShowWasNow() {
        Product product = Product.builder(PriceLabelFormat.SHOW_WAS_NOW)
                .wasPrice("20")
                .nowPrice("2")
                .build();
        assertThat(product.priceLabel(), is("Was £20, now £2.00"));
    }

    @Test
    public void testShowWasThenNow() {
        // show then2 if it's available
        Product product = Product.builder(PriceLabelFormat.SHOW_WAS_THEN_NOW)
                .wasPrice("20")
                .nowPrice("2")
                .then1Price("15")
                .then2Price("16")
                .build();
        assertThat(product.priceLabel(), is("Was £20, then £16, now £2.00"));

        // show then1 if no then2
        product = Product.builder(PriceLabelFormat.SHOW_WAS_THEN_NOW)
                .wasPrice("20")
                .nowPrice("2")
                .then1Price("15")
                .build();
        assertThat(product.priceLabel(), is("Was £20, then £15, now £2.00"));

        // if neither available, show same as SHOW_WAS_NOW
        product = Product.builder(PriceLabelFormat.SHOW_WAS_THEN_NOW)
                .wasPrice("20")
                .nowPrice("2")
                .build();
        assertThat(product.priceLabel(), is("Was £20, now £2.00"));
    }

    @Test
    public void testShowPercDscount() {
        Product product = Product.builder(PriceLabelFormat.SHOW_PERC_DISCOUNT)
                .wasPrice("20")
                .nowPrice("10")
                .build();
        assertThat(product.priceLabel(), is("50% off - now £10"));

        product = Product.builder(PriceLabelFormat.SHOW_PERC_DISCOUNT)
                .wasPrice("9")
                .nowPrice("6")
                .build();
        assertThat(product.priceLabel(), is("33% off - now £6.00"));
    }
}
