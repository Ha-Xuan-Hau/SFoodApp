package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.entity.OrderDetail;
import com.example.prm392.entity.OrderShip;
import com.example.prm392.repository.OrderShipRepository;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        OrderShipRepository orderShipRepository = new OrderShipRepository();
//        Long completedAt = System.currentTimeMillis(); // Hoặc lấy từ dữ liệu
//        OrderShip orderShip = new OrderShip("1", "1","1","", "", 86050,"Hỏa tốc",completedAt, completedAt);
//        orderShipRepository.insert(orderShip);



        Button btnGoToShipper = findViewById(R.id.btnGoToShipper);
        btnGoToShipper.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShipperActivity.class);
            intent.putExtra("shipperId", 1);
            startActivity(intent);
        });

    }
}

