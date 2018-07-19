package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
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
        if(appointment!=null){
            if(appointment.getNumericalOrder()!=-1){
                holder.txtNumber.setText(appointment.getNumericalOrder()+"");
            }
            if(appointment.getName()!=null){
                holder.txtName.setText(appointment.getName());
            }
            if(appointment.getStatus()!=0){
                holder.txtStatus.setVisibility(View.VISIBLE);
            }
            if(appointment.getNote()!=null){
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

                    if(!appointment.isExpand()){
                        //toggling visibility
                        holder.linearLayout.setVisibility(View.VISIBLE);

                        //adding sliding effect
                        holder.linearLayout.startAnimation(slideDown);
                        appointment.setExpand(true);
                    }else{
                        //toggling visibility
                        holder.linearLayout.setVisibility(View.GONE);

                        //adding sliding effect
                        holder.linearLayout.startAnimation(slideUp);
                        appointment.setExpand(false);

                    }
                }
            });

        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(holder.getAdapterPosition());
                }
            }
        });
        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
//        (holder.content).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(mContext, "onClick:" + mDatas.get(holder.getAdapterPosition()).name, Toast.LENGTH_SHORT).show();
////                Log.d("TAG", "onClick() called with: v = [" + v + "]");
//            }
//        });
        //置顶：
        holder.btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mOnSwipeListener){
                    mOnSwipeListener.onTop(holder.getAdapterPosition());
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
        void onDel(int pos);
        void onTop(int pos);
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
        Button btnDelete;
        Button btnUnRead;
        Button btnTop;
        public ViewHolder(View itemView) {
            super(itemView);
//            imgAvatar =  itemView.findViewById(R.id.img_avatar_user);
            txtName = itemView.findViewById(R.id.txt_patient_name);
            txtNumber = itemView.findViewById(R.id.txt_appointment_number);
            txtStatus = itemView.findViewById(R.id.txt_appointment_status);
            txtNote = itemView.findViewById(R.id.txt_note);
            linearLayout = itemView.findViewById(R.id.linear_layout_note);
            btnViewMore = itemView.findViewById(R.id.btn_view_note);
            btnDelete =  itemView.findViewById(R.id.btnDelete);
            btnUnRead =  itemView.findViewById(R.id.btnUnRead);
            btnTop = itemView.findViewById(R.id.btnTop);
        }
    }
}
