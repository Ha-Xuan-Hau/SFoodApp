package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity(tableName = "Shipper")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipper {
    @PrimaryKey(autoGenerate = true)
    private int shipperId;
    private String fullName;
    private String phone;
    private String cccd;
    private String email;
    private String status;
}


