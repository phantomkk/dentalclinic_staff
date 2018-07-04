package com.dentalclinic.capstone.admin.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class URLDrawable  extends BitmapDrawable {
    public Drawable mDrawable;

    public URLDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    @Override
    public void draw(Canvas canvas) {
        if(mDrawable != null) {
            mDrawable.draw(canvas);
        }
    }
}
