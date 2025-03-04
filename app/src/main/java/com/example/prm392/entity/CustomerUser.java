package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "CustomerUser")
public class CustomerUser {
    @PrimaryKey(autoGenerate = true)
    private int customerId;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String pref;

    public CustomerUser() {
    }

    public CustomerUser(int customerId, String email, String phone, String address, String password, String pref) {
        this.customerId = customerId;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.pref = pref;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPref() {
        return pref;
    }

    public void setPref(String pref) {
        this.pref = pref;
    }
}

