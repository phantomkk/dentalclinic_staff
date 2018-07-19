package com.dentalclinic.capstone.admin.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.ajithvgiri.searchdialog.OnSearchItemSelected;
import com.ajithvgiri.searchdialog.SearchListItem;
import com.ajithvgiri.searchdialog.SearchableDialog;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.ImageFileAdapter;
import com.dentalclinic.capstone.admin.adapter.ToothSpinnerAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.requestobject.TreatmentHistoryRequest;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.api.services.ToothService;
import com.dentalclinic.capstone.admin.api.services.TreatmentDetailService;
import com.dentalclinic.capstone.admin.api.services.TreatmentHistoryService;
import com.dentalclinic.capstone.admin.api.services.TreatmentService;
import com.dentalclinic.capstone.admin.models.Medicine;
import com.dentalclinic.capstone.admin.models.MedicineQuantity;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.models.Tooth;
import com.dentalclinic.capstone.admin.models.Treatment;
import com.dentalclinic.capstone.admin.models.TreatmentHistory;
import com.dentalclinic.capstone.admin.models.TreatmentImage;
import com.dentalclinic.capstone.admin.models.TreatmentStep;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class CreateTreatmentDetailActivity extends BaseActivity {
    private AutoCompleteTextView actTmDetailNote;
    private TextView lblTooth;
    private TextView lblTreatment;
    private TextView lblTreatmentStep;
    private TextView lblMedicineQuantity;
    private String current = "0";
    private ToothSpinnerAdapter adapter;
    private ImageFileAdapter imageAdapter;
    private List<Tooth> listTooth;
    private List<Treatment> listTreatment;
    private List<TreatmentStep> crrTreatmentSteps;
    private List<MedicineQuantity> selectedMedicine;
    private List<MedicineQuantity> listMedicine;
    private Button btnShowListTreatmentStep;
    private Button btnShowListMedicine;
    private Button btnImagePicker;
    private Button btnCreateTmHistory;
    private int treatmentHistoryId = -1;
    private RecyclerView recyclerView;
    private Treatment currentTreatment;
    private TreatmentHistory currentTreatmentHistory;
    private Tooth currentTooth;
    private Patient currentPatient;
    private SearchableDialog searchableDialogTreatment;
    private SearchableDialog searchableDialogTooth;
    private List<SearchListItem> listItemsTreatment;
    private List<SearchListItem> listItemsTooth;
    public static final String TM_HISTORY_ID_BUNDLE = "PATIENT_BUNDLE";

    public static String LIST_STEP = "LIST_STEP";
    public static String LIST_MEDICINE = "LIST_MEDICINE";
    public static String CURRENT_STEP = "CURRENT_STEP";
    public static final String PATIENT_BUNDLE = "PATIENT_BUNDLE";
    public static final String TOOTH_BUNDLE = "TOOTH_BUNDLE";
    public static final String TREATMENT_BUNDLE = "TREATMENT_BUNDLE";
    public static final String TREATMENT_HISTORY_BUNDLE = "TREATMENT_HISTORY_BUNDLE";
    public static String SELECTED_MEDICINE = "SELECTED_MEDICINE";
    public static final int REQUEST_CODE_STEP = 121;
    public static final int REQUEST_CODE_MEDICINE = 129;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_treatment_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }
        actTmDetailNote = findViewById(R.id.act_note_tmdetail);
        lblTooth = findViewById(R.id.lbl_tooth_slt);
        lblTreatment = findViewById(R.id.lbl_treatment_slt);
        lblTreatmentStep = findViewById(R.id.lbl_treatmentstep_slt);
        lblMedicineQuantity = findViewById(R.id.lbl_medicine_slt);
        listTooth = new ArrayList<>();
        btnShowListTreatmentStep = findViewById(R.id.btn_list_treatmentstep);
        btnShowListMedicine = findViewById(R.id.btn_list_medicine);
        btnImagePicker = findViewById(R.id.btn_list_images);
        btnCreateTmHistory = findViewById(R.id.btn_create_tmhistory);
        recyclerView = findViewById(R.id.recyclerView);
        listTreatment = new ArrayList<>();
        listMedicine = new ArrayList<>();
        crrTreatmentSteps = new ArrayList<>();
        selectedMedicine = new ArrayList<>();
        adapter = new ToothSpinnerAdapter(this, android.R.layout.simple_spinner_item, listTooth);
        if (getIntent() != null) {
            currentTreatmentHistory = (TreatmentHistory) getIntent().getSerializableExtra(TREATMENT_HISTORY_BUNDLE);
            if (currentTreatmentHistory != null) {
                currentTooth = currentTreatmentHistory.getTooth();
                currentTreatment = currentTreatmentHistory.getTreatment();
                crrTreatmentSteps = currentTreatmentHistory.getTreatment() != null ?
                        currentTreatmentHistory.getTreatment().getTreatmentSteps() : null;
                lblTreatment.setText(currentTreatment.getName());
                lblTooth.setText(currentTooth.getToothName());
            }
        }
        btnShowListTreatmentStep.setOnClickListener((v) -> {
            if (currentTreatment != null) {
                Intent intent = new Intent(CreateTreatmentDetailActivity.this, StepListActivity.class);
                intent.putExtra(LIST_STEP, (ArrayList<TreatmentStep>) currentTreatment.getTreatmentSteps());
                if (currentTreatment != null && currentTreatment.getTreatmentSteps() != null) {
                    intent.putExtra(CURRENT_STEP, (ArrayList<TreatmentStep>) crrTreatmentSteps);
                } else {
                    logError("btnShowTreatmentStep", "currentTreatment!=null && currentTreatment.getTreatmentSteps()!=null else");
                }

                startActivityForResult(intent, REQUEST_CODE_STEP);
            } else {
                showMessage("Vui lòng chọn loại điều trị");
            }
        });
        btnShowListMedicine.setOnClickListener((v) -> {
            Intent intent = new Intent(CreateTreatmentDetailActivity.this, MedicineListActivity.class);
            intent.putExtra(LIST_MEDICINE, (ArrayList<MedicineQuantity>) listMedicine);
            if (selectedMedicine != null) {
                intent.putExtra(SELECTED_MEDICINE, (ArrayList<MedicineQuantity>) selectedMedicine);
            } else {
                logError("btnShowListMedicine", "selectedMedicine != null else");
            }

            startActivityForResult(intent, REQUEST_CODE_MEDICINE);

        });
        btnCreateTmHistory.setOnClickListener((v) -> {
            callApiTreatmentService();
        });
        prepareMedicineDummy();
        IntentFilter filter = new IntentFilter("Hello Main");
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, filter);

        btnImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

        imageAdapter = new ImageFileAdapter(this, images,
                new ImageFileAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Image item, int position) {
                        Intent intent = new Intent(CreateTreatmentDetailActivity.this, PhotoViewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConst.IMAGE_OBJ, new TreatmentImage(images.get(position).getPath()));
                        intent.putExtra(AppConst.BUNDLE, bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemDelete(Image item, int position) {
                        imageAdapter.deleteItem(position);
                    }
                });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(imageAdapter);
    }


    private ArrayList<Image> images = new ArrayList<>();

    private void start() {

        ImagePicker.with(this)
                .setFolderMode(false)
                .setCameraOnly(false)
                .setMultipleMode(true)
                .setSelectedImages(images)
                .setMaxSize(10)
                .start();
    }

    private void clearTreatmentStepLabel() {
        lblTreatmentStep.setText("");
    }

    private void updateTreatmentStepLabel(List<TreatmentStep> steps) {
        String stepsStr = "";
        for (TreatmentStep s : steps) {
            stepsStr += s.getName() + "\n";
        }
        lblTreatmentStep.setText(stepsStr);
    }


    private void updateMedicineLabel(List<MedicineQuantity> medicines) {
        String mStr = "";
        for (MedicineQuantity s : medicines) {
            mStr += s.getMedicine().getName() + " ___ " + s.getQuantity() + " viên" + "\n";
        }
        lblMedicineQuantity.setText(mStr);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle b = (data == null ? null : data.getExtras());
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_STEP) {
                if (b != null) {
                    ArrayList<TreatmentStep> list = b.get(CURRENT_STEP) instanceof ArrayList ?
                            (ArrayList<TreatmentStep>) b.get(CURRENT_STEP) : null;
                    if (list != null) {
                        crrTreatmentSteps.clear();
                        crrTreatmentSteps.addAll(list);
                        updateTreatmentStepLabel(crrTreatmentSteps);
                    }
                }
            } else if (requestCode == REQUEST_CODE_MEDICINE) {
                if (b != null) {
                    ArrayList<MedicineQuantity> list = b.get(SELECTED_MEDICINE) instanceof ArrayList ?
                            (ArrayList<MedicineQuantity>) b.get(SELECTED_MEDICINE) : null;
                    if (list != null) {
                        selectedMedicine.clear();
                        selectedMedicine.addAll(list);
                        updateMedicineLabel(selectedMedicine);
                    }
                }
            }
        }

        if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            imageAdapter.setData(images);
        }
    }

    public List<SearchListItem> convertTreatmentList(List<Treatment> lst) {
        List<SearchListItem> listItems = new ArrayList<>();
        for (Treatment t : lst) {
            listItems.add(new SearchListItem(t.getId(), t.getName()));
        }
        return listItems;
    }

    public List<SearchListItem> convertToothList(List<Tooth> lst) {
        List<SearchListItem> listItems = new ArrayList<>();
        for (Tooth t : lst) {
            listItems.add(new SearchListItem(t.getToothNumber(), t.getToothName()));
        }
        return listItems;
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showMessage("HELLO lan 2");
        }

    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public String getMainTitle() {
        return "Tạo điều trị chi tiết";
    }

    @Override
    public void onCancelLoading() {

    }

    private void prepareMedicineDummy() {
        Medicine m1 = new Medicine();
        m1.setId(1);
        m1.setName("paradol");
        Medicine m2 = new Medicine();
        m2.setId(2);
        m2.setName("paracetamol");
        Medicine m3 = new Medicine();
        m3.setId(3);
        m3.setName("pharmaton");
        Medicine m4 = new Medicine();
        m4.setId(4);
        m4.setName("tiffy");
        Medicine m5 = new Medicine();
        m5.setId(5);
        m5.setName("aquavina");
        Medicine m6 = new Medicine();
        m6.setId(6);
        m6.setName("beberin");
        Medicine m7 = new Medicine();
        m7.setId(7);
        m7.setName("torbrin");
        Medicine m8 = new Medicine();
        m8.setId(8);
        m8.setName("acitonin");
        Medicine m9 = new Medicine();
        m9.setId(9);
        m9.setName("hiruscar");
        Medicine m10 = new Medicine();
        m10.setId(10);
        m10.setName("thuoc te");
        List<Medicine> lst = new ArrayList<>();
        lst.add(m1);
        lst.add(m2);
        lst.add(m3);
        lst.add(m4);
        lst.add(m5);
        lst.add(m6);
        lst.add(m7);
        lst.add(m8);
        lst.add(m9);
        lst.add(m10);
        for (Medicine m : lst) {
            MedicineQuantity q = new MedicineQuantity(m.getId(), 0, 0);
            q.setMedicine(m);
            listMedicine.add(q);
        }
    }


    private void callApiTreatmentService() {
        TreatmentHistoryRequest requestObj = new TreatmentHistoryRequest();
        Staff s = CoreManager.getStaff(this);
//        if (currentTreatment == null) {
//            showErrorMessage("Vui lòng chọn điều trị");
//            return;
//        }
//        if (currentTooth == null) {
//            showErrorMessage("Vui lòng chọn loại răng");
//            return;
//        }
//        if (s == null) {
//            showErrorMessage("Không tìm thấy thông tin đăng nhập");
//            return;
//        }
//        if (currentPatient == null) {
//            showErrorMessage("Không tìm thấy bệnh nhân");
//            return;
//        }
//        String tmDetailNote = actTmDetailNote.getText().toString().trim();
//        if (tmDetailNote.length() == 0) {
//            showErrorMessage("Vui lòng nhập mô tả chi tiết điều trị");
//            return;
//        }
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("treatment_history_id", treatmentHistoryId + "");
        builder.addFormDataPart("staff_id", "5");
        builder.addFormDataPart("patient_id", "1");
        builder.addFormDataPart("detail_note", actTmDetailNote.getText().toString() + " ");
        for (MedicineQuantity mq : selectedMedicine) {
            builder.addFormDataPart("medicine_id[]", mq.getMedicineId() + "");
            builder.addFormDataPart("medicine_quantity[]", mq.getQuantity() + "");
        }
        for (TreatmentStep st : crrTreatmentSteps) {
            builder.addFormDataPart("step_id[]", st.getStepId() + "");
        }
        for (Image image : images) {
            File f = new File(image.getPath());
            builder.addFormDataPart("images[]", image.getName(), RequestBody.create(MediaType.parse("image/*"), f));
        }
        MultipartBody requestBody = builder.build();
        TreatmentDetailService service = APIServiceManager.getService(TreatmentDetailService.class);
        showLoading();
        service.createTreatmentDetail(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<SuccessResponse> successResponse) {
                        hideLoading();
                        if (successResponse.isSuccessful()) {
                            if (successResponse.body() != null) {
                                SuccessResponse response = successResponse.body();
                                showSuccessMessage(response != null ? response.getMessage() : "");
                            }
                        } else if (successResponse.code() == 500) {
                            showFatalError(successResponse.errorBody(), "callApiTreatmentService");
                        } else if (successResponse.code() == 401) {
                            showErrorUnAuth();
                        } else if (successResponse.code() == 400) {
                            showBadRequestError(successResponse.errorBody(), "callApiTreatmentService");
                        } else {
                            showErrorMessage("Lỗi không xác định");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showErrorMessage("Lỗi máy chủ");
                        hideLoading();
                    }
                });
    }

    private class CombineClass {
        private Response<List<Treatment>> listTreatment;
        private Response<List<Tooth>> listTooth;

        public CombineClass(Response<List<Treatment>> listTreatment,
                            Response<List<Tooth>> listTooth) {
            this.listTreatment = listTreatment;
            this.listTooth = listTooth;
        }

        public Response<List<Treatment>> getListTreatment() {
            return listTreatment;
        }

        public void setListTreatment(Response<List<Treatment>> listTreatment) {
            this.listTreatment = listTreatment;
        }

        public Response<List<Tooth>> getListTooth() {
            return listTooth;
        }

        public void setListTooth(Response<List<Tooth>> listTooth) {
            this.listTooth = listTooth;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}