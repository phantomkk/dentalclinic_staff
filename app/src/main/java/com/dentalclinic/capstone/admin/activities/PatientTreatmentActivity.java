package com.dentalclinic.capstone.admin.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.TreatmentHistoryAdapter;
import com.dentalclinic.capstone.admin.models.Event;
import com.dentalclinic.capstone.admin.models.Tooth;
import com.dentalclinic.capstone.admin.models.Treatment;
import com.dentalclinic.capstone.admin.models.TreatmentDetail;
import com.dentalclinic.capstone.admin.models.TreatmentHistory;
import com.dentalclinic.capstone.admin.utils.AppConst;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<TreatmentDetail> details = treatmentHistories.get(i).getTreatmentDetails();
                if (details != null) {
                    if (!details.isEmpty()) {
                        Intent intent = new Intent(PatientTreatmentActivity.this, TreatmentDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConst.TREATMENT_HISTORY_OBJ, treatmentHistories.get(i));
                        intent.putExtra(AppConst.BUNDLE, bundle);
                        startActivity(intent);
                    }
                }
            }
        });
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
