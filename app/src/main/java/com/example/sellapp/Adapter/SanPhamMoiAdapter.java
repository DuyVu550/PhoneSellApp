package com.example.sellapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sellapp.Activity.ChitietActivity;
import com.example.sellapp.EventBus.SuaXoaEvent;
import com.example.sellapp.Interface.ItemClickListener;
import com.example.sellapp.Model.SanPhamMoi;
import com.example.sellapp.R;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
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
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGia.setText(decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiaSP())) + "D");
        Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imghinhAnh);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, ChitietActivity.class);
                    intent.putExtra("chitiet", sanPhamMoi);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else{
                    EventBus.getDefault().postSticky(new SuaXoaEvent(sanPhamMoi));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        TextView txtGia, txtTen;
        ImageView imghinhAnh;
        private ItemClickListener itemClickListener;
       public MyViewHolder(@NonNull View itemView){
           super(itemView);
           txtGia = itemView.findViewById(R.id.itemsp_gia);
           txtTen = itemView.findViewById(R.id.itemsp_ten);
           imghinhAnh = itemView.findViewById(R.id.itemsp_image);
           itemView.setOnClickListener(this);
           itemView.setOnCreateContextMenuListener(this);
           itemView.setOnLongClickListener(this);
       }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
           itemClickListener.onClick(view, getAdapterPosition(), false);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0,0,getAdapterPosition(), "Sửa");
            menu.add(0,1, getAdapterPosition(), "Xóa");
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
            return false;
        }
    }

}

