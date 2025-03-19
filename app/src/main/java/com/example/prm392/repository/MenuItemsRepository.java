package com.example.prm392.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.prm392.DTO.MenuItemDTO;
import com.example.prm392.Dao.MenuItemsDao;
import com.example.prm392.database.AppDatabase;
import com.example.prm392.entity.MenuItems;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuItemsRepository {
    private MenuItemsDao menuItemsDao;
    private LiveData<List<MenuItems>> allMenuItems;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private LiveData<List<MenuItemDTO>> allMenuItemsWithRestaurant;
    public MenuItemsRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        menuItemsDao = db.menuItemsDao();
        allMenuItemsWithRestaurant = menuItemsDao.getAllMenuItemsWithRestaurant();
    }

    public LiveData<List<MenuItemDTO>> getAllMenuItemsWithRestaurant() {
        return allMenuItemsWithRestaurant;
    }

    public LiveData<List<MenuItems>> getAllMenuItems() {
        return allMenuItems;
    }

    public void insert(MenuItems menuItem) {
        executorService.execute(() -> menuItemsDao.insert(menuItem));
    }

    public void update(MenuItems menuItem) {
        executorService.execute(() -> menuItemsDao.update(menuItem));
    }

    public void delete(MenuItems menuItem) {
        executorService.execute(() -> menuItemsDao.delete(menuItem));
    }

    public void deleteMenuItemById(int id) {
        executorService.execute(() -> menuItemsDao.deleteById(id));
    }
}
