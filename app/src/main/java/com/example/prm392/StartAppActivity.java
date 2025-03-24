package com.example.prm392;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartAppActivity extends AppCompatActivity implements View.OnClickListener{
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_app);

        ((Button)findViewById(R.id.btn_customer)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_shipper)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_restaurant)).setOnClickListener(this);
        sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        String userole = sharedPreferences.getString("userRole", "");
        if(!userole.isBlank()){
            Intent intent = new Intent(StartAppActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_customer){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userRole", "customer");
            editor.apply();
        }
        else if(view.getId() == R.id.btn_shipper){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userRole", "shipper");
            editor.apply();
        }
        else if(view.getId() == R.id.btn_restaurant){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userRole", "restaurant");
            editor.apply();
        }
        Intent intent = new Intent(StartAppActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}