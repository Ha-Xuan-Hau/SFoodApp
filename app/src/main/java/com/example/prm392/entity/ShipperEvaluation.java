package com.example.prm392.entity;

public class ShipperEvaluation {
    private String id; // Firebase sử dụng chuỗi ID
    private String orderId;
    private float starRate;
    private String review;
    private long createdAt; // Lưu dưới dạng timestamp

    public ShipperEvaluation() {
        // Constructor rỗng cần thiết cho Firebase
    }

    public ShipperEvaluation(String id, String orderId, float starRate, String review, long createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.starRate = starRate;
        this.review = review;
        this.createdAt = createdAt;
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

    public float getStarRate() {
        return starRate;
    }

    public void setStarRate(float starRate) {
        this.starRate = starRate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    public interface OnFindEvaluationListener {
        void onSuccess(ShipperEvaluation evaluation);
        void onFailure(String errorMessage);
    }
}
