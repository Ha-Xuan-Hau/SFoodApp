package com.example.prm392.repository;

import android.content.Context;

import com.example.prm392.Dao.PaymentsDao;
import com.example.prm392.database.AppDatabase;
import com.example.prm392.entity.Payments;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PaymentsRepository {
    private PaymentsDao paymentsDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public PaymentsRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        paymentsDao = db.paymentsDao();
    }

    public void insert(Payments payment) {
        executorService.execute(() -> paymentsDao.insert(payment));
    }

    public Payments getPaymentByOrder(int orderId) {
        return paymentsDao.getPaymentByOrder(orderId);
    }
}

