package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuItemDetailActivity extends AppCompatActivity {

    private EditText etMenuName, etPrice, etDescription, etId;
    private Spinner spinnerRestaurant;
    private Button btnUpdate;

    // Danh sách nhà hàng giả định
    private String[] restaurantList = {"Nhà hàng A", "Nhà hàng B", "Nhà hàng C"};

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
        String id = intent.getStringExtra("id");
        String menuName = intent.getStringExtra("menu_name");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("description");
        String selectedRestaurant = intent.getStringExtra("restaurant_id");

        // Set dữ liệu lên UI
        etId.setText(id);
        etMenuName.setText(menuName);
        etPrice.setText(price);
        etDescription.setText(description);

        // Đổ danh sách nhà hàng vào Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, restaurantList);
        spinnerRestaurant.setAdapter(adapter);

        // Chọn nhà hàng đã lưu trước đó
//        int selectedIndex = 0;
//        for (int i = 0; i < restaurantList.length; i++) {
//            if (restaurantList[i].equals(selectedRestaurant)) {
//                selectedIndex = i;
//                break;
//            }
//        }
//        spinnerRestaurant.setSelection(selectedIndex);

        // Xử lý khi bấm nút Cập Nhật
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish(); // Đóng activity và quay về màn hình trước đó
        });
        btnUpdate.setOnClickListener(v -> updateMenuItem());
    }

    private void updateMenuItem() {
        // Lấy dữ liệu từ input
        String updatedName = etMenuName.getText().toString().trim();
        String updatedPrice = etPrice.getText().toString().trim();
        String updatedDescription = etDescription.getText().toString().trim();
        String updatedRestaurant = spinnerRestaurant.getSelectedItem().toString();

        // TODO: Gửi dữ liệu về server hoặc cập nhật vào cơ sở dữ liệu
        // Hiển thị thông báo cập nhật thành công (tạm thời in ra log)
        System.out.println("Đã cập nhật: " + updatedName + ", " + updatedPrice + ", " + updatedDescription + ", " + updatedRestaurant);
    }
}
