package com.kt.iot.mobile.ui.fragment.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.MemberApiNew;
import com.kt.gigaiot_sdk.PushApiNew;
import com.kt.gigaiot_sdk.data.MemberApiResponse;
import com.kt.gigaiot_sdk.data.PushApiResponse;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.Util;

import java.util.ArrayList;

/*
 * Updated by DASOM
 * 사용자 정보 화면 Fragment
 */
public class AccountFragment extends Fragment {
    private TextView mEtUserName, mEtUserPhone, mEtUserEmail;
    private PieChart openDeviceYN;
    private PieChart usedDeviceYN;
    ArrayList<PieEntry> yValuesDeviceYN = new ArrayList<PieEntry>();
    ArrayList<PieEntry> yValuesUsedYN = new ArrayList<PieEntry>();
    int deviceOpenY = 0;
    int deviceUsedY = 0;
    int deviceSize = 0;
    ArrayList<Integer> colors = new ArrayList<>();
    LinearLayout chartArea;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, null);

        mEtUserName = view.findViewById(R.id.et_account_username);
        mEtUserPhone = view.findViewById(R.id.et_account_phone);
        mEtUserEmail = view.findViewById(R.id.et_account_mail);

        colors.add(Color.parseColor("#AAAAAA"));
        colors.add(Color.parseColor("#27284d"));

        drawChartView(view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new GetMemberInfoTask().getMemberInfo();
    }

    public void drawChartView(View view) {
        chartArea = view.findViewById(R.id.chart_area);
        openDeviceYN = view.findViewById(R.id.openDeviceYN);
        usedDeviceYN = view.findViewById(R.id.usedDeviceYN);

        deleteChart(yValuesDeviceYN);
        deleteChart(yValuesUsedYN);
        drawChart(openDeviceYN, "디바이스\n공개 여부", yValuesDeviceYN, Util.getDeviceOpenY(), (Util.getDeviceCount() - Util.getDeviceOpenY()), "공개", "비공개");
        drawChart(usedDeviceYN, "디바이스\n사용 여부", yValuesUsedYN, Util.getDeviceUsedY(), (Util.getDeviceCount()  - Util.getDeviceUsedY()), "사용", "비사용");

        chartArea.setVisibility(View.VISIBLE);
    }

    private class GetMemberInfoTask {
        protected void getMemberInfo() {
            MemberApiNew memberApi = new MemberApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<MemberApiResponse>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDoing(MemberApiResponse response) {
                            if(response != null && response.getResponseCode().equals(ApiConstants.CODE_OK)){
                                mEtUserName.setText(response.getMember().getUserNm());
                                mEtUserEmail.setText(response.getMember().getEmail());
                                mEtUserPhone.setText(response.getMember().getTelNo());
                            }
                        }

                        @Override
                        public void onFail() {

                        }
                    });

            memberApi.getMemberInfo(ApplicationPreference.getInstance().getPrefAccountMbrSeq());
        }
    }

    public static class PushSessionDeleteTask {
        public void pushSessionDelete() {
            PushApiNew pushApi = new PushApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<PushApiResponse>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDoing(PushApiResponse pushApiResponse) {

                        }

                        @Override
                        public void onFail() {

                        }
                    });

            pushApi.gcmSessionDelete(ApplicationPreference.getInstance().getPrefGcmRegId());

        }
    }

    public void drawChart(PieChart pieChart, String title, ArrayList<PieEntry> pieEntries, int x, int y, String labelX, String labelY) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        pieChart.setCenterText(title);
        pieChart.setCenterTextColor(Color.parseColor("#c0392b"));

        pieChart.setDrawHoleEnabled(false);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        pieChart.getLegend().setEnabled(false);

        // entry label styling
        pieChart.setEntryLabelColor(Color.parseColor("#FFFFFF"));
        pieChart.setEntryLabelTextSize(10f);

        pieEntries.add(new PieEntry((float) x, labelX));
        pieEntries.add(new PieEntry((float) y, labelY));

        PieDataSet dataSet = new PieDataSet(pieEntries, null);
        dataSet.setColors(colors);

        PieData data = new PieData((dataSet));
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.parseColor("#FFFFFF"));
        pieChart.setData(data);

        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    public void deleteChart(ArrayList<PieEntry> pieEntries) {
        chartArea.setVisibility(View.INVISIBLE);
        pieEntries.clear();
        deviceOpenY = 0;
        deviceUsedY = 0;
        deviceSize = 0;
    }

}
