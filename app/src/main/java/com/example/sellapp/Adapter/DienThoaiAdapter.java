package com.example.sellapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sellapp.Model.SanPhamMoi;
import com.example.sellapp.R;

import java.text.DecimalFormat;
import java.util.List;

public class DienThoaiAdapter extends RecyclerView.Adapter<DienThoaiAdapter.MyViewHolder> {
    Context context;
    List<SanPhamMoi> array;

    public DienThoaiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public DienThoaiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dien_thoai, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DienThoaiAdapter.MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi = array.get(position);
        holder.tensp.setText(sanPhamMoi.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.giasp.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiaSP())) + " Đ");
        holder.mota.setText(sanPhamMoi.getMota());
        Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imgHinh);
    }

    @Override
    public int getItemCount() {
        return array.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tensp, giasp, mota;
        ImageView imgHinh;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tensp = itemView.findViewById(R.id.itemdt_ten);
            giasp = itemView.findViewById(R.id.item_gia);
            imgHinh = itemView.findViewById(R.id.item_dtImage);
            mota = itemView.findViewById(R.id.item_mota);
        }
    }
}
