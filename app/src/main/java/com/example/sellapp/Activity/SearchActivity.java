package com.example.sellapp.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellapp.Adapter.DienThoaiAdapter;
import com.example.sellapp.Model.SanPhamMoi;
import com.example.sellapp.Model.SanPhamMoiModel;
import com.example.sellapp.R;
import com.example.sellapp.retrofit.APIBanhang;
import com.example.sellapp.retrofit.RetrofitClient;
import com.example.sellapp.utils.Utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    DienThoaiAdapter adapter;
    List<SanPhamMoi> sanPhamMoiList;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIBanhang apiBanhang;
    EditText edtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        AnhXa();
        ActionBar();

    }
    private void AnhXa(){
        sanPhamMoiList = new ArrayList<>();
        apiBanhang = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanhang.class);
        toolbar = findViewById(R.id.toolbarSearch);
        recyclerView = findViewById(R.id.recycleView_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerView.setLayoutManager(layoutManager);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    sanPhamMoiList.clear();
                    adapter = new DienThoaiAdapter(getApplicationContext(), sanPhamMoiList);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    getDataSearch(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    private void getDataSearch(String str_search) {
        sanPhamMoiList.clear();
        compositeDisposable.add(apiBanhang.search(str_search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            sanPhamMoiList = sanPhamMoiModel.getResult();
                            adapter = new DienThoaiAdapter(getApplicationContext(), sanPhamMoiList);
                            recyclerView.setAdapter(adapter);

                        }
                ));



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

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}