package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "Menu_Items",
        foreignKeys = {
                @ForeignKey(entity = Restaurant.class, parentColumns = "id", childColumns = "restaurantId")
        })
public class MenuItems {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int restaurantId;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String status;

    public MenuItems() {
    }

    public MenuItems(int id, int restaurantId, String name, String description, double price, String imageUrl, String status) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

