package com.etti.classroomsbooking;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

import static com.etti.classroomsbooking.util.Constant.DATE_IN_MILLIS;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    DatePicker datePicker;
    FloatingActionButton floatingActionButton;
    Long dateInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        datePicker = (DatePicker) findViewById(R.id.calendarDatePicker);
        datePicker.setMinDate(System.currentTimeMillis());
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
    }

    public Calendar getDateInstance(DatePicker datePicker){
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        return calendar;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ClassroomsActivity.class);
        dateInMillis = getDateInstance(datePicker).getTimeInMillis();
        intent.putExtra(DATE_IN_MILLIS, dateInMillis);
        startActivity(new Intent(getApplicationContext(), ClassroomsActivity.class));
    }
}
