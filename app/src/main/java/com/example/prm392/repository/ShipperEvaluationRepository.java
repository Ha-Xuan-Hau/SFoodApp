package com.example.prm392.repository;

import android.content.Context;

import com.example.prm392.Dao.ShipperEvaluationDao;
import com.example.prm392.database.AppDatabase;
import com.example.prm392.entity.ShipperEvaluation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShipperEvaluationRepository {
    private ShipperEvaluationDao shipperEvaluationDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ShipperEvaluationRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        shipperEvaluationDao = db.shipperEvaluationDao();
    }

    public void insert(ShipperEvaluation evaluation) {
        executorService.execute(() -> shipperEvaluationDao.insert(evaluation));
    }

    public ShipperEvaluation getEvaluationByOrder(int orderId) {
        return shipperEvaluationDao.getEvaluationByOrder(orderId);
    }
}

