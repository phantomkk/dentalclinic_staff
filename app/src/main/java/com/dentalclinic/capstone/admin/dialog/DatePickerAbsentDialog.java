package com.dentalclinic.capstone.admin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.api.requestobject.AbsentRequest;

public class DatePickerAbsentDialog  extends Dialog  {

    public Activity c;
    public Dialog d;
    public TextView btnSubmit, btnCancle;
    private OnButtonClickListener onButtonClickListener;
    private CalendarView calendarView;
    private AutoCompleteTextView autoCompleteTextView ;

    public interface OnButtonClickListener {
        void onSubmitClick(AbsentRequest absentRequest);
    }

    public DatePickerAbsentDialog(Activity a) {
        super(a);
        this.c = a;
    }

    public OnButtonClickListener getOnButtonClickListener() {
        return onButtonClickListener;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_date_picker);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancle = findViewById(R.id.btn_cancle);
        calendarView = findViewById(R.id.calendarView);
        autoCompleteTextView = findViewById(R.id.edt_note);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbsentRequest absentRequest = new AbsentRequest();
                absentRequest.setCalendars(calendarView.getSelectedDates());
                absentRequest.setNote(autoCompleteTextView.getText().toString());
                onButtonClickListener.onSubmitClick(absentRequest);
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


}
