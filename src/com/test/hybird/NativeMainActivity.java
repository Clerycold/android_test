package com.test.hybird;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.test.hybird.ActivityQRcodeScanner.BarcodeScanner;
import com.test.hybird.contectLogin.LoginGoogleMain;
import com.test.hybird.contectLogin.LoginLineMain;
import com.test.hybird.contectLogin.LoginMainActivity;
import com.test.hybird.control.RemindToast;
import com.test.hybird.control.RemindUI;

/**
 * Created by clery on 2016/12/23.
 */

public class NativeMainActivity extends Activity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    public void startCordovaActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void startCordovaActivityWithLayout(View view) {
        Intent intent = new Intent(this, TestCordovaWithLayoutActivity.class);
        startActivity(intent);
    }

    public void startFacebook(View view){
        Intent intent = new Intent(this, LoginGoogleMain.class);
        startActivity(intent);
    }

    public void startLine(View view){
        Intent intent = new Intent(this, LoginLineMain.class);
        startActivityForResult(intent,100);
//        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){

            if(data != null){
                Log.d("---scussec",data.getStringExtra("mid"));
                Log.d("---scussec",data.getStringExtra("displayName"));
                Log.d("---scussec",data.getStringExtra("pictureUrl"));
                Log.d("---scussec",data.getStringExtra("statusMessage"));

                RemindToast.showText(getApplicationContext(),data.getStringExtra("mid"));
            }


        }
    }

    public void startCodeScanner(View view){


        int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 無權限，向使用者請求
            ActivityCompat.requestPermissions(NativeMainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }else{
            //已有權限，執行儲存程式
            Intent intent = new Intent(view.getContext(), BarcodeScanner.class);
            startActivity(intent);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }
    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Intent intent = new Intent(getApplicationContext(), BarcodeScanner.class);
                startActivity(intent);

            } else {
                RemindUI.setToast(getApplicationContext(),"無法開啟掃描器功能");
            }
        }
    }
}