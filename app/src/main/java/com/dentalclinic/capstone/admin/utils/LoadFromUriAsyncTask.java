package com.dentalclinic.capstone.admin.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class LoadFromUriAsyncTask extends AsyncTask<Uri, Void, Bitmap> {
    private final WeakReference<TextView> mTextViewRef;
    private final URLDrawable mUrlDrawable;
    private final Picasso mImageUtils;

    public LoadFromUriAsyncTask(TextView textView, URLDrawable urlDrawable) {
        mImageUtils = Picasso.get();
        mTextViewRef = new WeakReference<>(textView);
        mUrlDrawable = urlDrawable;
    }

    @Override
    protected Bitmap doInBackground(Uri... params) {
        try {
            return mImageUtils.load(params[0]).get();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result == null) {
            return;
        }
        if (mTextViewRef.get() == null) {
            return;
        }
        TextView textView = mTextViewRef.get();
        // change the reference of the current mDrawable to the result
        // from the HTTP call
        mUrlDrawable.mDrawable = new BitmapDrawable(textView.getResources(), result);
        // set bound to scale image to fit width and keep aspect ratio
        // according to the result from HTTP call
        int width = textView.getWidth();
        int height = Math.round(1.0f * width *
                mUrlDrawable.mDrawable.getIntrinsicHeight() /
                mUrlDrawable.mDrawable.getIntrinsicWidth());
        mUrlDrawable.setBounds(0, 0, width, height);
        mUrlDrawable.mDrawable.setBounds(0, 0, width, height);
        // force redrawing bitmap by setting text
        textView.setText(textView.getText());
    }
}
