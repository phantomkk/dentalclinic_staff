package com.dentalclinic.capstone.admin.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.PaymentAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.services.PaymentService;
import com.dentalclinic.capstone.admin.dialog.CreateNewPaymentDialog;
import com.dentalclinic.capstone.admin.models.Payment;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.dentalclinic.capstone.admin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PatientPaymentActivity extends BaseActivity {

    private List<Payment> payments = new ArrayList<>();
    private ExpandableListView expandableListView;
    private PaymentAdapter adapter;
    private TextView txtMessage;
    private FloatingActionButton btnCreateNew;
    private String phone;
    private CreateNewPaymentDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }
        txtMessage = findViewById(R.id.lbl_message);
        btnCreateNew = findViewById(R.id.btn_create_new);
        expandableListView = findViewById(R.id.eplv_list_payment);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        Intent i = getIntent();
        payments = (ArrayList<Payment>) i.getSerializableExtra(PatientDetailActivity.LIST_PAYMENT);
        phone = i.getStringExtra(AppConst.PHONE);
        if (payments.isEmpty()) {
            txtMessage.setVisibility(View.VISIBLE);
        } else {
            txtMessage.setVisibility(View.GONE);
        }
        adapter = new PaymentAdapter(this, payments, expandableListView);
        expandableListView.setAdapter(adapter);
        if (Utils.isDentist(PatientPaymentActivity.this)) {
            btnCreateNew.setVisibility(View.GONE);
        }
        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!payments.isEmpty()) {
                    dialog = new CreateNewPaymentDialog(PatientPaymentActivity.this, payments.get(payments.size() - 1));
                    dialog.setListener(new CreateNewPaymentDialog.OnButtonClickListener() {
                        @Override
                        public void onSave(float money, Payment payment) {
                            CreateNewPaymentDetail(money, payment, phone);
                        }
                    });
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }else{
                    showMessage("Bệnh nhân này chưa có chi trả hoặc chưa được khám bệnh");
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public String getMainTitle() {
        return "Lịch sử chi trả";
    }

    @Override
    public void onCancelLoading() {

    }

    public void CreateNewPaymentDetail(float money, Payment payment, String phone) {
        showLoading();
        PaymentService service = APIServiceManager.getService(PaymentService.class);
        service.updatePayment(phone, CoreManager.getStaff(this).getId(), money, payment.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<Payment>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<List<Payment>> listResponse) {
                        if (listResponse.isSuccessful()) {
                            payments.clear();
                            payments.addAll(listResponse.body());
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                            showSuccessMessage("Câp nhật thanh toán thành công");
                        } else if (listResponse.code() == 500) {
                            showFatalError(listResponse.errorBody(), "logoutOnServer");
                        } else if (listResponse.code() == 401) {
                            showErrorUnAuth();
                        } else if (listResponse.code() == 400) {
                            showBadRequestError(listResponse.errorBody(), "logoutOnServer");
                        } else {
                            showDialog(getString(R.string.error_message_api));
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        hideLoading();
                    }
                });

    }
}
