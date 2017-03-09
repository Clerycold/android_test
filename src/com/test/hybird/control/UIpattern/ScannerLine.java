package com.test.hybird.control.UIpattern;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.test.hybird.R;
import com.test.hybird.control.ScreenWH;

/**
 * Created by clery on 2016/12/29.
 */

public class ScannerLine extends View {

    private int mWidth;
    private int mHight;

    private float moveY = 0;
    private int distanceY =0;

    Paint p;

    public ScannerLine(Context context) {
        super(context);

        initView();

    }

    private void initView() {

        mWidth= ScreenWH.getScreenWidth();
        mHight=ScreenWH.getNoStatus_bar_Height(getContext().getApplicationContext());

        p = new Paint();
        p.setColor(getResources().getColor(R.color.colorMain));
        p.setStrokeWidth(3);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine((float)((mWidth/6.3)*0.5),(float)((mHight/10)*1.4)+moveY,(float)((mWidth/6.3)*5.7),(float)((mHight/10)*1.4)+moveY,p);
    }

    public void setmoveY(){

        if(moveY <= 0){
            distanceY = 2;
        }else if(moveY >= (int)(double)((mHight/10)*5.2)){
            distanceY = -2;
        }

        moveY += distanceY;

        invalidate();
    }
}