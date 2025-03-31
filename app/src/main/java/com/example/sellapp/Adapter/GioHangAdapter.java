package com.example.sellapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.service.autofill.FillEventHistory;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sellapp.EventBus.tinhTongEvent;
import com.example.sellapp.Interface.IImageClickListener;
import com.example.sellapp.Model.GioHang;
import com.example.sellapp.R;
import com.example.sellapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.time.temporal.Temporal;
import java.util.List;

import okhttp3.internal.Util;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public GioHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gio_hang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangAdapter.MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_giohang_tensp.setText(gioHang.getTensp());
        holder.item_giohang_soluong.setText(gioHang.getSoluong() + "");
        Glide.with(context).load(gioHang.getHinhsp()).into(holder.item_giohang_image);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_giohang_gia.setText("Giá: " + decimalFormat.format(gioHang.getGiasp()));
        long gia = gioHang.getSoluong() * gioHang.getGiasp();
        holder.item_giohang_gia2.setText(decimalFormat.format(gia));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Utils.mangmuahang.add(gioHang);
                    EventBus.getDefault().postSticky(new tinhTongEvent());
                }else{
                    for(int i =0; i < Utils.mangmuahang.size(); i++){
                        if(Utils.mangmuahang.get(i).getIdsp() == gioHang.getIdsp()){
                            Utils.mangmuahang.remove(i);
                            EventBus.getDefault().postSticky(new tinhTongEvent());
                        }
                    }
                }

            }
        });
        holder.setListener(new IImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int value) {
                if(value == 1){
                    if(gioHangList.get(pos).getSoluong() > 1){
                        int soLuongMoi = gioHangList.get(pos).getSoluong() - 1;
                        gioHangList.get(pos).setSoluong(soLuongMoi);
                        holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong() + "");
                        long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                        holder.item_giohang_gia2.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new tinhTongEvent());
                    }
                    else if(gioHangList.get(pos).getSoluong() == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm này");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.mangiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new tinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                } else if (value == 2) {
                    if(gioHangList.get(pos).getSoluong() < 10){
                        int soLuongMoi = gioHangList.get(pos).getSoluong() + 1;
                        gioHangList.get(pos).setSoluong(soLuongMoi);
                    }
                    holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong() + "");
                    long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                    holder.item_giohang_gia2.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new tinhTongEvent());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }
    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_image, imgtru, imgcong;
        TextView item_giohang_tensp, item_giohang_gia, item_giohang_soluong, item_giohang_gia2;
        IImageClickListener listener;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_gia2 = itemView.findViewById(R.id.item_giohang_giasp2);
            imgtru = itemView.findViewById(R.id.item_giohang_tru);
            imgcong = itemView.findViewById(R.id.item_giohang_cong);
            checkBox = itemView.findViewById(R.id.item_giohang_check);
            imgcong.setOnClickListener(this);
            imgtru.setOnClickListener(this);
        }
        public void setListener(IImageClickListener listener){
            this.listener = listener;
        }
        @Override
        public void onClick(View view) {
            if(view == imgtru){
                listener.onImageClick(view, getAdapterPosition(), 1);
            }
            else if(view == imgcong){
                listener.onImageClick(view, getAdapterPosition(), 2);
            }
        }
    }
}
