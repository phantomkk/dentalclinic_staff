package com.dentalclinic.capstone.admin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.TreatmentImage;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoanglam on 8/23/17.
 */

public class ImageFileAdapter extends RecyclerView.Adapter<ImageFileAdapter.ImageViewHolder> {

    private Context context;
    private List<Image> images;
    private LayoutInflater inflater;
    private List<TreatmentImage> treatmentImages;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Image item, int position);
        void onItemDelete(Image item, int position);
    }


    public ImageFileAdapter(Context context, ArrayList<Image> images, OnItemClickListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.images = images;
        this.listener = listener;
    }

    public ImageFileAdapter(Context context, List<TreatmentImage> treatmentImages, OnItemClickListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.treatmentImages = treatmentImages;
        this.listener = listener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(inflater.inflate(R.layout.item_image_file, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        final Image image = images.get(position);
        Glide.with(context)
                .load(image.getPath())
                .apply(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                .into(holder.imageView);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemDelete(image,position);
//                images.remove(position);
//                notifyDataSetChanged();
            }
        });

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

    public void setData(List<Image> images) {
        this.images.clear();
        if (images != null) {
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        Button button;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_thumbnail);
            button = itemView.findViewById(R.id.btn_delete_image);
        }

        public void bind(final Image item,int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
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
