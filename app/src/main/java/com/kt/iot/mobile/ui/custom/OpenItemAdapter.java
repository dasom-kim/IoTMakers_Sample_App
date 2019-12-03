package com.kt.iot.mobile.ui.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kt.iot.mobile.android.R;

public class OpenItemAdapter extends BaseAdapter {
    /*0001:에너지, 0002:보안/안전
    0003:미디어, 0004:헬스/의료
    0005:커넥티드카, 0006:교육
    0007:유통/물류, 0008:여행/레져
    0009:스마트홈, 0010:스마트시티
    0011:농업, 0012:엔터테인먼트
    0013:기타*/

    private int[] items;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private String[] itemsId = {"0000", "0001", "0002", "0003", "0004", "0005", "0006", "0007", "0008", "0009", "0010", "0011", "0012", "0013"};

    public OpenItemAdapter(Context context, int[] items) {
        this.mContext = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public String getItem(int position) {
        return itemsId[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemHolder holder;

        if(row == null) {
            row = layoutInflater.inflate(R.layout.open_list_item, parent, false);

            holder = new ItemHolder();
            holder.tvOpenItemName = row.findViewById(R.id.list_open_item_name);
            holder.tvOpenItemName.setTag(0);
            holder.tvOpenItemName.setText(items[position]);
        }

        return row;
    }

    private class ItemHolder{
        TextView tvOpenItemName;
    }
}
