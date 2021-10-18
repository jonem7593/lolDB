package com.hyun.loldb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context con;

    TextView tvNo, tvName, tvPirce, tvGrade;

   ArrayList<LolItem> items;

    public MyAdapter(Context con, ArrayList<LolItem> items) {
        this.con = con;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = View.inflate(con, R.layout.lolitem, null);

        tvNo = itemView.findViewById(R.id.tvNo);
        tvName = itemView.findViewById(R.id.tvName);
        tvPirce = itemView.findViewById(R.id.tvPrice);
        tvGrade = itemView.findViewById(R.id.tvGrade);

        LolItem lol = items.get(position);
        tvNo.setText("" + (position) + 1);
        tvName.setText(lol.getItemname());
        tvPirce.setText(lol.getPrice());
        tvGrade.setText(lol.getGrade());


        return itemView;
    }

}//BaseAdapter
