package com.example.sellapp.retrofit;

import com.example.sellapp.Model.LoaiSPModel;
import com.example.sellapp.Model.SanPhamMoiModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

;

public interface APIBanhang {
    @GET("getLoai.php")
    Observable<LoaiSPModel> getLoaiSP();
    @GET("getSpMoi.php")
    Observable<SanPhamMoiModel> getSpMoi();
    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
            );
    @POST("dangky.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> dangKy(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile
    );
}

