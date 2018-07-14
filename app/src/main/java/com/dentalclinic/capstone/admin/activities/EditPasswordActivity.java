package com.dentalclinic.capstone.admin.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.models.User;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.Validation;

import io.reactivex.disposables.Disposable;

public class EditPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText txtPassword, txtConfirmPassword, txtCurrentPassword;
    private Staff user;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }
        Bundle bundle = getIntent().getBundleExtra(AppConst.BUNDLE);
        if (bundle.getSerializable(AppConst.STAFF_OBJ) != null) {
            user = (Staff) bundle.getSerializable(AppConst.STAFF_OBJ);
        } else {
            user = new Staff();
            user.setPhone("00");
        }
        txtPassword = findViewById(R.id.edt_password);
        txtCurrentPassword = findViewById(R.id.edt_current_password);
        txtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnChangePassword = findViewById(R.id.btn_update_password);
        btnChangePassword.setOnClickListener(this);

    }

    @Override
    public String getMainTitle() {
        return getResources().getString(R.string.edit_pass_title);
    }

    @Override
    public void onCancelLoading() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public boolean isValid() {
        boolean isValid = true;
        txtPassword.setError(null);
        txtConfirmPassword.setError(null);
        View focusView = null;
        String password = txtPassword.getText().toString().trim();
        String currentPassword = txtCurrentPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPassword.getText().toString().trim();
        if (!Validation.isPasswordValid(currentPassword)) {
            txtCurrentPassword.setError(getString(R.string.error_invalid_password));
            isValid = false;
            focusView = txtCurrentPassword;
        } else if (!confirmPassword.equals(password)) {
            txtConfirmPassword.setError(getString(R.string.error_invalid_confirm_password));
            isValid = false;
            focusView = txtConfirmPassword;
        } else if (!Validation.isPasswordValid(password)) {
            txtPassword.setError(getString(R.string.error_invalid_password));
            isValid = false;
            focusView = txtPassword;
        }
        if (!isValid) {
            focusView.requestFocus();
        }
        return isValid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_password:
                if (isValid()) {
//                    callApiUpdate(patient.getPhone(), txtCurrentPassword.getText().toString().trim(), txtPassword.getText().toString().trim());
                }
                break;
        }
    }

    private Disposable userServiceDisposable;

//    public void callApiUpdate(String phone, String currentPassword, String newPassword) {
//        showLoading();
//        UserService userService = APIServiceManager.getService(UserService.class);
//
//        userService.changePassword(phone, currentPassword, newPassword)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        userServiceDisposable = d;
//                        hideLoading();
//                    }
//
//                    @Override
//                    public void onSuccess(Response<SuccessResponse> response) {
//                        if (response.isSuccessful()) {
//
//                            showMessage(getResources().getString(R.string.success_message_api));
//                            finish();
//                        } else {
//                            try {
//                                ErrorResponse errorResponse = Utils.parseJson(response.errorBody().string(), ErrorResponse.class);
//                                showDialog(errorResponse.getErrorMessage());
//                            } catch (IOException e) {
//
//                                showDialog(getResources().getString(R.string.error_message_api));
//                            } catch (JsonSyntaxException e){
////
//                                showDialog(getResources().getString(R.string.error_message_api));
//
//                            }
////                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditPasswordActivity.this)
////                                    .setMessage(getResources().getString(R.string.error_message_api))
////                                    .setPositiveButton("Thử lại", (DialogInterface dialogInterface, int i) -> {
////                                    });
////                            alertDialog.show();
//                        }
//                        hideLoading();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        logError("CallApiRegister", e.getMessage());
//                        hideLoading();
//                    }
//                });
//    }


}
