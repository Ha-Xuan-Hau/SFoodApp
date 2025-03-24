package com.example.prm392.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392.DTO.MenuItemDTO;
import com.example.prm392.entity.MenuItems;

import java.util.List;

@Dao
public interface MenuItemsDao {
    @Insert
    void insert(MenuItems menuItem);

    @Query("SELECT * FROM Menu_Items WHERE restaurantId = :restaurantId")
    List<MenuItems> getMenuByRestaurant(int restaurantId);

    @Query("SELECT * FROM menu_items")
    LiveData<List<MenuItems>> getAllMenuItems();

    @Query("SELECT m.id, m.name AS menu_name, m.price, m.description, " +
            "r.email AS restaurant_email, r.id AS restaurant_id, m.imageUrl AS uri " +
            "FROM Menu_Items m JOIN Restaurants r ON m.restaurantId = r.id")
    LiveData<List<MenuItemDTO>> getAllMenuItemsWithRestaurant();

    @Update
    void update(MenuItems menuItems);

    @Delete
    void delete(MenuItems menuItems);

    @Query("DELETE FROM Menu_Items WHERE id = :id")
    void deleteById(int id);
}

