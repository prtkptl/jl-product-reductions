package com.pratik.jlreductions;

import com.google.common.base.Strings;
import com.pratik.jlreductions.entities.ColorSwatch;
import com.pratik.jlreductions.entities.Product;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductsModel {
    private static final String CATEGORY_URL = "https://jl-nonprod-syst.apigee.net/v1/categories/600001506/products?key=2ALHCAAs6ikGRBoy6eTHA58RaG097Fma";
    private static final String FILE_PATH = "static/example.json";

    static final String PRODUCTS_KEY = "products";
    private static final String PRICE_KEY = "price";
    private static final String PRICE_WAS_KEY = "was";
    private static final String PRICE_NOW_KEY = "now";
    private static final String PRICE_FROM_KEY = "from";
    private static final String PRICE_THEN1_KEY = "then1";
    private static final String PRICE_THEN2_KEY = "then2";
    private static final String TITLE_KEY = "title";
    private static final String PRODUCT_ID_KEY = "productId";

    private static final String COLOR_SWATCHES_KEY = "colorSwatches";
    private static final String COLOR_KEY = "color";
    private static final String BASIC_COLOR_KEY = "basicColor";
    private static final String SKU_ID_KEY = "skuId";

    public List<Product> getProducts(PriceLabelFormat labelFormat) {
        JSONObject json = loadFromURL();
        return buildProducts(json, labelFormat);
    }

    List<Product> buildProducts(JSONObject json, PriceLabelFormat labelFormat) {
        List<Product> products = new ArrayList<>();

        JSONArray productsArr = json.getJSONArray(PRODUCTS_KEY);
        // build a list of products with reductions
        for (int i = 0 ; i < productsArr.length(); i++) {
            JSONObject productObj = productsArr.getJSONObject(i);
            JSONObject priceObj = productObj.getJSONObject(PRICE_KEY);
            String priceWas = priceObj.getString(PRICE_WAS_KEY);

            // no was price, so there can't be a reduction - skip
            if (Strings.isNullOrEmpty(priceWas))
                continue;

            Object priceNowObj = priceObj.get(PRICE_NOW_KEY);
            String priceNow = (priceNowObj instanceof JSONObject)
                    ? ((JSONObject) priceNowObj).getString(PRICE_FROM_KEY)
                    : (String) priceNowObj;

            Product.Builder product = Product.builder(labelFormat)
                    .title(productObj.getString(TITLE_KEY))
                    .productId(productObj.getString(PRODUCT_ID_KEY))
                    .wasPrice(priceWas)
                    .nowPrice(priceNow)
                    .colorSwatches(buildColorSwatches(productObj));

            String priceThen1 = priceObj.getString(PRICE_THEN1_KEY);
            if (!Strings.isNullOrEmpty(priceThen1))
                product.then1Price(priceThen1);

            String priceThen2 = priceObj.getString(PRICE_THEN2_KEY);
            if (!Strings.isNullOrEmpty(priceThen2))
                product.then2Price(priceThen2);

            products.add(product.build());
        }

        // return the products sorted by price reduction descending
        return products.stream()
                .sorted(Comparator.comparing(Product::priceReduction).reversed())
                .collect(Collectors.toList());
    }

    List<ColorSwatch> buildColorSwatches(JSONObject productObj) {
        List<ColorSwatch> swatches = new ArrayList<>();

        JSONArray swatchesArr = productObj.getJSONArray(COLOR_SWATCHES_KEY);
        // build a list of color swatches for the product
        for (int i = 0 ; i < swatchesArr.length(); i++) {
            JSONObject swatchObj = swatchesArr.getJSONObject(i);
            swatches.add(
                    ColorSwatch.builder()
                            .color(swatchObj.getString(COLOR_KEY))
                            .rgbColor(swatchObj.getString(BASIC_COLOR_KEY))
                            .skuid(swatchObj.getString(SKU_ID_KEY))
                            .build()
            );
        }

        return swatches;
    }

    private JSONObject loadFromURL() {
        StringBuilder sb = new StringBuilder();

        try (InputStream is = new URL(CATEGORY_URL).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }

            return new JSONObject(sb.toString());
        }
        catch (IOException e) {
            // error with getting JSON, assume similar to empty string
        }

        return new JSONObject();
    }

    JSONObject loadFromFile(String file) {
        try {
            URL filepath = getClass().getClassLoader().getResource(file);
            Path path = Paths.get(filepath.toURI());
            return new JSONObject(new String(Files.readAllBytes(path), StandardCharsets.UTF_8));
        } catch (IOException | URISyntaxException e) {
            // error with getting JSON, assume similar to empty string
        }

        return new JSONObject();
    }
}
