//package com.dentalclinic.capstone.admin.activities;
//
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AlertDialog;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//
//
//import com.dentalclinic.capstone.admin.R;
//
//import io.reactivex.SingleObserver;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
////import io.reactivex.schedulers.Schedulers;
//import retrofit2.Response;
//
//
//public class ForgotPasswordActivity extends BaseActivity {
//    private Button btnForgotPassword;
//    private AutoCompleteTextView txtPhone;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_forgot_password);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
//        }
//        txtPhone = findViewById(R.id.txt_phone_rspw);
//        btnForgotPassword = findViewById(R.id.btn_forgot_password);
//        btnForgotPassword.setOnClickListener((v) -> {
//            attemptResetPw();
//        });
//
//    }
//
//    @Override
//    public String getMainTitle() {
//        return "Lấy lại mật khẩu";
//    }
//
//    @Override
//    public void onCancelLoading() {
//
//    }
//
//    private void attemptResetPw() {
//        String phone = txtPhone.getText().toString();
//
//        boolean cancel = false;
//        View focusView = null;
//
//        // Check for a valid password, if the user entered one.
//
//        if (TextUtils.isEmpty(phone) && !isPhoneValid(phone)) {
//            txtPhone.setError(getString(R.string.error_invalid_phone));
//            focusView = txtPhone;
//            cancel = true;
//        }
//        if (cancel) {
//            focusView.requestFocus();
//            return;
//        } else {
//            callApiResetPW(phone);
//        }
//    }
//
//    private void callApiResetPW(String phone) {
//        UserService service = APIServiceManager.getService(UserService.class);
//        service.resetPassword(phone)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(Response<SuccessResponse> response) {
//                        if (response.isSuccessful()) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this)
//                                    .setCancelable(false)
//                                    .setTitle(getString(R.string.dialog_default_title))
//                                    .setMessage("Mật khẩu mới đã được gửi. Bạn vui lòng kiểm tra tin nhắn điện thoại.")
//                                    .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            finish();
//                                        }
//                                    });
//                            builder.create().show();
//                        } else if (response.code() == 500) {
//                            showFatalError(response.errorBody(), "callApiResetPW");
//                        } else if (response.code() == 401) {
//                            showErrorUnAuth();
//                        } else if (response.code() == 400) {
//                            showBadRequestError(response.errorBody(), "callApiResetPW");
//                        } else {
//                            showErrorMessage(getString(R.string.error_on_error_when_call_api));
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        showErrorMessage(getString(R.string.error_on_error_when_call_api));
//                    }
//                });
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        finish();
//        return true;
//    }
//}
