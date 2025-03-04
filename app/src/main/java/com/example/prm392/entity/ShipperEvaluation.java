package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "ShipperEvaluation",
        foreignKeys = {
                @ForeignKey(entity = OrderShip.class, parentColumns = "orderShipId", childColumns = "orderId")
        })
public class ShipperEvaluation {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int orderId;
    private float starRate;
    private String review;
    private Date createdAt;

    public ShipperEvaluation() {
    }

    public ShipperEvaluation(int id, int orderId, float starRate, String review, Date createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.starRate = starRate;
        this.review = review;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
