package com.kt.iot.mobile.ui.fragment.device;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.DeviceApiNew;
import com.kt.gigaiot_sdk.ProtocolApiNew;
import com.kt.gigaiot_sdk.data.BindType;
import com.kt.gigaiot_sdk.data.DeviceApiResponse;
import com.kt.gigaiot_sdk.data.Protocol;
import com.kt.gigaiot_sdk.data.ProtocolApiResponse;
import com.kt.gigaiot_sdk.data.SvcTgt;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;
import java.util.ArrayList;

/*
 * Updated by DASOM
 * 미사용 (디바이스 등록)
 */
public class DeviceRegFragment extends Fragment implements View.OnClickListener{
    private final String TAG = DeviceRegFragment.class.getSimpleName();
    private EditText mEtSpotDevId, mEtDevNm, mEtSpotDevPw, mEtDevModelNm, mEttermlMakrN;
    private SvcTgt mSvcTgt;
    private Spinner mSpProtId, mSpBindTypeCd;
    Button mBtRegist;
    private ArrayList<Protocol> mArrayProtocols;
    private ArrayList<BindType> mArrayBindTypes;
    private String mRootGwCncId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_device_registration, container, false);

        mEtSpotDevId = v.findViewById(R.id.et_device_reg_spotdevid);
        mEtDevNm = v.findViewById(R.id.et_device_reg_devnm);
        mEtSpotDevPw = v.findViewById(R.id.et_device_reg_spotdevpw);
        mEtDevModelNm = v.findViewById(R.id.et_device_reg_modelnm);
        mEttermlMakrN = v.findViewById(R.id.et_device_reg_makrnm);

        mSpProtId = v.findViewById(R.id.sp_device_reg_protid);
        mSpBindTypeCd = v.findViewById(R.id.sp_device_reg_bindcd);

        mBtRegist = v.findViewById(R.id.bt_device_reg_register);
        mBtRegist.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new GetProtocolListTask().getProtocol();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //등록 버튼
            case R.id.bt_device_reg_register:
                //1. 각 입력 필드 확인
                //2. 상위 게이트웨이 연결 ID 조회
                //3. 등록 API실행

                if(checkInput()) {
                    new GetRootGwCncIdTask().getRootGwCncId();
                } else {
                    return;
                }

                break;
        }

    }

    private boolean checkInput(){

        if(TextUtils.isEmpty(mEtSpotDevId.getText().toString())){
            Toast.makeText(getActivity(), "현장장치 아이디를 입력해 주세요.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(TextUtils.isEmpty(mEtDevNm.getText().toString())){
            Toast.makeText(getActivity(), "현장장치 이름 입력해 주세요.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(TextUtils.isEmpty(mEtSpotDevPw.getText().toString())){
            Toast.makeText(getActivity(), "현장장치 비밀번호를 입력해 주세요.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(TextUtils.isEmpty(mEtDevModelNm.getText().toString())){
            Toast.makeText(getActivity(), "장치 모델명을를 입력해 주세요.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(TextUtils.isEmpty(mEttermlMakrN.getText().toString())){
            Toast.makeText(getActivity(), "제조사명을 입력해 주세요.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(mSpProtId.getSelectedItemPosition()==0){
            Toast.makeText(getActivity(), "프로토콜 아이디를 선택해 주세요.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(mSpBindTypeCd.getSelectedItemPosition()==0){
            Toast.makeText(getActivity(), "바인드 유형 코드를 선택해 주세요.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    public void setSvcTgt(SvcTgt svcTgt){
        mSvcTgt = svcTgt;
    }

    private class GetProtocolListTask {
        protected void getProtocol () {
            ProtocolApiNew protocolApi = new ProtocolApiNew (
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<ProtocolApiResponse>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDoing(ProtocolApiResponse response) {
                            if (response != null && response.getResponseCode().equals(ApiConstants.CODE_OK)) {

                                mArrayProtocols = response.getProtocols();

                                ArrayList<String> array = new ArrayList<String>();
                                String[] arrayProtocolNames = {};

                                array.add("no selection");

                                for (Protocol temp : mArrayProtocols) {
                                    array.add(temp.getProtNm());
                                }

                                arrayProtocolNames = array.toArray(new String[array.size()]);


                                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, arrayProtocolNames);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                mSpProtId.setAdapter(adapter);
                                mSpProtId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (position > 0) {
                                            new GetBindTypeListTask().getBindType(mArrayProtocols.get(position - 1).getProtId());

                                        } else if (position == 0) {
                                            String[] arrayInit = {"no selection"};

                                            ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, arrayInit);
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                            mSpBindTypeCd.setAdapter(adapter);
                                            mArrayBindTypes = null;

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFail() {

                        }
                    }
            );

            protocolApi.getProtocolList();
        }
    }

    private class GetBindTypeListTask {
        protected void getBindType(String portId) {
            ProtocolApiNew protocolApi = new ProtocolApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<ProtocolApiResponse>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDoing(ProtocolApiResponse response) {
                            if(response != null && response.getResponseCode().equals(ApiConstants.CODE_OK)){

                                mArrayBindTypes = response.getBindTypes();

                                ArrayList<String> array = new ArrayList<String>();
                                String[] arrayBintypeNames = {};

                                array.add("no selection");

                                for(BindType temp : mArrayBindTypes){

                                    array.add(temp.getBindTypeNm());
                                }

                                arrayBintypeNames = array.toArray(new String[array.size()]);

                                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, arrayBintypeNames);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                mSpBindTypeCd.setAdapter(adapter);

                            }
                        }

                        @Override
                        public void onFail() {

                        }
                    });

            protocolApi.getBindTypeList(portId);
        }
    }

    private class GetRootGwCncIdTask {
        protected void getRootGwCncId() {
            ProtocolApiNew protocolApi = new ProtocolApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<ProtocolApiResponse>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDoing(ProtocolApiResponse response) {
                            if(response.getResponseCode().equals(ApiConstants.CODE_OK)) {
                                mRootGwCncId = response.getRootGws().get(0).getRootGwCnctId();
                                new PostDeviceRegTask().postDeviceReg();

                            }
                        }

                        @Override
                        public void onFail() {

                        }
                    });

            protocolApi.getRootGwCncId(mSvcTgt.getSvcTgtSeq(),
                    mArrayProtocols.get(mSpProtId.getSelectedItemPosition()-1).getProtId(),
                    mArrayBindTypes.get(mSpBindTypeCd.getSelectedItemPosition()-1).getBindTypeCd());
        }
    }

    private class PostDeviceRegTask {
        ProgressDialog progressDialog;

        protected void postDeviceReg() {
            DeviceApiNew deviceApi = new DeviceApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<DeviceApiResponse>() {
                        @Override
                        public void onStart() {
                            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.common_wait), true, false);
                        }

                        @Override
                        public void onDoing(DeviceApiResponse response) {
                            if (response != null && response.getResponseCode().equals(ApiConstants.CODE_OK)){
                                Toast.makeText(getActivity(), "디바이스 등록이 성공하였습니다.", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "PostDeviceRegTask result :: spotDevSeq = " + response.getDevices().get(0).getSpotDevSeq() + " | devModelseq = "
                                        + response.getDevices().get(0).getDevModelSeq() + " | status = " + response.getDevices().get(0).getStatus());
                                getActivity().setResult(getActivity().RESULT_OK);
                                getActivity().finish();

                            } else {
                                Toast.makeText(getActivity(), "디바이스 등록이 실패하였습니다.\n[" + response.getMessage() +"]", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(getActivity(), "디바이스 등록이 실패하였습니다.", Toast.LENGTH_LONG).show();
                        }
                    });

            deviceApi.deviceRegistration(mSvcTgt.getSvcTgtSeq(), mEtDevNm.getText().toString(),
                                                                        mEtSpotDevId.getText().toString(), mEtSpotDevPw.getText().toString(),
                                                                        mEtDevModelNm.getText().toString(), mEttermlMakrN.getText().toString(),
                                                                        mArrayProtocols.get(mSpProtId.getSelectedItemPosition()-1).getProtId(),
                                                                        mArrayBindTypes.get(mSpBindTypeCd.getSelectedItemPosition()-1).getBindTypeCd(),
                                                                        mSvcTgt.getMbrSeq(),
                                                                        ApplicationPreference.getInstance().getPrefAccountId(),
                                                                        mRootGwCncId, "A");
        }
    }


}
