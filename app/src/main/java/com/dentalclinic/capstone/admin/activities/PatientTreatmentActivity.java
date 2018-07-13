package com.dentalclinic.capstone.admin.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.TreatmentHistoryAdapter;
import com.dentalclinic.capstone.admin.models.Event;
import com.dentalclinic.capstone.admin.models.Tooth;
import com.dentalclinic.capstone.admin.models.Treatment;
import com.dentalclinic.capstone.admin.models.TreatmentHistory;
import com.dentalclinic.capstone.admin.utils.AppConst;

import java.io.Serializable;
import java.util.ArrayList;

public class PatientTreatmentActivity extends BaseActivity {
    ArrayList<TreatmentHistory> treatmentHistories;
    public ListView listView;
    public TreatmentHistoryAdapter adapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_treatment);
        listView = findViewById(R.id.list_treatment);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent i = getIntent();
        if(treatmentHistories == null){
            treatmentHistories = new ArrayList<>();
        }
        adapter = new TreatmentHistoryAdapter(PatientTreatmentActivity.this, treatmentHistories);
        listView.setAdapter(adapter);
        if (i != null && i.getBundleExtra(AppConst.BUNDLE) != null) {
            Bundle bundle = i.getBundleExtra(AppConst.BUNDLE);
            Serializable serializable = bundle.getSerializable(PatientDetailActivity.LIST_TREATMENT);
            treatmentHistories.addAll((ArrayList<TreatmentHistory>) serializable);
            adapter.notifyDataSetChanged();

        }
    }


    @Override
    public String getMainTitle() {
        return "Lịch sử khám";
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
