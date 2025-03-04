package com.example.prm392.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.ShipperEvaluation;

@Dao
public interface ShipperEvaluationDao {
    @Insert
    void insert(ShipperEvaluation evaluation);

    @Query("SELECT * FROM ShipperEvaluation WHERE orderId = :orderId")
    ShipperEvaluation getEvaluationByOrder(int orderId);
}


