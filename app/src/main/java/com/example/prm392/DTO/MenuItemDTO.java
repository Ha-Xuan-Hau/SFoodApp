package com.example.prm392.DTO;

public class MenuItemDTO {
    public int id;
    public String menu_name;
    public float price;
    public String description;
    public String restaurant_email;
    public MenuItemDTO(int id, String menu_name, float price, String description, String restaurant_email) {
        this.id = id;
        this.menu_name = menu_name;
        this.price = price;
        this.description = description;
        this.restaurant_email = restaurant_email;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRestaurant_email() {
        return restaurant_email;
    }

    public void setRestaurant_email(String restaurant_email) {
        this.restaurant_email = restaurant_email;
    }
}
