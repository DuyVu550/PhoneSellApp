package com.example.sellapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sellapp.Adapter.GioHangAdapter;
import com.example.sellapp.EventBus.tinhTongEvent;
import com.example.sellapp.R;
import com.example.sellapp.utils.Utils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity {

    TextView gioHangTrong, tongTien;
    Toolbar toolbarGioHang;
    RecyclerView recyclerView;
    Button btnMuaHang;
    GioHangAdapter adapter;
    long tongTienSP = 0;

    // Khai báo spinner và ImageView
    Spinner spinnerPaymentMethod;
    ImageView imgQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gio_hang);
        Anhxa();
        InitControl();
        tinhTongTien();
    }

    private void tinhTongTien(){
        tongTienSP = 0;
        for(int i = 0; i < Utils.mangiohang.size(); i++){
            tongTienSP += Utils.mangiohang.get(i).getGiasp() * Utils.mangiohang.get(i).getSoluong();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongTien.setText(decimalFormat.format(tongTienSP));
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
        else {
            adapter = new GioHangAdapter(getApplicationContext(), Utils.mangiohang);
            recyclerView.setAdapter(adapter);
        }
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
                intent.putExtra("tongTien", tongTienSP);
                // Không xóa giỏ hàng ở đây
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(tinhTongEvent event){
        if(event != null){
            tinhTongTien();
        }
    }
}
