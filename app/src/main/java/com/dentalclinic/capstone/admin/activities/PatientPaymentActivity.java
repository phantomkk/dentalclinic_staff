package com.dentalclinic.capstone.admin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.PaymentAdapter;
import com.dentalclinic.capstone.admin.models.Payment;

import java.util.ArrayList;
import java.util.List;

public class PatientPaymentActivity extends BaseActivity {

    private List<Payment> payments = new ArrayList<>();
    private ExpandableListView expandableListView;
    private PaymentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_payment);
        expandableListView = findViewById(R.id.eplv_list_payment);
    }

    @Override
    public String getMainTitle() {
        return "Lịch sử chi trả";
    }

    @Override
    public void onCancelLoading() {

    }
}
