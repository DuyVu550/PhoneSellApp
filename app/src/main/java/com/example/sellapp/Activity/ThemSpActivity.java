package com.example.sellapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sellapp.Model.SanPhamMoi;
import com.example.sellapp.R;
import com.example.sellapp.databinding.ActivityThemSpBinding;
import com.example.sellapp.retrofit.APIBanhang;
import com.example.sellapp.retrofit.RetrofitClient;
import com.example.sellapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThemSpActivity extends AppCompatActivity {

    Spinner spinner;
    int loai = 0;
    ActivityThemSpBinding binding;
    APIBanhang apiBanhang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    SanPhamMoi sanPhamSua;
    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityThemSpBinding.inflate(getLayoutInflater());
        apiBanhang = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanhang.class);
        setContentView(binding.getRoot());
        initView();
        initData();
        Intent intent = getIntent();
        sanPhamSua = intent.getSerializableExtra("sua", SanPhamMoi.class);

        if(sanPhamSua == null){
            flag = false;

        }
        else{
            flag = true;
            binding.btnthem.setText("Sửa");
            binding.mota.setText(sanPhamSua.getMota());
            binding.giasp.setText(sanPhamSua.getGiaSP() + "");
            binding.tensp.setText(sanPhamSua.getTensp());
            binding.hinhanh.setText(sanPhamSua.getHinhanh());
            binding.spinnerLoai.setSelection(sanPhamSua.getLoai());
        }
    }

    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Vui lòng chọn:");
        stringList.add("loai 1");
        stringList.add("loai 2");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
        spinner.setAdapter(adapter);
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               loai = position;
           }
           @Override
           public void onNothingSelected(AdapterView<?> parent) {
           }
       });
       binding.btnthem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(flag == false){
                   String str_ten = binding.tensp.getText().toString().trim();
                   String str_gia = binding.giasp.getText().toString().trim();
                   String str_mota = binding.mota.getText().toString().trim();
                   String str_hinhAnh = binding.hinhanh.getText().toString().trim();
                   if(TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia)|| TextUtils.isEmpty(str_mota)|| TextUtils.isEmpty(str_hinhAnh)|| loai == 0){
                       Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ", Toast.LENGTH_SHORT).show();
                   }else{
                       compositeDisposable.add(apiBanhang.insertSp(str_ten, str_gia, str_hinhAnh, str_mota, (loai)).
                               subscribeOn(Schedulers.io()).
                               observeOn(AndroidSchedulers.mainThread()).
                               subscribe(
                                       messageModel -> {
                                           if(messageModel.isSuccess()){
                                               Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                           }
                                           else{
                                               Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                           }
                                       }
                               ));
                   }
               }
               else{
                   suaSanPham();
               }

           }
       });
    }

    private void suaSanPham() {
        String str_ten = binding.tensp.getText().toString().trim();
        String str_gia = binding.giasp.getText().toString().trim();
        String str_mota = binding.mota.getText().toString().trim();
        String str_hinhAnh = binding.hinhanh.getText().toString().trim();
        if(TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia)|| TextUtils.isEmpty(str_mota)|| TextUtils.isEmpty(str_hinhAnh)|| loai == 0){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ", Toast.LENGTH_SHORT).show();
        }else{
            compositeDisposable.add(apiBanhang.updateSp(str_ten, str_gia, str_hinhAnh, str_mota, loai, sanPhamSua.getId()).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(
                            messageModel -> {
                                if(messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                    ));
        }
    }

    private void initView() {
        spinner = findViewById(R.id.spinner_loai);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}