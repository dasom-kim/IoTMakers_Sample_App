package com.kt.iot.mobile.ui.fragment.device;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.Callback.ImageCallback;
import com.kt.gigaiot_sdk.DeviceApiNew;
import com.kt.gigaiot_sdk.DeviceImage;
import com.kt.gigaiot_sdk.DeviceNewApiNew;
import com.kt.gigaiot_sdk.data.DeviceApiResponse;
import com.kt.gigaiot_sdk.data.DeviceApiResponseNew;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.ModifyDeviceMgr;
import com.kt.iot.mobile.utils.Util;

import java.io.File;
import java.io.IOException;

/*
 * Updated by DASOM
 * 디바이스 설정 화면 Fragment
 */
public class DeviceSettingFragment extends Fragment implements View.OnClickListener {

    private final String TAG = DeviceSettingFragment.class.getSimpleName();
    private final int REQ_PICK_FROM_CAMERA = 0;
    private final int REQ_PICK_FROM_ALBUM = 1;
    private final int REQ_CROP_IMAGE = 2;
    private ImageView mIvMain, mIvImageSetting;
    private EditText mEtDevNm, mEtSpotDevId, mEtDevModelNm, mEtDevModelId;
    private Button mClearDevNm, mClearSpotDevId;
    private RelativeLayout mBtModify;
    private Uri mImageUri;
    private Uri mAlbumUri;
    private String mCurrentPhotoPath;
    private String full_path;
    private DeviceNew mDevice;
    private RequestManager requestManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_device_setting, container, false);

        mIvMain = v.findViewById(R.id.iv_device_setting_main);
        mIvImageSetting = v.findViewById(R.id.iv_device_setting_camera);
        mIvImageSetting.setOnClickListener(this);

        mEtDevNm = v.findViewById(R.id.et_device_setting_devnm);
        mEtSpotDevId = v.findViewById(R.id.et_device_setting_spotdev_id);
        mEtDevModelNm = v.findViewById(R.id.et_device_setting_dev_model);
        mEtDevModelId = v.findViewById(R.id.et_device_setting_model_id);

        mClearDevNm = v.findViewById(R.id.clear_devnm);
        mClearDevNm.setOnClickListener(this);

        mBtModify = v.findViewById(R.id.bt_device_setting_modify);
        mBtModify.setOnClickListener(this);

        mEtDevNm.setText(mDevice.getName());
        mEtSpotDevId.setText(mDevice.getId());
        mEtDevModelNm.setText(mDevice.getModel().getName());
        mEtDevModelId.setText(mDevice.getModel().getId());

        requestManager = Glide.with(getActivity());

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity().isFinishing()) {
            return;
        }

        if(mDevice != null) {
            if(!TextUtils.isEmpty(mDevice.getImageFileId())){
                DeviceImage deviceImage = new DeviceImage(
                        ApplicationPreference.getInstance().getPrefAccessToken(),
                        new ImageCallback() {
                            @Override
                            public void onDoing(String base64) {
                                byte[] imageAsBytes = Base64.decode(base64.getBytes(), Base64.DEFAULT);

                                requestManager
                                        .load(imageAsBytes)
                                        .asBitmap()
                                        .error(R.drawable.white_solid)
                                        .into(mIvMain);
                            }

                            @Override
                            public void onFail() {
                                mIvMain.setImageResource(R.drawable.bg_01);
                            }
                        });

                deviceImage.getDeviceImage(mDevice.getTarget().getSequence(), mDevice.getSequence());
            } else {
                mIvMain.setImageResource(R.drawable.bg_01);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.clear_devnm:
                mEtDevNm.setText("");
                break;

            case R.id.iv_device_setting_camera:
                createChooseDialog();
                break;

            case R.id.bt_device_setting_modify:
                if(checkInput()){
                    new DeviceModifyTask().getDeviceModify();
                }

                break;

        }
    }

    private boolean checkInput(){

        if(TextUtils.isEmpty(mEtDevNm.getText().toString())){
            Toast.makeText(getActivity(),"현장장치 이름을 작성해주세요", Toast.LENGTH_LONG).show();
            return false;
        }

        if(TextUtils.isEmpty(mEtSpotDevId.getText().toString())){
            Toast.makeText(getActivity(),"현장장치 아이디를 작성해주세요", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    public void setDevice(DeviceNew device){
        mDevice = device;
    }

    private void createChooseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("사진 설정하기")
                .setItems(R.array.image_menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d(TAG, "onClick item which = " + which);

                        switch (which) {

                            case 0:
                            {
                                captureCamera();
                            }
                            break;

                            case 1:
                            {
                                getAlbum();
                            }
                            break;

                        }

                    }
                })
                .setNegativeButton(R.string.common_cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void captureCamera() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;

                try {
                    photoFile = Util.createSaveCropFile(getActivity());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoFile != null) {
                    Uri providerUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", photoFile);
                    mImageUri = providerUri;
                    mCurrentPhotoPath = photoFile.getAbsolutePath();

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerUri);

                    startActivityForResult(takePictureIntent, REQ_PICK_FROM_CAMERA);
                }
            }
        } else {
            Toast.makeText(getActivity(), "저장 공간 접근이 불가능한 기기입니다.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void getAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQ_PICK_FROM_ALBUM);
    }

    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
        Toast.makeText(getActivity(), "사진이 앨범에 저장되었습니다.", Toast.LENGTH_LONG).show();

        mAlbumUri = contentUri;
        full_path = mAlbumUri.getPath();
        new UploadImgTask().uploadImg();
    }

    public void cropImage(Uri mImageUri, Uri mAlbumUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(mImageUri, "image/*");

        intent.putExtra("aspectX", 3);
        intent.putExtra("aspectY", 5);
        intent.putExtra("scale", true);
        intent.putExtra("output", mAlbumUri);

        startActivityForResult(intent, REQ_CROP_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != getActivity().RESULT_OK){
            return;
        }

        switch(requestCode){

            case REQ_PICK_FROM_CAMERA:
            {
                galleryAddPic();
            }
            break;

            case REQ_PICK_FROM_ALBUM:
            {
                if (data.getData() != null) {
                    try {
                        File albumFile = null;
                        albumFile = Util.createSaveCropFile(getActivity());
                        mImageUri = data.getData();
                        mAlbumUri = Uri.fromFile(albumFile);
                        cropImage(mImageUri, mAlbumUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;

            case REQ_CROP_IMAGE:
            {
                Log.d(TAG, "onActivityResult REQ_CROP_IMAGE");
                full_path = mAlbumUri.getPath();
                Log.d(TAG, "onActivityResult REQ_CROP_IMAGE full_path = " + full_path);

                new UploadImgTask().uploadImg();
            }
                break;

        }


    }

    private class UploadImgTask {
        String imgPath = full_path;

        protected void uploadImg() {
            DeviceApiNew deviceApi = new DeviceApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<DeviceApiResponse>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDoing(DeviceApiResponse response) {
                            if(response != null && response.getResponseCode().equals(ApiConstants.CODE_OK)){
                                Snackbar.make(getView(), "이미지 업로드가 처리되었습니다.", Snackbar.LENGTH_LONG).show();
                                Bitmap crop = BitmapFactory.decodeFile(imgPath);
                                mIvMain.setImageBitmap(crop);
                                String imgFileName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
                                mDevice.setImageFileId(imgFileName);
                                ModifyDeviceMgr.setModifyDevice(mDevice);
                            } else {
                                Snackbar.make(getView(), "이미지 업로드가 실패되었습니다.\n[" + response.getMessage() +"]", Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFail() {
                            Snackbar.make(getView(), "이미지 업로드가 실패되었습니다.", Snackbar.LENGTH_LONG).show();
                        }
                    });

            deviceApi.uploadDeviceImg(mDevice, full_path);
        }
    }

    private class DeviceModifyTask {
        protected void getDeviceModify() {
            DeviceNewApiNew deviceApi = new DeviceNewApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<DeviceApiResponseNew>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDoing(DeviceApiResponseNew response) {
                            Snackbar.make(getView(), "디바이스 수정이 처리되었습니다.", Snackbar.LENGTH_LONG).show();

                            mDevice.setName(mEtDevNm.getText().toString());
                            mDevice.setId(mEtSpotDevId.getText().toString());

                            ModifyDeviceMgr.setModifyDevice(mDevice);
                        }

                        @Override
                        public void onFail() {
                            Snackbar.make(getView(), "디바이스 수정이 실패되었습니다.", Snackbar.LENGTH_LONG).show();
                        }
                    });

            deviceApi.putNewdeviceModify(mDevice.getSequence(), mEtDevNm.getText().toString(),
                    mDevice.getUsed(), mDevice.getPublished(), mDevice.getStatus(), mDevice.getAuthenticationKey(),
                    mDevice.getConnectionId(), mDevice.getConnectionType(), mDevice.getTarget().getSequence());

        }
    }

}
