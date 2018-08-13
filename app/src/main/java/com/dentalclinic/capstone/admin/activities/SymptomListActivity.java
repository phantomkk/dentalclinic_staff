package com.dentalclinic.capstone.admin.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.SymptomAdapter;
import com.dentalclinic.capstone.admin.models.Symptom;

import java.util.ArrayList;
import java.util.List;

public class SymptomListActivity extends Activity {

    private TextView btnSelectDone;
    private TextView btnSelectCancel;
    private ListView listView;
    private EditText edtSearchSymptom;
    private ArrayAdapter<Symptom> SymptomAdapter;

    private List<Symptom> listTotalSymptom;
    private List<Symptom> listCurrentSymptom;
    private ArrayList<Symptom> patientSymptomes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_list);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnSelectDone = findViewById(R.id.btn_select_done);
        btnSelectDone = findViewById(R.id.btn_select_done);
        btnSelectCancel = findViewById(R.id.btn_select_cancel);
        edtSearchSymptom = findViewById(R.id.edt_search_symptom);
        listView = findViewById(R.id.list_symptom_activity);
        listTotalSymptom = (ArrayList<Symptom>) getIntent().getSerializableExtra(CreateTreatmentActivity.LIST_SYMPTOM);
        patientSymptomes = (ArrayList<Symptom>) getIntent().getSerializableExtra(CreateTreatmentActivity.SELECTED_SYMPTOM);
        if (patientSymptomes == null) {
            patientSymptomes = new ArrayList<>();
        }
        if (listCurrentSymptom == null) {
            listCurrentSymptom = new ArrayList<>();
            listCurrentSymptom.addAll(listTotalSymptom);
        }
        SymptomAdapter = new SymptomAdapter(
                this,
                listCurrentSymptom,
                patientSymptomes
        );
        listView.setAdapter(SymptomAdapter);
        edtSearchSymptom.addTextChangedListener(new TextWatcher() {
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
                SymptomAdapter.notifyDataSetChanged();
            }
        });
        btnSelectDone.setOnClickListener((v) -> {
            Intent intent = new Intent();
            intent.putExtra(CreateTreatmentActivity.SELECTED_SYMPTOM, patientSymptomes);
            setResult(RESULT_OK, intent);
            finish();
        });
        btnSelectCancel.setOnClickListener((v) -> {
            finish();
        });
    }

    public void search(String keyword) {
        listCurrentSymptom.clear();
        for (Symptom catalog : listTotalSymptom) {
            if (catalog.getName().toUpperCase().contains(keyword.toUpperCase())) {
                listCurrentSymptom.add(catalog);
            }
        }
    }

}
