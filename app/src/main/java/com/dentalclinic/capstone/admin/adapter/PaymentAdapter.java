package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.Payment;
import com.dentalclinic.capstone.admin.models.PaymentDetail;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.dentalclinic.capstone.admin.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PaymentAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Payment> listDataHeader; // header titles
    private List<Payment> listDataHeaderOriginal = new ArrayList<>();
    private ExpandableListView listView;
    private Fragment fragment;
private BtnCheckoutListenter btnCheckOutClickListener;
    //    private Fragment fragment;
    public PaymentAdapter(Context context, List<Payment> listDataHeader) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataHeaderOriginal.addAll(listDataHeader);
    }

    public PaymentAdapter(Context context,
                          List<Payment> listDataHeader,
                          Fragment fragment,
                          BtnCheckoutListenter btnCheckOutClickListener) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.fragment = fragment;
          this.btnCheckOutClickListener = btnCheckOutClickListener;
    }

//    public PaymentAdapter(Context context, List<Payment> listDataHeader, ExpandableListView listView, Fragment fragment) {
//        this.context = context;
//        this.listDataHeader = listDataHeader;
//        this.listView = listView;
//        this.fragment = fragment;
//    }

    public List<Payment> getListDataHeaderOriginal() {
        return listDataHeaderOriginal;
    }

    public void setListDataHeaderOriginal(List<Payment> listDataHeaderOriginal) {
        this.listDataHeaderOriginal = listDataHeaderOriginal;
    }

    public PaymentAdapter(Context context,
                          List<Payment> listDataHeader,
                          ExpandableListView listView,
                          Fragment fragment,
                          BtnCheckoutListenter listener) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listView = listView;
        this.fragment = fragment;
        this.btnCheckOutClickListener = listener;
    }

    public PaymentAdapter(Context context, List<Payment> listDataHeader, ExpandableListView listView) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listView = listView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return listDataHeader.get(groupPosition).getPaymentDetails().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        PaymentDetail paymentDetail = (PaymentDetail) getChild(groupPosition, childPosition);
        ChildViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ChildViewHolder();
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_payment_detail, null);
            TextView txtStaffName = convertView.findViewById(R.id.txt_staff_name_1);
            TextView txtDate = convertView.findViewById(R.id.txt_date);
            TextView txtPrepaidMoney = convertView.findViewById(R.id.txt_prepaid_money);
            ImageView imgStaffAvatar = convertView.findViewById(R.id.img_staff_avatar);

//        else{
//            viewHolder = (ChildViewHolder) convertView.getTag();
//        }


            if (paymentDetail.getReceivedMoney() != null) {
                Staff staff = paymentDetail.getReceptionist();
                if (staff != null) {
                    txtStaffName.setText(staff.getName());
                    Picasso.get().load(staff.getAvatar()).into(imgStaffAvatar);
                }
            }
            if (paymentDetail.getDateCreate() != null) {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                txtDate.setText(DateUtils.changeDateFormat(paymentDetail.getDateCreate(), DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_APP_2));
            }
            if (paymentDetail.getReceivedMoney() != null) {
                txtPrepaidMoney.setText(Utils.formatMoney(paymentDetail.getReceivedMoney()) + context.getResources().getString(R.string.current_unit));
            }
        }
        return convertView;
    }


    public class ChildViewHolder {
        TextView txtStaffName, txtDate, txtPrepaidMoney;
        CircleImageView imgStaffAvatar;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataHeader.get(groupPosition).getPaymentDetails().size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Payment payment = (Payment) getGroup(groupPosition);
        GroupViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new GroupViewHolder();
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_payment, null);
            viewHolder.txtTreatmentName = convertView.findViewById(R.id.txt_treatment_name);
            viewHolder.txtTotal = convertView.findViewById(R.id.txt_total);
            viewHolder.txtPaid = convertView.findViewById(R.id.txt_paid);
            viewHolder.txtNotPayYet = convertView.findViewById(R.id.txt_not_pay_yet);
            viewHolder.txtStatus = convertView.findViewById(R.id.txt_status);
            viewHolder.imgExpIcon = convertView.findViewById(R.id.img_expand_icon);
//            viewHolder.btnCheckOut = convertView.findViewById(R.id.btn_check_out_paypal);
            viewHolder.lnStatus = convertView.findViewById(R.id.ln_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isExpanded) {
                    listView.expandGroup(groupPosition);

                } else {
                    listView.collapseGroup(groupPosition);
                }
            }
        });

//        viewHolder.btnCheckOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btnCheckOutClickListener.onClick(view,payment);
//            }
//        });
        if (isExpanded) {
            viewHolder.imgExpIcon.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        } else {
            viewHolder.imgExpIcon.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        }
        if (payment.getTreatmentNames() != null) {
            String treatmentName = "";
            for (int i = 0; i < payment.getTreatmentNames().size(); i++) {
                if (i == payment.getTreatmentNames().size() - 1) {
                    treatmentName += payment.getTreatmentNames().get(i);
                } else {
                    treatmentName += payment.getTreatmentNames().get(i) + ", ";
                }
            }
            viewHolder.txtTreatmentName.setText(treatmentName);
        }
        if (payment.getTotalPrice() != null) {
            viewHolder.txtTotal.setText(Utils.formatMoney(payment.getTotalPrice()) + context.getResources().getString(R.string.current_unit));
        }
        if (payment.getPaid() != null) {
            viewHolder.txtPaid.setText(Utils.formatMoney(payment.getPaid()) + context.getResources().getString(R.string.current_unit));
        }
        if(payment.getTotalPrice()!=null && payment.getPaid()!=null){
            viewHolder.txtNotPayYet.setText(Utils.formatMoney(payment.getTotalPrice()-payment.getPaid()) + context.getResources().getString(R.string.current_unit));
        }
        switch (payment.getStatus()){
            case 0:
                viewHolder.txtStatus.setText(context.getResources().getString(R.string.status_not_done));
                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.color_red_500));
                break;
            case 1:
                viewHolder.txtStatus.setText(context.getResources().getString(R.string.status_done));
                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.color_green_500));
                break;
            case 2:
                viewHolder.txtStatus.setText(context.getResources().getString(R.string.status_cancle));
                viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.color_red_500));
                break;
        }
//        if (payment.isDone() == 1) {
//            viewHolder.txtStatus.setText(context.getResources().getString(R.string.status_done));
//            viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.color_green_500));
//        } else {
//            viewHolder.txtStatus.setText(context.getResources().getString(R.string.status_not_done));
//            viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.color_red_500));
//        }
        return convertView;
    }

    private static final int REQUEST_CODE_PAYMENT = 1;





public interface BtnCheckoutListenter{
    void onClick(View v, Payment p);
}
    public class GroupViewHolder {
        TextView txtTreatmentName, txtTotal, txtPaid, txtNotPayYet, txtStatus;
        ImageView imgExpIcon;
        Button btnCheckOut;
        LinearLayout lnStatus;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void filterData(String query) {
        query = query.toLowerCase();
        Log.v("ServiceAdapter", String.valueOf(listDataHeader.size()));
        Log.v("original", String.valueOf(listDataHeaderOriginal.size()));
        Log.v("query", query);
        listDataHeader.clear();
        if (query.isEmpty()) {
            listDataHeader.addAll(listDataHeaderOriginal);
        } else {
            for (Payment payment : listDataHeaderOriginal) {
                List<PaymentDetail> paymentDetails = payment.getPaymentDetails();
                Log.v("paymentDetails", String.valueOf(paymentDetails.size()));
                List<PaymentDetail> newPaymentDetails = new ArrayList<PaymentDetail>();
                for (PaymentDetail paymentDetail : paymentDetails) {
                    String date = DateUtils.changeDateFormat(paymentDetail.getDateCreate(), DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_APP_2);
                    if (paymentDetail.getDateCreate().contains(query.toLowerCase())) {
                        newPaymentDetails.add(paymentDetail);
                    }
                }
//                if (newPaymentDetails.size() > 0) {
//                    Log.v("newPaymentDetails", String.valueOf(newPaymentDetails.size()));
//                    Payment tPayment = new Payment();
//                    if (payment.getId() != -1) {
//                        tPayment.setId(payment.getId());
//                    }
//                    if (payment.getUser() != null) {
//                        tPayment.setUser(payment.getUser());
//                    }
//                    if (payment.getPaid() != null) {
//                        tPayment.setPaid(payment.getPaid());
//                    }
//                    if (payment.getTotalPrice() != null) {
//                        tPayment.setTotalPrice(payment.getTotalPrice());
//                    }
//                    if (payment.getTreatmentHistories() != null) {
//                        tPayment.setTreatmentHistories(payment.getTreatmentHistories());
//                    }
//                    if (payment.getNotePayable() != null) {
//                        tPayment.setNotePayable(payment.getNotePayable());
//                    }
//
//                    tPayment.setPaymentDetails(newPaymentDetails);
//                    listDataHeader.add(tPayment);
//                }
            }
        }

        Log.v("ServiceAdapter", String.valueOf(listDataHeader.size()));
        notifyDataSetChanged();
    }


}
