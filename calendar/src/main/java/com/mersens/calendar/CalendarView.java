package com.mersens.calendar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mersens on 2017/5/3 13:58
 * Email:626168564@qq.com
 */

public class CalendarView extends FrameLayout {
    private ImageView btn_left;
    private ImageView btn_right;
    private GridView grid_days;
    private TextView tv_title;
    private CalendarAdapter adapter;
    private Calendar curDate;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initViews();
        initEvent();
        initDatas(true);
    }

    private void initViews() {
        curDate = Calendar.getInstance();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.calendar_layout, this);
        btn_left = (ImageView) findViewById(R.id.btn_left);
        btn_right = (ImageView) findViewById(R.id.btn_right);
        grid_days = (GridView) findViewById(R.id.grid_days);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    private void initEvent() {

        btn_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, -1);
                initDatas(false);
            }
        });

        btn_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, 1);
                initDatas(false);
            }
        });

    }

    private void initDatas(boolean inMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.ENGLISH);
        tv_title.setText(sdf.format(curDate.getTime()));
        ArrayList<Date> cells = new ArrayList<Date>();
        Calendar calendar = (Calendar) curDate.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int prevDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -prevDays);
        int maxCellCount = 6 * 7;
        while (cells.size() < maxCellCount) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        adapter = new CalendarAdapter(getContext(), cells);
        grid_days.setAdapter(adapter);

    }

    private class CalendarAdapter extends ArrayAdapter<Date> {
        private LayoutInflater mInflater;


        public CalendarAdapter(Context context, ArrayList<Date> list) {
            super(context, R.layout.day_layout, list);
            mInflater = LayoutInflater.from(context);

        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.day_layout, parent, false);
            }
            Date date = getItem(position);
            CalendarTextView tv_day = (CalendarTextView) convertView.findViewById(R.id.tv_day);
            Date now = curDate.getTime();
            tv_day.setText(String.valueOf(date.getDate()));
            boolean isTheSameMonth = false;

            if (date.getMonth() == now.getMonth()) {
                isTheSameMonth = true;
            }
            if (isTheSameMonth) {
                //当月的日期
                tv_day.setTextColor(Color.parseColor("#3e3e39"));
            } else {
                //不是当月的日期
                tv_day.setTextColor(Color.parseColor("#bdbdbd"));
            }
            //当前日期为当天
            Date d = new Date();
            if (d.getDate() == date.getDate() && d.getMonth() == date.getMonth() &&
                    d.getYear() == date.getYear()) {
                    tv_day.setTextColor(Color.parseColor("#e51c23"));
                    tv_day.setSelect(true);
                }
            return convertView;
        }
    }
}
