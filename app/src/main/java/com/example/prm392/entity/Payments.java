package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity(tableName = "Payments",
        foreignKeys = {
                @ForeignKey(entity = OrderShip.class, parentColumns = "orderShipId", childColumns = "orderId")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payments {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int orderId;
    private double amount;
    private Date paymentTime;
}

