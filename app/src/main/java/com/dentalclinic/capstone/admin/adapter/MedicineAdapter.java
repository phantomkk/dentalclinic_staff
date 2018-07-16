package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.Medicine;
import com.dentalclinic.capstone.admin.models.MedicineQuantity;

import java.util.List;

public class MedicineAdapter extends ArrayAdapter<MedicineQuantity> {

    private List<MedicineQuantity> listMedicineQuantity;
    private List<MedicineQuantity> selectedMedicines;

    private static class ViewHolder {
        //        public int quantity;
        public TextView txtMedicineName;
        public EditText edtQuantity;
        //        public Button btnSelect;
        public Button btnSubtract;
        public Button btnAdd;
        public boolean isSelected = false;
    }

    public MedicineAdapter(@NonNull Context context,
                           List<MedicineQuantity> listMedicineQuantity,
                           List<MedicineQuantity> selectedMedicines
    ) {
        super(context, 0, listMedicineQuantity);
        this.listMedicineQuantity = listMedicineQuantity;
        this.selectedMedicines = selectedMedicines;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MedicineQuantity medicine = listMedicineQuantity.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_medicine, parent, false);
            viewHolder.txtMedicineName = convertView.findViewById(R.id.txt_medicine_name);
            viewHolder.edtQuantity = convertView.findViewById(R.id.edt_quantity_medicine);
//            viewHolder.btnSelect = convertView.findViewById(R.id.btn_select_medicine);
            viewHolder.btnSubtract = convertView.findViewById(R.id.btn_subtract_qty_medicine);
            viewHolder.btnAdd = convertView.findViewById(R.id.btn_add_qty_medicine);
            convertView.setTag(viewHolder);
            convertView.setOnClickListener((View view) -> {
            });
            viewHolder.edtQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        String qt =viewHolder.edtQuantity.getText().toString();
                        int quantity = Integer.parseInt(qt);
                        medicine.setQuantity(quantity);
                        updateQuantityInList(medicine);
                    }
                }
            });

            //set Listener
            viewHolder.btnAdd.setOnClickListener((v) -> {
                if (medicine != null) {
//                viewHolder.quantity++;
                    medicine.setQuantity(medicine.getQuantity() + 1);
                    updateQuantityInList(medicine);
                    viewHolder.edtQuantity.setText(medicine.getQuantity() + "");

                }
            });
            viewHolder.btnSubtract.setOnClickListener((v) -> {
                if (medicine != null) {
                    medicine.setQuantity(medicine.getQuantity() - 1);
                    updateQuantityInList(medicine);
                    viewHolder.edtQuantity.setText(medicine.getQuantity() + "");
                }
            });
            if (medicine != null) {
                viewHolder.txtMedicineName.setText(medicine.getMedicine().getName());
                viewHolder.edtQuantity.setText(medicine.getQuantity() + "");
                MedicineQuantity tmp;
                if ((tmp = isInSelectedList(medicine)) != null) {
                    viewHolder.edtQuantity.setText(tmp.getQuantity() + "");
                    medicine.setQuantity(tmp.getQuantity());
                }
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


//        viewHolder.btnSelect.setOnClickListener((v) -> {
//            if (viewHolder.isSelected) {
//                viewHolder.btnSelect.setText("Chọn");
//                removeMedicine(medicine);
//            } else {
//                viewHolder.btnSelect.setText("Bỏ chọn");
//                selectedMedicines.add(medicine);
//            }
//            viewHolder.isSelected = !viewHolder.isSelected;
//        });
        return convertView;

    }

    private synchronized void updateQuantityInList(MedicineQuantity medicine) {
        MedicineQuantity medicineInlist = isInSelectedList(medicine);
        if (medicine.getQuantity() < 0) {
            medicine.setQuantity(0);
        }
        if (medicine.getQuantity() == 0) {
            if (medicineInlist != null) {
                removeMedicine(medicine);
                return;
            }
        }
        if (medicine.getQuantity() > 0 && medicineInlist == null) {
            selectedMedicines.add(medicine);
        } else if (medicine.getQuantity() > 0 && medicineInlist != null) {
            medicineInlist.setQuantity(medicine.getQuantity());
        }
    }


    private void removeMedicine(MedicineQuantity medicineQuantity) {
        MedicineQuantity tmp = null;
        for (MedicineQuantity m : selectedMedicines) {
            if (medicineQuantity.getMedicineId() == m.getMedicineId()) {
                tmp = m;
                break;
            }
        }
        if (tmp != null) {
            selectedMedicines.remove(tmp);
        }
    }


    private MedicineQuantity isInSelectedList(MedicineQuantity a) {
        if (selectedMedicines == null || selectedMedicines.size() == 0) {
            return null;
        }
        for (MedicineQuantity tmp : selectedMedicines) {
            if (a.getMedicineId() == tmp.getMedicineId()) {
                return tmp;
            }
        }
        return null;
    }

}

