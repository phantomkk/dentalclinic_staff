package com.dentalclinic.capstone.admin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.api.services.StaffService;
import com.dentalclinic.capstone.admin.api.services.UserService;
import com.dentalclinic.capstone.admin.fragment.BaseWeekViewFragment;
import com.dentalclinic.capstone.admin.fragment.CalendarFragment;
import com.dentalclinic.capstone.admin.fragment.MyAccoutFragment;
import com.dentalclinic.capstone.admin.fragment.SearchPatientFragment;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        setDataHeader(staff);
//
        getAllPhone();
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
                showMessage(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.closeSearch();
        SearchPatientFragment calendarFragment = new SearchPatientFragment();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, calendarFragment).commit();
    }
    private Disposable disposable;

    public MaterialSearchView getSearchView() {
        return searchView;
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
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Response<List<String>> userResponse) {
                        if (userResponse.isSuccessful()) {
                            strings =userResponse.body();
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

    @Override
    public String getMainTitle() {
        return "Dentail Clinic";
    }

    @Override
    public void onCancelLoading() {

    }

    private void setDataHeader(Staff staff){
        if(staff.getAvatar()!=null){
            Picasso.get().load(staff.getAvatar()).into(mAvatar);
        }
        if(staff.getName()!=null){
            mStaffName.setText(staff.getName());
        }
        if(staff.getPhone()!=null){
            mStaffPhone.setText(staff.getPhone());
        }
    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search_patient) {
//            Intent intent = new Intent(MainActivity.this, SearchPatientActivity.class);
//            startActivity(intent);
            SearchPatientFragment calendarFragment = new SearchPatientFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, calendarFragment).commit();
        } else if (id == R.id.nav_history) {
            BaseWeekViewFragment calendarFragment = new BaseWeekViewFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, calendarFragment).commit();
        } else if (id == R.id.nav_my_accout) {
            MyAccoutFragment newFragment = new MyAccoutFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, newFragment).commit();
        } else if (id == R.id.nav_log_out) {
            logoutOnServer();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutOnServer() {
        StaffService staffService = APIServiceManager.getService(StaffService.class);
        staffService.logout().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<SuccessResponse> successResponseResponse) {
                        showWarningMessage("Đăng xuất");
                        CoreManager.clearStaff(MainActivity.this);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {

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
        }
    }
}
