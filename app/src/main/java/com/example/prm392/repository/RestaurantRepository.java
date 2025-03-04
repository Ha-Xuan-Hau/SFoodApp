package com.example.prm392.repository;

import android.content.Context;

import com.example.prm392.Dao.RestaurantDao;
import com.example.prm392.database.AppDatabase;
import com.example.prm392.entity.Restaurant;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestaurantRepository {
    private RestaurantDao restaurantDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RestaurantRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        restaurantDao = db.restaurantDao();
    }

    public void insert(Restaurant restaurant) {
        executorService.execute(() -> restaurantDao.insert(restaurant));
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }
}

