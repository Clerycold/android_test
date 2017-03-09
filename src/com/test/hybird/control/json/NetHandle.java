package com.test.hybird.control.json;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by clery on 2016/12/29.
 */

public class NetHandle extends Handler{

    private final WeakReference<Activity> mActivity;

    String response;

    public NetHandle(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        /**
         * 避免內存外洩
         */
        if (mActivity.get() != null) {

        }

        switch (msg.what) {
            case 0:

                Bundle bundle = msg.getData();
                String response = bundle.getString("response");

                this.response = response;

                Log.d("---",this.response);

                break;
        }
    }

    public String getResponse() {
        return response;
    }
}
