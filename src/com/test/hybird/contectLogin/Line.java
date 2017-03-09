package com.test.hybird.contectLogin;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

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
import jp.line.android.sdk.model.AccessToken;
import jp.line.android.sdk.model.Profile;

/**
 * Created by clery on 2017/1/26.
 */

public class Line extends Application {

    Activity activity;

    @Override
    public void onCreate() {
        super.onCreate();
        LineSdkContextManager.initialize(this);
    }

    public void line_login(final Activity activity) {
        this.activity=activity;

        LineSdkContext sdkContext = LineSdkContextManager.getSdkContext();
        LineAuthManager authManager = sdkContext.getAuthManager();
        setline_login(authManager);

    }
    private void setline_login(final LineAuthManager authManager){
        LineLoginFuture loginFuture = authManager.login(activity);
        loginFuture.addFutureListener(new LineLoginFutureListener() {
            @Override
            public void loginComplete(LineLoginFuture future) {
                switch(future.getProgress()) {
                    case SUCCESS: // Login successfully
                        Log.d("--SUCCESS", "-----");
                        getProfile(authManager);
                        break;
                    case CANCELED: // Login canceled by user
                        Log.d("--canceled", "-----");
                        break;
                    default: // Error
                        Log.d("--Error", "-----");
                        checkException(future.getCause());
                        break;
                }
            }
        });
    }

    private void checkException(Throwable cause) {
        android.util.Log.e("TAG", String.format("Error message: %s", cause.getMessage()));
        if (cause instanceof LineSdkLoginException) {
            LineSdkLoginException loginException = (LineSdkLoginException)cause;
            LineSdkLoginError error = loginException.error;
            switch(error) {
                case FAILED_START_LOGIN_ACTIVITY:
                    Log.d("TAG", "FAILED_START_LOGIN_ACTIVITY");
                    // Failed launching LINE application or WebLoginActivity (Activity may be null)
                    break;
                case FAILED_A2A_LOGIN:
                    // Failed LINE login
                    Log.d("TAG", "FAILED_A2A_LOGIN");
                    break;
                case FAILED_WEB_LOGIN:
                    // Failed Web login
                    Log.d("TAG", "FAILED_WEB_LOGIN");
                    break;
                case UNKNOWN:
                    Log.d("TAG", "UNKNOWN");
                    // Un expected error occurred
                    break;
            }
        } else {
        }
    }

    public void getProfile(final LineAuthManager authManager) {
        ApiClient apiClient = LineSdkContextManager.getSdkContext().getApiClient();
        apiClient.getMyProfile(new ApiRequestFutureListener() {
            @Override
            public void requestComplete(ApiRequestFuture future) {
                switch(future.getStatus()) {
                    case SUCCESS:

                        Profile profile = (Profile) future.getResponseObject();
                        String mid = profile.mid;
                        String displayName = profile.displayName;
                        String pictureUrl = profile.pictureUrl;
                        String statusMessage = profile.statusMessage;

                        Intent intent=new Intent();
                        intent.putExtra("mid", mid);
                        intent.putExtra("displayName",displayName);
                        intent.putExtra("pictureUrl",pictureUrl);
                        intent.putExtra("statusMessage",statusMessage);
                        activity.setResult(100, intent);

                        Log.d("TAG", "mid ="+ mid);
                        Log.d("TAG", "displayName = "+ displayName);
                        Log.d("TAG", "pictureUrl = " + pictureUrl);
                        Log.d("TAG", "statusMessage = " + statusMessage);
                        activity.finish();
                        break;
                    case FAILED:
                        authManager.logout();
                        checkException(future.getCause());
                        setline_login(authManager);
                    default:
                        // do something for error...
                        break;
                };
            }
        });
    }
}
