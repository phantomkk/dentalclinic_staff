package com.dentalclinic.capstone.admin.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.ajithvgiri.searchdialog.OnSearchItemSelected;
import com.ajithvgiri.searchdialog.SearchListItem;
import com.ajithvgiri.searchdialog.SearchableDialog;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.ImageAdapter;
import com.dentalclinic.capstone.admin.adapter.ImageFileAdapter;
import com.dentalclinic.capstone.admin.adapter.ToothSpinnerAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.services.ToothService;
import com.dentalclinic.capstone.admin.api.services.TreatmentService;
import com.dentalclinic.capstone.admin.models.Medicine;
import com.dentalclinic.capstone.admin.models.MedicineQuantity;
import com.dentalclinic.capstone.admin.models.Tooth;
import com.dentalclinic.capstone.admin.models.Treatment;
import com.dentalclinic.capstone.admin.models.TreatmentImage;
import com.dentalclinic.capstone.admin.models.TreatmentStep;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

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
import retrofit2.Response;

public class CreateTreatmentActivity extends BaseActivity implements TextWatcher{
    private AutoCompleteTextView actPrice;
    private TextView lblTreatment;
    private TextView lblTreatmentStep;
    private TextView lblMedicineQuantity;
    private String current = "0";
    private ToothSpinnerAdapter adapter;
    private ImageFileAdapter imageAdapter;
    private Spinner spnTooth;
    private List<Tooth> listTooth;
    private List<Treatment> listTreatment;
    private List<TreatmentStep> crrTreatmentSteps;
    private List<MedicineQuantity> selectedMedicine;
    private List<MedicineQuantity> listMedicine;
    private Button btnShowListTreatment;
    private Button btnShowListTreatmentStep;
    private Button btnShowListMedicine;
    private Button btnImagePicker;
    private RecyclerView recyclerView;
    private Treatment currentTreatment;
    private SearchableDialog searchableDialog;
    private List<SearchListItem> listItems;
    public static String LIST_STEP = "LIST_STEP";
    public static String LIST_MEDICINE = "LIST_MEDICINE";
    public static String CURRENT_STEP = "CURRENT_STEP";
    public static String SELECTED_MEDICINE = "SELECTED_MEDICINE";
    public static final int REQUEST_CODE_STEP = 121;
    public static final int REQUEST_CODE_MEDICINE = 129;

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
        actPrice.addTextChangedListener(this);
        spnTooth = findViewById(R.id.spn_tooth);
        lblTreatment = findViewById(R.id.lbl_treatment_slt);
        lblTreatmentStep = findViewById(R.id.lbl_treatmentstep_slt);
        lblMedicineQuantity = findViewById(R.id.lbl_medicine_slt);
        listTooth = new ArrayList<>();
        btnShowListTreatment = findViewById(R.id.btn_list_treatments);
        btnShowListTreatmentStep = findViewById(R.id.btn_list_treatmentstep);
        btnShowListMedicine = findViewById(R.id.btn_list_medicine);
        btnImagePicker = findViewById(R.id.btn_list_images);
        recyclerView = findViewById(R.id.recyclerView);
        listTreatment = new ArrayList<>();
        listMedicine = new ArrayList<>();
        crrTreatmentSteps = new ArrayList<>();
        selectedMedicine = new ArrayList<>();
        adapter = new ToothSpinnerAdapter(this, android.R.layout.simple_spinner_item, listTooth);
        spnTooth.setAdapter(adapter);
//        spnTooth.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
//            public void onItemSelected(AdapterView<?> parent, View view, int pos,
//                                       long id) {
//                ((TextView) view).setTextColor(Color.BLACK);
//            }
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//
//        });
        listItems = convertTreatmentList(listTreatment);
        searchableDialog = new SearchableDialog(this, listItems, "Chọn điều trị");
        searchableDialog.setOnItemSelected(new OnSearchItemSelected() {
            @Override
            public void onClick(int i, SearchListItem searchListItem) {
                int treatmentId = searchListItem.getId();
                for (Treatment t : listTreatment) {
                    if (t.getId() == treatmentId) {
                        currentTreatment = t;
                        crrTreatmentSteps.clear();
                        crrTreatmentSteps.addAll(currentTreatment.getTreatmentSteps());
                        lblTreatment.setText(currentTreatment.getName());
                        updateTreatmentStepLabel(crrTreatmentSteps);
                        break;
                    }
                }
            }
        });
        btnShowListTreatment.setOnClickListener((v) -> {
            if (searchableDialog != null && listTreatment != null && listTreatment.size() > 0) {
                searchableDialog.show();
            } else {
                showMessage("Danh sách điều trị trống");
            }
        });

        btnShowListTreatmentStep.setOnClickListener((v) -> {
            if(currentTreatment!=null) {
                Intent intent = new Intent(CreateTreatmentActivity.this, StepListActivity.class);
                intent.putExtra(LIST_STEP, (ArrayList<TreatmentStep>) currentTreatment.getTreatmentSteps());
                if (currentTreatment != null && currentTreatment.getTreatmentSteps() != null) {
                    intent.putExtra(CURRENT_STEP, (ArrayList<TreatmentStep>) crrTreatmentSteps);
                } else {
                    logError("btnShowTreatmentStep", "currentTreatment!=null && currentTreatment.getTreatmentSteps()!=null else");
                }

                startActivityForResult(intent, REQUEST_CODE_STEP);
            }else{
                showMessage("Vui lòng chọn loại điều trị");
            }
        });
        btnShowListMedicine.setOnClickListener((v)->{
            Intent intent = new Intent(CreateTreatmentActivity.this, MedicineListActivity.class);
            intent.putExtra(LIST_MEDICINE, (ArrayList<MedicineQuantity>) listMedicine);
            if (selectedMedicine != null ) {
                intent.putExtra(SELECTED_MEDICINE, (ArrayList<MedicineQuantity>) selectedMedicine);
            } else {
                logError("btnShowListMedicine", "selectedMedicine != null else");
            }

            startActivityForResult(intent, REQUEST_CODE_MEDICINE);

        });
        prepareData();
        prepareMedicineDummy();
//        FirebaseMessaging.getInstance().subscribeToTopic("AAA");
        // REST OF YOUR CODE
        IntentFilter filter = new IntentFilter("Hello Main");
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, filter);
//        logError("NOTHING", FirebaseInstanceId.getInstance().getToken());

        //imagepicker

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
                Intent intent = new Intent(CreateTreatmentActivity.this, PhotoViewActivity.class);
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

    private ArrayList<Image> images= new ArrayList<>();

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
            mStr += s.getMedicine().getName()+ " ___ " +s.getQuantity()+" viên" + "\n";
        }
        lblMedicineQuantity.setText(mStr );
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
        return "Tạo liệu trình";
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

    private void prepareData() {
        showLoading();
        Single<Response<List<Treatment>>> treatmentService = APIServiceManager.getService(TreatmentService.class)
                .getAllTreatment().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Single<Response<List<Tooth>>> toothService = APIServiceManager.getService(ToothService.class)
                .getAllTooth().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        Single<CombineClass> combineClass = Single.zip(treatmentService, toothService,
                new BiFunction<Response<List<Treatment>>, Response<List<Tooth>>, CombineClass>() {
                    @Override
                    public CombineClass apply(
                            Response<List<Treatment>> listResponse,
                            Response<List<Tooth>> listResponse2)
                            throws Exception {
                        return new CombineClass(listResponse, listResponse2);
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
                    } else {
                        logError("onSuccess", "list tooth null");
                    }
                } else if (toothResponse.code() == 500) {
                    showFatalError(toothResponse.errorBody(), "prepareData");
                } else if (toothResponse.code() == 401) {
                    showErrorUnAuth();
                } else if (toothResponse.code() == 400) {
                    showBadRequestError(toothResponse.errorBody(), "prepareData");
                } else {
                    showDialog(getString(R.string.error_message_api));
                }

                Response<List<Treatment>> treatmentResponse = combineClass.listTreatment;
                if (treatmentResponse.isSuccessful()) {
                    if (treatmentResponse.body() != null) {
                        listTreatment.clear();
                        listTreatment.addAll(treatmentResponse.body());
                        listItems.addAll(convertTreatmentList(listTreatment));
                    } else {
                        logError("onSuccess", "list treatment null");
                    }
                } else if (treatmentResponse.code() == 500) {
                    showFatalError(treatmentResponse.errorBody(), "prepareData");
                } else if (treatmentResponse.code() == 401) {
                    showErrorUnAuth();
                } else if (treatmentResponse.code() == 400) {
                    showBadRequestError(treatmentResponse.errorBody(), "prepareData");
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

    private void getData() {

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
}
