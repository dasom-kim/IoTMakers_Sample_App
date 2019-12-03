package com.kt.iot.mobile.ui.fragment.device.control;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.DeviceNewApiNew;
import com.kt.gigaiot_sdk.TagStrmApiNew;
import com.kt.gigaiot_sdk.data.DeviceApiResponseNew;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.data.TagStrm;
import com.kt.gigaiot_sdk.data.TagStrmApiResponse;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;

import java.util.ArrayList;

public class DeviceCtrlListAdapter extends BaseAdapter{

    private final String TAG = DeviceCtrlListAdapter.class.getSimpleName();

    Context mContext;
    ArrayList<TagStrm> mData;
    DeviceNew mDevice;
    View view;

    Handler mHandler;

    public DeviceCtrlListAdapter(Context context, ArrayList<TagStrm> data, DeviceNew device){
        mContext = context;
        mData = data;
        mDevice = device;
        mHandler = new Handler();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ItemHolder holder;

        if(row == null){

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.itemview_ctrl, parent, false);

            holder = new ItemHolder();
            holder.tvCtrlName = row.findViewById(R.id.tv_ctrl_tagname);
            holder.btSendMsg= row.findViewById(R.id.bt_ctrl_send);
            holder.btSendMsg.setOnClickListener(mListener);
            row.setTag(holder);
        }

        holder = (ItemHolder) row.getTag();

        holder.tvCtrlName.setText(mData.get(position).getTagStrmId());
        holder.btSendMsg.setTag(new CtrlTag(null, position));

        return row;
    }

    private class ItemHolder{
        TextView tvCtrlName;
        Button btSendMsg;
    }

    private class CtrlTag{

        private EditText etCtrlMsg;
        private int position;

        public CtrlTag(EditText et, int position){
            etCtrlMsg = et;
            this.position = position;
        }

        public EditText getEtCtrlMsg() {
            return etCtrlMsg;
        }

        public int getPosition() {
            return position;
        }
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CtrlTag tag = (CtrlTag)v.getTag();
            Log.i(TAG, "onClick position = " + tag.getPosition());
            createSendCtrlMsgDialog(tag.getPosition());
            view = v;
        }
    };

    private void createSendCtrlMsgDialog(final int position){

        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dilogView = inflater.inflate(R.layout.dialog_device_ctrl, null);
        final EditText etCtrlMsg = dilogView.findViewById(R.id.et_device_ctrl);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("제어요청 보내기")
                .setView(dilogView)
                .setPositiveButton("보내기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String ctrlMsg = etCtrlMsg.getText().toString();

                        Log.d(TAG, "Dialog onClick ctrlMsg = " + ctrlMsg + " | tagStrmId=" + mData.get(position).getTagStrmId());
                        //TODO : 제어요청 API 호출

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                TagStrmApiNew tagStrmApi = new TagStrmApiNew(
                                        ApplicationPreference.getInstance().getPrefAccessToken(),
                                        new APICallback<TagStrmApiResponse>() {
                                            @Override
                                            public void onStart() {

                                            }

                                            @Override
                                            public void onDoing(final TagStrmApiResponse response) {
                                                if(response.getResponseCode().equals(ApiConstants.CODE_OK)){
                                                    mHandler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(mContext, "제어 요청 성공", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                } else if(response.getResponseCode().equals(ApiConstants.CODE_NG)){
                                                    mHandler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(mContext, "제어 요청 실패 " + "(" + response.getMessage() + ")", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onFail() {

                                            }
                                        });

                                tagStrmApi.sendCtrlMsg(mDevice.getTarget().getSequence(), mDevice.getSequence(), mDevice.getId(), mDevice.getConnectionId(),
                                        mData.get(position).getTagStrmId(), mData.get(position).getTagStrmValTypeCd(),
                                        ApplicationPreference.getInstance().getPrefAccountId(), ctrlMsg);

//                                504에러
//                                DeviceNewApiNew deviceNewApiNew = new DeviceNewApiNew(
//                                        ApplicationPreference.getInstance().getPrefAccessToken(),
//                                        new APICallback<DeviceApiResponseNew>() {
//                                            @Override
//                                            public void onStart() {
//
//                                            }
//
//                                            @Override
//                                            public void onDoing(final DeviceApiResponseNew response) {
//                                                if (response.getDevices() != null) {
//                                                    if (response.getMessage().equals(ApiConstants.CODE_OK)) {
//                                                        mHandler.post(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                Toast.makeText(mContext, "제어 요청 성공", Toast.LENGTH_LONG).show();
//                                                            }
//                                                        });
//                                                    } else {
//                                                        mHandler.post(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                Toast.makeText(mContext, "제어 요청 실패 " + "(" + response.getMessage() + ")", Toast.LENGTH_LONG).show();
//                                                            }
//                                                        });
//                                                    }
//                                                } else {
//                                                    mHandler.post(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            Toast.makeText(mContext, "제어 요청 실패", Toast.LENGTH_LONG).show();
//                                                        }
//                                                    });
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onFail() {
//                                                mHandler.post(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        Toast.makeText(mContext, R.string.internet_fail, Toast.LENGTH_LONG).show();
//                                                    }
//                                                });
//                                            }
//                                        }
//                                );
//
//                                deviceNewApiNew.putNewdeviceCtrl(mDevice.getSequence(), mData.get(position).getTagStrmId() , ctrlMsg);


                            }
                        }).start();
                    }
                })
                .setNegativeButton(R.string.common_cancel, null);


        AlertDialog dialog = builder.create();
        dialog.show();

    }


}
