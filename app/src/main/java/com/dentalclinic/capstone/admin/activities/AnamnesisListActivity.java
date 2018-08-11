package com.dentalclinic.capstone.admin.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.AnamnesisCalatalogAdapter;
import com.dentalclinic.capstone.admin.models.AnamnesisCatalog;

import java.util.ArrayList;
import java.util.List;

public class AnamnesisListActivity extends Activity {
    private TextView txtSelectDone;
    private TextView txtSelectCancel;
    private ListView listView;
    private EditText edtSearchAnamnesis;
    private ArrayAdapter<AnamnesisCatalog> anamnesisAdapter;

    private List<AnamnesisCatalog> listTotalAnamnesis;
    private List<AnamnesisCatalog> listCurrentAnamnesis;
    private ArrayList<AnamnesisCatalog> patientAnamnesises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamnesis_list);
        txtSelectDone = findViewById(R.id.btn_select_done);
        txtSelectDone = findViewById(R.id.btn_select_done);
        txtSelectCancel = findViewById(R.id.btn_select_cancel);
        edtSearchAnamnesis = findViewById(R.id.edt_search_anamnesis);
        listView = findViewById(R.id.list_anamnesis_activity);
        listTotalAnamnesis = (ArrayList<AnamnesisCatalog>) getIntent().getSerializableExtra(CreatePatientActivity.LIST_ANAMNESIS);
        patientAnamnesises = (ArrayList<AnamnesisCatalog>) getIntent().getSerializableExtra(CreatePatientActivity.PREVIOUS_ANAMNESIS);
        if (patientAnamnesises == null) {
            patientAnamnesises = new ArrayList<>();
        }
        if (listCurrentAnamnesis == null) {
            listCurrentAnamnesis = new ArrayList<>();
            listCurrentAnamnesis.addAll(listTotalAnamnesis);
        }
        anamnesisAdapter = new AnamnesisCalatalogAdapter(
                this,
                listCurrentAnamnesis,
                patientAnamnesises
        );
        listView.setAdapter(anamnesisAdapter);
        edtSearchAnamnesis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyword = editable.toString();
                search(keyword);
                anamnesisAdapter.notifyDataSetChanged();
            }
        });
        txtSelectDone.setOnClickListener((v) -> {
            Intent intent = new Intent();
            intent.putExtra(CreatePatientActivity.PATIENT_ANAMNESIS, patientAnamnesises);
            setResult(RESULT_OK, intent);
            finish();
        });
        txtSelectCancel.setOnClickListener((v) -> {
            finish();
        });
    }

    public void search(String keyword) {
        listCurrentAnamnesis.clear();
        for (AnamnesisCatalog catalog : listTotalAnamnesis) {
            if (catalog.getName().toUpperCase().contains(keyword.toUpperCase())) {
                listCurrentAnamnesis.add(catalog);
            }
        }
    }

}
