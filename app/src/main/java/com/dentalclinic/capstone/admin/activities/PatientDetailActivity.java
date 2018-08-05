package com.dentalclinic.capstone.admin.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.services.HistoryTreatmentService;
import com.dentalclinic.capstone.admin.api.services.PaymentService;
import com.dentalclinic.capstone.admin.models.Event;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.Payment;
import com.dentalclinic.capstone.admin.models.Tooth;
import com.dentalclinic.capstone.admin.models.Treatment;
import com.dentalclinic.capstone.admin.models.TreatmentHistory;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.dentalclinic.capstone.admin.utils.GenderUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PatientDetailActivity extends BaseActivity implements View.OnClickListener {
    private CircleImageView cvAvatar;
    private Button btnViewTreatment;
    private Button btnViewPayment;
    private Button btnEditPatient;
    private TextView txtName, txtGender, txtPhone, txtAddress, txtDateOfBirth;
    private Patient patient;
    public static String LIST_TREATMENT = "LIST_TREATMENT";
    public static String LIST_PAYMENT = "LIST_PAYMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }

        btnViewPayment = findViewById(R.id.btn_view_payment);
        btnViewTreatment = findViewById(R.id.btn_view_treatment);
        btnEditPatient = findViewById(R.id.btn_edit_patient);
        cvAvatar = findViewById(R.id.img_avatar_user);
        txtName = findViewById(R.id.txt_name);
        txtDateOfBirth = findViewById(R.id.txt_date_of_birth);
        txtGender = findViewById(R.id.txt_gender);
        txtPhone = findViewById(R.id.txt_phone);
        txtAddress = findViewById(R.id.txt_address);
        setListenter();
        Bundle bundle = getIntent().getBundleExtra(AppConst.BUNDLE);
        if (bundle != null) {
            patient = (Patient) bundle.getSerializable(AppConst.PATIENT_OBJ);
            if (patient != null) {
                setData(patient);
            }
        }
    }

    public void dummyData(ArrayList<TreatmentHistory> treatmentHistories) {
        TreatmentHistory treatmentHistory = new TreatmentHistory();
        treatmentHistory.setTreatment(new Treatment("Trám Răng", new Event(10)));
        treatmentHistory.setTooth(new Tooth("răng cửa"));
        treatmentHistory.setTotalPrice(Long.valueOf("100000"));
        treatmentHistory.setPrice(Long.valueOf("100000"));
        String dtStart3 = "1996-06-30 00:00:00";
        treatmentHistory.setCreateDate(dtStart3);
        treatmentHistory.setFinishDate(dtStart3);
//        treatmentHistory.setCreateDate(DateUtils.changeDateFormat(dtStart3, DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_APP_2));
//        treatmentHistory.setFinishDate(DateUtils.changeDateFormat(dtStart3, DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_APP_2));
        treatmentHistories.add(treatmentHistory);
        treatmentHistories.add(treatmentHistory);
    }

    public void setListenter() {
        btnViewTreatment.setOnClickListener(view -> {
            if (patient != null) {
//                ArrayList<TreatmentHistory> treatmentHistories = (ArrayList<TreatmentHistory>) patient.getTreatmentHistories();
////            ArrayList<TreatmentHistory> treatmentHistories = new ArrayList<TreatmentHistory>();
//            ///testttttttt
////            dummyData(treatmentHistories);
//            if (treatmentHistories != null && treatmentHistories.size() > 0) {
//                Intent intent = new Intent(PatientDetailActivity.this, PatientTreatmentActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(LIST_TREATMENT, treatmentHistories);
//                intent.putExtra(AppConst.BUNDLE, bundle);
//
//                startActivity(intent);
//            } else {
//                showMessage("Lịch sử điều trị");
//            }
//            }

                HistoryTreatmentService service = APIServiceManager.getService(HistoryTreatmentService.class);
                service.getHistoryTreatmentById(patient.getId())
                        .subscribeOn(Schedulers.newThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Response<List<TreatmentHistory>>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Response<List<TreatmentHistory>> listResponse) {
                                if (listResponse.isSuccessful()) {
                                    if (listResponse.body() != null) {
                                        Intent intent = new Intent(PatientDetailActivity.this, PatientTreatmentActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable(LIST_TREATMENT, (ArrayList<TreatmentHistory>) listResponse.body());
                                        intent.putExtra(AppConst.BUNDLE, bundle);
                                        startActivity(intent);
                                    }
                                } else if (listResponse.code() == 500) {
                                    showFatalError(listResponse.errorBody(), "callApi");
                                } else if (listResponse.code() == 401) {
                                    showErrorUnAuth();
                                } else if (listResponse.code() == 400) {
                                    showBadRequestError(listResponse.errorBody(), "callApi");
                                } else {
                                    showDialog(getString(R.string.error_message_api));
                                }
                                hideLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                showWarningMessage(getResources().getString(R.string.error_on_error_when_call_api));
                                hideLoading();
                            }
                        });

            }
        });
        btnViewPayment.setOnClickListener(view -> {
            showLoading();
            if (patient != null) {
                PaymentService paymentService = APIServiceManager.getService(PaymentService.class);
                paymentService.getByPhone(patient.getPhone())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Response<List<Payment>>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Response<List<Payment>> listResponse) {
                                ArrayList<Payment> list = (ArrayList<Payment>) listResponse.body();
                                if (list != null) {
                                    Intent intent = new Intent(PatientDetailActivity.this, PatientPaymentActivity.class);
                                    intent.putExtra(LIST_PAYMENT, list);
                                    intent.putExtra(AppConst.PHONE, txtPhone.getText());
                                    startActivity(intent);
                                } else if (listResponse.code() == 500) {
                                    showFatalError(listResponse.errorBody(), "prepareData");
                                } else if (listResponse.code() == 401) {
                                    showErrorUnAuth();
                                } else if (listResponse.code() == 400) {
                                    showBadRequestError(listResponse.errorBody(), "prepareData");
                                }
                                hideLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                showErrorMessage("Không thể kết nối đến server");
                                hideLoading();
                            }
                        });
//                List<Payment> listPayment = new ArrayList<>();
//                for (TreatmentHistory t : treatmentHistories) {
//
//                }
            }
        });
        btnEditPatient.setOnClickListener(view->{
            Intent intent = new Intent(PatientDetailActivity.this, EditPatientActivity.class);
            intent.putExtra(AppConst.PATIENT_OBJ, patient);
            startActivity(intent);
        });
    }

    private void callApiPatient(int patientId) {

    }

    private void setData(Patient patient) {
        if (patient != null) {
            if (patient.getAvatar() != null && patient.getAvatar().trim().length() > 0) {
//                Picasso.get().invalidate(patient.getAvatar());
                Picasso.get().load(patient.getAvatar()).into(cvAvatar);
            }
            if (patient.getName() != null) {
                txtName.setText(patient.getName());
            }
            if (patient.getDateOfBirth() != null) {
                txtDateOfBirth.setText(DateUtils.changeDateFormat(patient.getDateOfBirth(), DateTimeFormat.DATE_TIME_DB_2, DateTimeFormat.DATE_APP));
            }
            if (patient.getGender() != null) {
                txtGender.setText(GenderUtils.toString(patient.getGender()));
            }
            if (patient.getPhone() != null) {
                txtPhone.setText(patient.getPhone());
            }
            if (patient.getAddress() != null) {
                String address = patient.getAddress();
                if (patient.getDistrict() != null) {
                    address += ", " + patient.getDistrict().getName();
                    if (patient.getCity() != null) {
                        address += ", " + patient.getCity().getName();
                    }
                }
                txtAddress.setText(address);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public String getMainTitle() {
        return "Thông tin bệnh nhân";
    }

    @Override
    public void onCancelLoading() {
        showMessage("Bạn đã hủy");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

        }
    }
}
