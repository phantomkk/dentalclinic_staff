package com.dentalclinic.capstone.admin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.activities.PatientDetailActivity;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppointmentDetailDialog extends Dialog {
    public Activity c;
    public Dialog d;
    private Appointment appointment;
    private TextView txtPatientName, txtAppointmentName, txtDentistName, txtApppointmentNumber, txtStartTime, txtNote, btnClose;
    private CircleImageView imgPatientAvatar, imgDentistAvatar;
    private LinearLayout patientBlock, appointmentNameBlock;
    public AppointmentDetailDialog(Activity a, Appointment appointment) {
        super(a);
        this.c = a;
        this.appointment = appointment;
    }


    private String prePrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_appointment_detail);
        txtPatientName = findViewById(R.id.txt_name);
        txtAppointmentName = findViewById(R.id.txt_patient_name);
        txtDentistName = findViewById(R.id.txt_dentist_name);
        txtApppointmentNumber = findViewById(R.id.txt_appointment_number);
        txtStartTime = findViewById(R.id.txt_start_time);
        txtNote = findViewById(R.id.txt_note);
        btnClose = findViewById(R.id.btn_close);
        imgPatientAvatar = findViewById(R.id.img_patient_avatar);
        imgDentistAvatar = findViewById(R.id.img_dentist_avatar);
        patientBlock = findViewById(R.id.patient_detail_block);
        appointmentNameBlock = findViewById(R.id.appointment_name_block);
        if(appointment.getPatient()!=null){
            patientBlock.setVisibility(View.VISIBLE);
        }else{
            appointmentNameBlock.setVisibility(View.VISIBLE);
        }
        if(appointment.getPatient()!=null){
            if(appointment.getPatient().getAvatar()!=null){
                Picasso.get().load(appointment.getPatient().getAvatar()).into(imgPatientAvatar);
            }
            if(appointment.getPatient().getName()!=null){
                txtPatientName.setText(appointment.getPatient().getName());
                txtPatientName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            Intent intent = new Intent(getContext(), PatientDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(AppConst.PATIENT_OBJ, appointment.getPatient());
                            intent.putExtra(AppConst.BUNDLE, bundle);
                            c.startActivity(intent);
                    }
                });
            }
        }
        if(appointment.getName()!=null){
            txtAppointmentName.setText(appointment.getName());
        }
        if(appointment.getStaff()!=null){
            if(appointment.getStaff().getAvatar()!=null){
                Picasso.get().load(appointment.getStaff().getAvatar()).into(imgDentistAvatar);
            }
            if(appointment.getStaff().getName()!=null){
                txtDentistName.setText(appointment.getStaff().getName());
            }
        }
        if(appointment.getNumericalOrder()!=-1){
            txtApppointmentNumber.setText(appointment.getNumericalOrder()+"");
        }
        if(appointment.getStartTime()!=null){
            txtStartTime.setText(DateUtils.changeDateFormat(appointment.getStartTime(), DateTimeFormat.DATE_TIME_DB,DateTimeFormat.DATE_TIME_APP));
        }
        if(appointment.getNote()!=null){
            txtNote.setText(appointment.getNote());
        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


}
