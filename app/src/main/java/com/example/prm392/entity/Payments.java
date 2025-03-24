package com.example.prm392.entity;

public class Payments {
    private String id; // Chuyển từ int sang String để phù hợp với Firebase
    private String orderId; // Chuyển từ int sang String
    private double amount;
    private long paymentTime; // Sử dụng timestamp thay vì Date

    public Payments() {
        // Constructor rỗng bắt buộc để Firebase deserialize dữ liệu
    }

    public Payments(String id, String orderId, double amount, long paymentTime) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentTime = paymentTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(long paymentTime) {
        this.paymentTime = paymentTime;
    }
}
