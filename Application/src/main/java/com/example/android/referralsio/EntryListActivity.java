package com.example.android.referralsio;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

/**
 * Activity for holding EntryListFragment.
 */
public class EntryListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);

        XGPushConfig.enableDebug(this, BuildConfig.DEBUG);
        XGPushConfig.enableOtherPush(getApplicationContext(), true);
//        XGPushConfig.setHuaweiDebug(true);
        XGPushConfig.setMiPushAppId(getApplicationContext(), "2882303761518026703");
        XGPushConfig.setMiPushAppKey(getApplicationContext(), "5351802651703");
//        XGPushConfig.setMzPushAppId(this, "MZ_APPID");
//        XGPushConfig.setMzPushAppKey(this, "MZ_APPKEY");

        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.d("TPush", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
    }
}
