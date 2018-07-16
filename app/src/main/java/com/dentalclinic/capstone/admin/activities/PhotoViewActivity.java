package com.dentalclinic.capstone.admin.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.TreatmentImage;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

public class PhotoViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }
        Bundle bundle = getIntent().getBundleExtra(AppConst.BUNDLE);
        TreatmentImage image = (TreatmentImage) bundle.getSerializable(AppConst.IMAGE_OBJ);
        if(image!=null){
            PhotoView photoView = findViewById(R.id.iv_photo);
            Glide.with(this)
                    .load(image.getImageLink())
                    .apply(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(photoView);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
