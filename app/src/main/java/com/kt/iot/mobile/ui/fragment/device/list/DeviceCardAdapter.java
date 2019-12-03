package com.kt.iot.mobile.ui.fragment.device.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.Callback.ImageCallback;
import com.kt.gigaiot_sdk.DeviceApiStatus;
import com.kt.gigaiot_sdk.DeviceImage;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.data.DeviceStatus;
import com.kt.gigaiot_sdk.data.DeviceStatusResponse;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.kt.iot.mobile.data.GraphHtml.TAG;

/*
 * Updated by DASOM
 * 디바이스 목록 카드형 UI Adapter
 */
public class DeviceCardAdapter extends RecyclerView.Adapter<DeviceCardAdapter.CustomViewHolder> {

    public interface OnDeviceListSelectedListener {
        void onDeviceSelected(int position, DeviceNew device);
    }

    private OnDeviceListSelectedListener mCallback;
    private Context mContext;
    private ArrayList<DeviceNew> items;
    private RequestManager requestManager;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvIcon;
        TextView  mTvTitle;
        TextView  mTvSubTitle;
        ImageView  mTvStatus;

        @SuppressLint("NewApi")
        public CustomViewHolder(View itemView) {
            super(itemView);
            mCallback = (OnDeviceListSelectedListener) mContext;
            mIvIcon = itemView.findViewById(R.id.DeviceImage);
            mTvTitle = itemView.findViewById(R.id.DeviceName);
            mTvSubTitle = itemView.findViewById(R.id.custom_list_title_sub);
            mTvStatus = itemView.findViewById(R.id.DeviceStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Log.d(TAG, "item " + getAdapterPosition() + " is clicked!");
                    if(getAdapterPosition() < items.size()) {
                        if (mCallback != null) {
                            mCallback.onDeviceSelected(getAdapterPosition(), items.get(getAdapterPosition()));
                        }
                    }
                }
            });
        }
    }

    public DeviceCardAdapter(Context context, ArrayList<DeviceNew> items, RequestManager requestManager) {
        this.mContext = context;
        this.items = items;
        this.requestManager = requestManager;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_device_list_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        Log.d(TAG, "Bind View Holder Created");

        // 데이터 클래스에서 해당 리스트목록의 데이터를 가져온다.
        //Device item = items.get(position);
        DeviceNew item = items.get(position);

        Activity activity = (Activity) mContext;
        if (activity.isFinishing()) {
            return;
        }

        if(item != null) {
            // 디바이스 이미지 맵핑
            final DeviceImage deviceImage = new DeviceImage(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new ImageCallback() {
                        @Override
                        public void onDoing(String base64) {
                            byte[] imageAsBytes = Base64.decode(base64.getBytes(), Base64.DEFAULT);
                            requestManager.load(imageAsBytes)
                                    .asBitmap()
                                    .error(R.drawable.white_solid)
                                    .into(holder.mIvIcon);
                        }

                        @Override
                        public void onFail() {
                            holder.mIvIcon.setImageResource(R.drawable.empty_device_image);
                        }
                    });

            deviceImage.getDeviceImage(item.getTarget().getSequence(), item.getSequence());

            // 디바이스 연결 상태 맵핑
            DeviceApiStatus deviceApiStatus = new DeviceApiStatus(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<DeviceStatusResponse>() {
                        @Override
                        public void onStart() {
                            holder.mTvStatus.setImageResource(R.drawable.status_check);
                        }

                        @Override
                        public void onDoing(DeviceStatusResponse response) {
                            ArrayList<DeviceStatus> deviceStatus = response.getDeviceStatuses();

                            for (int i = 0; i < deviceStatus.size(); i++) {
                                if (deviceStatus.get(i).getStatus().equals(ApiConstants.boolean_true)) {
                                    holder.mTvStatus.setImageResource(R.drawable.status_true);
                                } else {
                                    holder.mTvStatus.setImageResource(R.drawable.status_false);
                                }
                            }
                        }

                        @Override
                        public void onFail() {
                            holder.mTvStatus.setImageResource(R.drawable.status_unknown);
                        }
                    }
            );

            deviceApiStatus.doPostDeviceStatus(item.getConnectionId(), item.getId());

            if (item.getName() != null)  {
                holder.mTvTitle.setText(item.getName());
            }

            if (item.getModel().getName() != null)  {
                holder.mTvSubTitle.setText(item.getModel().getName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}