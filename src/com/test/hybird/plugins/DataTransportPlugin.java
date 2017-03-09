package com.test.hybird.plugins;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.camera2.params.Face;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.test.hybird.ActivityTourTeach.TourTeach;
import com.test.hybird.R;
import com.test.hybird.contectLogin.CustomVolleyRequest;
import com.test.hybird.contectLogin.LoginMainActivity;
import com.test.hybird.control.BsetBitmap.ScreenShot;
import com.test.hybird.control.RemindUI;
import com.test.hybird.control.json.JsonSolve;
import com.test.hybird.dataListActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by clery on 2016/12/26.
 */

public class DataTransportPlugin extends CordovaPlugin implements FacebookCallback<LoginResult> {

    //执行插件的动作，可以有多个动作
    private static final String ACTION_data = "dataTransport";
    private static final String ACTION_screen = "getscreenshot";
    private static final String ACTION_facebook = "getFacebookId";
    private static final String ACTION_google = "getGoogleId";

    private CallbackContext callbackContext;

    private Context context;

    private final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    //Signing Options
    private GoogleSignInOptions gso;

    //google api client
    private GoogleApiClient mGoogleApiClient;

    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;

    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        try {

            this.callbackContext = callbackContext;
            context = cordova.getActivity().getApplicationContext();

            //判断接口传来的动作是否跟定义的动作一致
            if (ACTION_data.equals(action)) {
                //获取json
                JSONObject argJson = args.getJSONObject(0);
                JSONObject jsonObject = new JSONObject(argJson.getString("personl"));

                Intent mIntent = new Intent();
                JSONArray jsonArray = new JSONArray(jsonObject.getString("tourImg"));
                mIntent.putExtra("Animstyle", jsonObject.getString("Animstyle"));
                mIntent.putExtra("tourImg",JsonSolve.JsonStringToIntArray(jsonArray));
                mIntent.setClass(cordova.getActivity(), TourTeach.class);
                cordova.getActivity().startActivity(mIntent);
                return true;
            } else if (ACTION_screen.equals(action)) {


                this.callbackContext = callbackContext;

                int permission2 = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission2 != PackageManager.PERMISSION_GRANTED) {
                    // 無權限，向使用者請求
                    cordova.requestPermissions(this, REQUEST_CAMERA_PERMISSION, permissions);

                } else {

                    getScreenShot();
                }
                return true;
            } else if (ACTION_facebook.equals(action)) {

                FacebookSdk.sdkInitialize(context);
                callbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().logInWithReadPermissions(cordova.getActivity(), Arrays.asList("public_profile", "user_friends"));
                cordova.setActivityResultCallback(this);
                LoginManager.getInstance().registerCallback(callbackManager, this);

                return true;
            } else if (ACTION_google.equals(action)) {
                //Initializing google signin option
                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                //Initializing google api client
                mGoogleApiClient = new GoogleApiClient.Builder(context)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

                signIn();
                return true;
            } else {
                callbackContext.error("Invalid action");
                return false;
            }

        } catch (Exception e) {

            callbackContext.error(e.getMessage());
            return false;
        }
    }

    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        cordova.startActivityForResult(this, signInIntent, RC_SIGN_IN);
    }

    /**
     * activity 的回调接收方法
     *
     * @param requestCode The request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param intent      An Intent, which can return result data to the caller (various data can be
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (resultCode) {
            case Activity.RESULT_OK:

                if (callbackManager != null) {
                    callbackManager.onActivityResult(requestCode, resultCode, intent);
                }
                if (requestCode == RC_SIGN_IN) {
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
                    //Calling a new function to handle signin
                    handleSignInResult(result);
                }
                break;
            default:
                break;
        }
    }

    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();

            String googlelink = null;

            if (acct.getPhotoUrl() != null) {
                googlelink = acct.getPhotoUrl().toString();
            }

            final String[] jsonname = {"GoogleId", "GoogleLink"};
            final String[] jsonvalue = {acct.getId(), googlelink};

            Log.d("google",JsonSolve.createJson(jsonname, jsonvalue));

            getPluginResult(JsonSolve.createJson(jsonname, jsonvalue));


        } else {
            //If login fails
            Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    ////通过PluginResult和callbackContext返回给js接口
    private void getPluginResult(String string) {
        PluginResult pluginResults = new PluginResult(PluginResult.Status.OK, string);
        callbackContext.sendPluginResult(pluginResults);
        pluginResults.setKeepCallback(true);
        callbackContext.success();
    }

    private void getScreenShot() {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                ScreenShot screenShot = new ScreenShot(cordova.getActivity());
                Bitmap screenBitmap = screenShot.getScreenBitmap();

                if (screenBitmap != null) {
                    screenShot.SaveScreenBitmap(screenBitmap);
                    screenShot.GalleryAddPic();

                    RemindUI.setToast(context, "畫面擷取成功");

                    String str = "Success";

                    getPluginResult(str);

                } else {
                    RemindUI.setToast(context, "發生不明原因，畫面擷取失敗");
                    callbackContext.error("error");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        super.onRequestPermissionResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getScreenShot();

            } else {
                RemindUI.setToast(context, "無法擷取畫面，請確認權限");
                callbackContext.error("error");
            }
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        if (Profile.getCurrentProfile() == null) {
            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    profileTracker.stopTracking();
                    Profile.setCurrentProfile(profile2);
                }
            };
            // no need to call startTracking() on mProfileTracker
            // because it is called by its constructor, internally.
        } else {
            Profile profile = Profile.getCurrentProfile();
            String facebooklink = null;

            if (profile.getLinkUri() != null) {
                facebooklink = profile.getLinkUri().toString();
            }

            final String[] jsonname = {"FacebookId", "FacebookLink"};
            final String[] jsonvalue = {profile.getId(), facebooklink};

            Log.d("facebook",JsonSolve.createJson(jsonname, jsonvalue));

            getPluginResult(JsonSolve.createJson(jsonname, jsonvalue));
        }
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {
        //如果發生使用者不一樣 需要再重新撈一次資料 強制登出 再重新撈取資料
        if (error instanceof FacebookAuthorizationException) {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            }
        }
    }
}