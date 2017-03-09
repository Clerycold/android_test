package com.test.hybird.ActivityTourTeach;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.hybird.R;
import com.test.hybird.SamplePagerAdapter;
import com.test.hybird.ScreenWH;
import com.test.hybird.control.BsetBitmap.BitmapUtils;
import com.test.hybird.dataListActivity;

/**
 * Created by clery on 2017/2/7.
 */

public class TourTeach extends Activity implements ViewPager.OnPageChangeListener{
    ViewPager tourViewPage;
    SamplePagerAdapter samplePagerAdapter;

    private TextView textOmission;
    private Button btnfinsh;

    String animstyle;
    int animation_in;
    int animation_out;

    String[] tourImg ;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();
        xmlArrange();
        getSetData(getIntent());
        overridePendingTransition(animation_in,R.anim.animo_no);

        samplePagerAdapter = new SamplePagerAdapter();
        if(tourImg != null){
            for (int i = 0, j = tourImg.length; i < j; i++) {
                samplePagerAdapter.addPagelist(new TourTeach.PageViewContent(getApplicationContext(), tourImg[i]));
            }
        }
        tourViewPage.setAdapter(samplePagerAdapter);
        tourViewPage.addOnPageChangeListener(this);

    }
    //得到設定資料
    private void getSetData(Intent intent){
        if(intent != null){
            tourImg = intent.getStringArrayExtra("tourImg");
            animstyle = intent.getStringExtra("Animstyle");
        }
        if(!animstyle.equals("")){
            setAnimationData(Integer.parseInt(animstyle));
        }
    }
    //Activity啟動動畫 及結束動畫
    private void setAnimationData(int animstyle){
        switch (animstyle){
            case 0:
                animation_in = R.anim.slide_in_right;
                animation_out = R.anim.slide_out_right;
                break;
            case 1:
                animation_in = R.anim.slide_in_left;
                animation_out = R.anim.slide_out_left;
                break;
            case 2:
                animation_in = R.anim.slide_in_bottom;
                animation_out = R.anim.slide_out_bottom;
                break;
        }
    }
    private void initView(){
        tourViewPage = (ViewPager) findViewById(R.id.tourviewpage);
        textOmission = (TextView) findViewById(R.id.textOmission);
        btnfinsh = (Button) findViewById(R.id.btnfinsh);
    }

    private void xmlArrange(){
        RelativeLayout.LayoutParams btnfinsh_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth()/4,
                ScreenWH.getScreenHidth()/20);
        btnfinsh_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btnfinsh_lay.setMargins(0,(int)((ScreenWH.getScreenHidth()/10)*8.5),0,0);
        btnfinsh.setLayoutParams(btnfinsh_lay);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageSelected(int position) {

        if(position == tourImg.length-1){
            textOmission.setVisibility(View.GONE);
            btnfinsh.setVisibility(View.VISIBLE);
            btnfinsh.bringToFront();
        }else{
            textOmission.setVisibility(View.VISIBLE);
            btnfinsh.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //在xml下的點擊事件
    public void finishActivity(View view){
        finish();
    }

    //增加結束動畫
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,animation_out);
    }

    //pageview的佈局
    class PageViewContent extends RelativeLayout {

        private ImageView imageView;

        private int Width ;
        private int Height;

        private int resId;

        public PageViewContent(Context context, String resName) {
            super(context);

            Width=ScreenWH.getScreenWidth();
            Height=ScreenWH.getNoStatus_bar_Height(context);

            resId = getResources().getIdentifier(resName, "drawable", getPackageName());

            initView(context);
            setImgresID(context,resId);

        }
        private void initView(Context context) {

            imageView = new ImageView(context);

            RelativeLayout.LayoutParams imageView_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(imageView_lay);
            this.addView(imageView);

        }
        //本地圖片
        private void setImgresID(Context context, int resId) {
            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(context.getResources(), resId, Width, Height);
            imageView.setImageBitmap(bitmap);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
    }
}

