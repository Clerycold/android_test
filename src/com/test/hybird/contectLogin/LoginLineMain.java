package com.test.hybird.contectLogin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.linecorp.linesdk.LineApiResponse;
import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;
import com.test.hybird.R;

import jp.line.android.sdk.LineSdkContext;
import jp.line.android.sdk.LineSdkContextManager;
import jp.line.android.sdk.api.ApiClient;
import jp.line.android.sdk.api.ApiRequestFuture;
import jp.line.android.sdk.api.ApiRequestFutureListener;
import jp.line.android.sdk.exception.LineSdkLoginError;
import jp.line.android.sdk.exception.LineSdkLoginException;
import jp.line.android.sdk.login.LineAuthManager;
import jp.line.android.sdk.login.LineLoginFuture;
import jp.line.android.sdk.login.LineLoginFutureListener;
import jp.line.android.sdk.model.Profile;

import static android.os.Build.ID;

/**
 * Created by clery on 2017/1/25.
 */

public class LoginLineMain extends Activity{

    private static LineApiClient lineApiClient;

    private static int REQUEST_CODE = 200;

    ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        LineSdkContextManager.initialize(this);

        button = (ImageButton)findViewById(R.id.linebutton);
        button.setImageDrawable(getResources().getDrawable(R.drawable.btn_login_base));
        button.setScaleType(ImageView.ScaleType.FIT_CENTER);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Line line = new Line();
                line.line_login(LoginLineMain.this);
//                Intent loginIntent = LineLoginApi.getLoginIntent(getApplicationContext(), "1499397246");
//                startActivityForResult(loginIntent, REQUEST_CODE);
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Signature[] sigs = new Signature[0];
        try {
            sigs = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES).signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        for (Signature sig : sigs)
        {
            Log.d("MyApp", "Signature hashcode : " + sig.hashCode());
        }


//        Intent loginIntent = LineLoginApi.getLoginIntentWithoutLineAppAuth(getApplicationContext(), "1498299281");



//        LineApiClientBuilder apiClientBuilder = new LineApiClientBuilder(getApplicationContext(), "1498299281");
//        lineApiClient = apiClientBuilder.build();
//        lineApiClient.refreshAccessToken();
//
////        String accessToken = lineApiClient.getCurrentAccessToken().getResponseData().getAccessToken();
//
//        LineApiResponse verifyResponse = lineApiClient.verifyToken();
//
//        if (verifyResponse.isSuccess()) {
//
//            Log.i("TAG", "getResponseData: " + verifyResponse.getResponseData().toString());
//            Log.i("TAG", "getResponseCode: " + verifyResponse.getResponseCode().toString());
//
//
//        } else {
//
//            Log.i("TAG", "getResponseCode: " + verifyResponse.getResponseCode());
//            Log.i("TAG", "getErrorData: " + verifyResponse.getErrorData());
//
//
//        }
//        Log.d("---accessToken",accessToken);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("----line1","-----");

        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
        Log.d("---result",result.toString());

        switch (result.getResponseCode()) {

            case SUCCESS:
                Log.d("----lineSUCCESS","-----");

                Log.d("--display_name",result.getLineProfile().getDisplayName());
                Log.d("--status_message",result.getLineProfile().getStatusMessage());
                Log.d("--user_id",result.getLineProfile().getUserId());
                Log.d("--picture_url",result.getLineProfile().getPictureUrl().toString());

                break;
            case CANCEL:
                lineApiClient.logout();
                Log.d("----lineCANCEL","-----");
                break;
            case SERVER_ERROR:
                Log.d("----lineSERVER_ERROR","-----");
                break;
            case NETWORK_ERROR:
                Log.d("----lineNETWORK_ERROR","-----");
                break;
            case INTERNAL_ERROR:
                Log.d("----lineINTERNAL_ERROR","-----");
                break;
            default:
                // Login was cancelled by the user
                // Do something...
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right);
    }
}
