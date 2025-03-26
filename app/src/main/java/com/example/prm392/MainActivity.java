package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.entity.MenuItems;
import com.example.prm392.entity.Restaurant;
import com.example.prm392.entity.Shipper;
import com.example.prm392.repository.RestaurantRepository;
import com.example.prm392.repository.ShipperRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private TextView textViewResult;
    private Button btnAdd, btnGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ UI
        textViewResult = findViewById(R.id.textViewResult);
        btnAdd = findViewById(R.id.btnAdd);
        btnGet = findViewById(R.id.btnGet);

        // Kết nối Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://prm392-sfood-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference("menuItems");

        // Sự kiện nút "Thêm dữ liệu"
        btnAdd.setOnClickListener(v -> addMenuItem());

        // Sự kiện nút "Lấy dữ liệu"
        btnGet.setOnClickListener(v -> getMenuItems());
    }

    // 📝 Hàm thêm dữ liệu
    private void addMenuItem() {
        String itemId = databaseReference.push().getKey(); // Tạo ID tự động
        if (itemId != null) {
            MenuItems menuItem = new MenuItems(itemId, "1", "Pizza Special",
                    "Pizza ngon với sốt đặc biệt", 9.99,
                    "https://example.com/pizza.jpg", "available");

            databaseReference.child(itemId).setValue(menuItem)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Thêm thành công!"))
                    .addOnFailureListener(e -> Log.e("Firebase", "Lỗi khi thêm: " + e.getMessage()));
        }
    }

    // 📝 Hàm lấy dữ liệu
    private void getMenuItems() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder result = new StringBuilder();
                for (DataSnapshot data : snapshot.getChildren()) {
                    MenuItems item = data.getValue(MenuItems.class);
                    if (item != null) {
                        result.append("🍕 ").append(item.getName()).append(" - $")
                                .append(item.getPrice()).append("\n");
                    }
                }
                textViewResult.setText(result.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Lỗi khi lấy dữ liệu: " + error.getMessage());
            }
        });
    }
}

