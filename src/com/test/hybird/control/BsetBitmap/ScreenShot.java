package com.test.hybird.control.BsetBitmap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.test.hybird.control.RemindUI;
import com.test.hybird.control.ScreenWH;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by clery on 2017/1/13.
 */

public class ScreenShot {

    private File pictureFile;
    private Activity activity;
    private Context context;

    private boolean isWritable;

    public ScreenShot(Activity activity){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        isWritable = isExternalStorageWritable();
    }

    /**
     * 製作資料夾
     * @return
     */
    private File getDataFilePath(){
        pictureFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Kingnet_picture");

        if (!pictureFile.exists()) {
            RemindUI.setToast(context,"建立資料夾Kingnet_picture");

            if (!pictureFile.mkdirs()) {
                RemindUI.setToast(context,"建立資料夾失敗，請確認是否有儲存空間");
                return null;
            }
        }
        String TIMESTAMP= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return new File(pictureFile.getPath() + File.separator +
                "IMG_" + TIMESTAMP + ".jpg");
    }

    private Boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 將圖片儲存至外部空間
     * @param bitmap
     */

    public void SaveScreenBitmap(Bitmap bitmap){
        if(isWritable){
            try {
                FileOutputStream fos = new FileOutputStream(getDataFilePath());
                //第二個參數0~100  100表示不壓縮
                //Bitmap mBitmap = Bitmap.createScaledBitmap(picture, 600, 800, true);
                //mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                byte[] b = new byte[1024 * 8];
                fos.write(b);
                fos.flush();
                fos.close();

            } catch (FileNotFoundException e) {
                Log.d("TAG", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("TAG", "Error accessing file: " + e.getMessage());
            }
        }else{
            RemindUI.setToast(context,"無法製作檔案，請確認使用權限");
        }
    }

    /**
     * 擷取螢幕畫面 製作成bitmap檔回傳
     * @return
     */

    public Bitmap getScreenBitmap(){
        View mView = activity.getWindow().getDecorView();
        mView.setDrawingCacheEnabled(true);
        mView.buildDrawingCache();
        Bitmap mFullBitmap = mView.getDrawingCache();

        //取得系統狀態列高度
        Rect mRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(mRect);
        int mStatusBarHeight = mRect.top;

        //取得手機螢幕長寬尺寸
        int mPhoneWidth = ScreenWH.getScreenWidth();
        int mPhoneHeight = ScreenWH.getScreenHidth();

        //將狀態列的部分移除並建立新的Bitmap
        Bitmap mBitmap = Bitmap.createBitmap(mFullBitmap, 0, mStatusBarHeight, mPhoneWidth, mPhoneHeight - mStatusBarHeight);
        //將Cache的畫面清除
        mView.destroyDrawingCache();

        return mBitmap;
    }

    /**
     * 判斷資料夾是否存在後，進行掃描
     */

    public void GalleryAddPic(){
        //      判断SDK版本是不是4.4或者高于4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScanner mediaScanner = new MediaScanner(context);
            mediaScanner.scanFile(new File(pictureFile.getPath()), null);
        } else {
            if (pictureFile.isDirectory()) {    //// 判断e盘是否是目录
                pictureFile = new File(pictureFile.getPath());
                Uri contentUri = Uri.fromFile(pictureFile);
                Intent allmediaScanIntent = new Intent(Intent.ACTION_MEDIA_MOUNTED, contentUri);
                context.sendBroadcast(allmediaScanIntent);
            } else {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(pictureFile.getPath())));
                context.sendBroadcast(mediaScanIntent);
            }
        }
    }
}

/**
 * 掃描檔案文件
 */
class MediaScanner {

    private MediaScannerConnection mConn = null;
    private SannerClient mClient = null;
    private File mFile = null;
    private String mMimeType = null;

    public MediaScanner(Context context){
        if (mClient == null) {
            mClient = new SannerClient();
        }
        if (mConn == null) {
            mConn = new MediaScannerConnection(context, mClient);
        }
    }
    class SannerClient implements
            MediaScannerConnection.MediaScannerConnectionClient {

        public void onMediaScannerConnected() {

            if (mFile == null) {
                return;
            }
            scan(mFile, mMimeType);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mConn.disconnect();
        }
    }
    private void scan(File file, String type) {
        if (file.isFile()) {
            mConn.scanFile(file.getAbsolutePath(), null);
            return;
        }
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File f : file.listFiles()) {
            scan(f, type);
        }
    }
    public void scanFile(File file, String mimeType) {
        mFile = file;
        mMimeType = mimeType;
        mConn.connect();
    }
}
