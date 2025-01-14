package com.example.phonesellapp.Activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phonesellapp.Adapter.LoaiSPAdapter;
import com.example.phonesellapp.Model.LoaiSP;
import com.example.phonesellapp.R;
import com.example.phonesellapp.retrofit.APIBanhang;
import com.example.phonesellapp.retrofit.RetrofitClient;
import com.example.phonesellapp.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

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
    List<LoaiSP> mangloaiSP;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIBanhang apiBanhang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolBarMain);
        viewFlipper = findViewById(R.id.viewFlipper);
        recyclerView = findViewById(R.id.recycleView);
        listViewMain = findViewById(R.id.ListViewMain);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        mangloaiSP = new ArrayList<>();
        loaiSPAdapter = new LoaiSPAdapter(mangloaiSP, getApplicationContext());
        listViewMain.setAdapter(loaiSPAdapter);
        apiBanhang = RetrofitClient.getInstance(Utils.BASE_URL).create(APIBanhang.class);

        ActionBar();
        if(isConnected(this)){
            Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
            ActionViewFlipper();
            getLoaiSanPham();
        }
    }
    private void getLoaiSanPham(){
       compositeDisposable.add((apiBanhang.getLoaiSP().subscribeOn(Schedulers.io()).
               observeOn(AndroidSchedulers.mainThread()).
               subscribe(
                       loaiSPModel -> {
                           if(loaiSPModel.isSuccess()){
                               Toast.makeText(getApplicationContext(), loaiSPModel.getResult().get(0).getTensanpham(), Toast.LENGTH_LONG).show();
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
    private boolean isConnected(Context context) {
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