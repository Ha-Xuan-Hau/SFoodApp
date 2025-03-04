package com.example.prm392.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity(tableName = "CustomerUser")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUser {
    @PrimaryKey(autoGenerate = true)
    private int customerId;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String pref;
}

