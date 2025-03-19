package com.example.prm392.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.Restaurant;

import java.util.List;

@Dao
public interface RestaurantDao {
    @Insert
    void insert(Restaurant restaurant);
    @Query("SELECT * FROM restaurants")
    LiveData<List<Restaurant>> getAllRestaurantName();
    @Query("SELECT * FROM Restaurants")
    List<Restaurant> getAllRestaurants();


}

