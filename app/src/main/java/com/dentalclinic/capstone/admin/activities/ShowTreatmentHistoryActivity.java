package com.dentalclinic.capstone.admin.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.PatientAdapter;
import com.dentalclinic.capstone.admin.adapter.TreatmentHistoryAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.services.TreatmentHistoryService;
import com.dentalclinic.capstone.admin.api.services.TreatmentService;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.Treatment;
import com.dentalclinic.capstone.admin.models.TreatmentHistory;
import com.dentalclinic.capstone.admin.utils.AppConst;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ShowTreatmentHistoryActivity extends BaseActivity {
    private List<TreatmentHistory> treatmentHistories = new ArrayList<>();
    private TreatmentHistoryAdapter mAdapter;
    private ListView mListView;
    private TextView textView;
    private FloatingActionButton btnAddNew;
    private Patient patient;
    private TextView labelMessage;
    private final int REQUEST_CREATE_NEW_HISTORY = 99;
    private final int REQUEST_CREATE_NEW_DETAIL = 69;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_treatment_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }

        Bundle bundle = getIntent().getBundleExtra(AppConst.BUNDLE);
        if (bundle != null) {
            patient = (Patient) bundle.getSerializable(AppConst.PATIENT_OBJ);
        }
        prepareData();

        textView = findViewById(R.id.txt_label_message);
        labelMessage = findViewById(R.id.txt_label_message);
        btnAddNew = findViewById(R.id.btn_actions);
        mListView = findViewById(R.id.listView);
        mAdapter = new TreatmentHistoryAdapter(this, treatmentHistories);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TreatmentHistory selectedTreatment = treatmentHistories.get(i);
                Intent intent = new Intent(ShowTreatmentHistoryActivity.this, CreateTreatmentDetailActivity.class);
                intent.putExtra(CreateTreatmentDetailActivity.TREATMENT_HISTORY_BUNDLE, selectedTreatment);
                intent.putExtra("PATIENT_BUNDLE", patient);
                ////////dummy request code
                startActivityForResult(intent, REQUEST_CREATE_NEW_DETAIL);
            }
        });

        btnAddNew.setOnClickListener((v) -> {
            Intent intent = new Intent(ShowTreatmentHistoryActivity.this, CreateTreatmentActivity.class);

            intent.putExtra("PATIENT_BUNDLE", patient);
            startActivityForResult(intent, REQUEST_CREATE_NEW_HISTORY);
        });

    }

    @Override
    public String getMainTitle() {
        return "Lịch Sử Khám Bệnh";
    }

    @Override
    public void onCancelLoading() {

    }

    private void prepareData() {
        TreatmentHistoryService service = APIServiceManager.getService(TreatmentHistoryService.class);
        service.getByPatientId(patient.getId())
                .subscribeOn(
                        Schedulers.newThread()
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<TreatmentHistory>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<List<TreatmentHistory>> listResponse) {
                        if (listResponse.isSuccessful()) {
                            treatmentHistories.clear();
                            treatmentHistories.addAll(listResponse.body());
                            mAdapter.notifyDataSetChanged();
                            if(treatmentHistories.isEmpty()){
                                labelMessage.setVisibility(View.VISIBLE);
                            }else{
                                labelMessage.setVisibility(View.GONE);
                            }
                        } else {
                            showMessage("Lỗi máy chủ");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
//        treatmentHistories.add(new TreatmentHistory());
//        treatmentHistories.add(new TreatmentHistory());
//        treatmentHistories.add(new TreatmentHistory());
//        treatmentHistories.add(new TreatmentHistory());
//        treatmentHistories.add(new TreatmentHistory());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CREATE_NEW_HISTORY){
            if(resultCode == RESULT_OK){
                prepareData();
            }
        }else if(requestCode==REQUEST_CREATE_NEW_DETAIL){
            if(resultCode == RESULT_OK){

            }
        }
    }
}
