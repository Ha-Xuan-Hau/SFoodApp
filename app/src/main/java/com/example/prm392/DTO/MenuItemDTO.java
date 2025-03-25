package com.example.prm392.DTO;

public class MenuItemDTO {
    public String id;
    public String menu_name;
    public double price;
    public String description;
    public String restaurant_name;
    public String uri;
    public String restaurant_id;

    public MenuItemDTO(){

    }

    public MenuItemDTO(String id, String menu_name, double price, String description, String restaurant_name, String uri, String restaurant_id) {
        this.id = id;
        this.menu_name = menu_name;
        this.price = price;
        this.description = description;
        this.restaurant_name = restaurant_name;
        this.uri = uri;
        this.restaurant_id = restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public String getUri() {
        return uri;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
