package com.example.prm392.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.Payments;

@Dao
public interface PaymentsDao {
    @Insert
    void insert(Payments payment);

    @Query("SELECT * FROM Payments WHERE orderId = :orderId")
    Payments getPaymentByOrder(int orderId);
}

