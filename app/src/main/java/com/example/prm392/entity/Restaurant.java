package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity(tableName = "Restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String email;
    private String pass;
    private String phone;
    private String status;
    private float rating;
}

