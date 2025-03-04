package com.example.prm392.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.OrderShip;

import java.util.List;

@Dao
public interface OrderShipDao {
    @Insert
    void insert(OrderShip orderShip);

    @Query("SELECT * FROM OrderShip WHERE customerId = :customerId")
    List<OrderShip> getOrdersByCustomer(int customerId);
}

