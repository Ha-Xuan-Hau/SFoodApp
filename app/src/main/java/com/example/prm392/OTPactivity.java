package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.prm392.service.EmailSender;
import com.example.prm392.service.OTPGenerator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OTPactivity extends AppCompatActivity {
    private EditText edtOtp;
    private Button btnVerifyOtp;
    private TextView tvEmail;
    private String email;
    private String generatedOTP;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpactivity);

        email = getIntent().getStringExtra("email");

        edtOtp = findViewById(R.id.edt_OTP);
        btnVerifyOtp = findViewById(R.id.btn_otp_submit);
        tvEmail = findViewById(R.id.tv_otp_email);

        String tv = tvEmail.getText().toString() + " " + email;
        tvEmail.setText(tv);

        // Tạo và gửi OTP khi mở màn hình
        generatedOTP = OTPGenerator.generateOTP();
        sendOtpInBackground(email, generatedOTP);

        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP();
            }
        });
    }

    private void verifyOTP() {
        String otp = edtOtp.getText().toString().trim();

        if (TextUtils.isEmpty(otp)) {
            Toast.makeText(this, "Vui lòng nhập mã OTP!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (otp.equals(generatedOTP)) {
            Toast.makeText(this, "Xác nhận thành công!", Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();  // Đóng màn hình OTPActivity
        } else {
            Toast.makeText(this, "Mã OTP không đúng!", Toast.LENGTH_SHORT).show();
        }
    }

    // Chạy gửi OTP trên background thread
    private void sendOtpInBackground(String email, String otp) {
        executorService.execute(() -> {
            EmailSender.sendOtp(email, otp);
            runOnUiThread(() ->
                    Toast.makeText(this, "OTP đã được gửi!", Toast.LENGTH_SHORT).show()
            );
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();  // Đóng ExecutorService để tránh rò rỉ bộ nhớ
    }
}
