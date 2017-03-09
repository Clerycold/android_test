package com.test.hybird.plugins;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.test.hybird.control.BsetBitmap.ScreenShot;
import com.test.hybird.control.RemindUI;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by clery on 2017/1/12.
 */

public class GetScreenShot extends CordovaPlugin{

    private static final String ACTION_FLAG = "getscreenshot";

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    CallbackContext callbackContext;

    Context context;

    private final String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (ACTION_FLAG.equals(action) ) {

            context = cordova.getActivity().getApplicationContext();

            this.callbackContext = callbackContext;

            int permission2 = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission2 != PackageManager.PERMISSION_GRANTED) {
                // 無權限，向使用者請求
                cordova.requestPermissions(this, REQUEST_CAMERA_PERMISSION, permissions);

            }else{

                getScreenShot();
            }

            return true;
        }else {

            callbackContext.error("Invalid action");

            return false;
        }
    }
    private void getScreenShot(){
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                ScreenShot screenShot = new ScreenShot(cordova.getActivity());
                Bitmap screenBitmap = screenShot.getScreenBitmap();

                if(screenBitmap != null){
                    screenShot.SaveScreenBitmap(screenBitmap);
                    screenShot.GalleryAddPic();

                    RemindUI.setToast(context,"畫面擷取成功");

                    String str = "Success";

                    PluginResult pluginResults = new PluginResult(PluginResult.Status.OK, str);
                    callbackContext.sendPluginResult(pluginResults);
                    pluginResults.setKeepCallback(true);
                    callbackContext.success();

                }else{
                    RemindUI.setToast(context,"發生不明原因，畫面擷取失敗");
                    callbackContext.error("error");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        super.onRequestPermissionResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getScreenShot();

            } else {
                RemindUI.setToast(context,"無法擷取畫面，請確認權限");
                callbackContext.error("error");
            }
        }
    }
}
