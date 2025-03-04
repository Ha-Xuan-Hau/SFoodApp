package com.example.prm392.repository;

import android.content.Context;

import com.example.prm392.Dao.OrderShipDao;
import com.example.prm392.database.AppDatabase;
import com.example.prm392.entity.OrderShip;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderShipRepository {
    private OrderShipDao orderShipDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public OrderShipRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        orderShipDao = db.orderShipDao();
    }

    public void insert(OrderShip orderShip) {
        executorService.execute(() -> orderShipDao.insert(orderShip));
    }

    public List<OrderShip> getOrdersByCustomer(int customerId) {
        return orderShipDao.getOrdersByCustomer(customerId);
    }
}

