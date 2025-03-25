package com.example.sellapp.retrofit;

import com.example.sellapp.Model.LoaiSPModel;
import com.example.sellapp.Model.MessageModel;
import com.example.sellapp.Model.SanPhamMoiModel;
import com.example.sellapp.Model.UserModel;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
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
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> dangNhap(
            @Field("email") String email,
            @Field("pass") String pass
    );
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<UserModel> createOder(
            @Field("email") String email,
            @Field("sodienthoai") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int id,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );
    @POST("search.php")
    @FormUrlEncoded
    @NonNull
    Observable<SanPhamMoiModel> search(
        @Field("search") String search
    );
    @POST("xoa.php")
    @FormUrlEncoded
    @NonNull
    Observable<MessageModel> xoaSanPham(
            @Field("id") int id
    );
    @POST("insertsp.php")
    @FormUrlEncoded
    Observable<MessageModel> insertSp(
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int id
    );
    @POST("updatesp.php")
    @FormUrlEncoded
    Observable<MessageModel> updateSp(
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int idloai,
            @Field("id") int id
    );


}

