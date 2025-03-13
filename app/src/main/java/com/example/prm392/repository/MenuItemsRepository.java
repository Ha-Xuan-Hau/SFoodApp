package com.example.prm392.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.example.prm392.Dao.MenuItemsDao;
import com.example.prm392.database.AppDatabase;
import com.example.prm392.entity.MenuItems;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuItemsRepository {
    private MenuItemsDao menuItemsDao;
    private List<MenuItems> allMenuItems;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MenuItemsRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        menuItemsDao = db.menuItemsDao();
        allMenuItems = menuItemsDao.getAllMenuItems();
    }

    public void insert(MenuItems menuItem) {
        executorService.execute(() -> menuItemsDao.insert(menuItem));
    }

    public List<MenuItems> getMenuByRestaurant(int restaurantId) {
        return menuItemsDao.getMenuByRestaurant(restaurantId);
    }

    public List<MenuItems> getAllMenuItems() {
        return allMenuItems;
    }

    public void update(MenuItems menuItems) {
        executorService.execute(() -> menuItemsDao.update(menuItems));
    }

    public void delete(MenuItems menuItems) {
        executorService.execute(() -> menuItemsDao.delete(menuItems));
    }

    private static class InsertMenuItemsAsyncTask extends AsyncTask<MenuItems, Void, Void> {
        private MenuItemsDao menuItemsDao;

        private InsertMenuItemsAsyncTask(MenuItemsDao menuItemsDao) {
            this.menuItemsDao = menuItemsDao;
        }

        @Override
        protected Void doInBackground(MenuItems... menuItems) {
            menuItemsDao.insert(menuItems[0]);
            return null;
        }
    }
}

