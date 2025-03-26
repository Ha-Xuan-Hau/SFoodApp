package com.example.prm392;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class OnlineShopApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase
        FirebaseApp.initializeApp(this);
    }
}

