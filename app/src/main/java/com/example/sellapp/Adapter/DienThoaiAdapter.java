package com.example.sellapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sellapp.Activity.ChitietActivity;
import com.example.sellapp.Interface.ItemClickListener;
import com.example.sellapp.Model.SanPhamMoi;
import com.example.sellapp.R;

import java.text.DecimalFormat;
import java.util.List;

public class DienThoaiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi> array;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 0;

    public DienThoaiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dien_thoai, parent, false);
            return new MyViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            SanPhamMoi sanPhamMoi = array.get(position);
            myViewHolder.tensp.setText(sanPhamMoi.getTensp());
            myViewHolder.idsp.setText(sanPhamMoi.getId() + "");
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.giasp.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiaSP())) + " Đ");
            myViewHolder.mota.setText(sanPhamMoi.getMota());
            Glide.with(context).load(sanPhamMoi.getHinhanh()).into(myViewHolder.imgHinh);
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(!isLongClick){
                        Intent intent = new Intent(context, ChitietActivity.class);
                        intent.putExtra("chitiet", sanPhamMoi);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }else{
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }
    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView){
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);


        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tensp, giasp, mota, idsp;
        ImageView imgHinh;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tensp = itemView.findViewById(R.id.itemdt_ten);
            giasp = itemView.findViewById(R.id.item_gia);
            imgHinh = itemView.findViewById(R.id.item_dtImage);
            mota = itemView.findViewById(R.id.item_mota);
            idsp = itemView.findViewById(R.id.itemdt_idsp);
            itemView.setOnClickListener(this::onClick);
        }
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;

        }
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
}
