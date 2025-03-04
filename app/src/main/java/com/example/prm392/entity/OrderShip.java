package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;


@Entity(tableName = "OrderShip",
        foreignKeys = {
                @ForeignKey(entity = CustomerUser.class, parentColumns = "customerId", childColumns = "customerId"),
                @ForeignKey(entity = Shipper.class, parentColumns = "shipperId", childColumns = "shipperId")
        })
public class OrderShip {
    @PrimaryKey(autoGenerate = true)
    private int orderShipId;
    private int customerId;
    private int shipperId;
    private String orderStatus;
    private double totalPrice;
    private String deliveryType;
    private Date createdAt;
    private Date completedAt;

    public OrderShip() {
    }

    public OrderShip(int orderShipId, int customerId, int shipperId, String orderStatus, double totalPrice, String deliveryType, Date createdAt, Date completedAt) {
        this.orderShipId = orderShipId;
        this.customerId = customerId;
        this.shipperId = shipperId;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.deliveryType = deliveryType;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    public int getOrderShipId() {
        return orderShipId;
    }

    public void setOrderShipId(int orderShipId) {
        this.orderShipId = orderShipId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }
}

