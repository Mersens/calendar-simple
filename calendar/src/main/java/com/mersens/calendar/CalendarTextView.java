package com.mersens.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Mersens on 2017/5/3 16:30
 * Email:626168564@qq.com
 */

public class CalendarTextView extends TextView {
    private boolean isSelect = false;
    private boolean isToday = false;
    private int todayPaintColor = 0XFFE51C23;
    private int selectPaintColor = 0XFF259B24;
    private static final int DEFAULT_STROKE_WIDTH=1;
    private Paint mPaint;

    public CalendarTextView(Context context) {
        this(context, null);
    }

    public CalendarTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(dp2px(DEFAULT_STROKE_WIDTH));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float width = getWidth() / 2;
        if (isToday) {
            mPaint.setColor(todayPaintColor);
            canvas.drawCircle(0, 0, width * 0.5f, mPaint);
        }
        if (isSelect) {
            mPaint.setColor(selectPaintColor);
            canvas.drawCircle(0, 0, width * 0.5f, mPaint);
        }
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
        invalidate();
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean isToday) {
        this.isToday = isToday;
        invalidate();
    }

    public void setTodayPaintColor(int todayPaintColor) {
        this.todayPaintColor = todayPaintColor;

    }

    public void setSelectPaintColor(int selectPaintColor) {
        this.selectPaintColor = selectPaintColor;

    }
    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                getResources().getDisplayMetrics());

    }
}
