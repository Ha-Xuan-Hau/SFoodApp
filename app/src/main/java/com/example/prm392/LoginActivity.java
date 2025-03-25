package com.example.prm392;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.entity.CustomerUser;
import com.example.prm392.entity.Restaurant;
import com.example.prm392.entity.Shipper;
import com.example.prm392.repository.CustomerUserRepository;
import com.example.prm392.repository.RestaurantRepository;
import com.example.prm392.repository.ShipperRepository;
import com.example.prm392.service.PasswordUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private CustomerUserRepository customerUserRepository;
    private ShipperRepository shipperRepository;
    private RestaurantRepository restaurantRepository;
    private String userRole;
    private EditText edtEmail, edtPassword;
    private TextView tv_name_page;
    private CheckBox cbRemember;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        customerUserRepository = new CustomerUserRepository();
        shipperRepository = new ShipperRepository();
        restaurantRepository = new RestaurantRepository();

        sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        userRole = sharedPreferences.getString("userRole", "customer");

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        cbRemember = findViewById(R.id.cb_remember);
        tv_name_page = findViewById(R.id.tv_name_pageLogin);

        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);
        Button btnForgotPass = findViewById(R.id.btn_forgotPass);
        Button btnBack = findViewById(R.id.btn_BackStartApp);

        if (userRole.equals("customer")) {
            tv_name_page.setText("Đăng nhập người dùng");
        } else if (userRole.equals("shipper")) {
            tv_name_page.setText("Đăng nhập shipper");
        } else if (userRole.equals("restaurant")) {
            tv_name_page.setText("Đăng nhập nhà hàng");
        }
        //lưu dữ liệu đăng nhập
        sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        loadSavedCredentials();

        //xử lý đăng nhập
        btnLogin.setOnClickListener(view -> loginUser());
        //điều hướng đến màn hình đăng ký
        btnRegister.setOnClickListener(view -> registerUser());
        //điều hướng đến màn hình quên mật khẩu
        btnForgotPass.setOnClickListener(View -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });
        //điều hướng đến màn hình start app
        btnBack.setOnClickListener(View -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userRole");
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, StartAppActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void registerUser() {
        if (userRole.equals("customer")) {
            Intent intent = new Intent(LoginActivity.this, CustomerRegisterActivity.class);
            startActivity(intent);
        }
        if (userRole.equals("shipper")) {
            Intent intent = new Intent(LoginActivity.this, ShipRegisterActivity.class);
            startActivity(intent);
        }
        if (userRole.equals("restaurant")) {
            Intent intent = new Intent(LoginActivity.this, RestaurantRegisterActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Hãy điền tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email này không tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            if (userRole.equals("customer")) {
                customerUserRepository.login(email, pass, new CustomerUserRepository.OnLoginListener() {
                    @Override
                    public void onSuccess(CustomerUser user) {
                        runOnUiThread(() -> handleLoginSuccess(user));
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show());
                    }
                });
            } else if (userRole.equals("shipper")) {
                shipperRepository.login(email, pass, new ShipperRepository.OnLoginListener() {
                    @Override
                    public void onSuccess(Shipper user) {
                        runOnUiThread(() -> handleLoginSuccess(user));
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show());
                    }
                });
            } else if (userRole.equals("restaurant")) {
                restaurantRepository.login(email, pass, new RestaurantRepository.OnLoginListener() {
                    @Override
                    public void onSuccess(Restaurant user) {
                        runOnUiThread(() -> handleLoginSuccess(user));
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });
    }

    // Hàm xử lý đăng nhập thành công
    private void handleLoginSuccess(Object user) {
        if (user != null) {
            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            if (cbRemember.isChecked()) {
                saveCredentials(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim());
            } else {
                clearSavedCredentials();
            }

            Intent intent = null;
            if (userRole.equals("customer")) {
                intent = new Intent(LoginActivity.this, MenuItemsActivity.class);
            } else if (userRole.equals("shipper")) {
                intent = new Intent(LoginActivity.this, ShipperActivity.class);
                Shipper shipperUser = (Shipper) user;
                intent.putExtra("shipperId", shipperUser.getShipperId());
            } else if (userRole.equals("restaurant")) {
                intent = new Intent(LoginActivity.this, MenuItemsActivity.class);
                Restaurant restaurant = (Restaurant) user;
                intent.putExtra("restaurantId", restaurant.getId());
            }

            if (intent != null) {
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
        }
    }



    private void loadSavedCredentials() {
        if (sharedPreferences.getBoolean("remember", false)) {
            edtEmail.setText(sharedPreferences.getString("email", ""));
            edtPassword.setText(sharedPreferences.getString("password", ""));
            cbRemember.setChecked(true);
        }
    }

    private void saveCredentials(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("remember", true);
        editor.apply();
    }

    private void clearSavedCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}