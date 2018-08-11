package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.TreatmentHistory;
import com.dentalclinic.capstone.admin.models.TreatmentHistory;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.dentalclinic.capstone.admin.utils.Utils;

import java.util.List;

public class TreatmentHistoryAdapter extends ArrayAdapter<TreatmentHistory> {
    private List<TreatmentHistory> treatmentHistoryList;

    public TreatmentHistoryAdapter(Context context, List<TreatmentHistory> treatmentHistoryList) {
        super(context, 0, treatmentHistoryList);
        this.treatmentHistoryList = treatmentHistoryList;
    }

    private static class ViewHolder {
        TextView txtName, txtPrice, txtStartDate, txtFinishDate, txtToothName, txtDiscount, txtTotal, txtSymptoms;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_treatment_history, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.txt_treatment_name);
            viewHolder.txtPrice = convertView.findViewById(R.id.txt_price);
            viewHolder.txtStartDate = convertView.findViewById(R.id.txt_start_date);
            viewHolder.txtFinishDate = convertView.findViewById(R.id.txt_finish_date);
            viewHolder.txtToothName = convertView.findViewById(R.id.txt_tooth_name);
            viewHolder.txtDiscount = convertView.findViewById(R.id.txt_discount);
            viewHolder.txtTotal = convertView.findViewById(R.id.txt_total);
            viewHolder.txtSymptoms = convertView.findViewById(R.id.txt_symptoms);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TreatmentHistory treatmentHistory = treatmentHistoryList.get(position);
        if (treatmentHistory != null) {
//            SimpleDateFormat dateFormat = new SimpleDateFormat(DateTimeFormat.valueOf());
            if (treatmentHistory.getTreatment() != null) {
                viewHolder.txtName.setText(treatmentHistory.getTreatment().getName());
            }
            if (treatmentHistory.getPrice() != null) {
                viewHolder.txtPrice.setText(Utils.formatMoney(treatmentHistory.getPrice()) + getContext().getResources().getString(R.string.current_unit));
            }
            if (treatmentHistory.getCreateDate() != null) {
                viewHolder.txtStartDate.setText(DateUtils.changeDateFormat(treatmentHistory.getCreateDate(), DateTimeFormat.DATE_TIME_DB,
                        DateTimeFormat.DATE_FOTMAT));
            }
            if (treatmentHistory.getFinishDate() != null) {
                viewHolder.txtFinishDate.setText(DateUtils.changeDateFormat(treatmentHistory.getFinishDate(), DateTimeFormat.DATE_TIME_DB,
                        DateTimeFormat.DATE_FOTMAT));
            }
            if (treatmentHistory.getTooth() != null) {
                viewHolder.txtToothName.setText(treatmentHistory.getTooth().getToothName());
            }
            if (treatmentHistory.getTreatment() != null) {
                if (treatmentHistory.getTreatment().getEvent() != null) {
                    viewHolder.txtDiscount.setText(treatmentHistory.getTreatment().getEvent().getDiscount() + getContext().getResources().getString(R.string.percent));
                }else{
                    viewHolder.txtDiscount.setText("0" + getContext().getResources().getString(R.string.percent));
                }
            }
            if (treatmentHistory.getTotalPrice() != null) {
                viewHolder.txtTotal.setText(Utils.formatMoney(treatmentHistory.getTotalPrice())+ getContext().getResources().getString(R.string.current_unit));
            }
            if(treatmentHistory.getSymptoms()!=null){
                String rs = "";
                for (int i = 0 ; i <treatmentHistory.getSymptoms().size();i++){
                    if(i == treatmentHistory.getSymptoms().size()-1){
                        rs+="- "+treatmentHistory.getSymptoms();
                    }else{
                        rs="- "+ treatmentHistory.getSymptoms()+"\n";
                    }
                }
            }
        }
        return convertView;
    }
}
