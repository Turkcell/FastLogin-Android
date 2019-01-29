package com.turkcell.dssgate.testapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by omercanyuksel on 08/01/2018.
 */

public class WidgetReceiver extends BroadcastReceiver {

    private MainActivity activity;

    public WidgetReceiver(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        activity.showWidgetResult(intent);


    }
}
