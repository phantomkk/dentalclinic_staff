package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.models.City;

import java.util.List;

public class CitySpinnerAdapter extends ArrayAdapter {
    public CitySpinnerAdapter(@NonNull Context context, int resource, List<City> list) {
        super(context, resource, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View converView, ViewGroup parent){
        View v = LayoutInflater.from(getContext())
                .inflate(android.R.layout.simple_spinner_item, parent, false);
        TextView textView = v.findViewById(android.R.id.text1);
        City c = (City) getItem(position);
        if (c != null) {
            textView.setText(c.getName());
        }
        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
