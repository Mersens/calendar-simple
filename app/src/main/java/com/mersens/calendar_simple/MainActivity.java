package com.mersens.calendar_simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mersens.calendar.CalendarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarView calendarView=(CalendarView)findViewById(R.id.calendarView);
        calendarView.setOnDateSelectListener(new CalendarView.OnDateSelectListener() {
            @Override
            public void onSelect(String date) {
                Toast.makeText(MainActivity.this, date, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
