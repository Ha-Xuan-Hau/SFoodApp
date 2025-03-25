package com.example.prm392.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private String rating;
    private String description;
    private String discount;
    private String type;
    private String img_url;
    private String price;
    private String mode;

    public Product() {
    }

    public Product(String name, String rating, String description, String discount, String type, String img_Url, String price, String mode) {
        this.name = name != null ? name : "";
        this.rating = rating != null ? rating : "";
        this.description = description != null ? description : "";
        this.discount = discount != null ? discount : "";
        this.type = type != null ? type : "";
        this.img_url = img_Url != null ? img_Url : "";
        this.price = price;
        this.mode = mode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating != null ? rating : "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount != null ? discount : "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type != null ? type : "";
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url != null ? img_url : "";
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}