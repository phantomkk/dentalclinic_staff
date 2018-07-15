package com.dentalclinic.capstone.admin.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ajithvgiri.searchdialog.OnSearchItemSelected;
import com.ajithvgiri.searchdialog.SearchListItem;
import com.ajithvgiri.searchdialog.SearchableDialog;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.adapter.ToothSpinnerAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.services.ToothService;
import com.dentalclinic.capstone.admin.api.services.TreatmentService;
import com.dentalclinic.capstone.admin.models.Tooth;
import com.dentalclinic.capstone.admin.models.Treatment;
import com.dentalclinic.capstone.admin.models.TreatmentStep;

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

public class CreateTreatmentActivity extends BaseActivity implements TextWatcher {
    private AutoCompleteTextView actPrice;
    private TextView lblTreatment;
    private String current = "0";
    private ToothSpinnerAdapter adapter;
    private Spinner spnTooth;
    private List<Tooth> listTooth;
    private List<Treatment> listTreatment;
    private List<TreatmentStep> crrTreatmentSteps;
    private Button btnShowListTreatment;
    private Button btnShowListTreatmentStep;
    private Treatment currentTreatment;
    private SearchableDialog searchableDialog;
    private List<SearchListItem> listItems;
    public static String LIST_STEP = "LIST_STEP";
    public static String CURRENT_STEP = "CURRENT_STEP";
    public static int REQUEST_CODE_STEP = 121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_treatment);
        actPrice = findViewById(R.id.tv_price);
        actPrice.addTextChangedListener(this);
        spnTooth = findViewById(R.id.spn_tooth);
        lblTreatment = findViewById(R.id.lbl_treatment_slt);
        listTooth = new ArrayList<>();
        listTreatment = new ArrayList<>();
        crrTreatmentSteps = new ArrayList<>();
        adapter = new ToothSpinnerAdapter(this, android.R.layout.simple_spinner_item, listTooth);
        spnTooth.setAdapter(
                adapter
        );
        listItems = convertTreatmentList(listTreatment);
        searchableDialog = new SearchableDialog(this, listItems, "Chọn điều trị");
        searchableDialog.setOnItemSelected(new OnSearchItemSelected() {
            @Override
            public void onClick(int i, SearchListItem searchListItem) {
                int treatmentId = searchListItem.getId();
                for (Treatment t : listTreatment) {
                    if (t.getId() == treatmentId) {
                        currentTreatment = t;
                        crrTreatmentSteps.addAll(currentTreatment.getTreatmentSteps());
                        lblTreatment.setText(currentTreatment.getName());
                        break;
                    }
                }
            }
        });
        btnShowListTreatment = findViewById(R.id.btn_list_treatments);
        btnShowListTreatment.setOnClickListener((v) -> {
            if (searchableDialog != null && listTreatment != null && listTreatment.size() > 0) {
                searchableDialog.show();
            } else {
                showMessage("Danh sách điều trị trống");
            }
        });

        btnShowListTreatmentStep = findViewById(R.id.btn_list_treatmentstep);
        btnShowListTreatmentStep.setOnClickListener((v) -> {
            Intent intent = new Intent(CreateTreatmentActivity.this, StepListActivity.class);
            intent.putExtra(LIST_STEP, (ArrayList<TreatmentStep>) currentTreatment.getTreatmentSteps());
            if (currentTreatment != null && currentTreatment.getTreatmentSteps() != null) {
                intent.putExtra(CURRENT_STEP, (ArrayList<TreatmentStep>) crrTreatmentSteps);
            } else {
                logError("btnShowTreatmentStep", "currentTreatment!=null && currentTreatment.getTreatmentSteps()!=null else");
            }

            startActivityForResult(intent,REQUEST_CODE_STEP);
        });
        prepareData();
//        FirebaseMessaging.getInstance().subscribeToTopic("AAA");
        // REST OF YOUR CODE
        IntentFilter filter = new IntentFilter("Hello Main");
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, filter);
//        logError("NOTHING", FirebaseInstanceId.getInstance().getToken());
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
                        String listAnamnesis = "";

                    }
                }
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


//        ToothService service = APIServiceManager.getService(ToothService.class);
//        service.getAllTooth().
//                subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<Response<List<Tooth>>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(Response<List<Tooth>> listResponse) {
//                        hideLoading();
//                        if (listResponse.isSuccessful()) {
//                            if (listResponse.body() != null) {
//                                listTooth.addAll(listResponse.body());
//                                adapter.notifyDataSetChanged();
//                            }
//                        } else if (listResponse.code() == 500) {
//                            showFatalError(listResponse.errorBody(), "getTooth");
//                        } else if (listResponse.code() == 401) {
//                            showErrorUnAuth();
//                        } else if (listResponse.code() == 400) {
//                            showBadRequestError(listResponse.errorBody(), "getTooth");
//                        } else {
//                            logError("getTooth", "ELSE get tooth api");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        showErrorMessage("Không thể kết nối đến máy chủ");
//                    }
//                });
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
