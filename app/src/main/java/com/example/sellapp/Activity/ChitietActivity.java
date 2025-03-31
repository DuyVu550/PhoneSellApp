package com.example.sellapp.Activity;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.example.sellapp.Model.GioHang;
import com.example.sellapp.Model.SanPhamMoi;
import com.example.sellapp.Model.SanPhamMoiModel;
import com.example.sellapp.R;
import com.example.sellapp.utils.Utils;
import com.google.android.material.badge.BadgeDrawable;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChitietActivity extends AppCompatActivity {

    TextView tensp, giasp, mota;
    Button btnThem;
    Toolbar toolbar;
    ImageView imghinhanh;
    Spinner spinner;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chitiet);
        Anhxa();
        ActionBar();
        InitData();
        InitControl();
    }
    private void InitControl(){
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang();
            }
        });
    }
    private void themGioHang(){
        if(Utils.mangiohang.size() > 0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i = 0; i < Utils.mangiohang.size(); i++){
                if(Utils.mangiohang.get(i).getIdsp() == sanPhamMoi.getId()){
                    Utils.mangiohang.get(i).setSoluong(soluong + Utils.mangiohang.get(i).getSoluong());
                    long gia = Long.parseLong(sanPhamMoi.getGiaSP() + Utils.mangiohang.get(i).getSoluong());
                    Utils.mangiohang.get(i).setGiasp(gia);
                    flag = true;
                }
            }
            if(!flag){
                long gia = Long.parseLong(sanPhamMoi.getGiaSP()) * soluong;
                GioHang gioHang = new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setTensp(sanPhamMoi.getTensp());
                gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                Utils.mangiohang.add(gioHang);
            }
        }
        else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiaSP()) * soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setTensp(sanPhamMoi.getTensp());
            gioHang.setHinhsp(sanPhamMoi.getHinhanh());
            Utils.mangiohang.add(gioHang);
        }
        badge.setText(String.valueOf(Utils.mangiohang.size()));
    }
    private void Anhxa(){
        tensp = findViewById(R.id.txtTensp);
        giasp = findViewById(R.id.txtGiaSp);
        mota = findViewById(R.id.txtMota);
        btnThem = findViewById(R.id.btnThemVaoGioHang);
        spinner = findViewById(R.id.spinner);
        imghinhanh = findViewById(R.id.imgchitiet);
        toolbar = findViewById(R.id.toolbarChiTiet);
        badge = findViewById(R.id.menu_sl);
        FrameLayout frameLayout = findViewById(R.id.frameGioHang);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
            }
        });
        if(Utils.mangiohang != null){
            int total = 0;
            for(int i = 0; i < Utils.mangiohang.size(); i++){
                total += Utils.mangiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(total));
        }
    }
    private void InitData(){
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiaSP())) + " Đ");
        Integer[] arr = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arr);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.mangiohang != null){
            int total = 0;
            for(int i = 0; i < Utils.mangiohang.size(); i++){
                total += Utils.mangiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(total));
        }
    }
}