package com.example.sellapp.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellapp.Adapter.DienThoaiAdapter;
import com.example.sellapp.Model.SanPhamMoi;
import com.example.sellapp.R;
import com.example.sellapp.retrofit.APIBanhang;
import com.example.sellapp.retrofit.RetrofitClient;
import com.example.sellapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DienThoaiActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIBanhang apiBanhang;
    DienThoaiAdapter adapter;
    List<SanPhamMoi> sanPhamMoiList;
    int page;
    int loai = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dien_thoai);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleView_dienThoai);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        apiBanhang = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanhang.class);
        sanPhamMoiList = new ArrayList<>();
        loai = getIntent().getIntExtra("loai", 2);
        getData(); //l
        ActionBar();
    }
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void getData(){
        compositeDisposable.add((apiBanhang.getSanPham(page, loai).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(
                        SanPhamMoiModel -> {
                            if(SanPhamMoiModel.isSuccess()){
                                sanPhamMoiList = SanPhamMoiModel.getResult();
                                adapter = new DienThoaiAdapter(getApplicationContext(), sanPhamMoiList);
                                recyclerView.setAdapter(adapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Looix", Toast.LENGTH_LONG).show();
                        }

                )));
    }
    /* private void getSpMoi(){
         compositeDisposable.add(apiBanhang.getSpMoi().
                 subscribeOn(Schedulers.io()).
                 observeOn(AndroidSchedulers.mainThread()).
                 subscribe(
                         sanPhamMoiModel -> {
                             if(sanPhamMoiModel.isSuccess()){
                                 mangSpMoi = sanPhamMoiModel.getResult();
                                 spAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSpMoi);
                                 recyclerView.setAdapter(spAdapter);
                             }
                         }
                 ));
     }
     private void getLoaiSanPham(){
         compositeDisposable.add((apiBanhang.getLoaiSP().subscribeOn(Schedulers.io()).
                 observeOn(AndroidSchedulers.mainThread()).
                 subscribe(
                         loaiSPModel -> {
                             if(loaiSPModel.isSuccess()){
                                 mangloaiSP = loaiSPModel.getResult();
                                 loaiSPAdapter = new LoaiSPAdapter(mangloaiSP, getApplicationContext());
                                 listViewMain.setAdapter(loaiSPAdapter);
                             }
                         }
                 )));
     }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}