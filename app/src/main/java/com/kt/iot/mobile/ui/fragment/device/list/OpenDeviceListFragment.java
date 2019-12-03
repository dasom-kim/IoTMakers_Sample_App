package com.kt.iot.mobile.ui.fragment.device.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.PublicDeviceApi;
import com.kt.gigaiot_sdk.data.DeviceOpen;
import com.kt.gigaiot_sdk.data.DeviceOpenResponse;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class OpenDeviceListFragment extends Fragment implements View.OnClickListener {
    private ArrayList<DeviceOpen> mArrayDeviceOpen;
    private boolean mScrollOccured = false;
    private int mLastItemCount = 0;
    private RecyclerView mCardView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLinearLayoutManager;
    private TextView resultTextView;
    private View view;
    private TextView resultSize;
    private int mListMax = 0;
    private ArrayList<String> mCtgryCd;
    private int mPageNum = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mArrayDeviceOpen = new ArrayList<DeviceOpen>();

        view = inflater.inflate(R.layout.fragment_open_device_list, container, false);

        mArrayDeviceOpen = new ArrayList<>();
        mAdapter = new OpenDeviceCardAdapter(getActivity(), mArrayDeviceOpen);

        mCardView = view.findViewById(R.id.OpenDeviceList);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mCardView.setLayoutManager(mLinearLayoutManager);


        resultTextView = view.findViewById(R.id.search_result);
        resultSize = getActivity().findViewById(R.id.search_size);

        mCardView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == itemTotalCount) {
                    if(mArrayDeviceOpen.size() < mListMax) {
                        mScrollOccured = true;
                        mLastItemCount = mArrayDeviceOpen.size();
                        new GetOpenDevListTask().getOpenDeviceList(mPageNum++);
                    }
                }
            }
        });

        refresh();

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void setCtgryCd(ArrayList<String> ctgryCd) {
        mCtgryCd = ctgryCd;
    }

    public void refresh() {
        mCardView.setVisibility(View.GONE);

        if(mCtgryCd.size() > 0){
            mArrayDeviceOpen.clear();
            mAdapter.notifyDataSetChanged();
        }

        new GetOpenDevListTask().getOpenDeviceList(mPageNum++);
    }

    private class GetOpenDevListTask {
        protected void getOpenDeviceList(int pageNum) {
            Log.d(TAG, "get Open Device List!");
            PublicDeviceApi publicDeviceApi = new PublicDeviceApi(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<DeviceOpenResponse>() {
                        @Override
                        public void onStart() {
                            resultTextView.setText(R.string.loading_message);
                        }

                        @Override
                        public void onDoing(DeviceOpenResponse response) {
                            int result = response.getTotal();
                            mListMax = result;

                            if (response.getDeviceOpenList() != null && result > 0) {
                                resultTextView.setVisibility(View.GONE);

                                for(DeviceOpen deviceopen : response.getDeviceOpenList()){
                                    mArrayDeviceOpen.add(deviceopen);
                                }

                                mCardView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();

                                if (mScrollOccured) {
                                    mCardView.scrollToPosition(mLastItemCount - 1);
                                    mScrollOccured = false;
                                }

                                mCardView.setVisibility(View.VISIBLE);
                            } else {
                                resultTextView.setText(R.string.search_empty);
                            }

                            resultSize.setText(Integer.toString(result));
                        }

                        @Override
                        public void onFail() {
                            resultSize.setText("0");
                            Snackbar.make(mCardView, R.string.internet_fail, Snackbar.LENGTH_INDEFINITE)
                                    .setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            refresh();
                                        }
                                    }).show();
                        }
                    });

            publicDeviceApi.doGetPublicDeviceList(pageNum, mCtgryCd);
        }
    }


}