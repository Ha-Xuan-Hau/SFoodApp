package com.example.prm392;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.repository.CustomerUserRepository;
import com.example.prm392.repository.RestaurantRepository;
import com.example.prm392.repository.ShipperRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewPasswordActivity extends AppCompatActivity {
    private CustomerUserRepository customerUserRepository;
    private ShipperRepository shipperRepository;
    private RestaurantRepository restaurantRepository;
    private TextView tv_newpass;
    private Button btn_newpass;
    private EditText edtnewpass, edtconfnewpass;
    private String email;

    private String userRole,newpass, comfnewpass;
    private SharedPreferences sharedPreferences;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_password);

        // Initialize repositories
        customerUserRepository = new CustomerUserRepository();
        shipperRepository = new ShipperRepository();
        restaurantRepository = new RestaurantRepository();

        email = getIntent().getStringExtra("email").trim();
        sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        userRole = sharedPreferences.getString("userRole", "customer");

        tv_newpass = findViewById(R.id.tv_newpass);
        btn_newpass = (Button) findViewById(R.id.btn_newpass);
        edtnewpass = findViewById(R.id.edt_newpass);
        edtconfnewpass = findViewById(R.id.edt_comfnewpass);

        tv_newpass.setText("Nhập mật khẩu mới cho tài khoản: " + email);

        btn_newpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        newpass = edtnewpass.getText().toString().trim();
        comfnewpass = edtconfnewpass.getText().toString().trim();

        if (TextUtils.isEmpty(newpass) || TextUtils.isEmpty(comfnewpass)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newpass.equals(comfnewpass)) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            if (userRole.equals("customer")) {
                customerUserRepository.resetPassword(email, newpass, new CustomerUserRepository.OnPasswordChangeListener() {
                    @Override
                    public void onSuccess(String message) {
                        runOnUiThread(() -> {
                            Toast.makeText(NewPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();  // Đóng màn hình NewPasswordActivity
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        runOnUiThread(() ->
                                Toast.makeText(NewPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show()
                        );
                    }
                });
            } else if (userRole.equals("shipper")) {
                shipperRepository.resetPassword(email, newpass, new ShipperRepository.OnPasswordChangeListener() {
                    @Override
                    public void onSuccess(String message) {
                        runOnUiThread(() -> {
                            Toast.makeText(NewPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();  // Đóng màn hình NewPasswordActivity
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        runOnUiThread(() ->
                                Toast.makeText(NewPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show()
                        );
                    }
                });
            } else if (userRole.equals("restaurant")) {
                restaurantRepository.resetPassword(email, newpass, new RestaurantRepository.OnPasswordChangeListener() {
                    @Override
                    public void onSuccess(String message) {
                        runOnUiThread(() -> {
                            Toast.makeText(NewPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();  // Đóng màn hình NewPasswordActivity
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        runOnUiThread(() ->
                                Toast.makeText(NewPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show()
                        );
                    }
                });
            }
        });
    }

}