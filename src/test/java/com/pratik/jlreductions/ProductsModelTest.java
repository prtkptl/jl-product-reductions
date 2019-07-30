package com.pratik.jlreductions;

import com.pratik.jlreductions.entities.ColorSwatch;
import com.pratik.jlreductions.entities.Product;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ProductsModelTest {
    @Test
    public void testBuildProduct() {
        ProductsModel model = new ProductsModel();
        List<Product> products = model.buildProducts(
                model.loadFromFile("SimpleProduct.json"), PriceLabelFormat.SHOW_WAS_NOW );
        assertThat(products, hasSize(1));

        Product product = products.get(0);
        assertThat(product.productId, is("3525081"));
        assertThat(product.title, is("hush Marble Panel Maxi Dress, Multi"));
        assertThat(product.colorSwatches, hasSize(0));
        assertThat(product.getNowPrice(), is("£59"));
        assertThat(product.priceReduction(), is(11D));
        assertThat(product.priceLabel(), is("Was £70, now £59"));
    }

    @Test
    public void testBuildProductFilterItemWithoutPrice() {
        ProductsModel model = new ProductsModel();
        List<Product> products = model.buildProducts(
                model.loadFromFile("ProductsWithoutWasPrice.json"), PriceLabelFormat.SHOW_WAS_NOW );
        assertThat(products, hasSize(0));
    }

    @Test
    public void testBuildSwatches() {
        ProductsModel model = new ProductsModel();
        JSONObject json = model.loadFromFile("Swatches.json");

        List<ColorSwatch> swatches = model.buildColorSwatches(json);
        assertThat(swatches, hasSize(2));

        ColorSwatch swatch = swatches.get(0);
        assertThat(swatch.color, is("Orange"));
        assertThat(swatch.skuid, is("237437634"));
        assertThat(swatch.getRgbColor(), is("FFC800"));

        swatch = swatches.get(1);
        assertThat(swatch.color, is("Light Blue"));
        assertThat(swatch.skuid, is("237497964"));
        assertThat(swatch.getRgbColor(), is("0000FF"));
    }

    @Test
    public void testSortedProducts() {
        ProductsModel model = new ProductsModel();
        List<Product> products = model.buildProducts(
                model.loadFromFile("ProductsForSorting.json"), PriceLabelFormat.SHOW_WAS_NOW );
        assertThat(products, hasSize(3));
        assertThat(products.get(0).productId, is("2"));
        assertThat(products.get(1).productId, is("3"));
        assertThat(products.get(2).productId, is("1"));
    }
}
