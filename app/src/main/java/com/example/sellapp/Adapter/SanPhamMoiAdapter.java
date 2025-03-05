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

import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder> {
    Context context;
    List<SanPhamMoi> array;

    public SanPhamMoiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_moi, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi = array.get(position);
        holder.txtTen.setText(sanPhamMoi.getTensp());
        holder.txtGia.setText(sanPhamMoi.getGiaSP());
        Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imghinhAnh);
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtGia, txtTen;
        ImageView imghinhAnh;
       public MyViewHolder(@NonNull View itemView){
           super(itemView);
           txtGia = itemView.findViewById(R.id.itemsp_gia);
           txtTen = itemView.findViewById(R.id.itemsp_ten);
           imghinhAnh = itemView.findViewById(R.id.itemsp_image);
       }
   }
}

