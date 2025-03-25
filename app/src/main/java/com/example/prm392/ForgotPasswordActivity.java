package com.example.prm392;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.entity.CustomerUser;
import com.example.prm392.entity.Restaurant;
import com.example.prm392.entity.Shipper;
import com.example.prm392.repository.CustomerUserRepository;
import com.example.prm392.repository.RestaurantRepository;
import com.example.prm392.repository.ShipperRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForgotPasswordActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private CustomerUserRepository customerUserRepository;
    private ShipperRepository shipperRepository;
    private RestaurantRepository restaurantRepository;
    private SharedPreferences sharedPreferences;
    private String userRole;
    private EditText edtForgotEmail;
    private TextView tvForgotPass;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        // Initialize repositories
        customerUserRepository = new CustomerUserRepository();
        shipperRepository = new ShipperRepository();
        restaurantRepository = new RestaurantRepository();

        // Get user role from SharedPreferences
        sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        userRole = sharedPreferences.getString("userRole", "customer");

        // Initialize views
        edtForgotEmail = findViewById(R.id.edt_forgotemail);
        tvForgotPass = findViewById(R.id.tv_forgotpass);
        Button btnForgotPass = findViewById(R.id.btn_forgotpass);
        Button btnBackForgot = findViewById(R.id.btn_backforgot);

        // Set page title based on user role
        if (userRole.equals("customer")) {
            tvForgotPass.setText("Quên mật khẩu người dùng");
        } else if (userRole.equals("shipper")) {
            tvForgotPass.setText("Quên mật khẩu shipper");
        } else if (userRole.equals("restaurant")) {
            tvForgotPass.setText("Quên mật khẩu nhà hàng");
        }

        // Send OTP button click listener
        btnForgotPass.setOnClickListener(view -> sendOTP());

        // Back button click listener
        btnBackForgot.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void sendOTP() {
        email = edtForgotEmail.getText().toString().trim();

        // Validate email
        if (email.isEmpty()) {
            Toast.makeText(this, "Hãy nhập email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userRole.equals("customer")) {
            customerUserRepository.getCustomerByEmail(email, new CustomerUserRepository.OnFindUserListener() {
                @Override
                public void onSuccess(CustomerUser user) {

                    // Chuyển sang màn hình OTP và truyền dữ liệu
                    Intent intent = new Intent(ForgotPasswordActivity.this, OTPactivity.class);
                    intent.putExtra("email", email);
                    otpLauncher.launch(intent);
                }

                @Override
                public void onFailure(String errorMessage) {
                    showToast("Email chưa được đăng ký!");
                }
            });
        } else if (userRole.equals("shipper")) {
            shipperRepository.getByEmail(email, new ShipperRepository.OnFindUserListener() {
                @Override
                public void onSuccess(Shipper user) {

                    // Chuyển sang màn hình OTP và truyền dữ liệu
                    Intent intent = new Intent(ForgotPasswordActivity.this, OTPactivity.class);
                    intent.putExtra("email", email);
                    otpLauncher.launch(intent);
                }

                @Override
                public void onFailure(String errorMessage) {
                    showToast("Email chưa được đăng ký!");
                }
            });
        } else if (userRole.equals("restaurant")) {
            restaurantRepository.getByEmail(email, new RestaurantRepository.OnFindUserListener() {
                @Override
                public void onSuccess(Restaurant user) {

                    // Chuyển sang màn hình OTP và truyền dữ liệu
                    Intent intent = new Intent(ForgotPasswordActivity.this, OTPactivity.class);
                    intent.putExtra("email", email);
                    otpLauncher.launch(intent);
                }

                @Override
                public void onFailure(String errorMessage) {
                    showToast("Email chưa được đăng ký!");
                }
            });
        }
    }

    private final ActivityResultLauncher<Intent> newPasswordLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Chuyển về UI thread để mở LoginActivity
                    runOnUiThread(() -> {
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }
            }
    );
    private final ActivityResultLauncher<Intent> otpLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = new Intent(ForgotPasswordActivity.this, NewPasswordActivity.class);
                    intent.putExtra("email", email);
                    newPasswordLauncher.launch(intent);
                }
            }
    );


    private boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}
