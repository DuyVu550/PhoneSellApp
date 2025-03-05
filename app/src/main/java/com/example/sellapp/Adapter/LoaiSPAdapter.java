package com.example.sellapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sellapp.Model.LoaiSP;
import com.example.sellapp.R;

import java.util.List;

public class LoaiSPAdapter extends BaseAdapter {
    List<LoaiSP> array;
    Context context;
    public LoaiSPAdapter(List<LoaiSP> array, Context context) {
        this.array = array;
        this.context = context;
    }
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_sanpham, null);
            viewHolder.textTenSP = convertView.findViewById(R.id.item_tensanpham);
            viewHolder.img = convertView.findViewById(R.id.item_image);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textTenSP.setText(array.get(position).getTensanpham());
        Glide.with(context).load(array.get(position).getHinhanh()).into(viewHolder.img);
        return convertView;
    }
    public class ViewHolder{
        TextView textTenSP;
        ImageView img;
    }
}
