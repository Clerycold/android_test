package com.test.hybird.control;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by clery on 2017/1/17.
 */

public class RemindToast {
    private static Toast mtoast;

    /**
     * 單一模式 Singleton
     * @param context
     * @param str
     */
    public static void showText(Context context, String str){
        if(mtoast == null){
            synchronized(RemindToast.class) {
                if(mtoast == null){
                    mtoast = Toast.makeText(context,
                            str, Toast.LENGTH_SHORT);
                }
            }
        }else{
            mtoast.setText(str);
        }
        mtoast.show();
    }
}