package com.kt.iot.mobile.ui.fragment.device.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kt.gigaiot_sdk.Callback.ImageCallback;
import com.kt.gigaiot_sdk.DeviceImage;
import com.kt.gigaiot_sdk.data.DeviceOpen;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.Util;

import java.util.ArrayList;

import static com.kt.iot.mobile.data.GraphHtml.TAG;

/*
 * Updated by DASOM
 * 공공 디바이스 조회 화면 카드 UI Adapter
 */
public class OpenDeviceCardAdapter extends RecyclerView.Adapter<OpenDeviceCardAdapter.CustomViewHolder> {
    public interface OnOpenDeviceListSelectedListener {
        void onOpenDeviceSelected(int position, DeviceOpen deviceopen);
    }

    private OnOpenDeviceListSelectedListener mCallback;
    private Context mContext;
    private ArrayList<DeviceOpen> items;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView mTvUseYn;
        TextView mTvAmdDt;
        TextView  mTvTitle;
        TextView  mTvSubTitle;
        ImageView mIvImage;
        TextView mTvLocation;

        @SuppressLint("NewApi")
        public CustomViewHolder(View itemView) {
            super(itemView);
            mCallback = (OnOpenDeviceListSelectedListener) mContext;
            mTvUseYn = itemView.findViewById(R.id.open_device_useYN);
            mTvAmdDt = itemView.findViewById(R.id.open_device_amdDt);
            mTvTitle = itemView.findViewById(R.id.open_device_name);
            mTvSubTitle = itemView.findViewById(R.id.open_device_modelname);
            mIvImage = itemView.findViewById(R.id.open_device_image);
            mTvLocation = itemView.findViewById(R.id.open_device_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Log.d(TAG, "item " + getAdapterPosition() + " is clicked!");
                    if(getAdapterPosition() < items.size()) {
                        if (mCallback != null) {
                            mCallback.onOpenDeviceSelected(getAdapterPosition(), items.get(getAdapterPosition()));
                        }
                    }
                }
            });
        }
    }

    public OpenDeviceCardAdapter(Context context, ArrayList<DeviceOpen> items) {
        this.mContext = context;
        this.items = items;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_open_device_list_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        Log.d(TAG, "Bind View Holder Created");

        // 데이터 클래스에서 해당 리스트목록의 데이터를 가져온다.
        DeviceOpen item = items.get(position);

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
                            Glide.with(mContext)
                                    .load(imageAsBytes)
                                    .asBitmap()
                                    .error(R.drawable.white_solid)
                                    .into(holder.mIvImage);
                        }

                        @Override
                        public void onFail() {
                            holder.mIvImage.setImageResource(R.drawable.empty_device_image);
                        }
                    });

            deviceImage.getDeviceImage(item.getSvcTgtSeq(), item.getSpotDevSeq());

            if (item.getUseYn() != null) {
                if (item.getUseYn().equals("Y")) {
                    holder.mTvUseYn.setText(R.string.useY);
                } else {
                    holder.mTvUseYn.setText(R.string.useN);
                }
            }

            if (item.getAmdDt() != null) {
                holder.mTvAmdDt.setText(item.getAmdDt());
            }

            if (item.getDevNm() != null)  {
                holder.mTvTitle.setText(item.getDevNm());
            }

            if (item.getDevModelNm() != null)  {
                holder.mTvSubTitle.setText(item.getDevModelNm());
            }

            if (item.getLatitVal().length() > 0 && item.getLngitVal().length() > 0) {
                String address = Util.getAddress(mContext, Double.parseDouble(item.getLatitVal()), Double.parseDouble(item.getLngitVal()));
                holder.mTvLocation.setText(address);
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}