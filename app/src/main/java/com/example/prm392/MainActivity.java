package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.entity.Shipper;
import com.example.prm392.repository.ShipperRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShipperRepository shipperRepository = new ShipperRepository();
        shipperRepository.insertShipper(new Shipper("1","Tran Tien Dat","03700","111111","dat@gmail","123","active"), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Firebase", "Thêm shipper thành công!");
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase", "Lỗi khi thêm shipper: " + e.getMessage());
            }
        });


        Button btnGoToShipper = findViewById(R.id.btnGoToShipper);
        btnGoToShipper.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShipperActivity.class);
            intent.putExtra("shipperId", 1);
            startActivity(intent);
        });

    }
}

