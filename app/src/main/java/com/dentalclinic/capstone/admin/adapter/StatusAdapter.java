package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.Status;
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StatusAdapter extends FilterAdapter<Status> {
    private Context context;

    StatusAdapter(@NotNull List<? extends Status> items) {
        super(items);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private int[] mColors;
    private String[] mTitles;

    @NotNull
    @Override
    public FilterItem createView(int position, Status item) {
        FilterItem filterItem = new FilterItem(context);
        mColors = context.getResources().getIntArray(R.array.colors_status);
        mTitles = context.getResources().getStringArray(R.array.status);
        filterItem.setStrokeColor(mColors[0]);
        filterItem.setTextColor(mColors[0]);
        filterItem.setCornerRadius(14);
        filterItem.setCheckedTextColor(ContextCompat.getColor(context, android.R.color.white));
        filterItem.setColor(ContextCompat.getColor(context, android.R.color.white));
        filterItem.setCheckedColor(mColors[position]);
        filterItem.setText(item.getText());
        filterItem.deselect();

        return filterItem;
    }

}
