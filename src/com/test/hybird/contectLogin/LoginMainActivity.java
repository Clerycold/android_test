package com.test.hybird.contectLogin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.test.hybird.R;
import com.test.hybird.control.BsetBitmap.ImageHelper;
import com.test.hybird.control.SaveData;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.LogManager;

import static java.security.AccessController.getContext;
import static org.apache.cordova.engine.SystemWebViewEngine.TAG;

/**
 * Created by clery on 2017/1/17.
 */

public class LoginMainActivity extends Activity {

    CallbackManager callbackManager;
    private AccessToken accessToken;

    boolean facebookState;
    SaveData saveData;

    private ProfileTracker profileTracker;
    private ShareDialog shareDialog;

    Intent mIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        //宣告callback Manager
        callbackManager = CallbackManager.Factory.create();
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_facebook);

//        shareDialog = new ShareDialog(this);
//        shareDialog.registerCallback(
//                callbackManager, shareCallback);

//        saveData = new SaveData(getApplicationContext());
//        facebookState = saveData.readDataBoolean("facebook","facebook");

        mIntent = LoginMainActivity.this.getIntent();

        //找到button
//        final ImageView profilePicture = (ImageView) findViewById(R.id.profilePicture);
//        Button loginButton = (Button) findViewById(R.id.fb_login);
//        Button logoutButton = (Button) findViewById(R.id.fb_logout);
//        final CheckBox CodeCheckBox = (CheckBox)findViewById(R.id.CheckBox01);
//        final TextView facebook_name = (TextView) findViewById(R.id.facebook_name);
//        LoginButton fb_Button = (LoginButton) findViewById(R.id.fb_button);
//        final Button fb_post = (Button) findViewById(R.id.fb_post);
//        final LikeView likeView = (LikeView) findViewById(R.id.likeView);
//        likeView.setObjectIdAndType(
//                "https://www.facebook.com/kingnet.net/?fref=ts",
//                LikeView.ObjectType.OPEN_GRAPH);
//        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
//        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);

        facebookState = true ;
//        CodeCheckBox.setChecked(true);

//        final ShareButton shareButton = (ShareButton)findViewById(R.id.share_button);
//        ShareLinkContent content = new ShareLinkContent.Builder()
//                .setContentUrl(Uri.parse("https://www.facebook.com/kingnet.net/?fref=ts"))
//                .build();
//        shareButton.setShareContent(content);

//        loginButton.setOnClickListener(new Button.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                LoginManager.getInstance().logInWithReadPermissions(LoginMainActivity.this, Arrays.asList("public_profile", "user_friends"));
//                CodeCheckBox.setChecked(true);
//            }
//        });
//
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginManager.getInstance().logOut();
//                CodeCheckBox.setChecked(false);
//                facebook_name.setText("");
//                profilePicture.setImageBitmap(null);
//                Log.d("FB", "logoutbutton");
//                fb_post.setVisibility(View.GONE);
//                likeView.setVisibility(View.GONE);
//                shareButton.setVisibility(View.GONE);
//            }
//        });

        if(facebookState){
            LoginManager.getInstance().logInWithReadPermissions(LoginMainActivity.this, Arrays.asList("public_profile", "user_friends"));
        }

//        CodeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(CodeCheckBox.isChecked()){
//                    saveData.saveBooleanData("facebook","facebook",true);
//                }else{
//                    saveData.saveBooleanData("facebook","facebook",false);
//                }
//            }
//        });
//
//        fb_Button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                accessToken = loginResult.getAccessToken();
//
//                Log.d("FB","access token got.");
//
//
//                if(Profile.getCurrentProfile() == null) {
//                    profileTracker = new ProfileTracker() {
//                        @Override
//                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
//                            // profile2 is the new profile
//                            Log.v("facebook - profile", profile2.getFirstName());
//                            profileTracker.stopTracking();
//                        }
//                    };
//                    // no need to call startTracking() on mProfileTracker
//                    // because it is called by its constructor, internally.
//                    profileTracker.startTracking();
//                }
//                else {
//                    Profile profile = Profile.getCurrentProfile();
//                    new LoadProfileImage(profilePicture).execute(profile.getProfilePictureUri(200, 200).toString());
//                    Log.d("FB_profile",profile.getId());
//                    Log.d("FB_profile",profile.getFirstName());
//                    Log.d("FB_profile",profile.getName());
//                    Log.d("FB_profile",profile.getLinkUri().toString());
//                    Log.d("FB_profile",profile.getProfilePictureUri(200,200).toString());
//                }

                //send request and call graph api

//                GraphRequest request = GraphRequest.newMeRequest(
//                        accessToken,
//                        new GraphRequest.GraphJSONObjectCallback() {
//
//                            //當RESPONSE回來的時候
//
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {

                                //讀出姓名 ID FB個人頁面連結

//                                "id": "12345678",
//                                "birthday": "1/1/1950",
//                                "first_name": "Chris",
//                                "gender": "male",
//                                "last_name": "Colm",
//                                "link": "http://www.facebook.com/12345678",
//                                "location": {
//                                         "id": "110843418940484",
//                                         "name": "Seattle, Washington"
//                                },
//                                "locale": "en_US",
//                                        "name": "Chris Colm",
//                                        "timezone": -8,
//                                        "updated_time": "2010-01-01T16:40:43+0000",
//                                        "verified": true


//                                Log.d("FB","complete");
//                                Log.d("FB",object.optString("name"));
//                                Log.d("FB",object.optString("link"));
//                                Log.d("FB",object.optString("id"));
//
////                                fb_post.setVisibility(View.VISIBLE);
////                                likeView.setVisibility(View.VISIBLE);
////                                shareButton.setVisibility(View.VISIBLE);
//                            }
//                        });
//
//                //包入你想要得到的資料 送出request
//
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,link");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d("FB","CANCEL");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d("FB",error.toString());
//            }
//        });


        //幫 LoginManager 增加callback function

        //這邊為了方便 直接寫成inner class

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            //登入成功

            @Override
            public void onSuccess(LoginResult loginResult) {

                //accessToken之後或許還會用到 先存起來

                accessToken = loginResult.getAccessToken();

                Log.d("FB", "access token got.");

                if(Profile.getCurrentProfile() == null) {
                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            Log.v("facebook - profile", profile2.getFirstName());
                            profileTracker.stopTracking();
                            Profile.setCurrentProfile(profile2);
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
//                    new LoadProfileImage(profilePicture).execute(profile.getProfilePictureUri(200, 200).toString());
                    Log.d("FB_profile",profile.getId());
                    Log.d("FB_profile",profile.getFirstName());
                    Log.d("FB_profile",profile.getName());
                    Log.d("FB_profile",profile.getLinkUri().toString());
                    Log.d("FB_profile",profile.getProfilePictureUri(200,200).toString());
                    mIntent.putExtra("FacebookLink", profile.getLinkUri().toString());
                }

                //send request and call graph api

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {

                            //當RESPONSE回來的時候

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                //讀出姓名 ID FB個人頁面連結

                                Log.d("FB", "complete");
                                Log.d("FB", object.toString());
                                Log.d("FB", object.optString("name"));
                                Log.d("FB", object.optString("link"));
                                Log.d("FB", object.optString("id"));

//                                facebook_name.setText(object.optString("name"));
//                                fb_post.setVisibility(View.VISIBLE);
//                                likeView.setVisibility(View.VISIBLE);
//                                shareButton.setVisibility(View.VISIBLE);

//                                Intent mIntent = LoginMainActivity.this.getIntent();
                                String msg = object.optString("id");
                                mIntent.putExtra("FacebookId", object.optString("id"));
                                setResult(RESULT_OK, mIntent);
                                finish();

                            }
                        });

                //包入你想要得到的資料 送出request

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();
            }

            //登入取消

            @Override
            public void onCancel() {
                // App code

                Log.d("FB", "CANCEL");
            }

            //登入失敗

            @Override
            public void onError(FacebookException exception) {
                // App code

                Log.d("FB", exception.toString());
            }
        });

//        fb_post.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShareLinkContent linkContent = new ShareLinkContent.Builder()
////                        .setContentTitle("分享今網臉書官方網站")//代表連結中的內容標題
////                        .setContentDescription(
////                                "This app shows how to integrate Facebook Login to your Android App")//內容的說明，通常為 2 至 4 個句子
////                        .setContentUrl(Uri.parse("https://www.facebook.com/kingnet.net/?fref=ts"))//要分享的連結
//                        .build();
//                if (ShareDialog.canShow(ShareLinkContent.class)) {
//                    shareDialog.show(linkContent);
//                }
//            }
//        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... uri) {
            String url = uri[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            if (result != null) {

                Bitmap resized = Bitmap.createScaledBitmap(result,200,200, true);
                bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getApplicationContext(),resized,250,200,200, false, false, false, false));
                Log.d("-----bitmap","------");
            }
        }
    }

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("FacebookFragment", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("FacebookFragment", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("FacebookFragment", "Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(getApplicationContext())
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };
}

