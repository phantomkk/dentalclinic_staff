package com.dentalclinic.capstone.admin.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

public class DentistAxisValueFormatter  implements IAxisValueFormatter {
    private List<String> names;

    public DentistAxisValueFormatter(List<String> names) {
        this.names = names;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int pos = (int) value;

        return names.get(pos);
    }
}
