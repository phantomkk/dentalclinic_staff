package com.dentalclinic.capstone.admin.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import android.widget.Toast;

import com.ajithvgiri.searchdialog.OnSearchItemSelected;
import com.ajithvgiri.searchdialog.SearchListItem;
import com.ajithvgiri.searchdialog.SearchableDialog;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.ImageFileAdapter;
import com.dentalclinic.capstone.admin.adapter.ToothSpinnerAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.requestobject.TreatmentHistoryRequest;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.api.services.MedicineService;
import com.dentalclinic.capstone.admin.api.services.SymtomService;
import com.dentalclinic.capstone.admin.api.services.ToothService;
import com.dentalclinic.capstone.admin.api.services.TreatmentHistoryService;
import com.dentalclinic.capstone.admin.api.services.TreatmentService;
import com.dentalclinic.capstone.admin.models.Medicine;
import com.dentalclinic.capstone.admin.models.MedicineQuantity;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.models.Symptom;
import com.dentalclinic.capstone.admin.models.Tooth;
import com.dentalclinic.capstone.admin.models.Treatment;
import com.dentalclinic.capstone.admin.models.TreatmentImage;
import com.dentalclinic.capstone.admin.models.TreatmentStep;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.dentalclinic.capstone.admin.utils.Utils;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

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
import io.reactivex.functions.Function3;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class CreateTreatmentActivity extends BaseActivity implements TextWatcher {
    private AutoCompleteTextView actPrice;
    private AutoCompleteTextView actTmDetailNote;
    private AutoCompleteTextView actTmHistoryDescription;
    private TextView lblTooth;
    private TextView lblSymtoms;
    private TextView lblTreatment;
    private TextView lblTreatmentStep;
    private TextView lblMedicineQuantity;
    private TextView txtHintPrice;
    private String current = "0";
    private ToothSpinnerAdapter adapter;
    private ImageFileAdapter imageAdapter;
    private List<Tooth> listTooth;
    private List<Treatment> listTreatment;
    private List<TreatmentStep> crrTreatmentSteps;
    private List<MedicineQuantity> selectedMedicines;
    private List<MedicineQuantity> listMedicine;
    private List<Symptom> listSymptom;
    private List<Symptom> listSelectedSymptom;
    private Button btnShowListTooth;
    private Button btnShowListTreatment;
    private Button btnShowListTreatmentStep;
    private Button btnShowListMedicine;
    private Button btnImagePicker;
    private Button btnCreateTmHistory;
    private Button btnShowListSympToms;
    private RecyclerView recyclerView;
    private Treatment currentTreatment;
    private Tooth currentTooth;
    private Patient currentPatient;
    private SearchableDialog searchableDialogTreatment;
    private SearchableDialog searchableDialogTooth;
    private List<SearchListItem> listItemsTreatment;
    private List<SearchListItem> listItemsTooth;

    private int treatmentHistoryId = -1;
    public static String LIST_STEP = "LIST_STEP";
    public static String LIST_SYMPTOM = "LIST_SYMPTOM";
    public static String LIST_MEDICINE = "LIST_MEDICINE";
    public static String CURRENT_STEP = "CURRENT_STEP";
    public static final String PATIENT_BUNDLE = "PATIENT_BUNDLE";
    public static final String TM_HISTORY_ID_BUNDLE = "PATIENT_BUNDLE";
    public static String SELECTED_MEDICINE = "SELECTED_MEDICINE";
    public static String SELECTED_SYMPTOM = "SELECTED_SYMPTOM";
    public static final int REQUEST_CODE_STEP = 121;
    public static final int REQUEST_CODE_MEDICINE = 129;
    public static final int REQUEST_CODE_SYMPTOM = 125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_treatment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }
        actPrice = findViewById(R.id.tv_price);
        actTmHistoryDescription = findViewById(R.id.act_content_tmhistory);
        actTmDetailNote = findViewById(R.id.act_note_tmdetail);
        actPrice.addTextChangedListener(this);
        lblTooth = findViewById(R.id.lbl_tooth_slt);
        lblSymtoms = findViewById(R.id.lbl_symptoms_slt);
        lblTreatment = findViewById(R.id.lbl_treatment_slt);
        lblTreatmentStep = findViewById(R.id.lbl_treatmentstep);
        lblMedicineQuantity = findViewById(R.id.lbl_medicine_slt);
        txtHintPrice = findViewById(R.id.txt_hint_price);
        listTooth = new ArrayList<>();
        btnShowListTooth = findViewById(R.id.btn_list_tooth);
        btnShowListSympToms = findViewById(R.id.btn_list_symptoms);
        btnShowListTreatment = findViewById(R.id.btn_list_treatments);
        btnShowListTreatmentStep = findViewById(R.id.btn_list_treatmentstep);
        btnShowListMedicine = findViewById(R.id.btn_list_medicine);
        btnImagePicker = findViewById(R.id.btn_list_images);
        btnCreateTmHistory = findViewById(R.id.btn_create_tmhistory);
        recyclerView = findViewById(R.id.recyclerView);
        listTreatment = new ArrayList<>();
        listMedicine = new ArrayList<>();
        listSymptom = new ArrayList<>();
        listSelectedSymptom = new ArrayList<>();
        crrTreatmentSteps = new ArrayList<>();
        selectedMedicines = new ArrayList<>();
        adapter = new ToothSpinnerAdapter(this, android.R.layout.simple_spinner_item, listTooth);
        if (getIntent() != null) {
            currentPatient = (Patient) getIntent().getSerializableExtra(PATIENT_BUNDLE);
        }
        listItemsTreatment = convertTreatmentList(listTreatment);
        searchableDialogTreatment = new SearchableDialog(this, listItemsTreatment, "Chọn điều trị");
        searchableDialogTreatment.setOnItemSelected(new OnSearchItemSelected() {
            @Override
            public void onClick(int i, SearchListItem searchListItem) {
                int treatmentId = searchListItem.getId();
                for (Treatment t : listTreatment) {
                    if (t.getId() == treatmentId) {
                        currentTreatment = t;
                        actPrice.setText((t.getMaxPrice()) + "");
                        if (t.getMaxPrice().intValue() == t.getMinPrice().intValue()) {
                            txtHintPrice.setText("Giá cố định là " + formatVnCurrence(t.getMaxPrice() + ""));
                        } else {
                            txtHintPrice.setText("Giá từ " + formatVnCurrence(t.getMinPrice() + "")
                                    + " đến " + formatVnCurrence("" + t.getMaxPrice()));
                        }

                        treatmentHistoryId = currentTreatment.getId();
                        crrTreatmentSteps.clear();
                        crrTreatmentSteps.addAll(currentTreatment.getTreatmentSteps());
                        lblTreatment.setText(currentTreatment.getName());
//                        updateTreatmentStepLabel(crrTreatmentSteps);
//                        crrTreatmentSteps = t.getTreatmentSteps();
                        break;
                    }
                }
            }
        });
        listItemsTooth = convertToothList(listTooth);
        searchableDialogTooth = new SearchableDialog(this, listItemsTooth, "Chọn răng");
        searchableDialogTooth.setOnItemSelected(new OnSearchItemSelected() {
            @Override
            public void onClick(int i, SearchListItem searchListItem) {
                int toothId = searchListItem.getId();
                for (Tooth t : listTooth) {
                    if (t.getToothNumber() == toothId) {
                        currentTooth = t;
                        lblTooth.setText(currentTooth.getToothName());
                        break;
                    }
                }
            }
        });

        btnShowListTooth.setOnClickListener((v) -> {
            if (searchableDialogTooth != null && listTooth != null && listTooth.size() > 0) {
                searchableDialogTooth.show();
            } else {
                showMessage("Danh sách răng trống");
            }

        });
        btnShowListTreatment.setOnClickListener((v) -> {
            if (searchableDialogTreatment != null && listTreatment != null && listTreatment.size() > 0) {
                searchableDialogTreatment.show();
            } else {
                showMessage("Danh sách điều trị trống");
            }
        });
        btnShowListSympToms.setOnClickListener((v) -> {
            Intent intent = new Intent(CreateTreatmentActivity.this, SymptomListActivity.class);
            intent.putExtra(LIST_SYMPTOM, (ArrayList<Symptom>) listSymptom);
            if (listSelectedSymptom != null) {
                intent.putExtra(SELECTED_SYMPTOM, (ArrayList<Symptom>) listSelectedSymptom);
            } else {
                logError("btnShowListSympToms", "listPatientSymptom ==null  ");
            }
            startActivityForResult(intent, REQUEST_CODE_SYMPTOM);
        });
        btnShowListTreatmentStep.setOnClickListener((v) -> {
            if (currentTreatment != null) {
                Intent intent = new Intent(CreateTreatmentActivity.this, StepListActivity.class);
                intent.putExtra(LIST_STEP, (ArrayList<TreatmentStep>) crrTreatmentSteps);
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
            Intent intent = new Intent(CreateTreatmentActivity.this, MedicineListActivity.class);
            intent.putExtra(LIST_MEDICINE, (ArrayList<MedicineQuantity>) listMedicine);
            if (selectedMedicines != null) {
                intent.putExtra(SELECTED_MEDICINE, (ArrayList<MedicineQuantity>) selectedMedicines);
            } else {
                logError("btnShowListMedicine", "selectedMedicines != null else");
            }

            startActivityForResult(intent, REQUEST_CODE_MEDICINE);

        });
        btnCreateTmHistory.setOnClickListener((v) -> {
            callApiTreatmentService();
        });
        prepareData();

//        prepareMedicineDummy();
//        FirebaseMessaging.getInstance().subscribeToTopic("AAA");
        // REST OF YOUR CODE
        IntentFilter filter = new IntentFilter("Hello Main");
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, filter);
//        logError("NOTHING", FirebaseInstanceId.getInstance().getToken());

        //imagepicker

        btnImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                start();
                Pix.start(CreateTreatmentActivity.this, 100, 5);

            }
        });

        imageAdapter = new ImageFileAdapter(this, images,
                new ImageFileAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(String item, int position) {
                        Intent intent = new Intent(CreateTreatmentActivity.this, PhotoViewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConst.IMAGE_OBJ, new TreatmentImage(item));
                        intent.putExtra(AppConst.BUNDLE, bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemDelete(String item, int position) {
                        imageAdapter.deleteItem(position);
                    }
                });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(imageAdapter);
    }

    private ArrayList<String> images = new ArrayList<>();

    private void start() {
//
//        ImagePicker.with(this)
//                .setFolderMode(false)
//                .setCameraOnly(false)
//                .setMultipleMode(true)
//                .setSelectedImages(images)
//                .setMaxSize(10)
//                .start();
    }

    private void clearTreatmentStepLabel() {
        lblTreatmentStep.setText("");
    }

    private void updateTreatmentStepLabel(List<TreatmentStep> steps) {
        String stepsStr = "";
        for (TreatmentStep s : steps) {
            if (s.isCheck()) {
                stepsStr += "- " + s.getName() + "\n";
            }
        }
        lblTreatmentStep.setText(stepsStr);
    }


    private void updateMedicineLabel(List<MedicineQuantity> medicines) {
        String mStr = "";
        for (MedicineQuantity s : medicines) {
            mStr += Utils.getMedicineLine(s.getMedicine().getName(), s.getQuantity(), 40) + "\n";
//            mStr += s.getMedicine().getName() + " ___ " + s.getQuantity() + " viên" + "\n";
        }
        lblMedicineQuantity.setText(mStr);
    }

    private void updateSymtomLabel(List<Symptom> symptoms) {
        String mStr = "";
        for (Symptom s : symptoms) {
            mStr += s.getName() + "\n";
        }
        lblSymtoms.setText(mStr);
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
//                    updateTreatmentStepLabel(crrTreatmentSteps);


                }
            } else if (requestCode == REQUEST_CODE_SYMPTOM) {
                if (b != null) {
                    ArrayList<Symptom> list = b.get(SELECTED_SYMPTOM) instanceof ArrayList ?
                            (ArrayList<Symptom>) b.get(SELECTED_SYMPTOM) : null;
                    if (list != null) {
                        listSelectedSymptom.clear();
                        listSelectedSymptom.addAll(list);
                        updateSymtomLabel(listSelectedSymptom);
                    }
                }
            }
        }
//
//        if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
//            images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
//            imageAdapter.setData(images);
//        }

        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                imageAdapter.setData(returnValue);
//                for (String s : returnValue) {
//                    Log.e("val", " ->  " + s);
//                }*
//                images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
//                imageAdapter.setData(images);
            }
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
        return "Tạo điều trị";
    }

    @Override
    public void onCancelLoading() {

    }

    private List<MedicineQuantity> convertListMedicine(List<Medicine> list) {
        List<MedicineQuantity> listMQ = new ArrayList<>();
        for (Medicine m : list) {
            MedicineQuantity q = new MedicineQuantity(m.getId(), 0, 0);
            q.setMedicine(m);
            listMQ.add(q);
        }
        return listMQ;
    }

    private void prepareData() {
        showLoading();
        Single<Response<List<Treatment>>> treatmentService = APIServiceManager.getService(TreatmentService.class)
                .getAllTreatment().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Single<Response<List<Tooth>>> toothService = APIServiceManager.getService(ToothService.class)
                .getAllTooth().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        Single<Response<List<Medicine>>> medicineService = APIServiceManager.getService(MedicineService.class)
                .getAllMedicine().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        Single<Response<List<Symptom>>> symptomService = APIServiceManager.getService(SymtomService.class)
                .getAll().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());


        Single<CombineClass> combineClass = Single.zip(
                treatmentService,
                toothService,
                medicineService,
                symptomService,
                new Function4<
                        Response<List<Treatment>>,
                        Response<List<Tooth>>,
                        Response<List<Medicine>>,
                        Response<List<Symptom>>,
                        CombineClass>() {
                    @Override
                    public CombineClass apply(
                            Response<List<Treatment>> listResponse,
                            Response<List<Tooth>> listResponse2,
                            Response<List<Medicine>> listResponse3,
                            Response<List<Symptom>> listResponse4
                    )
                            throws Exception {
                        return new CombineClass(listResponse, listResponse2, listResponse3, listResponse4);
                    }
                });
        combineClass.subscribe(new SingleObserver<CombineClass>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(CombineClass combineClass) {
                Response<List<Tooth>> toothResponse = combineClass.listTooth;
                hideLoading();
                if (toothResponse.isSuccessful()) {
                    if (toothResponse.body() != null) {
                        listTooth.addAll(toothResponse.body());
                        adapter.notifyDataSetChanged();
                        listItemsTooth.addAll(convertToothList(listTooth));
                    } else {
                        logError("onSuccess", "list tooth null");
                    }
                } else if (toothResponse.code() == 500) {
                    showFatalError(toothResponse.errorBody(), "prepareData toothResponse");
                } else if (toothResponse.code() == 401) {
                    showErrorUnAuth();
                } else if (toothResponse.code() == 400) {
                    showBadRequestError(toothResponse.errorBody(), "prepareData toothResponse");
                } else {
                    showDialog(getString(R.string.error_message_api));
                }

                Response<List<Treatment>> treatmentResponse = combineClass.listTreatment;
                if (treatmentResponse.isSuccessful()) {
                    if (treatmentResponse.body() != null) {
                        listTreatment.clear();
                        listTreatment.addAll(treatmentResponse.body());
                        listItemsTreatment.addAll(convertTreatmentList(listTreatment));
                    } else {
                        logError("onSuccess", "list treatment null");
                    }
                } else if (treatmentResponse.code() == 500) {
                    showFatalError(treatmentResponse.errorBody(), "prepareData treatmentResponse");
                } else if (treatmentResponse.code() == 401) {
                    showErrorUnAuth();
                } else if (treatmentResponse.code() == 400) {
                    showBadRequestError(treatmentResponse.errorBody(), "prepareData treatmentResponse");
                } else {
                    showDialog(getString(R.string.error_message_api));
                }

                Response<List<Medicine>> medicineResponse = combineClass.listMedicine;
                if (medicineResponse.isSuccessful()) {
                    if (medicineResponse.body() != null) {
                        listMedicine.clear();
                        listMedicine.addAll(convertListMedicine(medicineResponse.body()));
                    } else {
                        logError("onSuccess", "list treatment null");
                    }
                } else if (medicineResponse.code() == 500) {
                    showFatalError(medicineResponse.errorBody(), "prepareData medicineResponse");
                } else if (medicineResponse.code() == 401) {
                    showErrorUnAuth();
                } else if (medicineResponse.code() == 400) {
                    showBadRequestError(medicineResponse.errorBody(), "prepareData medicineResponse");
                } else {
                    showDialog(getString(R.string.error_message_api));
                }
                Response<List<Symptom>> symptomResponse = combineClass.listSymtom;
                if (symptomResponse.isSuccessful()) {
                    if (symptomResponse.body() != null) {
                        listSymptom.clear();
                        listSymptom.addAll(symptomResponse.body());
                    } else {
                        logError("onSuccess", "list symptomResponse null");
                    }
                } else if (symptomResponse.code() == 500) {
                    showFatalError(symptomResponse.errorBody(), "prepareData symptomResponse");
                } else if (symptomResponse.code() == 401) {
                    showErrorUnAuth();
                } else if (symptomResponse.code() == 400) {
                    showBadRequestError(symptomResponse.errorBody(), "prepareData symptomResponse");
                } else {
                    showDialog(getString(R.string.error_message_api));
                }
            }

            @Override
            public void onError(Throwable e) {
                hideLoading();
                e.printStackTrace();
                showErrorMessage("Không thể kết nối đến server");
            }
        });
    }

    private void callApiTreatmentService() {
        TreatmentHistoryRequest requestObj = new TreatmentHistoryRequest();
        Staff s = CoreManager.getStaff(this);
        if (currentTreatment == null) {
            showErrorMessage("Vui lòng chọn điều trị");
            return;
        }
        if (currentTooth == null) {
            showErrorMessage("Vui lòng chọn loại răng");
            return;
        }
//        if (s == null) {
//            showErrorMessage("Không tìm thấy thông tin đăng nhập");
//            return;
//        }
//        if (currentPatient == null) {
//            showErrorMessage("Không tìm thấy bệnh nhân");
//            return;
//        }
        String price = actPrice.getText().toString().replaceAll("[đ,.]", "").trim();
        if (price.length() == 0 || price.equals("0")) {
            showErrorMessage("Vui lòng nhập giá");
            return;
        }
        double priceDb = Double.parseDouble(price);
        if (priceDb > currentTreatment.getMaxPrice() || priceDb < currentTreatment.getMinPrice()) {
            showErrorMessage("Vui lòng nhập giá hợp lệ");
            return;
        }
        String description = actTmHistoryDescription.getText().toString().trim();
        if (description.length() == 0) {
            showErrorMessage("Vui lòng nhập mô tả điều trị");
            return;
        }
        String tmDetailNote = actTmDetailNote.getText().toString().trim();
        if (tmDetailNote.length() == 0) {
            showErrorMessage("Vui lòng nhập mô tả chi tiết điều trị");
            return;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("treatment_id", currentTreatment.getId() + "");
        builder.addFormDataPart("treatment_history_id", treatmentHistoryId + "");
        builder.addFormDataPart("staff_id", s.getId() + "");
        builder.addFormDataPart("patient_id", currentPatient.getId() + "");
        builder.addFormDataPart("description", actTmHistoryDescription.getText().toString().trim() + "");
        builder.addFormDataPart("detail_note", actTmDetailNote.getText().toString() + "");
        builder.addFormDataPart("tooth_number", currentTooth.getToothNumber() + "");
        builder.addFormDataPart("price", actPrice.getText().toString().replaceAll("[đ,.]", ""));
        for (MedicineQuantity mq : selectedMedicines) {
            builder.addFormDataPart("medicine_id[]", mq.getMedicineId() + "");
            builder.addFormDataPart("medicine_quantity[]", mq.getQuantity() + "");
        }
        for (TreatmentStep st : crrTreatmentSteps) {
            if (st.isCheck()) {
                builder.addFormDataPart("step_id[]", st.getStepId() + "");
            }
        }
        for (Symptom st : listSelectedSymptom) {
            builder.addFormDataPart("symptom_id[]", st.getId() + "");
        }
        for (String image : images) {
            File f = new File(image);
            builder.addFormDataPart("images[]", f.getName(), RequestBody.create(MediaType.parse("image/*"), f));
        }
        MultipartBody requestBody = builder.build();
        TreatmentHistoryService service = APIServiceManager.getService(TreatmentHistoryService.class);
        showLoading();
        service.createTreatmentHistory(requestBody)
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
                                setResult(RESULT_OK);
                                finish();
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        current = charSequence.toString();
    }

    private String prePrice = "";

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("DEBUG", "HEAD: ");

        if (!s.toString().equals(prePrice)) {
//if(current)
            String cleanString = s.toString().replaceAll("[đ,.]", "");
            Log.d("DEBUG", "BEFORE: " + cleanString);
//            double parsed = Double.parseDouble(cleanString);
            String formatted = formatVnCurrence(cleanString);
            Log.d("DEBUG", "AFTER: " + formatted);
            prePrice = formatted;
            actPrice.setText(formatted);
            actPrice.setSelection(formatted.length() > 1 ? formatted.length() - 1 : formatted.length());
        }
    }

    public static String formatVnCurrence(String price) {


        NumberFormat format =
                new DecimalFormat("#,##0.00");// #,##0.00 ¤ (¤:// Currency symbol)
        format.setCurrency(Currency.getInstance(Locale.US));//Or default locale

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = format.format(Double.parseDouble(price));
        price = price.replaceAll(",", "\\.");

        if (price.endsWith(".00")) {
            int centsIndex = price.lastIndexOf(".00");
            if (centsIndex != -1) {
                price = price.substring(0, centsIndex);
            }
        }
        price = String.format("%sđ", price);
        return price;
    }

    @Override
    public void afterTextChanged(Editable editable) {
//        current=editable.toString();
//        current= current.replaceAll("[đ,. ]", "");
        String price = actPrice.getText().toString().replaceAll("[đ,.]", "").trim();
        if (currentTreatment != null) {

            double priceDb = Double.parseDouble(price);
            if (priceDb > currentTreatment.getMaxPrice() || priceDb < currentTreatment.getMinPrice()) {
                String minTmPrice = formatVnCurrence(currentTreatment.getMinPrice() + "");
                String maxTmPrice = formatVnCurrence(currentTreatment.getMaxPrice() + "");
                if (currentTreatment.getMinPrice().intValue() == currentTreatment.getMaxPrice().intValue()) {
                    actPrice.setError("Giá cố định là " + formatVnCurrence(currentTreatment.getMaxPrice() + ""));
                } else {
                    actPrice.setError("Giá nhập vào khoảng từ " + minTmPrice + " đến " + maxTmPrice);
                }

            } else {
                actPrice.setError(null);
            }
        }
    }

//    @Override
//    public void onItemClick(Image item, int position) {
//        Intent intent = new Intent(CreateTreatmentActivity.this, PhotoViewActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(AppConst.IMAGE_OBJ, new TreatmentImage(images.get(position).getPath()));
//                intent.putExtra(AppConst.BUNDLE, bundle);
//                startActivity(intent);
//    }
//
//    @Override
//    public void onItemDelete(Image item, int position) {
//            images.remove(position);
//            this.imageAdapter.notifyDataSetChanged();
//    }

    private class CombineClass {
        private Response<List<Treatment>> listTreatment;
        private Response<List<Tooth>> listTooth;
        private Response<List<Medicine>> listMedicine;
        private Response<List<Symptom>> listSymtom;

        public CombineClass(Response<List<Treatment>> listTreatment,
                            Response<List<Tooth>> listTooth,
                            Response<List<Medicine>> listMedicine,
                            Response<List<Symptom>> listSymtom) {
            this.setListTreatment(listTreatment);
            this.setListTooth(listTooth);
            this.setListSymtom(listSymtom);
            this.setListMedicine(listMedicine);
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

        public Response<List<Symptom>> getListSymtom() {
            return listSymtom;
        }

        public void setListTooth(Response<List<Tooth>> listTooth) {
            this.listTooth = listTooth;
        }

        public void setListSymtom(Response<List<Symptom>> listSymtom) {
            this.listSymtom = listSymtom;
        }

        public Response<List<Medicine>> getListMedicine() {
            return listMedicine;
        }

        public void setListMedicine(Response<List<Medicine>> listMedicine) {
            this.listMedicine = listMedicine;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(CreateTreatmentActivity.this, 100, 5);
                } else {
                    Toast.makeText(CreateTreatmentActivity.this, "Vui lòng xác nhận quyền truy cập để chọn ảnh", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
