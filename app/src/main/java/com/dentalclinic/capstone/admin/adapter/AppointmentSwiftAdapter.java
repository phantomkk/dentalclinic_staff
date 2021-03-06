package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

public class AppointmentSwiftAdapter extends RecyclerView.Adapter<AppointmentSwiftAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInfalter;
    private List<Appointment> appointments;

    public AppointmentSwiftAdapter(Context context, List<Appointment> appointments) {
        mContext = context;
        mInfalter = LayoutInflater.from(context);
        this.appointments = appointments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInfalter.inflate(R.layout.item_appointment, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ((SwipeMenuLayout) holder.itemView).setIos(true);

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
            switch (appointment.getStatus()) {
                case 1:
                    holder.txtStatus.setText("Sẵn Sàng");
                    holder.btnStart.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    holder.txtStatus.setText("Đang Khám");
                    holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.color_deep_orange_500));
                    holder.btnTreatment.setVisibility(View.VISIBLE);
                    holder.btnDone.setVisibility(View.VISIBLE);
                    holder.btnStart.setVisibility(View.GONE);
                    break;
                case 3:
                    holder.txtStatus.setText("Hoàn Thành");
                    holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.color_green_500));
                    holder.btnStart.setVisibility(View.GONE);
                    holder.btnDone.setVisibility(View.GONE);
                    holder.btnTreatment.setVisibility(View.GONE);
                    break;
                default:
                    break;

            }

            if (appointment.getNote() != null) {
                holder.txtNote.setText(appointment.getNote());
            }
            holder.btnViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation slideDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
                    Animation slideUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);

//                    //toggling visibility
//                    viewHolder.linearLayout.setVisibility(View.VISIBLE);
//
//                    //adding sliding effect
//                    viewHolder.linearLayout.startAnimation(slideDown);

                    if (!appointment.isExpand()) {
                        //toggling visibility
                        holder.linearLayout.setVisibility(View.VISIBLE);
                        holder.btnViewMore.setText("Ẩn");
                        //adding sliding effect
                        holder.linearLayout.startAnimation(slideDown);
                        appointment.setExpand(true);
                    } else {
                        //toggling visibility
                        holder.linearLayout.setVisibility(View.GONE);
                        holder.btnViewMore.setText("Xem Thêm");
                        //adding sliding effect
                        holder.linearLayout.startAnimation(slideUp);
                        appointment.setExpand(false);

                    }
                }
            });

        }

        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onStartClick(holder.getAdapterPosition());
                }
            }
        });


        holder.btnTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    mOnSwipeListener.onTreatmentClick(holder.getAdapterPosition());
                }

            }
        });
        holder.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    mOnSwipeListener.onDoneClick(holder.getAdapterPosition());
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

    @Override
    public int getItemCount() {
        return null != appointments ? appointments.size() : 0;
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onStartClick(int pos);

        void onTreatmentClick(int pos);

        void onDoneClick(int pos);

        void onItemClick(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //        CircleImageView imgAvatar;
        TextView txtName, txtNumber, txtStatus, txtNote;
        LinearLayout linearLayout;
        TextView btnViewMore;
        Button btnStart;
        Button btnTreatment;
        Button btnDone;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
//            imgAvatar =  itemView.findViewById(R.id.img_avatar_user);
            txtName = itemView.findViewById(R.id.txt_patient_name);
            txtNumber = itemView.findViewById(R.id.txt_appointment_number);
            txtStatus = itemView.findViewById(R.id.txt_appointment_status);
            txtNote = itemView.findViewById(R.id.txt_note);
            cardView = itemView.findViewById(R.id.card_view);
            linearLayout = itemView.findViewById(R.id.linear_layout_note);
            btnViewMore = itemView.findViewById(R.id.btn_view_note);
            btnStart = itemView.findViewById(R.id.btnStart);
            btnTreatment = itemView.findViewById(R.id.btnTreatment);
            btnDone = itemView.findViewById(R.id.btnDone);
        }
    }
}
