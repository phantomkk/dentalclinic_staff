package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.Symptom;
import com.dentalclinic.capstone.admin.models.Symptom;

import java.util.List;

public class SymptomAdapter  extends ArrayAdapter<Symptom> {
    List<Symptom> list;

    private List<Symptom> listPatientSymptom;

    private static class ViewHolder {
        public CheckBox chkSymptom;
        public TextView txtSymptom;
        public boolean checked = false;

    }

    public SymptomAdapter(@NonNull Context context,
                                     List<Symptom> list,
                                     List<Symptom> listPatientSymptom
    ) {
        super(context, 0, list);
        this.list = list;
        this.listPatientSymptom = listPatientSymptom;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SymptomAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new SymptomAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_symptom, parent, false);
            viewHolder.chkSymptom = convertView.findViewById(R.id.chk_symptom);
            viewHolder.txtSymptom = convertView.findViewById(R.id.txt_name_symptom);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SymptomAdapter.ViewHolder) convertView.getTag();
        }

        Symptom Symptom = list.get(position);
        if (Symptom != null) {

            viewHolder.txtSymptom.setText(Symptom.getName());
            if (isInPreviousList(Symptom)) {
                viewHolder.chkSymptom.setChecked(true);
                viewHolder.checked = true;
            }else{
                viewHolder.chkSymptom.setChecked(false);
                viewHolder.checked = false;
            }
            convertView.setOnClickListener((View view) -> {
                viewHolder.checked = !viewHolder.checked;
                viewHolder.chkSymptom.setChecked(viewHolder.checked);
                if (viewHolder.checked) {
                    listPatientSymptom.add(Symptom);
                } else {
                    deleteSymptom(Symptom.getId());
                }
            });
        }
        return convertView;

    }

    private void deleteSymptom(int id) {
        Symptom tmp = null;
        for (Symptom c : listPatientSymptom) {
            if (c.getId() == id) {
                tmp = c;
                break;
            }
        }
        if (listPatientSymptom != null && tmp!=null) {
            listPatientSymptom.remove(tmp);
        }
    }
    static int count = 0;
    private boolean isInPreviousList(Symptom a) {
        Log.d("COUNBT:", (count++)+"");
        if (listPatientSymptom == null) {
            return false;
        }
        for (Symptom tmp : listPatientSymptom) {
            if (a.getId() == tmp.getId()) {
                return true;
            }
        }
        return false;
    }
}
