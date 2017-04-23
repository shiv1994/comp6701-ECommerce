package com.example.shivr.e_commerce;

import android.widget.ImageView;

/**
 * Created by shivr on 21/04/2017.
 */

public class Product {
    private String name;
    private Double price;
    private String description;
    private ImageView imageView;

    public Product(String name, Double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
