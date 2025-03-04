package com.example.prm392.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.CustomerUser;

@Dao
public interface CustomerUserDao {
    @Insert
    void insert(CustomerUser customerUser);

    @Query("SELECT * FROM CustomerUser WHERE email = :email AND password = :password")
    CustomerUser login(String email, String password);
}

