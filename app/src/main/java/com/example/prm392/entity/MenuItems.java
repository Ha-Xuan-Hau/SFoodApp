package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity(tableName = "Menu_Items",
        foreignKeys = {
                @ForeignKey(entity = Restaurant.class, parentColumns = "id", childColumns = "restaurantId")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItems {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int restaurantId;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String status;
}

