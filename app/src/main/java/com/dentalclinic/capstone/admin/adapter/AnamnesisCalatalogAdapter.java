package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.AnamnesisCatalog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnamnesisCalatalogAdapter extends ArrayAdapter<AnamnesisCatalog> {
    List<AnamnesisCatalog> list;

    private List<AnamnesisCatalog> listPatientAnamnesis ;
    private static class ViewHolder {
        public CheckBox chkAnamnesis;
        public TextView txtAnamnesis;
        public boolean checked = false;

    }

    public AnamnesisCalatalogAdapter(@NonNull Context context,
                                     List<AnamnesisCatalog> list,
                                       List<AnamnesisCatalog> listPatientAnamnesis
    ) {
        super(context, 0, list);
        this.list = list;
        this.listPatientAnamnesis = listPatientAnamnesis;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_anamnesis, parent, false);
            viewHolder.chkAnamnesis = convertView.findViewById(R.id.chk_anamnesis);
            viewHolder.txtAnamnesis = convertView.findViewById(R.id.txt_name_anamnesis);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AnamnesisCatalog anamnesisCatalog = list.get(position);
        if (anamnesisCatalog != null) {

            viewHolder.txtAnamnesis.setText(anamnesisCatalog.getName());
            if(isInPreviousList(anamnesisCatalog)){
                viewHolder.chkAnamnesis.setChecked(true);
            }
            convertView.setOnClickListener((View view) -> {
                viewHolder.checked = !viewHolder.checked;
                viewHolder.chkAnamnesis.setChecked(viewHolder.checked);
                if(viewHolder.checked){
                    listPatientAnamnesis.add(anamnesisCatalog);
                }else{
                    listPatientAnamnesis.remove(anamnesisCatalog);
                }
            });
        }
        return convertView;

    }

    private boolean isInPreviousList(AnamnesisCatalog a){
        if(listPatientAnamnesis==null){
            return false;
        }
        for (AnamnesisCatalog tmp : listPatientAnamnesis) {
            if(a.getId() == tmp.getId()){
                return true;
            }
        }
        return false;
    }

}

