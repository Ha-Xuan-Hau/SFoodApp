package com.example.prm392.repository;

import android.content.Context;

import com.example.prm392.Dao.MenuItemsDao;
import com.example.prm392.database.AppDatabase;
import com.example.prm392.entity.MenuItems;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuItemsRepository {
    private MenuItemsDao menuItemsDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MenuItemsRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        menuItemsDao = db.menuItemsDao();
    }

    public void insert(MenuItems menuItem) {
        executorService.execute(() -> menuItemsDao.insert(menuItem));
    }

    public List<MenuItems> getMenuByRestaurant(int restaurantId) {
        return menuItemsDao.getMenuByRestaurant(restaurantId);
    }
}

