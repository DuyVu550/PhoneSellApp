package com.example.sellapp.utils;

import com.example.sellapp.Model.GioHang;
import com.example.sellapp.Model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<GioHang> mangiohang = new ArrayList<>();
    public static final String BASE_URL = "http://192.168.0.247/sellapp/";
    public static User user_current = new User();
}
