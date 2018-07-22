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
import com.dentalclinic.capstone.admin.models.Absent;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

public class AbsentSwiftAdapter extends RecyclerView.Adapter<AbsentSwiftAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInfalter;
    private List<Absent> absents;

    public AbsentSwiftAdapter(Context context, List<Absent> absents) {
        mContext = context;
        mInfalter = LayoutInflater.from(context);
        this.absents = absents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInfalter.inflate(R.layout.item_absent, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ((SwipeMenuLayout) holder.itemView).setIos(true);

        Absent absent = absents.get(position);
        if (absent != null) {
            if (absent.getStartDate() != null) {
                holder.txtStartDate.setText(DateUtils.changeDateFormat(absent.getStartDate(), DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_FOTMAT));
            }
            if (absent.getEndDate() != null) {
                holder.txtEndDate.setText(DateUtils.changeDateFormat(absent.getEndDate(), DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_FOTMAT));
            }
            if (absent.getReason() != null) {
                holder.txtReason.setText(absent.getReason());
            }
            if (absent.getStaffApprove() == null) {
                holder.txtStatus.setText("Đang Xác Nhân");
                holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.color_orange_500));
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnView.setVisibility(View.GONE);
            } else {
                holder.btnDelete.setVisibility(View.GONE);
                holder.btnView.setVisibility(View.VISIBLE);
                switch (absent.getIsApproved()){
                    case 0:
                        holder.txtStatus.setText("Đã Hủy");
                        holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.color_red_500));
                        break;
                    case 1:
                        holder.txtStatus.setText("Xác Nhân");
                        holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.color_green_500));
                        break;
                        default:
                            break;
                }
            }
        }

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onViewClick(holder.getAdapterPosition());
                }
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    mOnSwipeListener.onDelete(holder.getAdapterPosition());
                }

            }
        });
        holder.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation slideDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
                Animation slideUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);

//                    //toggling visibility
//                    viewHolder.linearLayout.setVisibility(View.VISIBLE);
//
//                    //adding sliding effect
//                    viewHolder.linearLayout.startAnimation(slideDown);

                if (!absent.isExpand()) {
                    //toggling visibility
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    holder.btnViewMore.setText("Ẩn");
                    //adding sliding effect
                    holder.linearLayout.startAnimation(slideDown);
                    absent.setExpand(true);
                } else {
                    //toggling visibility
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.btnViewMore.setText("Xem Thêm");
                    //adding sliding effect
                    holder.linearLayout.startAnimation(slideUp);
                    absent.setExpand(false);

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return null != absents ? absents.size() : 0;
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onViewClick(int pos);

        void onDelete(int pos);
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
        TextView txtStartDate, txtEndDate, txtStatus, txtReason;
        LinearLayout linearLayout;
        TextView btnViewMore;
        Button btnView;
        Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            txtStartDate = itemView.findViewById(R.id.txt_start_date);
            txtEndDate = itemView.findViewById(R.id.txt_end_date);
            txtStatus = itemView.findViewById(R.id.txt_appointment_status);
            txtReason = itemView.findViewById(R.id.txt_reason);
            linearLayout = itemView.findViewById(R.id.linear_layout_note);
            btnViewMore = itemView.findViewById(R.id.btn_view_more);
            btnView = itemView.findViewById(R.id.btn_view);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
