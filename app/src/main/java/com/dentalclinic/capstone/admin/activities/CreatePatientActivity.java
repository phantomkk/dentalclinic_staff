package com.dentalclinic.capstone.admin.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.CitySpinnerAdapter;
import com.dentalclinic.capstone.admin.adapter.DistrictSpinnerAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.requestobject.PatientProfileRequest;
import com.dentalclinic.capstone.admin.api.services.AddressService;
import com.dentalclinic.capstone.admin.api.services.AnamnesisCatalogService;
import com.dentalclinic.capstone.admin.api.services.StaffService;
import com.dentalclinic.capstone.admin.databaseHelper.DatabaseHelper;
import com.dentalclinic.capstone.admin.models.AnamnesisCatalog;
import com.dentalclinic.capstone.admin.models.City;
import com.dentalclinic.capstone.admin.models.District;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.Validation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class CreatePatientActivity extends BaseActivity {
    private Disposable addrServiceDisposable;
    private Disposable createServiceDisposable;
    private Disposable districtServiceDisposable;

    private EditText edtFullname;
    private EditText edtPhone;
    private EditText edtConfirmPassword;
    private RadioGroup radioGroup;
    private EditText edtAddress;
    private Button btnRegister;
    private Button btnSelectAnamnesis;
    private TextView tvBirthday;
    private TextView tvErrorBirthday;
    private Spinner spnCity;
    private Spinner spnDistrict;
    private DatabaseHelper cityDatabaseHelper = new DatabaseHelper(CreatePatientActivity.this);
    public static int REQUEST_CODE_ANAMNESIS = 111;
    public static String LIST_ANAMNESIS = "LIST_ANAMNESIS";
    public static String PATIENT_ANAMNESIS = "PATIENT_ANAMNESIS";
    public static String PREVIOUS_ANAMNESIS = "PREVIOUS_ANAMNESIS";
    AddressService addressService = APIServiceManager.getService(AddressService.class);

    private ArrayList<AnamnesisCatalog> listAnamnesisCatalog;
    private ArrayList<AnamnesisCatalog> patientAnamnesis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_patient);
        edtFullname = findViewById(R.id.edt_fullname_register);
        edtPhone = findViewById(R.id.edt_phone_register);
        edtAddress = findViewById(R.id.edt_address_register);
        radioGroup = findViewById(R.id.rg_gender_register);
        tvBirthday = findViewById(R.id.tv_birthday_register);
        tvErrorBirthday = findViewById(R.id.txt_error_date_register);
        btnRegister = findViewById(R.id.btn_register);
        btnSelectAnamnesis = findViewById(R.id.btn_select_anamnesis);
        spnCity = findViewById(R.id.spinner_city_register);
        spnDistrict = findViewById(R.id.spinner_district_register);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        btnRegister.setOnClickListener((view) -> {
            attemptRegister();
        });

        setEventForBirthday();

        if (cityDatabaseHelper.getAllCity().isEmpty()) {
            cityDatabaseHelper.insertDataCity();
        }
        if (cityDatabaseHelper.getAllDistrict().isEmpty()) {
            cityDatabaseHelper.insertDataDistrict();
        }
        spnCity.setAdapter(new CitySpinnerAdapter(
                CreatePatientActivity.this,
                android.R.layout.simple_spinner_item, cityDatabaseHelper.getAllCity()));
        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                City city = (City) spnCity.getSelectedItem();
                if (city != null) {
                    spnDistrict.setAdapter(new DistrictSpinnerAdapter(CreatePatientActivity.this,
                            android.R.layout.simple_spinner_item, cityDatabaseHelper.getDistrictOfCity(city.getId())));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listAnamnesisCatalog = new ArrayList<>();
        patientAnamnesis = new ArrayList<>();
        initAnamnesisData();
        btnSelectAnamnesis.setOnClickListener((v) -> {
            Intent intent = new Intent(CreatePatientActivity.this, AnamnesisListActivity.class);
//            Bundle bundleList = new Bundle();;
//            bundleList.putParcelableArrayList("LIST_ANAMNESIS", listAnamnesisCatalog);
            intent.putExtra(LIST_ANAMNESIS, listAnamnesisCatalog);
            intent.putExtra(PREVIOUS_ANAMNESIS, patientAnamnesis);
            startActivityForResult(intent, REQUEST_CODE_ANAMNESIS);
        });
    }

    private void initAnamnesisData() {
        showLoading();
        AnamnesisCatalogService service = APIServiceManager.getService(AnamnesisCatalogService.class);
        service.getAllCatalog().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<AnamnesisCatalog>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<List<AnamnesisCatalog>> listResponse) {
                        if (listResponse.isSuccessful()) {
                            if (listResponse.body() != null) {
                                listAnamnesisCatalog.addAll(listResponse.body());
                            }
                        } else if (listResponse.code() == 500) {
//                            showFatalError(listResponse.errorBody(), "getAnamnesisCatalog");
                        } else if (listResponse.code() == 401) {
//                            showErrorUnAuth();
                        } else if (listResponse.code() == 400) {
//                            showBadRequestError(listResponse.errorBody(), "getAnamnesisCatalog");
                        } else {
//                            showErrorMessage(getString(R.string.error_on_error_when_call_api));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle b = (data == null ? null : data.getExtras());
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ANAMNESIS) {
                if (b != null) {
                    ArrayList<AnamnesisCatalog> list = b.get(PATIENT_ANAMNESIS) instanceof ArrayList ?
                            (ArrayList<AnamnesisCatalog>) b.get(PATIENT_ANAMNESIS) : null;
                    if (list != null) {
                        patientAnamnesis.addAll(list);
                    }
                }
            }
        }
    }

    public void setEventForBirthday() {
        //init birthday section
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        tvBirthday.setOnClickListener((view) -> {
            DatePickerDialog dialog = new DatePickerDialog(CreatePatientActivity.this,
                    (DatePicker datePicker, int iYear, int iMonth, int iDay) -> {
                        tvBirthday
                                .setText(iYear + "-" + iMonth + "-" + iDay);
                        calendar.set(iYear, iMonth, iDay);
                        Calendar currentDay = Calendar.getInstance();
                        if (currentDay.before(calendar)) {
                            tvErrorBirthday.setText(getString(R.string.label_error_birthday));
                        } else {
                            tvErrorBirthday.setText("");

                        }
                    }, year, month, day);
            dialog.show();
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void attemptRegister() {
        edtFullname.setError(null);
        edtPhone.setError(null);
        edtAddress.setError(null);
        View focusView = null;
        boolean cancel = false;
        String name = edtFullname.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        String birthdayStr = tvBirthday.getText().toString().trim();
        District district = (District) spnDistrict.getSelectedItem();
        int districtID = -1;
        if (district != null) {
            districtID = district.getId();
        }
        if (!Validation.isNameValid(name)) {
            cancel = true;
            edtFullname.setError(getString(R.string.error_invalid_name));
            focusView = edtFullname;
        } else if (!Validation.isPhoneValid(phone)) {
            cancel = true;
            edtPhone.setError(getString(R.string.error_invalid_phone));
            focusView = edtPhone;

        } else if (!Validation.isAddressValid(address)) {
            cancel = true;
            edtAddress.setError(getString(R.string.error_invalid_address));
            focusView = edtAddress;
        } else if (Validation.isNullOrEmpty(birthdayStr)) {
            cancel = true;
            tvErrorBirthday.setText(getString(R.string.label_error_birthday));
            focusView = tvBirthday;
        } else if (birthdayStr != null && birthdayStr.equals(getString(R.string.label_birthday_register))) {
            cancel = true;
            tvErrorBirthday.setText(getString(R.string.label_error_birthday));
            focusView = tvBirthday;
        }
        String gender = getGenderValue(radioGroup.getCheckedRadioButtonId());
        if (cancel) {
            focusView.requestFocus();
        } else {
            PatientProfileRequest profileRequest = new PatientProfileRequest();
            profileRequest.setPhone(phone);
            profileRequest.setAddress(address);
            profileRequest.setDistrictId(districtID);
            profileRequest.setName(name);
            profileRequest.setGender(gender);
            profileRequest.setBirthday(birthdayStr);
            createPatientAPI(profileRequest);

        }
    }

    public String getGenderValue(int id) {
        String value;
        switch (id) {
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

    public void createPatientAPI(PatientProfileRequest requestObj) {
        showLoading();
        StaffService service = APIServiceManager.getService(StaffService.class);
        service.createProfile(requestObj)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<Patient>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        createServiceDisposable = d;
                    }

                    @Override
                    public void onSuccess(Response<Patient> patientResponse) {
                        if (patientResponse.isSuccessful()) {
                            Toast.makeText(CreatePatientActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreatePatientActivity.this)
                                    .setMessage("Đăng kí tài khoản thành công")
                                    .setPositiveButton("Đăng nhập", (DialogInterface dialogInterface, int i) -> {
                                        Intent intent = new Intent(CreatePatientActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    });
                            alertDialog.show();
                        } else if (patientResponse.code() == 500) {
                            showFatalError(patientResponse.errorBody(), "createPatientAPI");
                        } else if (patientResponse.code() == 401) {
                            showErrorUnAuth();
                        } else if (patientResponse.code() == 400) {
                            showBadRequestError(patientResponse.errorBody(), "createPatientAPI");
                        } else {
                            showErrorMessage(getString(R.string.error_on_error_when_call_api));
                        }

                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        logError("createPatientAPI", e.getMessage());
                        hideLoading();
                    }
                });
    }


    @Override
    public String getMainTitle() {
        return "Đăng kí tài khoản";
    }

    @Override
    public void onCancelLoading() {
        //do nothing
        if (addrServiceDisposable != null) {
            addrServiceDisposable.dispose();
        }
        if (districtServiceDisposable != null) {
            districtServiceDisposable.dispose();
        }
        if (createServiceDisposable != null) {
            createServiceDisposable.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addrServiceDisposable != null) {
            addrServiceDisposable.dispose();
        }
        if (districtServiceDisposable != null) {
            districtServiceDisposable.dispose();
        }
        if (createServiceDisposable != null) {
            createServiceDisposable.dispose();
        }
    }
}
