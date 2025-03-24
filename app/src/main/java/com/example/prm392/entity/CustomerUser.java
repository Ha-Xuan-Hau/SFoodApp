package com.example.prm392.entity;

public class CustomerUser {
    private String customerId; // Firebase dùng String làm khóa chính
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String pref;

    public CustomerUser() {
        // Bắt buộc để Firebase deserialize dữ liệu
    }

    public CustomerUser(String customerId, String fullName, String email, String phone, String address, String password, String pref) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.pref = pref;
    }
    public CustomerUser(String fullName,String email, String phone, String address, String password, String pref) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.pref = pref;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
