package com.dentalclinic.capstone.admin.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.TreatmentStepAdapter;
import com.dentalclinic.capstone.admin.models.TreatmentStep;

import java.util.ArrayList;
import java.util.List;

public class StepListActivity extends Activity {
    private TextView btnSelectDone;
    private TextView btnSelectCancel;
    private ListView listView;
    private ArrayAdapter<TreatmentStep> stepAdapter;

    private ArrayList<TreatmentStep> treatmentSteps;
    private ArrayList<TreatmentStep> currentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list_step);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnSelectDone = findViewById(R.id.btn_select_done);
        btnSelectCancel = findViewById(R.id.btn_select_cancel);
        listView = findViewById(R.id.list_step_activity);
        treatmentSteps = (ArrayList<TreatmentStep>) getIntent().getSerializableExtra(CreateTreatmentActivity.LIST_STEP);
        currentStep = (ArrayList<TreatmentStep>) getIntent().getSerializableExtra(CreateTreatmentActivity.CURRENT_STEP);
        if(currentStep ==null){
            currentStep = new ArrayList<>();
        }
        stepAdapter = new TreatmentStepAdapter(this, treatmentSteps, new TreatmentStepAdapter.OnItemClickListener() {
            @Override
            public void onCheck(int position, boolean isCheck) {
                treatmentSteps.get(position).setCheck(isCheck);
            }
        });


        listView.setAdapter(stepAdapter);

        btnSelectDone.setOnClickListener((v) -> {
            Intent intent = new Intent();
            intent.putExtra(CreateTreatmentActivity.CURRENT_STEP, treatmentSteps);
            setResult(RESULT_OK, intent);
            finish();
        });btnSelectCancel.setOnClickListener((v) -> {
            finish();
        });
    }


}
