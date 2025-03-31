package com.example.sellapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sellapp.Adapter.LoaiSPAdapter;
import com.example.sellapp.Adapter.SanPhamMoiAdapter;
import com.example.sellapp.Model.LoaiSP;
import com.example.sellapp.Model.SanPhamMoi;
import com.example.sellapp.Model.SanPhamMoiModel;
import com.example.sellapp.R;
import com.example.sellapp.retrofit.APIBanhang;
import com.example.sellapp.retrofit.RetrofitClient;
import com.example.sellapp.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    // Ánh xa
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView ;
    ListView listViewMain ;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    LoaiSPAdapter loaiSPAdapter;
    List<LoaiSP> mangloaiSP = new ArrayList<>();;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIBanhang apiBanhang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        AnhXa();
        ActionBar();
        if(isConnected(this)){
            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        int total = 0;
        for (int i = 0; i < Utils.mangiohang.size(); i++) {
            total += Utils.mangiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(total));
    }
    private void AnhXa(){
        toolbar = findViewById(R.id.toolBarMain);
        viewFlipper = findViewById(R.id.viewFlipper);
        recyclerView = findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        listViewMain = findViewById(R.id.ListViewMain);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        apiBanhang = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanhang.class);
        mangSpMoi = new ArrayList<>();
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.frameGioHang);
        imgSearch = findViewById(R.id.imgSearch);
        if(Utils.mangiohang == null){
            Utils.mangiohang = new ArrayList<>();
        }else {
            int total = 0;
            for (int i = 0; i < Utils.mangiohang.size(); i++) {
                total += Utils.mangiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(total));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gioHang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(gioHang);
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }
    private  void getEventClick(){
        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        Intent trangChu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangChu);
                        break;
                    case 4:
                        Intent dienThoai = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        dienThoai.putExtra("loai", 2);
                        startActivity(dienThoai);
                        break;
                    case 5:
                        Paper.book().delete("email");
                        Paper.book().delete("pass");
                        Intent dangNhap = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(dangNhap);
                        break;
                }

            }
        });
    }
    private void getSpMoi(){
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
    }
    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    private void ActionViewFlipper(){
        List<String> mangQuangCao = new ArrayList<>();
        mangQuangCao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        mangQuangCao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        mangQuangCao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for(int i =0; i < mangQuangCao.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext()); // view hinh anh
            Glide.with(getApplicationContext()).load(mangQuangCao.get(i)).into(imageView); // Lay hinh anh gan vao imageView
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setInAnimation(slide_out);
    }
    private boolean isConnected(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        boolean isAvailable = false;
        if (networkCapabilities != null) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                isAvailable = true;
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                isAvailable = true;
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                isAvailable = true;
            }
        }
        return isAvailable;
    }
}