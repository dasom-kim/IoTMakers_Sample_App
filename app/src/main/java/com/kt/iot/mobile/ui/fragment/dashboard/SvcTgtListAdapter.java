package com.kt.iot.mobile.ui.fragment.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kt.gigaiot_sdk.data.SvcTgt;

import java.util.ArrayList;

public class SvcTgtListAdapter extends ArrayAdapter<SvcTgt>{
    public interface OnSvcTgtListSelectedListener {
        void onSvcTgtSelected(int position, ArrayList<SvcTgt> svcTgt);
    }

    Context mContext;
    ArrayList<SvcTgt> mData;
    TextView textView;

    public SvcTgtListAdapter(Context context, int textViewResourceId, ArrayList<SvcTgt> items)
    {
        super(context, textViewResourceId, items);
        this.mContext = context;
        this.mData = items;


    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(
                    android.R.layout.simple_spinner_dropdown_item, parent, false);

        }

        textView = convertView.findViewById(android.R.id.text1);
        initText(textView, position);

        return convertView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(
                    android.R.layout.simple_spinner_item, parent, false);
        }

        textView = convertView.findViewById(android.R.id.text1);
        initText(textView, position);

        return convertView;
    }

    public void initText(TextView textView, int position) {
        textView.setText(mData.get(position).getSvcTgtNm());
        textView.setTextColor(Color.parseColor("#FFFFFF"));
    }

}