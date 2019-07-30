package com.pratik.jlreductions;

import com.pratik.jlreductions.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReductionsController {
    private static final String LABEL_TYPE_PARAM = "labelType";

    @Autowired
    ProductsModel model;

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProducts(@RequestParam(value = LABEL_TYPE_PARAM, required = false) String labelType) {
        return model.getProducts( PriceLabelFormat.getByParam(labelType) );
    }
}
