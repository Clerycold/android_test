package com.test.hybird;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

/**
 * Created by clery on 2016/12/23.
 */

public class TestCordovaWithLayoutActivity extends CordovaActivity {


    Button button;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_cordova_with_layout);
        super.init();

        loadUrl(launchUrl);

        button=(Button)findViewById(R.id.button);
        RelativeLayout.LayoutParams button_lay =new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth()/2,ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10);
        button_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        button_lay.setMargins(0,(ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10)*8,0,0);
        button.setLayoutParams(button_lay);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected CordovaWebView makeWebView() {
        SystemWebView webView = (SystemWebView)findViewById(R.id.cordovaWebView);
        return new CordovaWebViewImpl(new SystemWebViewEngine(webView));
    }

    @Override
    protected void createViews() {
        // Why are we setting a constant as the ID? This should be investigated
		/*
		appView.getView().setId(100);
		appView.getView().setLayoutParams(new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT));
		setContentView(appView.getView());
		*/

        if (preferences.contains("BackgroundColor")) {
            int backgroundColor = preferences.getInteger("BackgroundColor", Color.BLACK);
            // Background of activity:
            appView.getView().setBackgroundColor(backgroundColor);
        }

        appView.getView().requestFocusFromTouch();
    }

    @Override
    public void onDestroy() {
        SystemWebView webView = (SystemWebView)findViewById(R.id.cordovaWebView);
        ((ViewGroup)webView.getParent()).removeView(webView);
        webView.removeAllViews();
        // If we called webView.destory(), we will get 'mWebViewCore is null' error.
        //webView.destroy();
        super.onDestroy();
    }

}