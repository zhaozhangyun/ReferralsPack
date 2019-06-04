package com.example.android.referralsio;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * Activity for holding EntryListFragment.
 */
public class EntryListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);

        /**
         * com.samsung.android.sm_cn
         *      --> com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity
         *          --> com.samsung.android.sm.ui.battery.BatteryActivity
         *              --> com.samsung.android.sm.ui.battery.AppSleepListActivity
         */

        String clazz = "com.samsung.android.sm.ui.battery.BatteryActivity";
        if (!startActivitySafed("com.samsung.android.sm", clazz)) {
            if (!startActivitySafed("com.samsung.android.sm_cn", clazz)) {
                if (!startActivitySafed("com.samsung.android.lool", clazz)) {
                    Toast.makeText(this, "Smart manager not installed on this device",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean startActivitySafed(String pkg, String cls) {
        boolean result = false;

        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pkg, cls));
        try {
            startActivity(intent);
            result = true;
        } catch (ActivityNotFoundException ex) {
        }

        return result;
    }
}
