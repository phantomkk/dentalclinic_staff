package com.dentalclinic.capstone.admin.activities;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.responseobject.ErrorResponse;
import com.dentalclinic.capstone.admin.api.services.UserService;
import com.dentalclinic.capstone.admin.utils.Utils;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SearchPatientActivity extends BaseActivity {
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getAllPhone();
        setTitle(getResources().getString(R.string.search_patient_title));
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(true);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
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
    }


    private Disposable disposable;
    private List<String> strings= new ArrayList<>();
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
        return "Tìm kiếm";
    }

    @Override
    public void onCancelLoading() {

    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        super.onActivityResult(requestCode, resultCode, data);
    }
}
