package com.kt.iot.mobile.ui.fragment.policy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kt.iot.mobile.android.R;

public class PolicyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_policy, container, false);

        TextView tvTerm = v.findViewById(R.id.tv_term_body);
        TextView tvPolicy = v.findViewById(R.id.tv_policy_body);

        tvTerm.setMovementMethod(new ScrollingMovementMethod());
        tvPolicy.setMovementMethod(new ScrollingMovementMethod());

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
