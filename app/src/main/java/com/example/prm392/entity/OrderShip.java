package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity(tableName = "OrderShip",
        foreignKeys = {
                @ForeignKey(entity = CustomerUser.class, parentColumns = "customerId", childColumns = "customerId"),
                @ForeignKey(entity = Shipper.class, parentColumns = "shipperId", childColumns = "shipperId")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}

