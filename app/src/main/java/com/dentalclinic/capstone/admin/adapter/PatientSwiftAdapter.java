package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.dentalclinic.capstone.admin.utils.GenderUtils;
import com.dentalclinic.capstone.admin.utils.Utils;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 介绍：
 * 作者：zhangxutong
 * 邮箱：zhangxutong@imcoming.com
 * 时间： 2016/9/12.
 */

public class PatientSwiftAdapter extends RecyclerView.Adapter<PatientSwiftAdapter.FullDelDemoVH> {
    private Context mContext;
    private LayoutInflater mInfalter;
    private List<Patient> patients;

    public PatientSwiftAdapter(Context context, List<Patient> patients) {
        mContext = context;
        mInfalter = LayoutInflater.from(context);
        this.patients = patients;
    }

    @Override
    public FullDelDemoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FullDelDemoVH(mInfalter.inflate(R.layout.item_patient, parent, false));
    }

    @Override
    public void onBindViewHolder(final FullDelDemoVH holder, final int position) {
        ((SwipeMenuLayout) holder.itemView).setIos(true);

        Patient patient = patients.get(position);
        if(patient!=null){
            if(patient.getAvatar()!=null){
                Picasso.get().load(patient.getAvatar()).error(mContext.getDrawable(R.drawable.avatar)).into(holder.imgAvatar);
            }
            if(patient.getName()!=null){
                holder.txtName.setText(patient.getName());
            }
            if(patient.getDateOfBirth()!=null){
                holder.txtDateOfBirth.setText(DateUtils.changeDateFormat(patient.getDateOfBirth(), DateTimeFormat.DATE_TIME_DB_2, DateTimeFormat.DATE_APP));
            }
            if(patient.getGender()!=null){
                GenderUtils.changeTextView(patient.getGender(),holder.txtGender,mContext);
            }
        }


//        holder.btnUnRead.setVisibility(position % 3 == 0 ? View.GONE : View.VISIBLE);

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
        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
//        (holder.content).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(mContext, "onClick:" + mDatas.get(holder.getAdapterPosition()).name, Toast.LENGTH_SHORT).show();
////                Log.d("TAG", "onClick() called with: v = [" + v + "]");
//            }
//        });
        //置顶：
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mOnSwipeListener){
                    mOnSwipeListener.onItemClick(holder.getAdapterPosition());
                }

            }
        });
        if(Utils.isDentist(mContext)){
            holder.btnTreatment.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return null != patients ? patients.size() : 0;
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onTreatment(int pos);
        void onItemClick(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    class FullDelDemoVH extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar;
        TextView txtName, txtDateOfBirth, txtGender;
        Button btnTreatment;
        CardView cardView;
        public FullDelDemoVH(View itemView) {
            super(itemView);
            imgAvatar =  itemView.findViewById(R.id.img_avatar_user);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDateOfBirth =  itemView.findViewById(R.id.txt_date_of_birth);
            txtGender =  itemView.findViewById(R.id.txt_gender);
            btnTreatment =  itemView.findViewById(R.id.btnTreatment);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}

