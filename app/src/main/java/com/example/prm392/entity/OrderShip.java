package com.example.prm392.entity;

import androidx.annotation.Nullable;

public class OrderShip {
    private String orderShipId; // Chuyển từ int sang String để phù hợp với Firebase
    private String orderDetailId; // Chuyển từ int sang String
    private String customerId;
    @Nullable
    private String shipperId; // Shipper có thể null nên để @Nullable
    private String orderStatus;
    private double totalPrice;
    private String deliveryType;
    private long createdAt; // Lưu timestamp thay vì Date
    private long completedAt;

    public OrderShip() {
        // Constructor rỗng bắt buộc để Firebase deserialize dữ liệu
    }

    public OrderShip(String orderShipId, String orderDetailId, String customerId, @Nullable String shipperId, String orderStatus, double totalPrice, String deliveryType, long createdAt, long completedAt) {
        this.orderShipId = orderShipId;
        this.orderDetailId = orderDetailId;
        this.customerId = customerId;
        this.shipperId = shipperId;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.deliveryType = deliveryType;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    public String getOrderShipId() {
        return orderShipId;
    }

    public void setOrderShipId(String orderShipId) {
        this.orderShipId = orderShipId;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Nullable
    public String getShipperId() {
        return shipperId;
    }

    public void setShipperId(@Nullable String shipperId) {
        this.shipperId = shipperId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }
}
