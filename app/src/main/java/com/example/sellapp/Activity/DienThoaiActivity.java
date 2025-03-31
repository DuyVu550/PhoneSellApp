package com.example.sellapp.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
    Handler handler = new Handler();
    boolean isLoading = true;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dien_thoai);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleView_dienThoai);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        apiBanhang = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanhang.class);
        sanPhamMoiList = new ArrayList<>();
        getData(page); //l
        ActionBar();
        addEventLoad();
    }
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void addEventLoad(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isLoading){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamMoiList.size() - 1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }
    private void loadMore(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                sanPhamMoiList.add(null);
                adapter.notifyItemInserted(sanPhamMoiList.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sanPhamMoiList.remove(sanPhamMoiList.size() - 1);
                adapter.notifyItemRemoved(sanPhamMoiList.size());
                page++;
                getData(page);
                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }
    private void getData(int page){
        compositeDisposable.add((apiBanhang.getSanPham(page, loai).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(
                        SanPhamMoiModel -> {
                            if(SanPhamMoiModel.isSuccess()){
                                if(adapter == null){
                                    sanPhamMoiList = SanPhamMoiModel.getResult();
                                    adapter = new DienThoaiAdapter(getApplicationContext(), sanPhamMoiList);
                                    recyclerView.setAdapter(adapter);
                                }
                                else{
                                    int viTri = sanPhamMoiList.size() - 1;
                                    int soLuongAdd= SanPhamMoiModel.getResult().size();
                                    for(int i =0; i < viTri; i++){
                                        sanPhamMoiList.add(SanPhamMoiModel.getResult().get(i));
                                    }
                                    adapter.notifyItemRangeInserted(viTri, soLuongAdd);
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Hết dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Looix", Toast.LENGTH_LONG).show();
                        }

                )));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}