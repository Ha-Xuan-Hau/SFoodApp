package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.entity.Shipper;
import com.example.prm392.repository.ShipperRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProfileSetupActivity extends AppCompatActivity {
    private EditText nameTextView, emailTextView, cccdTextView, phoneTextView;
    private ShipperRepository shipperRepository = new ShipperRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_setup);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String shipperId = intent.getStringExtra("shipperId");
        String fullName = intent.getStringExtra("fullName");
        String email = intent.getStringExtra("email");
        String cccd= intent.getStringExtra("cccd");
        String phone= intent.getStringExtra("phone");

        // Tham chiếu đến các TextView
        nameTextView = findViewById(R.id.shipperName);
        emailTextView = findViewById(R.id.shipperEmail);
        cccdTextView = findViewById(R.id.cccd);
        phoneTextView = findViewById(R.id.shipperPhone);

        // Hiển thị dữ liệu
        if (fullName != null) {
            nameTextView.setText(fullName);
        }

        if (email != null) {
            emailTextView.setText(email);
        }

        if (cccd != null) {
            cccdTextView.setText(cccd);
        }

        if (phone != null) {
            phoneTextView.setText(phone);
        }

        // Đặt padding cho hệ thống gesture navigation
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_update), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish(); // Đóng Activity hiện tại, quay lại ShipperActivity
        });

        TextView btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> updateShipper(shipperId));
    }
    private void updateShipper(String shipperId) {
        // Lấy dữ liệu từ EditText
        String newName = nameTextView.getText().toString().trim();
        String newEmail = emailTextView.getText().toString().trim();
        String newCccd = cccdTextView.getText().toString().trim();
        String newPhone = phoneTextView.getText().toString().trim();

        // Lấy shipper từ database
        shipperRepository.getShipperById(shipperId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Shipper shipper = snapshot.getValue(Shipper.class);
                    if (shipper != null) {
                        // Cập nhật thông tin shipper
                        shipper.setFullName(newName);
                        shipper.setEmail(newEmail);
                        shipper.setCccd(newCccd);
                        shipper.setPhone(newPhone);

                        Log.d("ProfileSetupActivity", "Cập nhật Shipper ID: " + shipperId);

                        // Lưu vào database
                        shipperRepository.updateShipper(shipper, null, null);


                        // Cập nhật giao diện
                        runOnUiThread(() -> {
                            Toast.makeText(ProfileSetupActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show();

                            // Chuyển về ShipperActivity
                            Intent intent = new Intent(ProfileSetupActivity.this, ShipperActivity.class);
                            intent.putExtra("shipperId", shipperId);
                            startActivity(intent);
                            finish();
                        });
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(ProfileSetupActivity.this, "Không tìm thấy shipper!", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Lỗi Firebase: " + error.getMessage());
            }
        });
    }


}
