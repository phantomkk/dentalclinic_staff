package com.dentalclinic.capstone.admin.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.AnamnesisCalatalogAdapter;
import com.dentalclinic.capstone.admin.models.AnamnesisCatalog;

import java.util.ArrayList;
import java.util.List;

public class AnamnesisListActivity extends Activity {
    private Button btnSelectDone;
    private ListView listView;
    private ArrayAdapter<AnamnesisCatalog> anamnesisAdapter;

    private List<AnamnesisCatalog> listAnamnesisCatalog;
    private ArrayList<AnamnesisCatalog> patientAnamnesises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamnesis_list);
        btnSelectDone = findViewById(R.id.btn_select_done);
        listView = findViewById(R.id.list_anamnesis_activity);
        listAnamnesisCatalog = (ArrayList<AnamnesisCatalog>) getIntent().getSerializableExtra(CreatePatientActivity.LIST_ANAMNESIS);
        patientAnamnesises = (ArrayList<AnamnesisCatalog>) getIntent().getSerializableExtra(CreatePatientActivity.PREVIOUS_ANAMNESIS);
        if(patientAnamnesises==null){
            patientAnamnesises = new ArrayList<>();
        }
        anamnesisAdapter = new AnamnesisCalatalogAdapter(
                this,
                listAnamnesisCatalog,
                patientAnamnesises
                );
        listView.setAdapter(anamnesisAdapter);

        btnSelectDone.setOnClickListener((v) -> {
            Intent intent = new Intent();
            intent.putExtra(CreatePatientActivity.PATIENT_ANAMNESIS, patientAnamnesises);
            setResult(RESULT_OK, intent);
            finish();
        });
    }


}
