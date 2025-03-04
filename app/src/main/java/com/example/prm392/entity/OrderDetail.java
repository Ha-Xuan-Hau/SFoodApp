package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity(tableName = "OrderDetail",
        foreignKeys = {
                @ForeignKey(entity = OrderShip.class, parentColumns = "orderShipId", childColumns = "orderId"),
                @ForeignKey(entity = MenuItems.class, parentColumns = "id", childColumns = "menuItemsId")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int orderId;
    private int menuItemsId;
    private int quantity;
    private double price;
    private Date completedAt;
}

