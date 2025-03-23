package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392.entity.MenuItems;
import com.example.prm392.entity.Restaurant;
import com.example.prm392.viewmodel.MenuItemsViewModel;
import com.example.prm392.viewmodel.MenuItemsViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class MenuItemDetailActivity extends AppCompatActivity {

    private EditText etMenuName, etPrice, etDescription, etId;
    private Spinner spinnerRestaurant;
    private Button btnUpdate;
    private MenuItemsViewModel menuItemsViewModel;
    private List<Restaurant> restaurantList = new ArrayList<>();
    private List<String> restaurantNames = new ArrayList<>();

    // Danh sách nhà hàng giả định
    //private String[] restaurantList = {"Nhà hàng A", "Nhà hàng B", "Nhà hàng C"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_detail);

        // Ánh xạ UI
        etId = findViewById(R.id.et_id);
        etMenuName = findViewById(R.id.et_menu_name);
        etPrice = findViewById(R.id.et_price);
        etDescription = findViewById(R.id.et_description);
        spinnerRestaurant = findViewById(R.id.spinner_restaurant);
        btnUpdate = findViewById(R.id.btn_update);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        int id = getIntent().getIntExtra("id", -1);
        String menuName = intent.getStringExtra("menu_name");
        float price = intent.getFloatExtra("price",0);
        String description = intent.getStringExtra("description");
        int selectedRestaurant = intent.getIntExtra("restaurant_id",-1);

        // Set dữ liệu lên UI
        etId.setText(String.valueOf(id));
        etMenuName.setText(menuName);
        etPrice.setText(String.valueOf(price));
        etDescription.setText(description);

        menuItemsViewModel = new ViewModelProvider(this, new MenuItemsViewModelFactory(this))
                .get(MenuItemsViewModel.class);


        menuItemsViewModel.getAllRestaurants().observe(this, restaurants -> {
            restaurantList.clear();
            restaurantList.addAll(restaurants);
            restaurantNames.clear();
            int selectedPosition = -1;
            for (int i = 0; i < restaurants.size(); i++) {
                restaurantNames.add(restaurants.get(i).getEmail());
                if (restaurants.get(i).getId() == selectedRestaurant) {
                    selectedPosition = i; // Lưu vị trí của nhà hàng cần chọn
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, restaurantNames);
            spinnerRestaurant.setAdapter(adapter);

            if (selectedPosition != -1) {
                spinnerRestaurant.setSelection(selectedPosition);
            }
        });

        // Xử lý khi bấm nút Cập Nhật
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish();
        });
        btnUpdate.setOnClickListener(v -> updateMenuItem());
    }

    private void updateMenuItem() {
        // Lấy dữ liệu từ input
        int id = Integer.parseInt(etId.getText().toString().trim());
        String updatedName = etMenuName.getText().toString().trim();
        float updatedPrice = Float.parseFloat(etPrice.getText().toString().trim());
        String updatedDescription = etDescription.getText().toString().trim();
        int selectedRestaurantId = restaurantList.get(spinnerRestaurant.getSelectedItemPosition()).getId();

        MenuItems updatedMenuItem = new MenuItems(id,selectedRestaurantId, updatedName, updatedDescription, updatedPrice, "imageUrl","available");

        menuItemsViewModel.update(updatedMenuItem);

        Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

        // Quay lại màn hình danh sách
        finish();
    }
}
