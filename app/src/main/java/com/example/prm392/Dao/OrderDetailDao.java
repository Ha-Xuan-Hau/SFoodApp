package com.example.prm392.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.OrderDetail;

import java.util.List;

@Dao
public interface OrderDetailDao {
    @Insert
    void insert(OrderDetail orderDetail);

    @Query("SELECT * FROM OrderDetail WHERE orderId = :orderId")
    List<OrderDetail> getOrderDetails(int orderId);
}

