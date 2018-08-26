package com.dentalclinic.capstone.admin.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import io.reactivex.functions.BiFunction;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.CombinePatientClass;
import com.dentalclinic.capstone.admin.api.RetrofitClient;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.api.services.AppointmentService;
import com.dentalclinic.capstone.admin.api.services.PatientService;
import com.dentalclinic.capstone.admin.api.services.StaffService;
import com.dentalclinic.capstone.admin.api.services.UserService;
import com.dentalclinic.capstone.admin.fragment.AbsentFragment;
import com.dentalclinic.capstone.admin.fragment.Appointment2Fragment;
import com.dentalclinic.capstone.admin.fragment.AppointmentFragment;
import com.dentalclinic.capstone.admin.fragment.BarChartFragment;
import com.dentalclinic.capstone.admin.fragment.BaseWeekViewFragment;
import com.dentalclinic.capstone.admin.fragment.ChartFragment;
import com.dentalclinic.capstone.admin.fragment.MyAccoutFragment;
import com.dentalclinic.capstone.admin.fragment.SearchPatientFragment;
import com.dentalclinic.capstone.admin.fragment.SettingFragment;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.dentalclinic.capstone.admin.utils.SettingManager;
import com.dentalclinic.capstone.admin.utils.Utils;
import com.github.dewinjm.monthyearpicker.Util;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager = getSupportFragmentManager();
    private MaterialSearchView searchView;
    List<String> strings = new ArrayList<>();
    private CircleImageView mAvatar;
    private TextView mStaffName, mStaffPhone;
    Staff staff = new Staff();
    private NavigationView navigationView;
    private String phone = "";
    private MenuItem dentistAppointmentItem;
    private MenuItem clinicAppointmentItem;
    private MenuItem selectedMenuItem;
    private final int REQUEST_CREATE_PATIENT = 109;
    private static int numDentistAppointment = 0;
    private static int numClinicAppointment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Staff staff = CoreManager.getStaff(this);
        if (staff != null) {
            RetrofitClient.setAccessToken(staff.getAccessToken());
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        mAvatar = header.findViewById(R.id.img_staf_avatar);
        mStaffName = header.findViewById(R.id.txt_staff_name);
        mStaffPhone = header.findViewById(R.id.txt_staff_phone);
        staff = CoreManager.getStaff(MainActivity.this);
//        Utils.setVNLocale(this);
        SettingManager.initSetting(this);
        setDataHeader(staff);
//
//        getAllPhone();
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(true);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
//        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });
        searchView.setSubmitOnClick(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getPatienst(query);
                setTitle(query);
                searchPatientFragment.setPhone(query);
                phone = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.closeSearch();
        searchPatientFragment = new SearchPatientFragment();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, searchPatientFragment).commit();
        navigationView.getMenu().getItem(0).setChecked(true);

        if (Utils.isRceiption(MainActivity.this)) {
            navigationView.getMenu().findItem(R.id.nav_history).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_appointment_list).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_chart).setVisible(false);
            clinicAppointmentItem = navigationView.getMenu().findItem(R.id.nav_appointment_list_2);
//            setNumMenuItem(clinicAppointmentItem, 0);
            Utils.subscribeReloadClinicAppointment();
        } else if (Utils.isDentist(MainActivity.this)) {
            navigationView.getMenu().findItem(R.id.nav_appointment_list_2).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_bar_chart).setVisible(false);
            dentistAppointmentItem = navigationView.getMenu().findItem(R.id.nav_appointment_list);
//            setNumMenuItem(dentistAppointmentItem, 0);
            Utils.unsubscribeReloadClinicAppointment();
        }
    }

    private Disposable disposable;

    public MaterialSearchView getSearchView() {
        return searchView;
    }

    public void setSugesstion(List<String> sugesstion) {
        String[] stockArr = new String[sugesstion.size()];
        stockArr = sugesstion.toArray(stockArr);
        searchView.setSuggestions(stockArr);
    }

    public void getAllPhone() {
        showLoading();
        UserService userService = APIServiceManager.getService(UserService.class);
        userService.getAllPhone("")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<String>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Response<List<String>> userResponse) {
                        if (userResponse.isSuccessful()) {
                            strings = userResponse.body();
                            String[] stockArr = new String[strings.size()];
                            stockArr = strings.toArray(stockArr);
                            searchView.setSuggestions(stockArr);
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
                        logError(LoginActivity.class, "attemptLogin", e.getMessage());
                        hideLoading();
                    }
                });
    }

    public void getPatienst(String phone) {
        showLoading();
        Single patient = APIServiceManager.getService(PatientService.class)
                .getPatientsByPhone(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Single appointment = APIServiceManager.getService(AppointmentService.class)
                .getAppointmentByPhone(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Single<CombinePatientClass> combine = Single.zip(patient, appointment,
                new BiFunction<Response<List<Patient>>, Response<List<Appointment>>, CombinePatientClass>() {
                    @Override
                    public CombinePatientClass apply(Response<List<Patient>> patientResponse, Response<List<Appointment>> appointmentRespone) throws Exception {
                        return new CombinePatientClass(patientResponse, appointmentRespone);
                    }
                });
        combine.subscribe(new SingleObserver<CombinePatientClass>() {
            @Override
            public void onSubscribe(Disposable d) {
//                disposable = d;
            }

            @Override
            public void onSuccess(CombinePatientClass combinePatientClass) {
                if (combinePatientClass.getPatients() != null) {
                    if (combinePatientClass.getPatients().isSuccessful()) {
                        if (combinePatientClass.getPatients().body().isEmpty()) {
                            if (Utils.isRceiption(MainActivity.this)) {
                                showConfigCreateNewPatientDialog("Tạo thông tin bệnh nhân cho tài khoản này?");
                            }
                            if (searchPatientFragment != null) {
                                searchPatientFragment.setPatientsAndNotifiAdapter(new ArrayList<Patient>());
//                                searchPatientFragment.setAppointmentAndNotifiAdapter(new ArrayList<Appointment>());
                                searchPatientFragment.removeAllButton();
                                if (Utils.isRceiption(MainActivity.this)) {
                                    searchPatientFragment.addButtonNewPatient();
                                }
                                searchPatientFragment.addButtonAppointment();
                            }
                        } else {
                            if (searchPatientFragment != null) {
                                searchPatientFragment.setPatientsAndNotifiAdapter(combinePatientClass.getPatients().body());
//                                searchPatientFragment.setAppointmentAndNotifiAdapter(response.body().getAppointments());
//                                searchPatientFragment.enableAllButton();
                                searchPatientFragment.removeAllButton();
                                if (Utils.isRceiption(MainActivity.this)) {
                                    searchPatientFragment.addButtonNewPatient();
                                    searchPatientFragment.addButtonPayment();
                                }
                                searchPatientFragment.addButtonAppointment();
                            }
                        }
                    } else if (combinePatientClass.getPatients().code() == 500) {
                        showFatalError(combinePatientClass.getPatients().errorBody(), "callApiLogin");
                    } else if (combinePatientClass.getPatients().code() == 401) {
                        showErrorUnAuth();
                    } else if (combinePatientClass.getPatients().code() == 400) {

                        if (Utils.isRceiption(MainActivity.this)) {
                            showConfigCreateNewUserDialog("Tạo tài khoản cho bệnh nhân?");
                        }
                        if (searchPatientFragment != null) {
                            searchPatientFragment.setPatientsAndNotifiAdapter(new ArrayList<Patient>());
//                            searchPatientFragment.setAppointmentAndNotifiAdapter(new ArrayList<Appointment>());
                            searchPatientFragment.removeAllButton();
//                            searchPatientFragment.addButtonNewPatient();
                            if (Utils.isRceiption(MainActivity.this)) {
                                searchPatientFragment.addButtonNewPatient();
                            }
                            searchPatientFragment.addButtonAppointment();
                        }
//                        showBadRequestError(combinePatientClass.getAppointments().errorBody(),"combineGetAppointment");
                        logError("calPatient", "lỗi");
                    } else {
                        showErrorMessage(getString(R.string.error_on_error_when_call_api));
                    }
                }

                if (combinePatientClass.getAppointments() != null) {
                    if (combinePatientClass.getAppointments().isSuccessful()) {
                        if (combinePatientClass.getAppointments().body().isEmpty()) {
                            if (searchPatientFragment != null) {
                                searchPatientFragment.setAppointmentAndNotifiAdapter(new ArrayList<Appointment>());
                            }
                        } else {
                            if (searchPatientFragment != null) {
                                searchPatientFragment.setAppointmentAndNotifiAdapter(combinePatientClass.getAppointments().body());
                            }
                        }
                    } else if (combinePatientClass.getAppointments().code() == 500) {
                        showFatalError(combinePatientClass.getAppointments().errorBody(), "callApiLogin");
                    } else if (combinePatientClass.getAppointments().code() == 401) {
                        showErrorUnAuth();
                    } else if (combinePatientClass.getAppointments().code() == 400) {
                        showBadRequestError(combinePatientClass.getAppointments().errorBody(), "combineGetAppointment");
                    } else {
                        showErrorMessage(getString(R.string.error_on_error_when_call_api));
                    }
                }
                hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                hideLoading();
            }
        });


//        AppointmentService service = APIServiceManager.getService(AppointmentService.class);
//        service.getAppointmentByPhone(phone)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<Response<List<Appointment>>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        disposable = d;
//                    }
//
//                    @Override
//                    public void onSuccess(Response<List<Appointment>> response) {
//                        if (response.isSuccessful()) {
//                            if (response.body().isEmpty()) {
////                                if (Utils.isRceiption(MainActivity.this)) {
////                                    showConfigCreateNewPatientDialog("Tạo thông tin bệnh nhân cho tài khoản này?");
////                                }
////                                if (searchPatientFragment != null) {
////                                    searchPatientFragment.setPatientsAndNotifiAdapter(new ArrayList<Patient>());
////                                    searchPatientFragment.setAppointmentAndNotifiAdapter(new ArrayList<Appointment>());
////                                    searchPatientFragment.removeAllButton();
////                                    searchPatientFragment.addButtonNewPatient();
////                                }
//                                searchPatientFragment.setAppointmentAndNotifiAdapter(new ArrayList<Appointment>());
//                            } else {
//                                if (searchPatientFragment != null) {
////                                    searchPatientFragment.setPatientsAndNotifiAdapter(response.body().getPatients());
////                                    if (response.body().getAppointments() != null) {
//                                    searchPatientFragment.setAppointmentAndNotifiAdapter(response.body());
////                                    }
////                                    searchPatientFragment.enableAllButton();
////                                    searchPatientFragment.removeAllButton();
////                                    searchPatientFragment.addButtonNewPatient();
////                                    searchPatientFragment.addButtonPayment();
////                                    searchPatientFragment.addButtonAppointment();
//
//                                }
//                            }
//                        } else if (response.code() == 500) {
//                            showFatalError(response.errorBody(), "callApiLogin");
//                        } else if (response.code() == 401) {
//                            showErrorUnAuth();
//                        } else if (response.code() == 400) {
//                            if (Utils.isRceiption(MainActivity.this)) {
//                                showConfigCreateNewUserDialog("Tạo tài khoản cho bệnh nhân?");
//                            }
//                            if (searchPatientFragment != null) {
//                                searchPatientFragment.setPatientsAndNotifiAdapter(new ArrayList<Patient>());
//                                searchPatientFragment.setAppointmentAndNotifiAdapter(new ArrayList<Appointment>());
//                                searchPatientFragment.removeAllButton();
//                                searchPatientFragment.addButtonNewPatient();
//                            }
//                        } else {
//                            showErrorMessage(getString(R.string.error_on_error_when_call_api));
//                        }
//                        hideLoading();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        logError(LoginActivity.class, "attemptLogin", e.getMessage());
//                        hideLoading();
//                    }
//                });
    }

    @Override
    public String getMainTitle() {
        return "Quản lí phòng khám";
    }

    @Override
    public void onCancelLoading() {

    }

    public void showConfigCreateNewUserDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .setTitle(getString(R.string.dialog_default_title))
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, CreatePatientActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConst.PHONE, phone);
                        intent.putExtra(AppConst.BUNDLE, bundle);
                        startActivityForResult(intent, REQUEST_CREATE_PATIENT);
                    }
                });
        alertDialog.show();
    }

    public void showConfigCreateNewPatientDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_default_title))
                .setMessage(message)
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Thêm thông tin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        showMessage("Tạo mới bệnh nhân");
                        Intent intent = new Intent(MainActivity.this, CreatePatientActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConst.PHONE, phone);
                        intent.putExtra(AppConst.BUNDLE, bundle);
                        startActivityForResult(intent, REQUEST_CREATE_PATIENT);
                    }
                });
        alertDialog.show();
    }


    public void setDataHeader(Staff staff) {
        if (staff == null) {
            return;
        }
        if (staff.getAvatar() != null) {
            Picasso.get().load(staff.getAvatar()).error(R.drawable.avatar).into(mAvatar);
        }
        if (staff.getName() != null) {
            mStaffName.setText(staff.getName());
        }
        if (staff.getPhone() != null) {
            mStaffPhone.setText(staff.getPhone());
        }
    }
//    private void addButton

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private SearchPatientFragment searchPatientFragment;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        selectedMenuItem = item;
        int id = item.getItemId();
        if (id == R.id.nav_search_patient) {
//            Intent intent = new Intent(MainActivity.this, SearchPatientActivity.class);
//            startActivity(intent);
            searchPatientFragment = new SearchPatientFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, searchPatientFragment).commit();
        } else if (id == R.id.nav_history) {
            setTitle("Lịch hẹn");
            BaseWeekViewFragment calendarFragment = new BaseWeekViewFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, calendarFragment).commit();
        } else if (id == R.id.nav_appointment_list_2) {
            setTitle("Nhận bệnh");
            clearNumClinicAppointment(item);
            Appointment2Fragment appointment2Fragment = new Appointment2Fragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, appointment2Fragment).commit();
        } else if (id == R.id.nav_appointment_list) {
            //clear number in tab menuItem
            clearNumDentistAppointment(item);
            ///////////////////////
            setTitle("Khám bệnh");
            AppointmentFragment calendarFragment = new AppointmentFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, calendarFragment).commit();
        } else if (id == R.id.nav_bar_chart) {
            setTitle(getResources().getString(R.string.bar_chart_title));
            BarChartFragment barChartFragment = new BarChartFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, barChartFragment).commit();
        } else if (id == R.id.nav_chart) {
            setTitle(getResources().getString(R.string.chart_title));
            ChartFragment chartFragment = new ChartFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, chartFragment).commit();
        } else if (id == R.id.nav_request_absent) {
            setTitle("Nghỉ phép");
//            startActivity(new Intent(MainActivity.this, DatePickerActivity.class));
            AbsentFragment absentFragment = new AbsentFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, absentFragment).commit();
        } else if (id == R.id.nav_my_accout) {
            setTitle("Thông tin tài khoản");
            MyAccoutFragment newFragment = new MyAccoutFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, newFragment).commit();
        } else if (id == R.id.nav_setting) {
            setTitle("Cài Đặt");
            SettingFragment settingFragment = new SettingFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, settingFragment).commit();
            //donothing
        } else if (id == R.id.nav_log_out) {
            logoutOnServer();
            CoreManager.clearStaff(MainActivity.this);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            Utils.unsubscribeReloadClinicAppointment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutOnServer() {
        Staff staff = CoreManager.getStaff(this);
        StaffService staffService = APIServiceManager.getService(StaffService.class);
        staffService.logout(staff.getPhone()).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<SuccessResponse> successResponse) {
                        if (successResponse.isSuccessful()) {
                            logError("Logout on server", "Log out success");
                        } else if (successResponse.code() == 500) {
                            logError("Logout on server", "Log out 500");
                        } else if (successResponse.code() == 401) {
                            logError("Logout on server", "Log out showErrorUnAuth");
                        } else if (successResponse.code() == 400) {
                            logError("Logout on server", "Log out showBadRequestError");
                        } else {
                            logError("Logout on server", "Log out ELSE");
                        }
//                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        } else if (requestCode == REQUEST_CREATE_PATIENT) {
            if (resultCode == RESULT_OK) {
                getPatienst(phone);
                searchPatientFragment.getAllPhone();
            }
        }
    }

    public void increaseNumDentistAppointment() {
        if (Utils.isDentist(MainActivity.this) && dentistAppointmentItem != null && selectedMenuItem != dentistAppointmentItem) {
            numDentistAppointment++;
            setNumMenuItem(dentistAppointmentItem, numDentistAppointment + "");
        }
    }

    public void clearNumDentistAppointment(MenuItem item) {
        if (Utils.isDentist(MainActivity.this) && item != null) {
            numDentistAppointment = 0;
            setNumMenuItem(item, "");
        }
    }

    public void increaseNumClinicAppointment() {
        if (Utils.isRceiption(MainActivity.this) && clinicAppointmentItem != null) {
            numClinicAppointment++;
            setNumMenuItem(clinicAppointmentItem, numClinicAppointment + "");

        }
    }

    public void clearNumClinicAppointment(MenuItem item) {
        if (Utils.isRceiption(MainActivity.this) && item != null) {
            numClinicAppointment = 0;
            setNumMenuItem(item, "");
        }
    }

    public void setNumMenuItem(MenuItem item, String num) {
        TextView v = (TextView) item.getActionView();
        v.setText(num);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver, new IntentFilter(AppConst.ACTION_RELOAD));
        Log.d("DEBUG_TAG", "MainActivity REGISTER");
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mMessageReceiver);
        Log.d("DEBUG_TAG", "MainActivity PAUSE");
        super.onPause();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionReloadType = intent.getStringExtra(AppConst.ACTION_RELOAD_TYPE);
            if (actionReloadType.equals(AppConst.ACTION_RELOAD_DENTIST_APPOINTMENT)) {
                increaseNumDentistAppointment();
            } else if (actionReloadType.equals(AppConst.ACTION_RELOAD_CLINIC_APPOINTMENT)) {
                increaseNumClinicAppointment();
            }

        }
    };
}
