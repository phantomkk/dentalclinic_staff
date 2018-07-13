package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.dentalclinic.capstone.admin.utils.GenderUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientAdapter extends ArrayAdapter<Patient> {
    private List<Patient> patients;
    public PatientAdapter(Context context, List<Patient> patients) {
        super(context, 0, patients);
        this.patients = patients;
    }

    private static class ViewHolder {
        CircleImageView imgAvatar;
        TextView txtName, txtDateOfBirth, txtGender;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_patient, parent, false);
            viewHolder.imgAvatar = convertView.findViewById(R.id.img_avatar_user);
            viewHolder.txtName = convertView.findViewById(R.id.txt_name);
            viewHolder.txtDateOfBirth = convertView.findViewById(R.id.txt_date_of_birth);
            viewHolder.txtGender = convertView.findViewById(R.id.txt_gender);



            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }Z

        Patient patient = patients.get(position);
        if(patient!=null){
            if(patient.getAvatar()!=null){
                Picasso.get().load(patient.getAvatar()).error(getContext().getDrawable(R.drawable.avatar)).into(viewHolder.imgAvatar);
            }
            if(patient.getName()!=null){
                viewHolder.txtName.setText(patient.getName());
            }
            if(patient.getDateOfBirth()!=null){
                viewHolder.txtDateOfBirth.setText(DateUtils.changeDateFormat(patient.getDateOfBirth(), DateTimeFormat.DATE_TIME_DB_2, DateTimeFormat.DATE_APP));
            }
            if(patient.getGender()!=null){
                GenderUtils.changeTextView(patient.getGender(),viewHolder.txtGender,getContext());
            }
        }
        return convertView;

    }
}
