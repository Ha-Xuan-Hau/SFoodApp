package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;


@Entity(tableName = "Payments",
        foreignKeys = {
                @ForeignKey(entity = OrderShip.class, parentColumns = "orderShipId", childColumns = "orderId")
        })

public class Payments {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int orderId;
    private double amount;
    private Date paymentTime;

    public Payments() {
    }

    public Payments(int id, int orderId, double amount, Date paymentTime) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentTime = paymentTime;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }
}

