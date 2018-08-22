package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

public class AppointmentSwiftForReceipAdapter extends RecyclerView.Adapter<AppointmentSwiftForReceipAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInfalter;
    private List<Appointment> appointments;
    private int[] mColors;
    private String[] mTitles;

    public AppointmentSwiftForReceipAdapter(Context context, List<Appointment> appointments) {
        mContext = context;
        mInfalter = LayoutInflater.from(context);
        this.appointments = appointments;
    }

    @Override
    public AppointmentSwiftForReceipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppointmentSwiftForReceipAdapter.ViewHolder(mInfalter.inflate(R.layout.item_appointment_for_receiption, parent, false));
    }

    @Override
    public void onBindViewHolder(final AppointmentSwiftForReceipAdapter.ViewHolder holder, final int position) {
        ((SwipeMenuLayout) holder.itemView).setIos(true);
        mColors = mContext.getResources().getIntArray(R.array.colors_status);
        mTitles = mContext.getResources().getStringArray(R.array.status);
        Appointment appointment = appointments.get(position);
        if (appointment != null) {
            if (appointment.getNumericalOrder() != -1) {
                holder.txtNumber.setText(appointment.getNumericalOrder() + "");
            }
            if (appointment.getPatient() != null) {
                if (appointment.getPatient().getName() != null) {
                    holder.txtName.setText(appointment.getPatient().getName());
                }
            } else if (appointment.getName() != null) {
                holder.txtName.setText(appointment.getName());
            }
            if (appointment.getStatus() != 0) {
                holder.txtStatus.setVisibility(View.VISIBLE);

            }
            if (appointment.getStartTime() != null) {
                holder.txtStartTime.setText(DateUtils.changeDateFormat(appointment.getStartTime(), DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_TIME_APP));
            }
            if (appointment.getStaff() != null && appointment.getStaff().getName() != null) {
                holder.txtDentist.setText(appointment.getStaff().getName());
            }
            setStatus(appointment.getStatus(), holder.txtStatus);
            switch (appointment.getStatus()) {
                case 1:
//                    holder.txtStatus.setText("Sẵn Sàng");
                    break;
                case 2:
                    holder.btnChangeDoctor.setVisibility(View.GONE);
                    holder.btnCancle.setVisibility(View.GONE);
//                    holder.txtStatus.setText("Đang Khám");
//                    holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.color_deep_orange_500));
                    break;
                case 3:
                    holder.btnChangeDoctor.setVisibility(View.GONE);
                    holder.btnCancle.setVisibility(View.GONE);
//                    holder.txtStatus.setText("Hoàn Thành");
//                    holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.color_green_500));
                    break;
                case 4:
                    holder.btnChangeDoctor.setVisibility(View.GONE);
                    holder.btnCancle.setVisibility(View.GONE);
                    holder.itemView.setVisibility(View.GONE);
                    break;
                default:
                    break;

            }
            if (appointment.getPatient() != null) {
                holder.btnTreatment.setVisibility(View.GONE);
            }
            if (appointment.getPhone() != null) {
                holder.txtPhone.setText(appointment.getPhone());
            }

        }

        holder.btnTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onTreatment(holder.getAdapterPosition());
                }
            }
        });

        holder.btnChangeDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onChangeDoctorClick(holder.getAdapterPosition());
                }
            }
        });


        holder.btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    mOnSwipeListener.onCancleClick(holder.getAdapterPosition());
                }

            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    mOnSwipeListener.onItemClick(holder.getAdapterPosition());
                }

            }
        });


    }

    public void setStatus(int status, TextView textView) {

        textView.setText(mTitles[status+1]);
        textView.setTextColor(mContext.getResources().getColor(R.color.white));
        GradientDrawable gd = new GradientDrawable();

        // Specify the shape of drawable
        gd.setShape(GradientDrawable.RECTANGLE);

        // Set the fill color of drawable
        gd.setColor(mColors[status+1]); // make the background transparent

        // Create a 2 pixels width red colored border for drawable
//        gd.setStroke(0, Color.RED); // border width and color

        // Make the border rounded
        gd.setCornerRadius(14.0f); // border corner radius

        // Finally, apply the GradientDrawable as TextView background
        textView.setBackground(gd);
//        switch (status) {
//            case 0:
//                break;
//            case 1:
//                break;
//            case 2:
//                break;
//            case 3:
//                break;
//            default:
//                return;
//        }
    }

    @Override
    public int getItemCount() {
        return null != appointments ? appointments.size() : 0;
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onTreatment(int pos);

        void onChangeDoctorClick(int pos);

        void onCancleClick(int pos);

        void onItemClick(int pos);
    }

    private AppointmentSwiftForReceipAdapter.onSwipeListener mOnSwipeListener;

    public AppointmentSwiftForReceipAdapter.onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(AppointmentSwiftForReceipAdapter.onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //        CircleImageView imgAvatar;
        TextView txtName, txtNumber, txtStartTime, txtStatus, txtDentist, txtPhone;
        Button btnCancle;
        Button btnChangeDoctor;
        Button btnTreatment;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
//            imgAvatar =  itemView.findViewById(R.id.img_avatar_user);
            txtName = itemView.findViewById(R.id.txt_patient_name);
            txtPhone = itemView.findViewById(R.id.txt_patient_phone);
            txtNumber = itemView.findViewById(R.id.txt_appointment_number);
            txtStartTime = itemView.findViewById(R.id.txt_start_time);
            txtStatus = itemView.findViewById(R.id.txt_appointment_status);
            txtDentist = itemView.findViewById(R.id.txt_dentist);
            cardView = itemView.findViewById(R.id.card_view);
            btnCancle = itemView.findViewById(R.id.btn_cancle);
            btnChangeDoctor = itemView.findViewById(R.id.btn_change_doctor);
            btnTreatment = itemView.findViewById(R.id.btnTreatment);
        }
    }
}
