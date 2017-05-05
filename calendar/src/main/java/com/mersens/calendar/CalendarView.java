package com.mersens.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mersens on 2017/5/2
 * Email:626168564@qq.com
 */

public class CalendarView extends FrameLayout {
    private static final String TAG="CalendarView";
    private static final int DEFAULT_SELECT_DATE_COLOR=0XFF259B24;
    private static final int DEFAULT_CURRENT_DATE_COLOR=0XFFE51C23;
    private static final int DEFAULT_UNSELECT_DATE_COLOR=0XFF3E3E39;
    private static final int DEFAULT_WEEK_COLOR=DEFAULT_UNSELECT_DATE_COLOR;
    private static final int DEFAULT_TITLE_COLOR=DEFAULT_UNSELECT_DATE_COLOR;

    private int select_date_color;
    private int unselect_date_color;
    private int current_date_color;
    private int week_color;
    private int title_color;

    private ImageView btn_left;
    private ImageView btn_right;
    private GridView grid_days;
    private TextView tv_title;
    private LinearLayout layout_weeks;


    private CalendarAdapter adapter;
    private Calendar curDate;
    private OnDateSelectListener listener;
    private SimpleDateFormat sdf;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a=getContext().obtainStyledAttributes(attrs,R.styleable.CalendarView);
        try{
            if(a.hasValue(R.styleable.CalendarView_select_date_color)){
                select_date_color=a.getColor(R.styleable.CalendarView_select_date_color,
                        DEFAULT_SELECT_DATE_COLOR);
            }else {
                select_date_color=DEFAULT_SELECT_DATE_COLOR;
            }
            if(a.hasValue(R.styleable.CalendarView_unselect_date_color)){
                unselect_date_color=a.getColor(R.styleable.CalendarView_unselect_date_color,
                        DEFAULT_UNSELECT_DATE_COLOR);
            }else{
                unselect_date_color= DEFAULT_UNSELECT_DATE_COLOR;
            }
            if(a.hasValue(R.styleable.CalendarView_current_date_color)){
                current_date_color=a.getColor(R.styleable.CalendarView_current_date_color,
                        DEFAULT_CURRENT_DATE_COLOR);
            }else{
                current_date_color=DEFAULT_CURRENT_DATE_COLOR;
            }
            if(a.hasValue(R.styleable.CalendarView_week_color)){
                week_color=a.getColor(R.styleable.CalendarView_week_color,DEFAULT_WEEK_COLOR);
            }else {
                week_color=DEFAULT_WEEK_COLOR;
            }
            if(a.hasValue(R.styleable.CalendarView_title_color)){
                title_color=a.getColor(R.styleable.CalendarView_title_color,DEFAULT_TITLE_COLOR);
            }else{
                title_color=DEFAULT_TITLE_COLOR;
            }
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }finally {
            a.recycle();
        }
        initViews();
        initEvent();
        initDatas();
    }

    private void initViews() {
        curDate = Calendar.getInstance();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.calendar_layout, this);
        btn_left = (ImageView) findViewById(R.id.btn_left);
        btn_right = (ImageView) findViewById(R.id.btn_right);
        grid_days = (GridView) findViewById(R.id.grid_days);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setTextColor(title_color);

        layout_weeks=(LinearLayout)findViewById(R.id.layout_weeks);
        for(int i=0;i<layout_weeks.getChildCount();i++){
            View view=layout_weeks.getChildAt(i);
            if(view instanceof TextView){
                TextView tv_week=(TextView)view;
                tv_week.setTextColor(week_color);
            }
        }
    }

    private void initEvent() {

        btn_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, -1);
                initDatas();
            }
        });

        btn_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, 1);
                initDatas();
            }
        });
        grid_days.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CalendarTextView ct=(CalendarTextView) view;
                String date=ct.getText().toString();
                if(date!=null && !"".equals(date)){
                    for(CalendarTextView cv:adapter.getDateLists()){
                        if( cv.isSelect()){
                            if(cv.isToday()){
                                cv.setTextColor(current_date_color);
                                cv.setTodayPaintColor(current_date_color);
                            }else{
                                cv.setTextColor(unselect_date_color);
                            }
                            cv.setSelect(false);
                        }
                    }
                    if(!ct.isToday()){
                        ct.setSelectPaintColor(select_date_color);
                        ct.setTextColor(select_date_color);
                    }
                    ct.setSelect(true);
                    if(listener==null){
                        return;
                    }
                    listener.onSelect(sdf.format(curDate.getTime())+"-"+ct.getText().toString());
                }
            }
        });
    }
    private void initDatas() {
        sdf = new SimpleDateFormat("yyyy-M", Locale.ENGLISH);
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
        private ArrayList<CalendarTextView> dateLists;


        public CalendarAdapter(Context context, ArrayList<Date> list) {
            super(context, R.layout.day_layout, list);
            mInflater = LayoutInflater.from(context);
            dateLists=new ArrayList<CalendarTextView>();
        }

        public ArrayList<CalendarTextView> getDateLists(){
            return this.dateLists;
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
                tv_day.setTextColor(unselect_date_color);
                dateLists.add(tv_day);
            } else {
                //不是当月的日期
                tv_day.setText("");
            }
            //当前日期为当天
            Date d = new Date();
            if (d.getDate() == date.getDate() && d.getMonth() == date.getMonth() &&
                    d.getYear() == date.getYear()) {
                String prevDate = tv_day.getText().toString();
                if (prevDate != null && !"".equals(prevDate)) {
                    tv_day.setTodayPaintColor(current_date_color);
                    tv_day.setTextColor(current_date_color);
                    tv_day.setToday(true);
                }

            }
            return convertView;
        }
    }

    public void setOnDateSelectListener(OnDateSelectListener listener){
        this.listener=listener;
    }
    public interface OnDateSelectListener{
        void onSelect(String date);
    }


}
