package com.example.sellapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellapp.Adapter.GioHangAdapter;
import com.example.sellapp.R;
import com.example.sellapp.utils.Utils;

public class GioHangActivity extends AppCompatActivity {

    TextView gioHangTrong, tongTien;
    Toolbar toolbarGioHang;
    RecyclerView recyclerView;
    Button btnMuaHang;
    GioHangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gio_hang);
        Anhxa();
        InitControl();
    }
    private void Anhxa(){
        gioHangTrong = findViewById(R.id.txtgiohangtrong);
        toolbarGioHang = findViewById(R.id.toolbarGioHang);
        recyclerView = findViewById(R.id.recycleviewgiohang);
        tongTien = findViewById(R.id.txttongtien);
        btnMuaHang = findViewById(R.id.btnmuahang);

    }
    private void InitControl(){
        setSupportActionBar(toolbarGioHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarGioHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.mangiohang.size() == 0){
            gioHangTrong.setVisibility(View.VISIBLE);
        }
        else{
            adapter = new GioHangAdapter(getApplicationContext(), Utils.mangiohang);
            recyclerView.setAdapter(adapter);
        }


    }
}