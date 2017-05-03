package com.mersens.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Mersens on 2017/5/3 16:30
 * Email:626168564@qq.com
 */

public class CalendarTextView extends TextView {
    private boolean isSelect=false;
    private Paint mPaint;


    public CalendarTextView(Context context) {
        this(context,null);
    }

    public CalendarTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public CalendarTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#e51c23"));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(dp2px(1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isSelect){
            canvas.translate(getWidth()/2,getHeight()/2);
            float width=getWidth()/2;
            canvas.drawCircle(0,0,width*0.5f,mPaint);
        }

    }

    public void setSelect(boolean isSelect){
        this.isSelect=isSelect;
        invalidate();
    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());

    }
}
