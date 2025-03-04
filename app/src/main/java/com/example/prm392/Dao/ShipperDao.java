package com.example.prm392.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.Shipper;

import java.util.List;

@Dao
public interface ShipperDao {
    @Insert
    void insert(Shipper shipper);

    @Query("SELECT * FROM Shipper WHERE status = 'active'")
    List<Shipper> getActiveShippers();
}

