package com.dentalclinic.capstone.admin.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.TreatmentStepAdapter;
import com.dentalclinic.capstone.admin.models.TreatmentStep;

import java.util.ArrayList;
import java.util.List;

public class StepListActivity extends Activity {
    private Button btnSelectDone;
    private ListView listView;
    private ArrayAdapter<TreatmentStep> stepAdapter;

    private List<TreatmentStep> treatmentSteps;
    private ArrayList<TreatmentStep> currentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_step);
        btnSelectDone = findViewById(R.id.btn_select_done);
        listView = findViewById(R.id.list_step_activity);
        treatmentSteps = (ArrayList<TreatmentStep>) getIntent().getSerializableExtra(CreateTreatmentActivity.LIST_STEP);
        currentStep = (ArrayList<TreatmentStep>) getIntent().getSerializableExtra(CreateTreatmentActivity.PREVIOUS_STEP);
        if(currentStep ==null){
            currentStep = new ArrayList<>();
        }
        stepAdapter = new TreatmentStepAdapter(
                this,
                treatmentSteps,
                currentStep
                );
        listView.setAdapter(stepAdapter);

        btnSelectDone.setOnClickListener((v) -> {
            Intent intent = new Intent();
            intent.putExtra(CreateTreatmentActivity.PREVIOUS_STEP, currentStep);
            setResult(RESULT_OK, intent);
            finish();
        });
    }


}
