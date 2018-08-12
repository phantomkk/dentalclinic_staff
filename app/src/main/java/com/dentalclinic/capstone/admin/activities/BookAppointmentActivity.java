package com.dentalclinic.capstone.admin.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.requestobject.AppointmentRequest;
import com.dentalclinic.capstone.admin.api.services.AppointmentService;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.User;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.dentalclinic.capstone.admin.utils.Validation;

import java.util.Calendar;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class BookAppointmentActivity extends BaseActivity {
    private AutoCompleteTextView tvPhone;
    private AutoCompleteTextView tvFullname;
    private AutoCompleteTextView tvEstimatedTime;
    private TextView tvDate;
    private TextView tvDateError;
    private TextView tvEstimatedTimeError;
    private AutoCompleteTextView comtvNote;
    private Button btnbook_appt;
    private Disposable appointmentDisposable;
    private User user;
    private Patient patient;
    private boolean isDateValid = true;
    private int pos = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(
                    ContextCompat.getDrawable(BookAppointmentActivity.this, R.drawable.side_nav_bar)
            );
        }
        Bundle bundle = getIntent().getBundleExtra(AppConst.BUNDLE);
        if(bundle!=null){
            pos = bundle.getInt("POSITION_APPOINTMENT");
            patient = (Patient) bundle.getSerializable(AppConst.PATIENT_OBJ);
        }
//        ImageView img = findViewById(R.id.img_logo_quick_register);
        tvFullname = findViewById(R.id.tv_fullname_book_appt);
        tvDate = findViewById(R.id.tv_date_book_appt);
        tvDateError = findViewById(R.id.tv_date_error_book_appt);
        tvEstimatedTime = findViewById(R.id.tv_estimated_time);
        tvPhone = findViewById(R.id.tv_phone_book_appt);
        btnbook_appt = findViewById(R.id.btn_book_appt);
        comtvNote = findViewById(R.id.comtv_content_book_appt);
        tvPhone.clearFocus();
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Calendar currentDay = Calendar.getInstance();
        tvDate.setOnClickListener((view) ->
        {
            DatePickerDialog dialog = new DatePickerDialog(this,
                    (DatePicker datePicker, int iYear, int iMonth, int iDay) -> {
                        String date = iDay + "/" + (iMonth + 1) + "/" + iYear;
                        c.set(iYear, iMonth, iDay, 23, 59);
                        if (currentDay.after(c)) {
                            tvDateError.setText(getString(R.string.label_error_appnt_date));
                            isDateValid = false;
                        } else {
                            tvDateError.setText("");
                            isDateValid = true;
                        }
                        tvDate.setText(DateUtils.getDate(c.getTime(), DateTimeFormat.DATE_APP));
                        tvDate.setTextColor(
                                ContextCompat.getColor(BookAppointmentActivity.this, R.color.color_black)
                        );
                    }, year, month, day);
            dialog.setButton(DatePickerDialog.BUTTON_POSITIVE,getString(R.string.OK), dialog);
            dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.Cancel), (DialogInterface.OnClickListener)null);

            dialog.show();
        });
        Calendar cTime = Calendar.getInstance();
        tvEstimatedTime.setOnClickListener((v) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (TimePicker timePicker, int iHours, int iMinute) -> {
                        Calendar curentDay = Calendar.getInstance();
                        cTime.set(Calendar.HOUR, iHours);
                        cTime.set(Calendar.MINUTE, iMinute);
                        if (cTime.before(curentDay)) {
                            tvEstimatedTimeError.setText("Vui lòng chọn thời gian");
                        }else {
                            tvEstimatedTimeError.setText("");
                            String timeStr = iHours + ":" + iMinute;
                            tvEstimatedTime.setText(timeStr);
                        }
                    },
                    cTime.get(Calendar.HOUR),
                    cTime.get(Calendar.MINUTE),
                    true);
            timePickerDialog.show();
        });


        btnbook_appt.setOnClickListener((view) -> {
            if (isValidateForm()) {
                AppointmentRequest requestObj = getFormData();
                if (requestObj != null) {
                    callApi(requestObj);
                } else {
                    showWarningMessage("Error null");
                }
            }
        });
        setData();

    }

    @Override
    public String getMainTitle() {
        return "Đặt lịch";
    }

    @Override
    public void onCancelLoading() {
        if (appointmentDisposable != null) {
            appointmentDisposable.dispose();
        }
    }

    private void setData(){
        if(patient!=null){
            if(patient.getName()!=null){
                tvFullname.setText(patient.getName());
                tvFullname.setEnabled(false);
            }
            if(patient.getPhone()!=null){
                tvPhone.setText(patient.getPhone());
                tvPhone.setEnabled(false);
            }
        }
    }

    public AppointmentRequest getFormData() {
//        User user = CoreManager.getUser(this);
//        if (user != null) {
//            String phone = user.getPhone();
        String phone = tvPhone.getText().toString().trim();
        String dateBooking = tvDate.getText().toString().trim();
        String note = comtvNote.getText().toString().trim();
        String name = tvFullname.getText().toString().trim();
        String bookingDate = DateUtils.changeDateFormat(
                dateBooking,
                DateTimeFormat.DATE_APP,
                DateTimeFormat.DATE_TIME_DB);
        AppointmentRequest request = new AppointmentRequest();
        request.setStaffId(CoreManager.getStaff(BookAppointmentActivity.this).getId()+"");
        request.setDate(bookingDate);
        request.setNote(note);
        request.setFullname(name);
        request.setPhone(phone);
        if(patient !=null){
            if(patient.getId()!=-1){
                request.setPatientId(patient.getId()+"");
            }
        }
        return request;
//        }
//        return null;
    }

    public boolean isValidateForm() {
        boolean isAllFieldValid = true;
        String phone = tvPhone.getText().toString().trim();
        String note = comtvNote.getText().toString().trim();
        String txtDate = tvDate.getText().toString().trim();
        View viewFocus = null;
        if (!Validation.isPhoneValid(phone)) {
            viewFocus = tvPhone;
            tvPhone.setError(getString(R.string.error_invalid_phone));
            isAllFieldValid = false;
        } else if (Validation.isNullOrEmpty(txtDate)
                || (txtDate != null && txtDate.equals(getString(R.string.label_date_book_appt)))) {
            viewFocus = tvDateError;
            tvDateError.setText("Vui lòng chọn ngày");
            isAllFieldValid = false;
        }
        if (!isAllFieldValid) {
            viewFocus.requestFocus();
        }
        return isAllFieldValid && isDateValid;
    }

    public void callApi(AppointmentRequest requestObj) {
        showLoading();
        AppointmentService appointmentService =
                APIServiceManager.getService(AppointmentService.class);
        appointmentService.bookAppointment(requestObj)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<Appointment>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        appointmentDisposable = d;
                    }

                    @Override
                    public void onSuccess(Response<List<Appointment>> response) {
                        if (response.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookAppointmentActivity.this)
                                    .setTitle(getString(R.string.dialog_default_title))
                                    .setMessage("Đặt lịch thành công")
                                    .setPositiveButton("Xác nhận", (DialogInterface var1, int var2) -> {
                                        setResult(Activity.RESULT_OK,
                                                new Intent().putExtra("POSITION_APPOINTMENT", pos));
                                        finish();
//                                        finish();
                                    });
                            builder.create().show();
                        } else if (response.code() == 500) {
                            showFatalError(response.errorBody(), "appointmentService");
                        } else if (response.code() == 401) {
                            showErrorUnAuth();
                        } else if (response.code() == 400) {
                            showBadRequestError(response.errorBody(), "appointmentService");
                        } else {
                            showErrorMessage(getString(R.string.error_on_error_when_call_api));
                        }

                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        e.printStackTrace();
                        showWarningMessage(getResources().getString(R.string.error_on_error_when_call_api));
                        logError(BookAppointmentActivity.class, "callApi", e.getMessage());
                        hideLoading();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
