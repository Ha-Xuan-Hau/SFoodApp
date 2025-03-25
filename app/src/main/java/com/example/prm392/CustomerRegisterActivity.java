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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.entity.CustomerUser;
import com.example.prm392.repository.CustomerUserRepository;
import com.example.prm392.service.PasswordUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerRegisterActivity extends AppCompatActivity {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private CustomerUserRepository customerUserRepository;
    private EditText edtName, edtEmail, edtPhone, edtAddress, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private String name,email, phone, address, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        customerUserRepository = new CustomerUserRepository();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_register);

        // Ánh xạ ID từ XML
        edtEmail = findViewById(R.id.edt_register_CusEmail);
        edtName = findViewById(R.id.edt_register_cusname);
        edtPhone = findViewById(R.id.edt_register_cusPhone);
        edtAddress = findViewById(R.id.edt_register_cusAddress);
        edtPassword = findViewById(R.id.edt_register_cusPassword);
        edtConfirmPassword = findViewById(R.id.edt_register_cofirmPass);
        btnRegister = findViewById(R.id.btn_customerRegister);

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
        address = edtAddress.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)
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

        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra email tồn tại
        customerUserRepository.getCustomerByEmail(email, new CustomerUserRepository.OnFindUserListener() {
            @Override
            public void onSuccess(CustomerUser user) {
                showToast("Email đã được đăng ký!");
            }

            @Override
            public void onFailure(String errorMessage) {
                // Chuyển sang màn hình OTP và truyền dữ liệu
                Intent intent = new Intent(CustomerRegisterActivity.this, OTPactivity.class);
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
                        customerUserRepository.Register(name,email, phone, address, PasswordUtil.hashPassword(password));

                        // Chuyển về UI thread để mở LoginActivity
                        runOnUiThread(() -> {
                            Intent intent = new Intent(CustomerRegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    });
                    Intent intent = new Intent(CustomerRegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
    );
    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(CustomerRegisterActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}