package com.dentalclinic.capstone.admin.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.AnamnesisCalatalogAdapter;
import com.dentalclinic.capstone.admin.adapter.MedicineAdapter;
import com.dentalclinic.capstone.admin.models.Medicine;
import com.dentalclinic.capstone.admin.models.MedicineQuantity;

import java.util.ArrayList;
import java.util.List;

public class MedicineListActivity extends Activity {
    private TextView btnSelectDone;
    private ListView listView;
    private ArrayAdapter<MedicineQuantity> adapter;
    private EditText edtSearch;

    private List<MedicineQuantity> listMedicineQuantity;
    private ArrayList<MedicineQuantity> selectedMedicineQuantity;
    private ArrayList<MedicineQuantity> currentSearchList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnSelectDone = findViewById(R.id.btn_select_done);
        listView = findViewById(R.id.list_medicine_activity);
        edtSearch = findViewById(R.id.edt_search_medicine);

        listMedicineQuantity = (ArrayList<MedicineQuantity>) getIntent().getSerializableExtra(CreateTreatmentActivity.LIST_MEDICINE);
        selectedMedicineQuantity = (ArrayList<MedicineQuantity>) getIntent().getSerializableExtra(CreateTreatmentActivity.SELECTED_MEDICINE);
        if (listMedicineQuantity == null) {
            listMedicineQuantity = new ArrayList<>();
        }
        if (selectedMedicineQuantity == null) {
            selectedMedicineQuantity = new ArrayList<>();
        } if (currentSearchList == null) {
            currentSearchList = new ArrayList<>();
            currentSearchList.addAll(listMedicineQuantity);
        }

        adapter = new MedicineAdapter(
                this,
                currentSearchList,
                selectedMedicineQuantity
        );
        listView.setAdapter(adapter);

        btnSelectDone.setOnClickListener((v) -> {
            Intent intent = new Intent();
            intent.putExtra(CreateTreatmentActivity.SELECTED_MEDICINE, selectedMedicineQuantity);
            setResult(RESULT_OK, intent);
            finish();
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
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
                    adapter.notifyDataSetChanged();
            }
        });
    }

    public void search(String keyword){
        currentSearchList.clear();
        for (MedicineQuantity mq : listMedicineQuantity) {
            if (mq.getMedicine().getName().contains(keyword)) {
                currentSearchList.add(mq);
            }
        }
    }

}
