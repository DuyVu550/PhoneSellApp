package com.example.sellapp.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.sellapp.R;
import com.example.sellapp.retrofit.APIBanhang;
import com.example.sellapp.retrofit.RetrofitClient;
import com.example.sellapp.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {

    Toolbar toolbarThanhToan;
    APIBanhang apiBanhang;
    TextView txtTongTien, txtsodt, txtemail;
    EditText edtdiachi;
    long tongTien;
    Spinner spinnerPaymentMethod;
    ImageView imgQR;
    int total;
    AppCompatButton btnDatHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        CountItem();
        initControl();

        // Thiết lập listener cho spinner
        spinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String method = parent.getItemAtPosition(position).toString();
                if(method.equals("Thanh toán Momo")){
                    imgQR.setVisibility(View.VISIBLE);
                } else {
                    imgQR.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                imgQR.setVisibility(View.GONE);
            }
        });
    }
    private void CountItem(){
        total = 0;
        for(int i = 0; i < Utils.mangmuahang.size(); i++){
            total += Utils.mangmuahang.get(i).getSoluong();
        }
    }
    private void initControl() {
        setSupportActionBar(toolbarThanhToan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarThanhToan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongTien = getIntent().getLongExtra("tongTien", 0);
        txtTongTien.setText(decimalFormat.format(tongTien));
        txtemail.setText(Paper.book().read("email"));
        txtsodt.setText(Utils.user_current.getMobile());
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_diachi = edtdiachi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                } else {
                    // post data
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();
                    Log.d("test", new Gson().toJson(Utils.mangiohang));
                    compositeDisposable.add(apiBanhang.createOder(str_email, str_sdt, String.valueOf(tongTien), id, str_diachi, total, new Gson().toJson(Utils.mangiohang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_LONG).show();
                                        // Sau khi đặt hàng thành công, xóa giỏ hàng
//                                        Utils.mangmuahang.clear();
                                        if (Utils.mangmuahang != null && !Utils.mangmuahang.isEmpty()) {
                                            Utils.mangiohang.removeAll(Utils.mangmuahang);
                                        }

                                        // Chuyển màn hình
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        // Xử lý lỗi khi API gọi thất bại
                                        Log.e("ThanhToanActivity", "Error creating order", throwable);
                                        Toast.makeText(getApplicationContext(), "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                                    }
                            )
                    );


                }
            }
        });

    }
    private void initView(){
        apiBanhang = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanhang.class);
        toolbarThanhToan = findViewById(R.id.toolbarThanhToan);
        txtTongTien = findViewById(R.id.txtTien);
        txtemail = findViewById(R.id.txtEmail);
        edtdiachi = findViewById(R.id.edtDiaChi);
        txtsodt = findViewById(R.id.txtSoDienThoai);
        btnDatHang = findViewById(R.id.btnDatHang);
        spinnerPaymentMethod = findViewById(R.id.spinnerPaymentMethod);
        imgQR = findViewById(R.id.imgQR);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}