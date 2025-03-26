package com.example.prm392;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateRestaurantActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPass, edtPhone, edtStatus, edtRating;
    private Button btnUpdate;
    private DatabaseReference databaseReference;
    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_restaurant);

        // Ánh xạ View
        edtName = findViewById(R.id.edt_restaurant_name);
        edtEmail = findViewById(R.id.edt_restaurant_email);
        edtPass = findViewById(R.id.edt_restaurant_pass);
        edtPhone = findViewById(R.id.edt_restaurant_phone);
        edtStatus = findViewById(R.id.edt_restaurant_status);
        edtRating = findViewById(R.id.edt_restaurant_rating);
        btnUpdate = findViewById(R.id.btn_update_restaurant);

        restaurantId = getIntent().getStringExtra("restaurant_id");

        databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants").child(restaurantId);

        getRestaurantData();

        // Xử lý sự kiện cập nhật
        btnUpdate.setOnClickListener(v -> updateRestaurant());
    }
    private void getRestaurantData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    edtName.setText(snapshot.child("name").getValue(String.class));
                    edtEmail.setText(snapshot.child("email").getValue(String.class));
                    edtPass.setText(snapshot.child("pass").getValue(String.class));
                    edtPhone.setText(snapshot.child("phone").getValue(String.class));
                    edtStatus.setText(snapshot.child("status").getValue(String.class));

                    // Kiểm tra nếu rating tồn tại, lấy giá trị float
                    Float rating = snapshot.child("rating").getValue(Float.class);
                    edtRating.setText(rating != null ? String.valueOf(rating) : "0.0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateRestaurantActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRestaurant() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String status = edtStatus.getText().toString().trim();
        float rating;

        try {
            rating = Float.parseFloat(edtRating.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Rating không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || phone.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật Firebase
        databaseReference.child("name").setValue(name);
        databaseReference.child("email").setValue(email);
        databaseReference.child("pass").setValue(pass);
        databaseReference.child("phone").setValue(phone);
        databaseReference.child("status").setValue(status);
        databaseReference.child("rating").setValue(rating)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateRestaurantActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(UpdateRestaurantActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show());
    }
}