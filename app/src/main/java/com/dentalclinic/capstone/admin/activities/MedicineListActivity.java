package com.dentalclinic.capstone.admin.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.AnamnesisCalatalogAdapter;
import com.dentalclinic.capstone.admin.adapter.MedicineAdapter;
import com.dentalclinic.capstone.admin.models.MedicineQuantity;

import java.util.ArrayList;
import java.util.List;

public class MedicineListActivity extends Activity {
    private Button btnSelectDone;
    private ListView listView;
    private ArrayAdapter<MedicineQuantity> adapter;

    private List<MedicineQuantity> listMedicineQuantity;
    private ArrayList<MedicineQuantity> selectedMedicineQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamnesis_list);
        btnSelectDone = findViewById(R.id.btn_select_done);
        listView = findViewById(R.id.list_anamnesis_activity);
        listMedicineQuantity = (ArrayList<MedicineQuantity>) getIntent().getSerializableExtra(CreateTreatmentActivity.LIST_MEDICINE);
        selectedMedicineQuantity = (ArrayList<MedicineQuantity>) getIntent().getSerializableExtra(CreateTreatmentActivity.SELECTED_MEDICINE);
        if (listMedicineQuantity == null) {
            listMedicineQuantity = new ArrayList<>();
        }
        if (selectedMedicineQuantity == null) {
            selectedMedicineQuantity = new ArrayList<>();
        }

        adapter = new MedicineAdapter(
                this,
                listMedicineQuantity,
                selectedMedicineQuantity
        );
        listView.setAdapter(adapter);

        btnSelectDone.setOnClickListener((v) -> {
            Intent intent = new Intent();
            intent.putExtra(CreateTreatmentActivity.SELECTED_MEDICINE, selectedMedicineQuantity);
            setResult(RESULT_OK, intent);
            finish();
        });
    }


}
