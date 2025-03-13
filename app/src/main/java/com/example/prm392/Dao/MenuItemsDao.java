package com.example.prm392.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392.entity.MenuItems;

import java.util.List;

@Dao
public interface MenuItemsDao {
    @Insert
    void insert(MenuItems menuItem);

    @Query("SELECT * FROM Menu_Items WHERE restaurantId = :restaurantId")
    List<MenuItems> getMenuByRestaurant(int restaurantId);

    @Query("SELECT * FROM Menu_items")
    List<MenuItems> getAllMenuItems();

    @Update
    void update(MenuItems menuItems);

    @Delete
    void delete(MenuItems menuItems);
}

