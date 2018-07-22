package com.dentalclinic.capstone.admin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.api.requestobject.AbsentRequest;
import com.dentalclinic.capstone.admin.api.requestobject.ReqAbsentRequest;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.Calendar;

public class DatePickerAbsentDialog extends Dialog {

    public Activity c;
    public Dialog d;
    public TextView btnSubmit, btnCancle;
    private OnButtonClickListener onButtonClickListener;
    private CalendarView calendarView;
    private AutoCompleteTextView autoCompleteTextView;

    public interface OnButtonClickListener {
        void onSubmitClick(ReqAbsentRequest request);
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
        Calendar calendar = Calendar.getInstance();
        calendarView.setSoundEffectsEnabled(true);
        try {
            calendarView.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
        autoCompleteTextView = findViewById(R.id.edt_note);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()){
                    ReqAbsentRequest absentRequest = new ReqAbsentRequest();
                    absentRequest.setStartDate(DateUtils.getDate(calendarView.getSelectedDates().get(0).getTime(), DateTimeFormat.DATE_TIME_DB));
                    absentRequest.setEndDate(DateUtils.getDate(calendarView.getSelectedDates().get(calendarView.getSelectedDates().size() - 1).getTime(), DateTimeFormat.DATE_TIME_DB));
                    absentRequest.setReason(autoCompleteTextView.getText().toString());
                    onButtonClickListener.onSubmitClick(absentRequest);
                }
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


    private String errorMessage="Đã có lỗi xảy ra";
    private boolean isValid(){
        boolean rs = true;
        if(calendarView.getSelectedDates().isEmpty()){
            rs =false;
            StyleableToast.makeText(getContext(), "Vui lòng chọn ngày nghỉ", Toast.LENGTH_LONG, R.style.warningToast).show();
        }else if(calendarView.getSelectedDates().get(0).before(Calendar.getInstance())) {
            rs =false;
            StyleableToast.makeText(getContext(), "Vui lòng chọn ngày nghỉ trong tương lai", Toast.LENGTH_LONG, R.style.warningToast).show();
        }else if(autoCompleteTextView.getText().toString().trim().isEmpty()){
            rs = false;
            autoCompleteTextView.setError("Vui lòng điền lý do xin nghĩ");
            autoCompleteTextView.setFocusable(true);
        }

        return rs;
    }
    public boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null)
            return false;
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

}
