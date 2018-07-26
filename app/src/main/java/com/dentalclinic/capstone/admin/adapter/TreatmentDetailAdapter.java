package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.activities.PhotoViewActivity;
import com.dentalclinic.capstone.admin.custom.MyGridView;
import com.dentalclinic.capstone.admin.models.Medicine;
import com.dentalclinic.capstone.admin.models.Step;
import com.dentalclinic.capstone.admin.models.TreatmentDetail;
import com.dentalclinic.capstone.admin.models.TreatmentImage;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.dentalclinic.capstone.admin.utils.Utils;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TreatmentDetailAdapter extends ArrayAdapter<TreatmentDetail> {
    private List<TreatmentDetail> treatmentDetails;
    private ImageAdapter imageAdapter;
    public TreatmentDetailAdapter(Context context, List<TreatmentDetail> treatmentDetails) {
        super(context, 0, treatmentDetails);
        this.treatmentDetails = treatmentDetails;
    }

    private static class ViewHolder {
        TextView mDentistName, mDate, mTreatmentStep, mNote, mPrescription;
        RecyclerView recyclerView;
        LinearLayout llSteps, llPresctiption, llImages;
        CircleImageView imgDentistAvatar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_treatment_detail, parent, false);
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image, parent, false);
            viewHolder.llSteps = convertView.findViewById(R.id.ll_stetps);
            viewHolder.llPresctiption = convertView.findViewById(R.id.ll_preciptions);
            viewHolder.llImages = convertView.findViewById(R.id.ll_images);
            viewHolder.imgDentistAvatar = convertView.findViewById(R.id.img_dentist_avatar);
            viewHolder.mDentistName = convertView.findViewById(R.id.txt_dentist);
            viewHolder.mDate = convertView.findViewById(R.id.txt_date);
            viewHolder.mTreatmentStep = convertView.findViewById(R.id.txt_treatment_step);
            viewHolder.mNote = convertView.findViewById(R.id.txt_note);
            viewHolder.mPrescription = convertView.findViewById(R.id.txt_medicine);
            viewHolder.recyclerView = convertView.findViewById(R.id.recyclerView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TreatmentDetail treatmentDetail = treatmentDetails.get(position);
        if (treatmentDetail != null) {
            if (treatmentDetail.getDentist() != null) {
                viewHolder.mDentistName.setText(treatmentDetail.getDentist().getName());
                Picasso.get().load(treatmentDetail.getDentist().getAvatar()).into(viewHolder.imgDentistAvatar);
            }
            viewHolder.mDate.setText(
                    DateUtils.changeDateFormat(treatmentDetail.getCreatedDate(), DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_FOTMAT));
            String step = "";
            if (treatmentDetail.getSteps() != null) {
                if (treatmentDetail.getSteps().isEmpty()) {
                    viewHolder.llSteps.setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < treatmentDetail.getSteps().size(); i++) {
                        if (treatmentDetail.getSteps().get(i).getStep() != null) {
                            Step step1 = treatmentDetail.getSteps().get(i).getStep();
                            if (i == treatmentDetail.getSteps().size() - 1) {
                                step += "- " + step1.getName();
                            } else {
                                step += "- " + step1.getName() + "\n";
                            }
                        }
                    }
                }
            } else {
                viewHolder.llSteps.setVisibility(View.GONE);
            }
            viewHolder.mTreatmentStep.setText(step);
            viewHolder.mNote.setText(treatmentDetail.getNote());
            String prescription = "";
            if (treatmentDetail.getPrescriptions() != null) {
                if (treatmentDetail.getPrescriptions().isEmpty()) {
                    viewHolder.llPresctiption.setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < treatmentDetail.getPrescriptions().size(); i++) {
                        if (treatmentDetail.getPrescriptions().get(i).getMedicine() != null) {
                            Medicine medicine = treatmentDetail.getPrescriptions().get(i).getMedicine();
                            if (i == treatmentDetail.getPrescriptions().size() - 1) {
                                prescription += "- " + Utils.getMedicineLine(medicine.getName(), treatmentDetail.getPrescriptions().get(i).getQualtity(), 40);
                            } else {
                                prescription += "- " + Utils.getMedicineLine(medicine.getName(), treatmentDetail.getPrescriptions().get(i).getQualtity(), 40) + "\n";
                            }
                        }
                    }
                }
            } else {
                viewHolder.llPresctiption.setVisibility(View.GONE);
            }
            viewHolder.mPrescription.setText(prescription);
            if (treatmentDetail.getImages() != null) {
                if (treatmentDetail.getImages().isEmpty()) {
                    viewHolder.llImages.setVisibility(View.GONE);
                } else {
                    ArrayList<Image> images = new ArrayList<>();

                    imageAdapter = new ImageAdapter(getContext(), treatmentDetail.getImages(), new ImageAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(TreatmentImage item, int position) {
                            Intent intent = new Intent(getContext(), PhotoViewActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(AppConst.IMAGE_OBJ, treatmentDetail.getImages().get(position));
                            intent.putExtra(AppConst.BUNDLE, bundle);
                            getContext().startActivity(intent);
                        }
                    });

//                    new ImageFileAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(Image item, int position) {
//                            Intent intent = new Intent(getContext(), PhotoViewActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable(AppConst.IMAGE_OBJ, treatmentDetail.getImages().get(position));
//                            intent.putExtra(AppConst.BUNDLE, bundle);
//                            getContext().startActivity(intent);
//                        }
//
//                        @Override
//                        public void onItemDelete(Image item, int position) {
//                            showDialog("Bạn có chắc muốn xóa hình ảnh này");
//                            imageAdapter.deleteItem(position);
//                        }
//                    });
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    viewHolder.recyclerView.setLayoutManager(layoutManager);
                    viewHolder.recyclerView.setAdapter(imageAdapter);
                }
            } else {
                viewHolder.llImages.setVisibility(View.GONE);
            }

        }
        return convertView;
    }

    public void showDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Thử lại", (DialogInterface dialogInterface, int i) -> {

                });
        alertDialog.show();
    }
}
