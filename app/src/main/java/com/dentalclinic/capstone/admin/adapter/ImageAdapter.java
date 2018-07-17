package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.TreatmentImage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends ArrayAdapter<TreatmentImage> {
    private List<TreatmentImage> treatmentImages;

    public ImageAdapter(Context context, List<TreatmentImage> treatmentImages) {
        super(context, 0, treatmentImages);
        this.treatmentImages = treatmentImages;
    }

    private static class ViewHolder {
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_image, parent, false);
            viewHolder.imageView = convertView.findViewById(R.id.img_image_treatment);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TreatmentImage image = treatmentImages.get(position);
        if (image != null) {
//            ImageView iv = (ImageView) convertView.findViewById(R.id.img_image_treatment);
                Picasso.get().load(image.getImageLink()).into(viewHolder.imageView);
        }
        return convertView;
    }
}



