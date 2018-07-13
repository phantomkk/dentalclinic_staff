package com.dentalclinic.capstone.admin.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent i = getIntent();
        payments = (ArrayList<Payment>) i.getSerializableExtra(PatientDetailActivity.LIST_PAYMENT);
        adapter = new PaymentAdapter(this, payments);
        expandableListView.setAdapter(adapter);

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
}
