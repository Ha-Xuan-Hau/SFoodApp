package com.example.prm392.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Chuyển hướng trực tiếp đến HomeActivity
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish(); // Đóng LoginActivity để không quay lại
    }
}