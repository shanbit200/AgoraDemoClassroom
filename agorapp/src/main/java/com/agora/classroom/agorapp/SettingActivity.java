package com.agora.classroom.agorapp;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.agora.classroom.agorapp.base.BaseActivity;
import com.agora.classroom.agorapp.widget.EyeProtection;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.switch_eye_care)
    protected Switch switch_eye_care;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        switch_eye_care.setChecked(EyeProtection.isNeedShow());
    }

    @OnClick({R.id.iv_back, R.id.layout_policy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.layout_policy:
                /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.POLICY_URL));
                startActivity(intent);*/
                break;
        }
    }

    @OnCheckedChanged(R.id.switch_eye_care)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        EyeProtection.setNeedShow(isChecked);
        if (isChecked) {
            showEyeProtection();
        } else {
            dismissEyeProtection();
        }
    }

}
