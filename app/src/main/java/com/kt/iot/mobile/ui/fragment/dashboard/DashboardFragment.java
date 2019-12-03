package com.kt.iot.mobile.ui.fragment.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ModifyDeviceMgr;

public class DashboardFragment extends Fragment implements View.OnClickListener {
    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onClick(View v) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, null);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected!!");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        ModifyDeviceMgr.setModifyDevice(null);
    }

}
