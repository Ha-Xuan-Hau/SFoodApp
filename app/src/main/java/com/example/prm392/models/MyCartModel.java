package com.example.prm392.models;

import java.io.Serializable;

public class MyCartModel implements Serializable {
     String productName;
     String productPrice;
     String currentDate;
     String currentTime;
     String totalQuantity; // Thay đổi thành String nếu là kiểu dữ liệu String
     long totalPrice; // Sử dụng long cho kiểu dữ liệu totalPrice



    String documentId;
    public MyCartModel() {
        // Empty constructor required for Firebase
    }

    public MyCartModel(String productName, String productPrice, String currentDate, String currentTime, String totalQuantity, long totalPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    // Các phương thức getter và setter
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
