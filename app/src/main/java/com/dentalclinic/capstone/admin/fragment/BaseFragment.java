package com.dentalclinic.capstone.admin.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.utils.AppConst;

public class BaseFragment extends Fragment {
    private ProgressDialog mProgressDialog;

    protected void showLoading() {
        if(mProgressDialog==null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.waiting_msg));
            mProgressDialog.show();
        }
    }

    protected void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    protected void hideLoading() {
        if(mProgressDialog!=null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void showDialog(String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("Xong", (DialogInterface dialogInterface, int i) -> {
                });
        alertDialog.show();
    }
    public void logError(Class t, String method, String message) {
        Log.e(AppConst.DEBUG_TAG, t.getSimpleName() + "." + method + "(): " + message);
    }

    public void logError(String method, String message) {
//        Log.e(AppConst.DEBUG_TAG, this.getClass().getSimpleName() + "." + method + "(): " + message);
        Log.e(AppConst.DEBUG_TAG, "Fragment" + "." + method + "(): " + message);
    }
}
