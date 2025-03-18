package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Shipper")

public class Shipper {
    @PrimaryKey(autoGenerate = true)
    private int shipperId;
    private String fullName;
    private String phone;
    private String cccd;
    private String email;
    private String password;
    private String status;

    public Shipper() {
    }


    public Shipper(int shipperId, String fullName, String phone, String cccd, String email, String password, String status) {
        this.shipperId = shipperId;
        this.fullName = fullName;
        this.phone = phone;
        this.cccd = cccd;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


