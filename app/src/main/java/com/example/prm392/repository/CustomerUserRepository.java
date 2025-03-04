package com.example.prm392.repository;

import android.content.Context;

import com.example.prm392.Dao.CustomerUserDao;
import com.example.prm392.database.AppDatabase;
import com.example.prm392.entity.CustomerUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerUserRepository {
    private CustomerUserDao customerUserDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public CustomerUserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        customerUserDao = db.customerUserDao();
    }

    public void insert(CustomerUser customerUser) {
        executorService.execute(() -> customerUserDao.insert(customerUser));
    }

    public CustomerUser login(String email, String password) {
        return customerUserDao.login(email, password);
    }
}

