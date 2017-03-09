package com.test.hybird.ActivityQRcodeScanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.hybird.ActivityQRcodeScanner.presenter.CodeScannerPreCompl;
import com.test.hybird.ActivityQRcodeScanner.view.CodeScannerView;
import com.test.hybird.control.UIpattern.ScannerLine;
import com.test.hybird.control.UIpattern.SquareView;
import com.test.hybird.control.camera.CameraCheck;
import com.test.hybird.control.camera.CameraPreview;
import com.test.hybird.R;
import com.test.hybird.control.RemindUI;
import com.test.hybird.control.ScreenWH;
import com.test.hybird.control.json.NetRunnable;
import com.test.hybird.control.json.NetUtils;


import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.Timer;
import java.util.TimerTask;

import me.dm7.barcodescanner.zbar.BarcodeFormat;

/**
 * Created by clery on 2016/12/27.
 */

public class BarcodeScanner extends Activity implements CodeScannerView,DialogInterface.OnClickListener{

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    private TextView scannerExTxt;
    private TextView titleTxt;
    private ImageScanner scanner;

    FrameLayout preview;
    SquareView squareView;
    ScannerLine scannerLine;

    CodeScannerPreCompl codeScannerPreCompl;
    AlertDialog UserOpenDialog = null;
    AlertDialog UserNoCameraDialog = null;
    AlertDialog UserOpenScussce = null;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.barcode_scanner);

        codeScannerPreCompl = new CodeScannerPreCompl(getApplicationContext(),this);

        initControls();
        ShowSquareView();
    }

    private void initControls() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        autoFocusHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:

                        Bundle bundle = msg.getData();
                        String response = bundle.getString("response");

                        ShowAlertDialog(response,2);

                        break;
                    case 1:
                        scannerLine.setmoveY();
                        break;
                }
            }
        };
        Timer timer01 = new Timer();
        timer01.schedule(task, 0, 5);

        if(CameraCheck.checkCameraHardware(getApplicationContext())){
            mCamera = getCameraInstance();
            mPreview = new CameraPreview(BarcodeScanner.this, mCamera, previewCb,
                    autoFocusCB);

            preview = (FrameLayout) findViewById(R.id.cameraPreview);
            preview.addView(mPreview);

            setScanner();

        }else{
            ShowAlertDialog(null,1);
        }

        titleTxt = (TextView)findViewById(R.id.titleTxt);
        scannerExTxt=(TextView)findViewById(R.id.qrscannerex);

        RelativeLayout.LayoutParams titleTxt_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10);
        titleTxt_lay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        titleTxt.setLayoutParams(titleTxt_lay);

        RelativeLayout.LayoutParams txt_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txt_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        txt_lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        txt_lay.setMargins(0,0,0,ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10);
        scannerExTxt.setLayoutParams(txt_lay);

        titleTxt.setText("行動條碼掃描器");
        scannerExTxt.setText("將行動條碼對準畫面即可讀取。");
    }

    private void setScanner(){
        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        scanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
        for (BarcodeFormat format : BarcodeFormat.ALL_FORMATS) {
            scanner.setConfig(format.getId(), Config.ENABLE, 1);
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            RemindUI.setToast(getApplicationContext(),"開啟相機發生錯誤");
        }
        return c;
    }

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {

                codeScannerPreCompl.setPreviewingState(false);

                SymbolSet syms = scanner.getResults();

                for (Symbol sym : syms) {

                    String scanResult = sym.getData().trim();

                    ShowAlertDialog(scanResult,0);

                    codeScannerPreCompl.setCodeScannerState(true);

                    break;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if(codeScannerPreCompl.CheckPreviewingState()){
                mCamera.autoFocus(autoFocusCB);

            }
        }
    };

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            autoFocusHandler.sendMessage(message);
        }
    };

    void CheckbarcodeScanned(){
        if (codeScannerPreCompl.CheckCodeScannerState()) {

            mCamera.setPreviewCallback(previewCb);
            mCamera.autoFocus(autoFocusCB);
            mCamera.startPreview();

            codeScannerPreCompl.setCodeScannerState(false);
            codeScannerPreCompl.setPreviewingState(true);
        }
    }

    /**
     * 移除攝影功能 釋放攝影鏡頭
     */
    private void releaseCamera() {
        if (mCamera != null) {

            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;

            codeScannerPreCompl.setPreviewingState(false);
            codeScannerPreCompl.setEnmergenceState();

            preview.removeAllViews();
            squareView = null;

            autoFocusHandler.removeCallbacks(doAutoFocus);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(codeScannerPreCompl.CheckEnmergenceState() && mCamera == null){
            mCamera = getCameraInstance();
            mPreview = new CameraPreview(BarcodeScanner.this, mCamera, previewCb,
                    autoFocusCB);
            preview.addView(mPreview);
            ShowSquareView();

            codeScannerPreCompl.setEnmergenceState();
            codeScannerPreCompl.setPreviewingState(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        releaseCamera();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            releaseCamera();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void ShowAlertDialog(String message,int type) {
        switch (type){
            case 0 :
                mCamera.setPreviewCallback(null);
                UserOpenDialog = new android.app.AlertDialog.Builder(BarcodeScanner.this)
                        .setTitle("是否開通住戶")
                        .setMessage(message)
                        .setPositiveButton("確定", this)
                        .setNegativeButton("取消", this)
                        .show();
                break;
            case 1 :
                UserNoCameraDialog = new android.app.AlertDialog.Builder(BarcodeScanner.this)
                        .setTitle("抱歉")
                        .setMessage("此手機不支援掃描功能")
                        .setPositiveButton("確定", this)
                        .show();
                break;
            case 2:
                UserOpenScussce = new android.app.AlertDialog.Builder(BarcodeScanner.this)
                        .setTitle("開通訊息")
                        .setMessage(message)
                        .setPositiveButton("確定", this)
                        .show();
                break;
        }

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {

        if (dialogInterface == UserOpenDialog) {

            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    CheckbarcodeScanned();

                    final String msg = "{\"comId\":\"15121601\",\"tabletId\":\"1512160091\",\"validCode\":\"66T6VXPH96PRR413\",\"openCode\":\"66T6VXPH96PRR413\"}";

                    if(NetUtils.isConnected(getApplicationContext())){
                        new Thread(new NetRunnable(autoFocusHandler,"validMobileCode",msg)).start();
                    }else{
                        RemindUI.setToast(getApplicationContext(),"請檢查網路，再重新開通。");
                    }

                    break;
                case DialogInterface.BUTTON_NEGATIVE:

                    CheckbarcodeScanned();
                    RemindUI.setToast(getApplicationContext(), "不開通此用戶");

                    break;
            }
        } else if (dialogInterface == UserNoCameraDialog) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    finish();

                    break;
            }
        } else if (dialogInterface == UserOpenScussce) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    finish();

                    break;
            }
        }
    }

    /**
     * 建立方框
     */
    @Override
    public void ShowSquareView() {
        if(squareView==null){
            squareView = new SquareView(getApplicationContext());
            preview.addView(squareView);

            scannerLine = new ScannerLine(getApplicationContext());
            preview.addView(scannerLine);
        }
    }
}