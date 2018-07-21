package com.dentalclinic.capstone.admin.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.dentalclinic.capstone.admin.R;

import java.util.Calendar;
import java.util.List;

public class DatePickerActivity extends BaseActivity {
    private CalendarView calendarView;
    private AutoCompleteTextView autoCompleteTextView ;
    private TextView btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }
        calendarView = findViewById(R.id.calendarView);
        autoCompleteTextView = findViewById(R.id.edt_note);
        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Calendar> eventDays = calendarView.getSelectedDates();
                for (Calendar calendar: eventDays){
                    showMessage(calendar.getTime().toString());
                }
            }
        });




    }

    @Override
    public String getMainTitle() {
        return "Xin nghĩ";
    }

    @Override
    public void onCancelLoading() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
