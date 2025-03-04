package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;


@Entity(tableName = "OrderDetail",
        foreignKeys = {
                @ForeignKey(entity = OrderShip.class, parentColumns = "orderShipId", childColumns = "orderId"),
                @ForeignKey(entity = MenuItems.class, parentColumns = "id", childColumns = "menuItemsId")
        })
public class OrderDetail {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int orderId;
    private int menuItemsId;
    private int quantity;
    private double price;
    private Date completedAt;

    public OrderDetail() {
    }

    public OrderDetail(int id, int orderId, int menuItemsId, int quantity, double price, Date completedAt) {
        this.id = id;
        this.orderId = orderId;
        this.menuItemsId = menuItemsId;
        this.quantity = quantity;
        this.price = price;
        this.completedAt = completedAt;
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

    public int getMenuItemsId() {
        return menuItemsId;
    }

    public void setMenuItemsId(int menuItemsId) {
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

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }
}

