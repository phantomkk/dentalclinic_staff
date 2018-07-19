package com.dentalclinic.capstone.admin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.TreatmentDetailAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.services.HistoryTreatmentService;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.TreatmentDetail;
import com.dentalclinic.capstone.admin.models.TreatmentHistory;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.CoreManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class TreatmentDetailActivity extends BaseActivity {
    ListView listView;
    TreatmentHistory treatmentHistory;
    List<TreatmentDetail> details = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.side_nav_bar));
//        prepareData();
        Bundle bundle = getIntent().getBundleExtra(AppConst.BUNDLE);
        if(bundle!=null){
            details = ((TreatmentHistory)bundle.getSerializable(AppConst.TREATMENT_HISTORY_OBJ)).getTreatmentDetails();
        }
        listView = findViewById(R.id.lv_treatment_detail);
        TreatmentDetailAdapter adapter = new TreatmentDetailAdapter(this, details);
        listView.setAdapter(adapter);

    }

    @Override
    public String getMainTitle() {
        return getResources().getString(R.string.treatment_detail_title_activity);
    }

    @Override
    public void onCancelLoading() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }



}
