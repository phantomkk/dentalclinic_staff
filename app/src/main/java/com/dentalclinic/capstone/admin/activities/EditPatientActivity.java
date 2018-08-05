package com.dentalclinic.capstone.admin.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.UnicodeSet;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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
import com.dentalclinic.capstone.admin.api.requestobject.PatientProfileRequest;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.api.services.AddressService;
import com.dentalclinic.capstone.admin.api.services.AnamnesisCatalogService;
import com.dentalclinic.capstone.admin.api.services.PatientService;
import com.dentalclinic.capstone.admin.api.services.StaffService;
import com.dentalclinic.capstone.admin.databaseHelper.DatabaseHelper;
import com.dentalclinic.capstone.admin.models.AnamnesisCatalog;
import com.dentalclinic.capstone.admin.models.City;
import com.dentalclinic.capstone.admin.models.District;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.dentalclinic.capstone.admin.utils.GenderUtils;
import com.dentalclinic.capstone.admin.utils.Validation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class EditPatientActivity extends BaseActivity {
    private Disposable addrServiceDisposable;
    private Disposable createServiceDisposable;
    private Disposable districtServiceDisposable;

    private EditText edtFullname;
    private EditText edtPhone;
    private RadioGroup radioGroup;
    private EditText edtAddress;
    private Button btnUpdate;
    private Button btnSelectAnamnesis;
    private TextView tvBirthday;
    private TextView tvErrorBirthday;
    private Spinner spnCity;
    private Spinner spnDistrict;
    private Patient patient;
    private DatabaseHelper cityDatabaseHelper = new DatabaseHelper(EditPatientActivity.this);
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
        setContentView(R.layout.activity_edit_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }
        if (patientAnamnesis == null) {
            patientAnamnesis = new ArrayList<>();
        }

        edtFullname = findViewById(R.id.edt_fullname_edit);
        edtPhone = findViewById(R.id.edt_phone_edit);
        edtAddress = findViewById(R.id.edt_address_edit);
        radioGroup = findViewById(R.id.rg_gender_edit);
        tvBirthday = findViewById(R.id.tv_birthday_edit);
        tvErrorBirthday = findViewById(R.id.txt_error_date_edit);
        btnUpdate = findViewById(R.id.btn_edit);
        btnSelectAnamnesis = findViewById(R.id.btn_select_anamnesis);
        spnCity = findViewById(R.id.spinner_city_edit);
        spnDistrict = findViewById(R.id.spinner_district_edit);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        Intent i = getIntent();
        if (i != null) {
            patient = (Patient) i.getSerializableExtra(AppConst.PATIENT_OBJ);
            if (patient != null && patient.getListAnamnesis() != null) {
                patientAnamnesis.addAll(patient.getListAnamnesis());
            }
            setData(patient);
        }
        btnUpdate.setOnClickListener((view) -> {
            attemptUpdate();
        });

        setEventForBirthday();

        if (cityDatabaseHelper.getAllCity().isEmpty()) {
            cityDatabaseHelper.insertDataCity();
        }
        if (cityDatabaseHelper.getAllDistrict().isEmpty()) {
            cityDatabaseHelper.insertDataDistrict();
        }
        spnCity.setAdapter(new CitySpinnerAdapter(
                EditPatientActivity.this,
                android.R.layout.simple_spinner_item, cityDatabaseHelper.getAllCity()));
        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                City city = (City) spnCity.getSelectedItem();
                if (city != null) {
                    spnDistrict.setAdapter(new DistrictSpinnerAdapter(EditPatientActivity.this,
                            android.R.layout.simple_spinner_item, cityDatabaseHelper.getDistrictOfCity(city.getId())));


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (patient.getDistrict() != null) {
            spnDistrict.setSelection(cityDatabaseHelper.getPositionDistrictById(patient.getDistrict()));
        } if (patient.getCity() != null) {
            spnCity.setSelection(cityDatabaseHelper.getPositionCityById(patient.getCity().getId()));
        }
        listAnamnesisCatalog = new ArrayList<>();
        initAnamnesisData();
        btnSelectAnamnesis.setOnClickListener((v) -> {
            Intent intent = new Intent(EditPatientActivity.this, AnamnesisListActivity.class);
//            Bundle bundleList = new Bundle();;
//            bundleList.putParcelableArrayList("LIST_ANAMNESIS", listAnamnesisCatalog);
            intent.putExtra(LIST_ANAMNESIS, listAnamnesisCatalog);
            intent.putExtra(PREVIOUS_ANAMNESIS, patientAnamnesis);
            startActivityForResult(intent, REQUEST_CODE_ANAMNESIS);
        });
    }

    private void setData(Patient patient) {
        if (patient != null) {
//            if (patient.getAvatar() != null && patient.getAvatar().trim().length() > 0) {
////                Picasso.get().invalidate(patient.getAvatar());
//                Picasso.get().load(patient.getAvatar()).into(cvAvatar);
//            }
            if (patient.getName() != null) {
                edtFullname.setText(patient.getName());
            }
            if (patient.getDateOfBirth() != null) {
                tvBirthday.setText(DateUtils.changeDateFormat(patient.getDateOfBirth(), DateTimeFormat.DATE_TIME_DB_2, DateTimeFormat.DATE_APP));
            }
            if (patient.getGender() != null) {
                switch (patient.getGender()) {
                    case "MALE":
                        RadioButton v1 = findViewById(R.id.rbt_male_edit);
                        v1.setChecked(true);
                        break;
                    case "FEMALE":
                        RadioButton v2 = findViewById(R.id.rbt_female_edit);
                        v2.setChecked(true);
                        break;
                    default:
                        RadioButton v3 = findViewById(R.id.rbt_others_edit);
                        v3.setChecked(true);
                        break;
                }
            }
            if (patient.getPhone() != null) {
                edtPhone.setText(patient.getPhone());
            }
            if (patient.getAddress() != null) {
                String address = patient.getAddress();
                edtAddress.setText(address);
            }
        }
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
                        patientAnamnesis.clear();
                        patientAnamnesis.addAll(list);
                        String listAnamnesis = "";
                        for (AnamnesisCatalog a : patientAnamnesis) {
                            listAnamnesis += a.getName() + "\n";
                        }
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
            DatePickerDialog dialog = new DatePickerDialog(EditPatientActivity.this,
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

    public void attemptUpdate() {
        edtFullname.setError(null);
        edtPhone.setError(null);
        edtAddress.setError(null);
        View focusView = null;
        boolean cancel = false;
        String name = edtFullname.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
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
        List<Integer> listAnamnesis = new ArrayList<>();
        for (AnamnesisCatalog ac : patientAnamnesis) {
            listAnamnesis.add(ac.getId());
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            PatientProfileRequest profileRequest = new PatientProfileRequest();
            profileRequest.setPhone(phone);
            profileRequest.setId(patient.getId()+"");
            profileRequest.setAddress(address);
            profileRequest.setDistrictId(districtID);
            profileRequest.setName(name);
            profileRequest.setGender(gender);
            String newBirth = DateUtils.changeDateFormat(birthdayStr,DateTimeFormat.DATE_APP, DateTimeFormat.DATE_TIME_DB);
            profileRequest.setBirthday(newBirth);
            if (listAnamnesis.size() > 0) {
                profileRequest.setListAnamnesis(listAnamnesis);
            }
            createPatientAPI(profileRequest);

        }
    }

    public String getGenderValue(int id) {
        String value;
        switch (id) {
            case R.id.rbt_male_edit:
                value = AppConst.GENDER_MALE;
                break;
            case R.id.rbt_female_edit:
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
        PatientService service = APIServiceManager.getService(PatientService.class);
        service.changePatientInfo(requestObj)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        createServiceDisposable = d;
                    }

                    @Override
                    public void onSuccess(Response<SuccessResponse> patientResponse) {
                        if (patientResponse.isSuccessful()) {
//                            Toast.makeText(CreatePatientActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreatePatientActivity.this)
//                                    .setMessage("Đăng kí tài khoản thành công")
//                                    .setPositiveButton("Đăng nhập", (DialogInterface dialogInterface, int i) -> {
////                                        Intent intent = new Intent(CreatePatientActivity.this, LoginActivity.class);
////                                        startActivity(intent);
//                                    });
//                            alertDialog.show();
                            setResult(RESULT_OK);
                            finish();
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
        return "Chỉnh sửa thông tin bệnh nhân";
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
