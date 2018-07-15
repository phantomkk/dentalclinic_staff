package com.dentalclinic.capstone.admin.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.PatientAdapter;
import com.dentalclinic.capstone.admin.adapter.TreatmentHistoryAdapter;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.TreatmentHistory;

import java.util.ArrayList;
import java.util.List;

public class ShowTreatmentHistoryActivity extends BaseActivity{
    private List<TreatmentHistory> treatmentHistories = new ArrayList<>();
    private TreatmentHistoryAdapter mAdapter;
    private ListView mListView;
    private TextView textView;
    private FloatingActionButton btnAddNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_treatment_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }
        prepareData();
        textView = findViewById(R.id.txt_label_message);
        btnAddNew = findViewById(R.id.btn_actions);
        mListView = findViewById(R.id.listView);
        mAdapter = new TreatmentHistoryAdapter(this, treatmentHistories);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showMessage(i+"");
            }
        });

    }

    @Override
    public String getMainTitle() {
        return "Lịch Sử Khám Bệnh";
    }

    @Override
    public void onCancelLoading() {

    }

    private void prepareData(){
        treatmentHistories.add(new TreatmentHistory());
        treatmentHistories.add(new TreatmentHistory());
        treatmentHistories.add(new TreatmentHistory());
        treatmentHistories.add(new TreatmentHistory());
        treatmentHistories.add(new TreatmentHistory());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
