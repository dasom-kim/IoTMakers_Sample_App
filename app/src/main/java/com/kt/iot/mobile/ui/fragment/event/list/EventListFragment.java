package com.kt.iot.mobile.ui.fragment.event.list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.EventApiNew;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.data.Event;
import com.kt.gigaiot_sdk.data.EventApiResponse;
import com.kt.gigaiot_sdk.data.EventLog;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventListFragment extends ListFragment {
    private final String TAG = EventListFragment.class.getSimpleName();
    public static final String ACTION_RECIVER_EVENT = "com.kt.iot.mobile.action.EVENT";
    public static final String CATEGORY_RECIVER_EVENT = "com.kt.iot.mobile.category.EVENT";
    private DeviceNew mDevice;
    private EventLogListAdapter mAdapter;
    private ArrayList<Event> mArrayEvents;
    private ArrayList<EventLog> mArrayEventLogs;
    private EventPushReceiver mEventPushReceiver;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mArrayEvents = new ArrayList<>();

        Log.d(TAG, "event Activity created");
        setEmptyText("이벤트가 없습니다");
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mEventPushReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(ACTION_RECIVER_EVENT);
        filter.addCategory(CATEGORY_RECIVER_EVENT);

        mEventPushReceiver = new EventPushReceiver();
        getActivity().registerReceiver(mEventPushReceiver, filter);
    }

    public void setDevice(DeviceNew device){
        mDevice = device;
    }

    public void refresh(){
        new GetEventListTask().getEventList();
    }

    private class GetEventListTask {
        protected void getEventList() {
            EventApiNew eventApi = new EventApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<EventApiResponse>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDoing(EventApiResponse response) {
                            mArrayEvents = response.getEvents();

                            if(mArrayEvents != null && mArrayEvents.size() > 0){
                                Log.d(TAG, "onActivityCreated mArrayEvents.size = " + mArrayEvents.size());

                                new GetEventLogListTask().getEventLogList();
                            } else {
                                setListAdapter(null);
                            }
                        }

                        @Override
                        public void onFail() {

                        }
                    });

            eventApi.getEventList(ApplicationPreference.getInstance().getPrefAccountId(), mDevice.getTarget().getSequence());
        }
    }

    private class GetEventLogListTask {
        protected void getEventLogList() {
            EventApiNew eventApi = new EventApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<EventApiResponse>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDoing(EventApiResponse response) {
                            if(response.getEventLogs() != null && response.getEventLogs().size() > 0){
                                for(EventLog eventLog : response.getEventLogs()){
                                    mArrayEventLogs.add(eventLog);
                                }
                            }

                            Log.i(TAG, "event log list size: " + String.valueOf(mArrayEventLogs.size()));

                            if(mArrayEventLogs.size() > 1){
                                Collections.sort(mArrayEventLogs, eventLogComparator);
                                mAdapter = new EventLogListAdapter(getActivity(), android.R.layout.simple_list_item_1, mArrayEventLogs);
                                setListAdapter(mAdapter);
                            } else {
                                setListAdapter(null);
                            }
                        }

                        @Override
                        public void onFail() {

                        }
                    });

            mArrayEventLogs = new ArrayList<>();

            long lastTime = Util.getTodayStartTimestamp();
            Log.d(TAG, "GetEventLogListTask lastTime = " + lastTime + " | date = " + Util.timestampToFormattedStr(lastTime));

            for(Event event : mArrayEvents){
                Log.d(TAG, "GetEventLogListTask getLog : event name = " + event.getStatEvetNm());
                //eventApi.getEventLogList(mDevice.getSpotDevSeq(), mDevice.getSvcTgtSeq(), event.getEventId(), lastTime);
                eventApi.getEventLogList(mDevice.getSequence(), mDevice.getTarget().getSequence(), event.getEventId(), lastTime);
            }
        }
    }

    private Comparator<EventLog> eventLogComparator = new Comparator<EventLog>() {

        @Override
        public int compare(EventLog lhs, EventLog rhs) {
            return Util.FormatedTimeToTimestamp(lhs.getOutbDtm()) > Util.FormatedTimeToTimestamp(rhs.getOutbDtm())
                    ? -1 : Util.FormatedTimeToTimestamp(lhs.getOutbDtm()) < Util.FormatedTimeToTimestamp(rhs.getOutbDtm())
                    ? 1:0;        //최신 로그가 상단에 오도록 정렬한다.
        }
    };


    private class EventPushReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i(TAG, "EventPushReceiver onReceive!");
            String strEventLog = intent.getStringExtra("msg");

            EventLog eventLog = new Gson().fromJson(strEventLog, EventLog.class);

            if(mArrayEventLogs != null && mAdapter != null) {
                mArrayEventLogs.add(0, eventLog);
                mAdapter.notifyDataSetChanged();
            }

        }
    }

}
