package com.example.sellapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sellapp.R;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangky;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);
        Anhxa();
        initControl();

    }
    private void Anhxa(){
        txtdangky = findViewById(R.id.txtdangky);
    }
    private void initControl(){
        txtdangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DangKyActivity.class);
                startActivity(intent);
            }
        });
    }
}