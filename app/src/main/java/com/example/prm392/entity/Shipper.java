package com.example.prm392.entity;

public class Shipper {
    private String shipperId; // ID được lưu dưới dạng String
    private String fullName;
    private String phone;
    private String cccd;
    private String email;
    private String pass;
    private String status;

    public Shipper() {
        // Constructor rỗng cần thiết cho Firebase
    }

    public Shipper(String shipperId, String fullName, String phone, String cccd, String email, String pass, String status) {
        this.shipperId = shipperId;
        this.fullName = fullName;
        this.phone = phone;
        this.cccd = cccd;
        this.email = email;
        this.pass = pass;
        this.status = status;
    }

    public String getShipperId() {
        return shipperId;
    }

    public void setShipperId(String shipperId) {
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
