package com.example.phonesellapp.retrofit;

import com.example.phonesellapp.Model.LoaiSPModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

;

public interface APIBanhang {
    @GET("getLoai.php")
    Observable<LoaiSPModel> getLoaiSP();
}
