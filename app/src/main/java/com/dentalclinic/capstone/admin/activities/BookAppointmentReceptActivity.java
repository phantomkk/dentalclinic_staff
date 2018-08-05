package com.dentalclinic.capstone.admin.activities;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ajithvgiri.searchdialog.OnSearchItemSelected;
import com.ajithvgiri.searchdialog.SearchListItem;
import com.ajithvgiri.searchdialog.SearchableDialog;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.requestobject.AppointmentRequest;
import com.dentalclinic.capstone.admin.api.services.AppointmentService;
import com.dentalclinic.capstone.admin.api.services.StaffService;
import com.dentalclinic.capstone.admin.fragment.SearchPatientFragment;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.models.User;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.dentalclinic.capstone.admin.utils.Validation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class BookAppointmentReceptActivity extends BaseActivity {
    private AutoCompleteTextView tvPhone;
    private AutoCompleteTextView tvFullname;
    private AutoCompleteTextView tvEstimatedTime;
    private TextView tvDate;
    private TextView tvDateError;
    private TextView tvDentist;
    private TextView tvEstimatedTimeError;
    private AutoCompleteTextView comtvNote;
    private Button btnBookAppt;
    private Button btnShowListDentist;
    private Disposable appointmentDisposable;
    private User user;
    private Patient patient;
    private boolean isDateValid = true;
    private List<SearchListItem> listItemDentist;
    private List<Staff> listDentist;
    private SearchableDialog searchDentistDialog;
    private Staff currentDentist;
    private RadioButton rbtDefault, rbtDoctor;
    private LinearLayout linearLayout;
    private Animation slideDown;
    private Animation slideUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment_recept);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(
                    ContextCompat.getDrawable(BookAppointmentReceptActivity.this, R.drawable.side_nav_bar)
            );
        }
        Intent i = getIntent();
        if (i != null) {
            patient = (Patient) i.getSerializableExtra(SearchPatientFragment.PATIENT_INFO);
            currentDentist = (Staff) i.getSerializableExtra(SearchPatientFragment.STAFF_INFO);
        }
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
//        ImageView img = findViewById(R.id.img_logo_quick_register);
        rbtDefault = findViewById(R.id.rbt_default);
        rbtDoctor = findViewById(R.id.rbt_doctor);
        linearLayout = findViewById(R.id.linear_choose_doctor);
        rbtDefault.setChecked(true);
        rbtDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout.startAnimation(slideDown);
                currentDentist = null;
            }
        });

        rbtDoctor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                linearLayout.setVisibility(View.GONE);
                linearLayout.startAnimation(slideUp);
            }
        });

        tvFullname = findViewById(R.id.tv_fullname_book_appt);
        tvDate = findViewById(R.id.tv_date_book_appt);
        tvDentist = findViewById(R.id.lbl_dentist_slt);
        tvDateError = findViewById(R.id.tv_date_error_book_appt);
        tvEstimatedTime = findViewById(R.id.tv_estimated_time);
        tvPhone = findViewById(R.id.tv_phone_book_appt);
        btnBookAppt = findViewById(R.id.btn_book_appt);
        btnShowListDentist = findViewById(R.id.btn_list_dentist);
        comtvNote = findViewById(R.id.comtv_content_book_appt);
        tvPhone.clearFocus();
        if (patient != null) {
            tvFullname.setText(patient.getName());
            tvPhone.setText(patient.getPhone());
        }
        if (listDentist == null) {
            listDentist = new ArrayList<>();
        }
        if (listItemDentist == null) {
            listItemDentist = new ArrayList<>();
        }

        callApiGetListDentist();
        searchDentistDialog = new SearchableDialog(this, listItemDentist, "Tìm kiếm nha sĩ");
        setListener();
    }

    private void setListener() {
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
                                ContextCompat.getColor(BookAppointmentReceptActivity.this, R.color.color_black)
                        );
                    }, year, month, day);
            dialog.show();
        });

        btnBookAppt.setOnClickListener((view) -> {
            if (isValidateForm()) {
                AppointmentRequest requestObj = getFormData();
                if (requestObj != null) {
                    callApi(requestObj);
                } else {
                    showWarningMessage("Error null");
                }
            }
        });
        btnShowListDentist.setOnClickListener((v) -> {
            if (searchDentistDialog != null && listDentist != null && listDentist.size() > 0) {
                searchDentistDialog.show();
            } else {
                showMessage("Danh sách răng trống");
            }
        });

        searchDentistDialog.setOnItemSelected(new OnSearchItemSelected() {
            @Override
            public void onClick(int position, SearchListItem searchListItem) {
                int dentistId = searchListItem.getId();
                tvDentist.setText(searchListItem.getTitle());
                for (Staff s : listDentist) {
                    if (s.getId() == dentistId) {
                        currentDentist = s;
                        break;
                    }
                }
            }
        });
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

    public AppointmentRequest getFormData() {
        String phone = tvPhone.getText().toString().trim();
        String dateBooking = tvDate.getText().toString().trim();
        String note = comtvNote.getText().toString().trim();
        String name = tvFullname.getText().toString().trim();
        String bookingDate = DateUtils.changeDateFormat(
                dateBooking,
                DateTimeFormat.DATE_APP,
                DateTimeFormat.DATE_TIME_DB);
        int estimatedTime = Integer.parseInt(tvEstimatedTime.getText().toString());
        logError("LOG_ABC", estimatedTime + "");
        String time = getTimeInStr(estimatedTime);
        AppointmentRequest request = new AppointmentRequest();
        request.setDate(bookingDate);
        request.setNote(note);
        request.setFullname(name);
        request.setPhone(phone);
        if (patient != null) {
            request.setPatientId(patient.getId() + "");
            logError("METHOD", "PATINET ko null" );
        }
        if (currentDentist != null) {
            request.setStaffId(currentDentist.getId() + "");
        }
        request.setEstimatedTime(time);
        return request;
    }

    private String getTimeInStr(int number) {
        String hoursInStr = "";
        String minInStr = "";
        int hour = number / 60;
        int minute = number % 60;
        if (hour < 10) {
            hoursInStr = "0" + hour;
        } else {
            hoursInStr = hour + "";
        }
        if (minute < 10) {
            minInStr = "0" + minute;
        } else {
            minInStr = minute + "";
        }
        return hoursInStr + ":" + minInStr + ":00";
    }

    public boolean isValidateForm() {
        boolean isAllFieldValid = true;
        String phone = tvPhone.getText().toString().trim();
        String note = comtvNote.getText().toString().trim();
        String txtDate = tvDate.getText().toString().trim();
        String txtEstimatedTime = tvEstimatedTime.getText().toString().trim();
        View viewFocus = null;
        int min = 0;
        if (!Validation.isPhoneValid(phone)) {
            viewFocus = tvPhone;
            tvPhone.setError(getString(R.string.error_invalid_phone));
            isAllFieldValid = false;
        } else if (Validation.isNullOrEmpty(txtDate)
                || (txtDate != null && txtDate.equals(getString(R.string.label_date_book_appt)))) {
            viewFocus = tvDateError;
            tvDateError.setText("Vui lòng chọn ngày");
            isAllFieldValid = false;
        } else if (txtEstimatedTime.trim().length() == 0) {
            tvEstimatedTime.setError("Vui lòng điền số phút ước tính");
        } else if ((Integer.parseInt(tvEstimatedTime.getText().toString().trim())) > 120) {
            tvEstimatedTime.setError("Số phút không được vượt quá 120 phút");
            isAllFieldValid = false;
            viewFocus = tvEstimatedTime;
        } else if ((Integer.parseInt(tvEstimatedTime.getText().toString().trim())) < 5) {
            tvEstimatedTime.setError("Số phút nhỏ nhất là 5 phút");
            isAllFieldValid = false;
            viewFocus = tvEstimatedTime;
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookAppointmentReceptActivity.this)
                                    .setTitle(getString(R.string.dialog_default_title))
                                    .setMessage("Đặt lịch thành công")
                                    .setPositiveButton("Xác nhận", (DialogInterface var1, int var2) -> {
                                        finish();
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
                        logError(BookAppointmentReceptActivity.class, "callApi", e.getMessage());
                        hideLoading();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void callApiGetListDentist() {
        StaffService service = APIServiceManager.getService(StaffService.class);
        Calendar c = Calendar.getInstance();
        String currentDate = DateUtils.getDate(c.getTime(), DateTimeFormat.DATE_TIME_DB_2);
        service.getAvailableDentist(currentDate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<Staff>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<List<Staff>> listResponse) {
                        if (listResponse.isSuccessful()) {
                            if (listResponse.body() != null) {
                                listDentist.addAll(listResponse.body());
                                listItemDentist.addAll(convertListDentist(listDentist));

                            }
                        } else if (listResponse.code() == 500) {
                            showFatalError(listResponse.errorBody(), "callApiGetListDentist");
                        } else if (listResponse.code() == 401) {
                            showErrorUnAuth();
                        } else if (listResponse.code() == 400) {
                            showBadRequestError(listResponse.errorBody(), "callApiGetListDentist");
                        } else {
                            showDialog(getString(R.string.error_message_api));
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showErrorMessage("Không thể kết nối đến máy chủ");
                    }
                });
    }

    private List<SearchListItem> convertListDentist(List<Staff> list) {
        List<SearchListItem> listItems = new ArrayList<>();
        for (Staff s : list) {
            listItems.add(new SearchListItem(s.getId(), s.getName()));
        }
        return listItems;
    }

}
