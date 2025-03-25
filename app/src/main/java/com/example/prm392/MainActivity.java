package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.entity.Restaurant;
import com.example.prm392.entity.Shipper;
import com.example.prm392.repository.RestaurantRepository;
import com.example.prm392.repository.ShipperRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private RestaurantRepository restaurantRepository;
    private TextView textViewResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restaurantRepository = new RestaurantRepository();
        textViewResult = findViewById(R.id.textViewResult); // TextView trong layout để hiển thị dữ liệu

        // Thêm nhà hàng mới vào Firebase
        insertRestaurant();
    }

    private void insertRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Cloud Nine Restaurant");
        restaurant.setEmail("cloudnine@example.com");
        restaurant.setPass("123456");
        restaurant.setPhone("0123456789");
        restaurant.setStatus("active");
        restaurant.setRating(4.5f);

        restaurantRepository.insert(restaurant,
                aVoid -> {
                    Toast.makeText(MainActivity.this, "Thêm nhà hàng thành công!", Toast.LENGTH_SHORT).show();
                    getRestaurantById(restaurant.getId()); // Lấy lại dữ liệu sau khi thêm thành công
                },
                e -> Toast.makeText(MainActivity.this, "Thêm thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private void getRestaurantById(String restaurantId) {
        restaurantRepository.getRestaurantById(restaurantId, new RestaurantRepository.OnRestaurantLoadedListener() {
            @Override
            public void onRestaurantLoaded(Restaurant restaurant) {
                if (restaurant != null) {
                    String result = "Tên: " + restaurant.getName() + "\n" +
                            "Email: " + restaurant.getEmail() + "\n" +
                            "SĐT: " + restaurant.getPhone() + "\n" +
                            "Trạng thái: " + restaurant.getStatus() + "\n" +
                            "Rating: " + restaurant.getRating();
                    textViewResult.setText(result);
                } else {
                    textViewResult.setText("Không tìm thấy nhà hàng!");
                }
            }
        });
    }
}

