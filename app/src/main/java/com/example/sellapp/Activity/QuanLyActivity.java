package com.example.sellapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellapp.Adapter.SanPhamMoiAdapter;
import com.example.sellapp.EventBus.SuaXoaEvent;
import com.example.sellapp.Model.SanPhamMoi;
import com.example.sellapp.R;
import com.example.sellapp.retrofit.APIBanhang;
import com.example.sellapp.retrofit.RetrofitClient;
import com.example.sellapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanLyActivity extends AppCompatActivity {

    ImageView img_them, img_DangXuat;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIBanhang apiBanhang;
    RecyclerView recyclerViewQuanLy;
    List<SanPhamMoi> list = new ArrayList<>();
    SanPhamMoiAdapter spAdapter;
    SanPhamMoi sanPhamMoiSuaXoa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_ly);
        apiBanhang = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanhang.class);
        initView();
        initControl();
        getSpMoi();
    }

    private void initControl() {
        img_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThemSpActivity.class);
                startActivity(intent);
            }
        });
        img_DangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyActivity.this);
                builder.setMessage("Bạn muốn đăng xuất?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

    }
    private void initView() {
        img_them = findViewById(R.id.img_them);
        img_DangXuat = findViewById(R.id.img_dangXuat);
        recyclerViewQuanLy = findViewById(R.id.recyclerView_QuanLy);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewQuanLy.setHasFixedSize(true);
        recyclerViewQuanLy.setLayoutManager(layoutManager);
    }
    private void getSpMoi(){
        compositeDisposable.add(apiBanhang.getSpMoi().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                list = sanPhamMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(), list);
                                recyclerViewQuanLy.setAdapter(spAdapter);
                            }
                        }
                ));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Sửa")){
            suaSanPham();
        } else if(item.getTitle().equals("Xóa")){
            xoaSanPham();
        }
        return super.onContextItemSelected(item);
    }

    private void xoaSanPham() {
        compositeDisposable.add(apiBanhang.xoaSanPham(sanPhamMoiSuaXoa.getId()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(
                      messageModel -> {
                          if(messageModel.isSuccess()){
                              Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                              getSpMoi();
                          }
                          else{
                              Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                          }
                      }
                ));

    }

    private void suaSanPham() {
        Intent intent = new Intent(getApplicationContext(), ThemSpActivity.class);
        intent.putExtra("sua", sanPhamMoiSuaXoa);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventSuaXoa(SuaXoaEvent event){
        if(event != null){
            sanPhamMoiSuaXoa = event.getSanPhamMoi();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}