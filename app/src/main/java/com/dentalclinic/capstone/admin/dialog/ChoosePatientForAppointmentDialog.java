package com.dentalclinic.capstone.admin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.activities.CreatePatientActivity;
import com.dentalclinic.capstone.admin.adapter.PatientAdapter;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.Payment;
import com.dentalclinic.capstone.admin.utils.AppConst;

import java.util.List;

public class ChoosePatientForAppointmentDialog extends Dialog {
    public Activity c;
    public Dialog d;
    private List<Patient> patients;
    private ListView listView;
    private TextView btnClose, btnCreateNewPatient;
    private PatientAdapter adapter;
    private ChoosePatientForAppointmentDialog.OnButtonClickListener listener;

    public interface OnButtonClickListener {
//        void onSave(float money, Payment payment);
        void onItemClick(Patient patient);
        void onCreatePatientClick();
    }

    public OnButtonClickListener getListener() {
        return listener;
    }

    public void setListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    public ChoosePatientForAppointmentDialog(Activity a, List<Patient> patients) {
        super(a);
        this.c = a;
        this.patients = patients;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choose_patient_for_appointment);
        listView = findViewById(R.id.list_patient);
        btnClose = findViewById(R.id.btn_close);
        btnCreateNewPatient = findViewById(R.id.btn_create_new_patient);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnCreateNewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCreatePatientClick();
//                Intent intent = new Intent(c, CreatePatientActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(AppConst.PHONE, patients.get(0).getPhone());
//                intent.putExtra(AppConst.BUNDLE, bundle);
//                c.startActivityForResult(intent,AppConst.REQUEST_CREATENEW_PATIENT);
            }
        });
        adapter = new PatientAdapter(c,patients);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onItemClick(patients.get(i));
            }
        });



    }
}
