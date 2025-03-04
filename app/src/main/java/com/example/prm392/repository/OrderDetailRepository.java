package com.example.prm392.repository;

import android.content.Context;

import com.example.prm392.Dao.OrderDetailDao;
import com.example.prm392.database.AppDatabase;
import com.example.prm392.entity.OrderDetail;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderDetailRepository {
    private OrderDetailDao orderDetailDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public OrderDetailRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        orderDetailDao = db.orderDetailDao();
    }

    public void insert(OrderDetail orderDetail) {
        executorService.execute(() -> orderDetailDao.insert(orderDetail));
    }

    public List<OrderDetail> getOrderDetails(int orderId) {
        return orderDetailDao.getOrderDetails(orderId);
    }
}

