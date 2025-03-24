package com.example.prm392.entity;

import java.util.Date;

public class OrderDetail {
    private String id; // Chuyển từ int sang String để phù hợp với Firebase
    private String menuItemsId; // Cũng đổi từ int sang String để khớp với Firebase
    private int quantity;
    private double price;
    private long completedAt; // Lưu timestamp thay vì Date

    public OrderDetail() {
        // Bắt buộc để Firebase deserialize dữ liệu
    }

    public OrderDetail(String id, String menuItemsId, int quantity, double price, long completedAt) {
        this.id = id;
        this.menuItemsId = menuItemsId;
        this.quantity = quantity;
        this.price = price;
        this.completedAt = completedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuItemsId() {
        return menuItemsId;
    }

    public void setMenuItemsId(String menuItemsId) {
        this.menuItemsId = menuItemsId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }

}
