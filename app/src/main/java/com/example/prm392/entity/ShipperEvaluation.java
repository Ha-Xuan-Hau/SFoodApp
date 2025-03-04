package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity(tableName = "ShipperEvaluation",
        foreignKeys = {
                @ForeignKey(entity = OrderShip.class, parentColumns = "orderShipId", childColumns = "orderId")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipperEvaluation {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int orderId;
    private float starRate;
    private String review;
    private Date createdAt;
}
