package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.prm392.entity.Shipper;
import com.example.prm392.repository.ShipperRepository;
import com.example.prm392.service.PasswordUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShipRegisterActivity extends AppCompatActivity {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ShipperRepository shipperRepository;
    private EditText edtEmail,edtName, edtPhone,edtcccd, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private String email,name, phone,cccd, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shipperRepository = new ShipperRepository();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ship_register);

        // Ánh xạ ID từ XML
        edtEmail = findViewById(R.id.edt_register_shipEmail);
        edtName = findViewById(R.id.edt_register_shipname);
        edtPhone = findViewById(R.id.edt_register_shipPhone);
        edtPassword = findViewById(R.id.edt_register_shipPassword);
        edtcccd = findViewById(R.id.edt_shipcccd);
        edtConfirmPassword = findViewById(R.id.edt_register_shipcofirmPass);
        btnRegister = (Button)findViewById(R.id.btn_shipperRegister);

        // Xử lý sự kiện click nút Đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });

    }
    private void handleRegister() {
        email = edtEmail.getText().toString().trim();
        name = edtName.getText().toString().trim();
        phone = edtPhone.getText().toString().trim();
        cccd = edtcccd.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) ||TextUtils.isEmpty(name)|| TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.matches("\\d{9,12}")) {
            Toast.makeText(this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cccd.matches("\\d{12}")) {
            Toast.makeText(this, "Căn cước công dân không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra email tồn tại
        shipperRepository.getByEmail(email, new ShipperRepository.OnFindUserListener() {
            @Override
            public void onSuccess(Shipper user) {
                showToast("Email đã được đăng ký 1!");
            }

            @Override
            public void onFailure(String errorMessage) {
                // Chuyển sang màn hình OTP và truyền dữ liệu
                Intent intent = new Intent(ShipRegisterActivity.this, OTPactivity.class);
                intent.putExtra("email", email);
                otpLauncher.launch(intent);
            }
        });

    }


    private final ActivityResultLauncher<Intent> otpLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Chạy đăng ký trên một thread khác
                    executorService.execute(() -> {
                        shipperRepository.Register(name,email, phone, cccd, password);

                        // Chuyển về UI thread để mở LoginActivity
                        runOnUiThread(() -> {
                            Intent intent = new Intent(ShipRegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    });
                }
            }
    );
    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(ShipRegisterActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}