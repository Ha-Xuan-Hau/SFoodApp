package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Restaurants")
public class Restaurant {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String email;
    private String pass;
    private String phone;
    private String status;
    private float rating;

    public Restaurant() {
    }

    public Restaurant(int id, String name, String email, String pass, String phone, String status, float rating) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.status = status;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

