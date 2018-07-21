package com.dentalclinic.capstone.admin.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.CitySpinnerAdapter;
import com.dentalclinic.capstone.admin.adapter.DistrictSpinnerAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.requestobject.StaffProfileRequest;
import com.dentalclinic.capstone.admin.api.requestobject.UpdateUserRequest;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.api.services.StaffService;
import com.dentalclinic.capstone.admin.databaseHelper.DatabaseHelper;
import com.dentalclinic.capstone.admin.fragment.MyAccoutFragment;
import com.dentalclinic.capstone.admin.models.City;
import com.dentalclinic.capstone.admin.models.District;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.models.User;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.dentalclinic.capstone.admin.utils.Validation;

import java.util.Calendar;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class EditAccoutActivity extends BaseActivity implements View.OnClickListener {
    EditText txtName;
    EditText txtEmail;
    EditText txtDegree;
    TextView txtDateOfBirth, txtDateError;
    AutoCompleteTextView txtAddress;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale, rbOther;
    Spinner spDistrict, spCity;
    Button btnUpdate;
    Staff staff;
    private Disposable addrServiceDisposable;
    private Disposable registerServiceDisposable;
    private Disposable districtServiceDisposable;
    private DatabaseHelper cityDatabaseHelper = new DatabaseHelper(EditAccoutActivity.this);
    private DistrictSpinnerAdapter districtSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_accout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }
        Bundle bundle = getIntent().getBundleExtra(AppConst.BUNDLE);

        txtName = findViewById(R.id.edt_name);
        txtEmail = findViewById(R.id.edt_email);
        txtDegree = findViewById(R.id.edt_degree);
        rgGender = findViewById(R.id.rg_gender_register);
        rbMale = findViewById(R.id.rbt_male_register);
        rbFemale = findViewById(R.id.rbt_female_register);
        rbOther = findViewById(R.id.rbt_others_register);
        txtDateOfBirth = findViewById(R.id.txt_birthday);
        txtDateError = findViewById(R.id.txt_error_date);
        txtAddress = findViewById(R.id.edt_address);
        spDistrict = findViewById(R.id.spinner_district);
        spCity = findViewById(R.id.spinner_city);
        btnUpdate = findViewById(R.id.btn_register);
        btnUpdate.setOnClickListener(this);
        txtDateOfBirth.setOnClickListener(this);
        if (bundle.getSerializable(AppConst.STAFF_OBJ) != null) {
            staff = (Staff) bundle.getSerializable(AppConst.STAFF_OBJ);
            setDataUser(staff);
        } else {
            staff = new Staff();
        }

        if (cityDatabaseHelper.getAllCity().isEmpty()) {
            cityDatabaseHelper.insertDataCity();
        }
        if (cityDatabaseHelper.getAllDistrict().isEmpty()) {
            cityDatabaseHelper.insertDataDistrict();
        }
        spCity.setAdapter(new CitySpinnerAdapter(
                EditAccoutActivity.this,
                android.R.layout.simple_spinner_item, cityDatabaseHelper.getAllCity()));
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                City city = (City) spCity.getSelectedItem();
                if (city != null) {
                    spDistrict.setAdapter(new DistrictSpinnerAdapter(EditAccoutActivity.this,
                            android.R.layout.simple_spinner_item, cityDatabaseHelper.getDistrictOfCity(city.getId())));
                    if (staff.getDistrict() != null) {
                        spDistrict.setSelection(cityDatabaseHelper.getPositionDistrictById(staff.getDistrict()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (staff.getCity() != null) {
            spCity.setSelection(cityDatabaseHelper.getPositionCityById(staff.getCity().getId()));
        }
    }

    @Override
    public String getMainTitle() {
        return getResources().getString(R.string.edit_acc_title);
    }

    @Override
    public void onCancelLoading() {
        if (registerServiceDisposable != null) {
            registerServiceDisposable.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registerServiceDisposable != null) {
            registerServiceDisposable.dispose();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                attemptRegister();
                break;
            case R.id.txt_birthday:
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(EditAccoutActivity.this,
                        (DatePicker datePicker, int iYear, int iMonth, int iDay) -> {
                            txtDateOfBirth
                                    .setText(iYear + "-" + iMonth + "-" + iDay);
                            calendar.set(iYear, iMonth, iDay);
                            Calendar currentDay = Calendar.getInstance();
                            if (currentDay.before(calendar)) {
                                txtDateError.setText(getString(R.string.label_error_birthday));
                            } else {
                                txtDateError.setText("");

                            }
                        }, year, month, day);
                dialog.show();
                break;
        }
    }


    public void attemptRegister() {
        txtName.setError(null);
        txtAddress.setError(null);
        View focusView = null;
        boolean cancel = false;
        String name = txtName.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String degree = txtDegree.getText().toString().trim();
        String address = txtAddress.getText().toString().trim();
        String birthdayStr = txtDateOfBirth.getText().toString().trim();
        District district = (District) spDistrict.getSelectedItem();
        int districtID = -1;
        if (district != null) {
            districtID = district.getId();
        }
        if (!Validation.isNameValid(name)) {
            cancel = true;
            txtName.setError(getString(R.string.error_invalid_name));
            focusView = txtName;
        }
        if (!Validation.isEmailValid(email)) {
            cancel = true;
            txtEmail.setError(getString(R.string.error_invalid_email2));
            focusView = txtEmail;
        }
        if (!Validation.isDegreeValid(degree)) {
            cancel = true;
            txtDegree.setError(getString(R.string.error_invalid_degree));
            focusView = txtDegree;
        } else if (!Validation.isAddressValid(address)) {
            cancel = true;
            txtAddress.setError(getString(R.string.error_invalid_address));
            focusView = txtAddress;
        } else if (Validation.isNullOrEmpty(birthdayStr)) {
            cancel = true;
            txtDateOfBirth.setText(getString(R.string.label_error_birthday));
            focusView = txtDateOfBirth;
        } else if (birthdayStr != null && birthdayStr.equals(getString(R.string.label_birthday_register))) {
            cancel = true;
            txtDateError.setText(getString(R.string.label_error_birthday));
            focusView = txtDateError;
        }
        String gender = getGenderValue(rgGender.getCheckedRadioButtonId());
        if (cancel) {
            focusView.requestFocus();
        } else {
            StaffProfileRequest request = new StaffProfileRequest();
            request.setStaffId(staff.getId());
            request.setEmail(email);
            request.setName(name);
            request.setAddress(address);
            request.setGender(gender);
            request.setDegree(degree);
            request.setDistrictId(districtID);
            request.setBirthday(birthdayStr);
            request.setDistrict(district);
            callApiUpdate(request);

        }
    }

    public void setDataUser(Staff user) {
        if (user != null) {
            if (user.getName() != null) {
                txtName.setText(user.getName());
            }
            if (user.getEmail() != null) {
                txtEmail.setText(user.getEmail());
            }
            if (user.getDegree() != null) {
                txtDegree.setText(user.getDegree());
            }
            if (user.getAddress() != null) {
                txtAddress.setText(user.getAddress());
            }
            if (user.getDateOfBirth() != null) {
                txtDateOfBirth.setText(user.getDateOfBirth());
            }
            if (user.getGender() != null) {
                checkedGender(user.getGender());
            }
        }
    }

    public void checkedGender(String gender) {
        switch (gender) {
            case AppConst.GENDER_MALE:
                rbMale.setChecked(true);
                break;
            case AppConst.GENDER_FEMALE:
                rbFemale.setChecked(true);
                break;
            case AppConst.GENDER_OTHER:
                rbOther.setChecked(true);
                break;
            default:
                rbOther.setChecked(true);
                break;
        }
    }

    public String getGenderValue(int gender) {
        String value;
        switch (gender) {
            case R.id.rbt_male_register:
                value = AppConst.GENDER_MALE;
                break;
            case R.id.rbt_female_register:
                value = AppConst.GENDER_FEMALE;
                break;
            default:
                value = AppConst.GENDER_OTHER;
                break;
        }
        return value;
    }

    public void callApiUpdate(StaffProfileRequest requestObj) {
        showLoading();
        StaffService userService = APIServiceManager.getService(StaffService.class);
        userService.updateStaffInfo(requestObj)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        registerServiceDisposable = d;
                    }

                    @Override
                    public void onSuccess(Response<SuccessResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                showMessage("Thay đổi thành công");
                                staff.setEmail(requestObj.getEmail());
                                staff.setAddress(requestObj.getAddress());
                                staff.setName(requestObj.getName());
                                staff.setDegree(requestObj.getDegree());
                                staff.setGender(requestObj.getGender());
                                staff.setDateOfBirth(requestObj.getBirthday());
                                staff.setDistrict(requestObj.getDistrict());
                                CoreManager.setStaff(EditAccoutActivity.this, staff);
                                setResult(RESULT_OK);
                                finish();
                            }
                        } else if (response.code() == 500) {
                            showFatalError(response.errorBody(), "callApiUpdate");
                        } else if (response.code() == 401) {
                            showErrorUnAuth();
                        } else if (response.code() == 400) {
                            showBadRequestError(response.errorBody(), "callApiUpdate");
                        } else {
                            showDialog(getString(R.string.error_message_api));
                        }

                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        hideLoading();
                    }
                });
    }


}
