package com.kt.iot.mobile.ui.activity;

import android.graphics.Color;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kt.gigaiot_sdk.data.DeviceOpen;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.custom.OpenItemAdapter;
import com.kt.iot.mobile.ui.fragment.device.list.OpenDeviceCardAdapter;
import com.kt.iot.mobile.ui.fragment.device.list.OpenDeviceListFragment;
import com.kt.iot.mobile.utils.Util;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

/*
 * Updated by DASOM
 * 공공 디바이스 검색 및 조회 화면
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, OpenDeviceCardAdapter.OnOpenDeviceListSelectedListener {
    private ImageView mCloseBtn;
    private FloatingActionButton mFloatingActionBtn;
    private GridView mGridView;
    private OpenItemAdapter itemAdapter;
    private BottomSheetBehavior bottomSheetBehavior;
    private RelativeLayout layoutBottomSheet;
    private LinearLayout mainBottomSheet;


    /*0001:에너지, 0002:보안/안전
    0003:미디어, 0004:헬스/의료
    0005:커넥티드카, 0006:교육
    0007:유통/물류, 0008:여행/레져
    0009:스마트홈, 0010:스마트시티
    0011:농업, 0012:엔터테인먼트
    0013:기타*/

    private int[] items = {R.string.search_0000, R.string.search_0001, R.string.search_0002, R.string.search_0003, R.string.search_0004,
            R.string.search_0005, R.string.search_0006, R.string.search_0007, R.string.search_0008, R.string.search_0009, R.string.search_0010,
            R.string.search_0011, R.string.search_0012, R.string.search_0013};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mCloseBtn = findViewById(R.id.close_search);
        mCloseBtn.setOnClickListener(this);

        mFloatingActionBtn = findViewById(R.id.search_open_device);
        mFloatingActionBtn.setOnClickListener(this);

        mGridView = findViewById(R.id.list_open_item);
        itemAdapter = new OpenItemAdapter(this, items);
        mGridView.setAdapter(itemAdapter);
        Util.setStatusBarColor(this, Color.parseColor("#27284d"));

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.list_open_item_name);

                String color;
                Object object;
                if (textView.getTag().equals(0)) {
                    color = "#e74c3c";
                    object = 1;
                } else {
                    color = "#ecf0f1";
                    object = 0;
                }

                if (position == 0) {
                    for (int i = 0 ; i < items.length; i++) {
                        View gridObject = mGridView.getChildAt(i);
                        textView = gridObject.findViewById(R.id.list_open_item_name);
                        textView.setBackgroundColor(Color.parseColor(color));
                        textView.setTag(object);
                    }
                } else {
                    textView.setBackgroundColor(Color.parseColor(color));
                    textView.setTag(object);
                }
            }
        });

        mainBottomSheet = findViewById(R.id.main_bottom_sheet);
        mainBottomSheet.setOnClickListener(this);
        layoutBottomSheet = findViewById(R.id.search_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
    }


    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.close_search: {
                finish();
            }

            break;

            case R.id.search_open_device: {
                searchItem();
            }

            break;

            case R.id.main_bottom_sheet: {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED );
                }
            }

            break;
        }
    }

    public void searchItem() {
        TextView tvItem;
        boolean flag = false;
        ArrayList<String> ctgryCd = new ArrayList<>();
        int k = 0;

        // "전체"는 사용자를 위한 선택 조건. API 들어갈 땐 넣어주지 않음
        for (int i = 1; i < items.length; i++) {
            View gridObject = mGridView.getChildAt(i);
            tvItem = gridObject.findViewById(R.id.list_open_item_name);

            // selected items
            if (tvItem.getTag().equals(1)) {
                flag = true;
                ctgryCd.add(k, itemAdapter.getItem(i));
            }
        }

        if (flag == false) {
            Snackbar.make(mGridView, "분류를 한 가지 이상 선택해주세요!", Snackbar.LENGTH_LONG).show();
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            OpenDeviceListFragment fragment = new OpenDeviceListFragment();
            fragment.setCtgryCd(ctgryCd);
            getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment_container, fragment).commit();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onOpenDeviceSelected(int position, DeviceOpen deviceopen) {

    }
}
