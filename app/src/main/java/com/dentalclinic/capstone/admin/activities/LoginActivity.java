package com.dentalclinic.capstone.admin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.RetrofitClient;
import com.dentalclinic.capstone.admin.api.requestobject.LoginRequest;
import com.dentalclinic.capstone.admin.api.services.StaffService;
import com.dentalclinic.capstone.admin.api.services.UserService;
import com.dentalclinic.capstone.admin.model.FingerAuthObj;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.models.User;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.marcoscg.fingerauth.FingerAuth;
import com.marcoscg.fingerauth.FingerAuthDialog;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

//import com.dentalclinic.capstone.admin.firebase.FirebaseDataReceiver;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {
    // UI references.
    private AutoCompleteTextView txtPhone;
    private EditText txtPassword;
    private View mProgressView;
    private View mLoginFormView;
    private View btnLinkAppointment;
    private View btnSingin;
    private TextView txtErrorServer;
    private FingerAuthDialog fingerAuthDialog;


    private Disposable disposable;

    @Override
    public String getMainTitle() {
        return "Đăng nhập";
    }

    @Override
    public void onCancelLoading() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Staff staff = CoreManager.getStaff(this);
        if (staff != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            final boolean hasFingerprintSupport = FingerAuth.hasFingerprintSupport(this);
            if (hasFingerprintSupport && CoreManager.getFingerAuthObj(LoginActivity.this)!=null) {
                createAndShowDialog();
            }
        }
        // Set up the login form.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }
        txtPhone = findViewById(R.id.txt_phone_loginact);
        txtErrorServer = findViewById(R.id.txt_error_server_loginact);
        txtPassword = findViewById(R.id.password);

        txtPassword.setOnEditorActionListener((TextView textView, int id, KeyEvent keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });
        mLoginFormView = findViewById(R.id.login_form);
        btnSingin = findViewById(R.id.btn_signin_loginact);
        btnSingin.setOnClickListener((view) ->
        {
            attemptLogin();
        });
        txtPassword.clearFocus();
        txtPhone.clearFocus();

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        txtErrorServer.setText("");
        txtPhone.setError(null);
        txtPassword.setError(null);

        // Store values at the time of the login attempt.
        String phone = txtPhone.getText().toString();
        String password = txtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.admin.

        if (TextUtils.isEmpty(phone) && !isPhoneValid(phone)) {
            txtPhone.setError(getString(R.string.error_invalid_phone));
            focusView = txtPhone;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            txtPassword.setError(getString(R.string.error_invalid_password));
            focusView = txtPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            callApiLogin(phone, password);
        }
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.matches("^\\d{9,}$");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 8;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void callApiLogin(String phone, String password) {
        showLoading();
        LoginRequest request = new LoginRequest();
        request.setPassword(password);
        request.setPhone(phone);
        request.setNotifToken(FirebaseInstanceId.getInstance().getToken());
        StaffService staffService = APIServiceManager.getService(StaffService.class);
        staffService.login(phone, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<Staff>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Response<Staff> userResponse) {
                        if (userResponse.isSuccessful()) {
//                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            //////donothing
                            if (userResponse.body() != null) {
                                Staff u = userResponse.body();
                                CoreManager.setStaff(LoginActivity.this, u);
                                CoreManager.setFingerAuthObj(LoginActivity.this,new FingerAuthObj(phone,password));
                                RetrofitClient.setAccessToken(u.getAccessToken());
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        } else if (userResponse.code() == 500) {
                            showFatalError(userResponse.errorBody(), "callApiLogin");
                        } else if (userResponse.code() == 401) {
                            showErrorUnAuth();
                        } else if (userResponse.code() == 400) {
                            showBadRequestError(userResponse.errorBody(), "callApiLogin");
                        } else {
                            showErrorMessage(getString(R.string.error_on_error_when_call_api));
                        }
                        hideLoading();

                    }

                    @Override
                    public void onError(Throwable e) {
                        showWarningMessage(getResources().getString(R.string.error_on_error_when_call_api));
                        logError(LoginActivity.class, "attemptLogin", e.getMessage());
                        hideLoading();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void createAndShowDialog() {
        fingerAuthDialog = new FingerAuthDialog(this)
                .setTitle("Đăng Nhập Vân Tay")
                .setCancelable(false)
                .setPositiveButton("Đăng Nhập", null)
                .setNegativeButton("Hủy", null)
                .setOnFingerAuthListener(new FingerAuth.OnFingerAuthListener() {
                    @Override
                    public void onSuccess() {
                        FingerAuthObj fingerAuthObj= CoreManager.getFingerAuthObj(LoginActivity.this);
                        if(fingerAuthObj!=null){
                            callApiLogin(fingerAuthObj.getPhone(),fingerAuthObj.getPassword());
                        }else{
                            showErrorMessage("Đăng nhập vân tay không thành công.");
                        }
//                        Toast.makeText(LoginActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {
                        showErrorMessage("Đăng nhập vân tay không thành công.");

//                        Toast.makeText(LoginActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        showErrorMessage("Đăng nhập vân tay không thành công.");

//                        Toast.makeText(LoginActivity.this, "onError", Toast.LENGTH_SHORT).show();
                    }
                });
        fingerAuthDialog.show();
    }
}

