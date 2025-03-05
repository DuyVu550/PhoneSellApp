package com.example.sellapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.sellapp.Model.SanPhamMoi;
import com.example.sellapp.R;

import java.text.DecimalFormat;

public class ChitietActivity extends AppCompatActivity {

    TextView tensp, giasp, mota;
    Button btnThem;
    Toolbar toolbar;
    ImageView imghinhanh;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chitiet);
        Anhxa();
        ActionBar();
        InitData();
    }
    private void Anhxa(){
        tensp = findViewById(R.id.txtTensp);
        giasp = findViewById(R.id.txtGiaSp);
        mota = findViewById(R.id.txtMota);
        btnThem = findViewById(R.id.btnThemVaoGioHang);
        spinner = findViewById(R.id.spinner);
        imghinhanh = findViewById(R.id.imgchitiet);
        toolbar = findViewById(R.id.toolbarChiTiet);
    }
    private void InitData(){
        SanPhamMoi sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet", SanPhamMoi.class);
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiaSP())) + " Đ");
        Integer[] arr = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arr);
        spinner.setAdapter(adapterspin);
    }
    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}