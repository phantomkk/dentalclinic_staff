package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.Appointment;

import org.w3c.dom.Text;

import java.util.List;

public class AppointmentAdapter extends ArrayAdapter<Appointment> {
    private List<Appointment> appointments;
    public AppointmentAdapter(Context context, List<Appointment> appointments) {
        super(context, 0, appointments);
        this.appointments = appointments;
    }

    private static class ViewHolder {
//        CircleImageView imgAvatar;
//        TextView txtName, txtDateOfBirth, txtGender;
        TextView txtNumber, txtName, txtStatus, txtNote;
        LinearLayout linearLayout;
        TextView button;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_appointment, parent, false);
            viewHolder.txtNumber = convertView.findViewById(R.id.txt_appointment_number);
            viewHolder.txtName = convertView.findViewById(R.id.txt_patient_name);
            viewHolder.txtStatus = convertView.findViewById(R.id.txt_appointment_status);
            viewHolder.txtNote = convertView.findViewById(R.id.txt_note);
            viewHolder.linearLayout = convertView.findViewById(R.id.linear_layout_note);
            viewHolder.button = convertView.findViewById(R.id.btn_view_note);


//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(getContext(),"click",Toast.LENGTH_LONG).show();
//                }
//            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Appointment appointment = appointments.get(position);
        if(appointment!=null){
            if(appointment.getNumericalOrder()!=-1){
                viewHolder.txtNumber.setText(appointment.getNumericalOrder()+"");
            }
            if(appointment.getName()!=null){
                viewHolder.txtName.setText(appointment.getName());
            }
            if(appointment.getStatus()!=0){
                viewHolder.txtStatus.setVisibility(View.VISIBLE);
            }
            if(appointment.getNote()!=null){
                viewHolder.txtNote.setText(appointment.getNote());
            }
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
                    Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);

//                    //toggling visibility
//                    viewHolder.linearLayout.setVisibility(View.VISIBLE);
//
//                    //adding sliding effect
//                    viewHolder.linearLayout.startAnimation(slideDown);

                    if(!appointment.isExpand()){
                        //toggling visibility
                        viewHolder.linearLayout.setVisibility(View.VISIBLE);

                        //adding sliding effect
                        viewHolder.linearLayout.startAnimation(slideDown);
                        appointment.setExpand(true);
                    }else{
                        //toggling visibility
                        viewHolder.linearLayout.setVisibility(View.GONE);

                        //adding sliding effect
                        viewHolder.linearLayout.startAnimation(slideUp);
                        appointment.setExpand(false);

                    }
                }
            });

        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Appointment appointment = appointments.get(position);
        int rs =0;
        if(appointment!=null){
            if(appointment.getStatus()!=-1){
                if(appointment.getStatus()== 1){
                    rs =1;
                }
            }
        }
        return rs;
    }
}
