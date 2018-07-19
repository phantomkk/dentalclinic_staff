package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.TreatmentImage;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<TreatmentImage> images;
    private final ImageAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TreatmentImage item, int position);

    }

    public ImageAdapter(Context context, List<TreatmentImage> treatmentImages, ImageAdapter.OnItemClickListener listener) {
        this.context = context;
        this.images = treatmentImages;
        this.listener = listener;
        inflater = LayoutInflater.from(context);

    }


    //    public ImageAdapter(Context context, ArrayList<Image> images, ImageFileAdapter.OnItemClickListener listener) {
//        this.context = context;
//        inflater = LayoutInflater.from(context);
//        this.images = images;
//        this.listener = listener;
//    }
//
//    public ImageAdapter(Context context, List<TreatmentImage> treatmentImages, ImageFileAdapter.OnItemClickListener listener) {
//        this.context = context;
//        inflater = LayoutInflater.from(context);
//        this.treatmentImages = treatmentImages;
//        this.listener = listener;
//        this.images = new ArrayList<>();
//        for (TreatmentImage treatmentImage: treatmentImages) {
//            Image image = new Image(treatmentImage.getId(), treatmentImage.getId()+"", treatmentImage.getImageLink());
//            this.images.add(image);
//        }
//    }

    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageAdapter.ImageViewHolder(inflater.inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ImageViewHolder holder, int position) {
        final TreatmentImage image = images.get(position);
        Glide.with(context)
                .load(image.getImageLink())
                .apply(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                .into(holder.imageView);

        holder.bind(images.get(position), position, listener);
    }


    public void deleteItem(int index) {
        images.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setData(List<TreatmentImage> images) {
        this.images.clear();
        if (images != null) {
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_thumbnail);
        }

        public void bind(final TreatmentImage item, int position, final ImageAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, position);
                }
            });
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.onItemDelete(item,position);
//                }
//            });
        }
    }
}



