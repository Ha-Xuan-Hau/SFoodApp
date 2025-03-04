package com.example.prm392.repository;

import android.content.Context;

import com.example.prm392.Dao.ShipperDao;
import com.example.prm392.database.AppDatabase;
import com.example.prm392.entity.Shipper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShipperRepository {
    private ShipperDao shipperDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ShipperRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        shipperDao = db.shipperDao();
    }

    public void insert(Shipper shipper) {
        executorService.execute(() -> shipperDao.insert(shipper));
    }

    public List<Shipper> getActiveShippers() {
        return shipperDao.getActiveShippers();
    }
}

