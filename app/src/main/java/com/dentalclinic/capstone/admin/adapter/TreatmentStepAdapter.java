package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.TreatmentStep;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.List;

public class TreatmentStepAdapter extends ArrayAdapter<TreatmentStep> {
    private List<TreatmentStep> list;

    private List<TreatmentStep> listCurrentStep;

    public OnItemClickListener listener;

    public interface OnItemClickListener {
        void onCheck(int position, boolean isCheck);
    }

    private static class ViewHolder {
        public CheckBox chkStep;
        public TextView txtStep;
        public boolean checked = false;

    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public OnItemClickListener getListener() {
        return listener;
    }

    public TreatmentStepAdapter(@NonNull Context context, List<TreatmentStep> list, OnItemClickListener listener) {
        super(context, 0, list);
        this.list = list;
        this.listener = listener;
    }

    public TreatmentStepAdapter(@NonNull Context context,
                                List<TreatmentStep> list,
                                List<TreatmentStep> listCurrentStep,
                                OnItemClickListener listener) {
        super(context, 0, list);
        this.list = list;
        this.listCurrentStep = listCurrentStep;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_treatment_step, parent, false);
            viewHolder.chkStep = convertView.findViewById(R.id.chk_step);
            viewHolder.txtStep = convertView.findViewById(R.id.txt_name_step);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TreatmentStep step = list.get(position);
        if (step != null) {

            if (step.getName() != null) {
                viewHolder.txtStep.setText((step.getName()));
            }
            if (step.isCheck()) {
                viewHolder.chkStep.setChecked(true);
                viewHolder.checked = true;
            } else {
                viewHolder.chkStep.setChecked(false);
                viewHolder.checked = false;
            }
//            if (isInPreviousList(step)) {
//                viewHolder.chkStep.setChecked(true);
//                viewHolder.checked = true;
//            }else{
//                viewHolder.chkStep.setChecked(false);
//                viewHolder.checked = false;
//            }
//
            viewHolder.chkStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCheck(position, !step.isCheck());
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (step.isCheck()) {
                        viewHolder.chkStep.setChecked(false);
                        viewHolder.checked = false;
//                        step.setCheck(false);
                    } else {

                        viewHolder.chkStep.setChecked(true);
                        viewHolder.checked = true;
//                        step.setCheck(true);
                    }
                    listener.onCheck(position, !step.isCheck());
                }
            });

//            convertView.setOnClickListener((View view) -> {
//                viewHolder.checked = !viewHolder.checked;
//                viewHolder.chkStep.setChecked(viewHolder.checked);
//                if (viewHolder.checked) {
//                    listCurrentStep.add(step);
//                } else {
//                    deleteStep(step );
//                }
//            });
        }
        return convertView;

    }

    private void deleteStep(TreatmentStep step) {
        TreatmentStep tmp = null;
        for (TreatmentStep c : listCurrentStep) {
            if (c.getStepId() == step.getStepId() && c.getTreatmentId() == step.getTreatmentId()) {
                tmp = c;
                break;
            }
        }
        if (listCurrentStep != null && tmp != null) {
            listCurrentStep.remove(tmp);
        }
    }

    private boolean isInPreviousList(TreatmentStep a) {
        if (listCurrentStep == null) {
            return false;
        }
        for (TreatmentStep tmp : listCurrentStep) {
            if (a.getStepId() == tmp.getStepId() && a.getTreatmentId() == tmp.getTreatmentId()) {
                return true;
            }
        }
        return false;
    }

}

