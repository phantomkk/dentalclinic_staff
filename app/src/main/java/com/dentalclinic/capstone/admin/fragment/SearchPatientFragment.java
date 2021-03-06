package com.dentalclinic.capstone.admin.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajithvgiri.searchdialog.SearchableDialog;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.activities.BookAppointmentActivity;
import com.dentalclinic.capstone.admin.activities.BookAppointmentReceptActivity;
import com.dentalclinic.capstone.admin.activities.CreatePatientActivity;
import com.dentalclinic.capstone.admin.activities.LoginActivity;
import com.dentalclinic.capstone.admin.activities.MainActivity;
import com.dentalclinic.capstone.admin.activities.PatientDetailActivity;
import com.dentalclinic.capstone.admin.activities.PatientPaymentActivity;
import com.dentalclinic.capstone.admin.adapter.AppointmentSwift2Adapter;
import com.dentalclinic.capstone.admin.adapter.PatientAdapter;
import com.dentalclinic.capstone.admin.adapter.PatientSwiftAdapter;
import com.dentalclinic.capstone.admin.adapter.SearchDentistAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.api.services.AppointmentService;
import com.dentalclinic.capstone.admin.api.services.PatientService;
import com.dentalclinic.capstone.admin.api.services.PaymentService;
import com.dentalclinic.capstone.admin.api.services.StaffService;
import com.dentalclinic.capstone.admin.api.services.UserService;
import com.dentalclinic.capstone.admin.dialog.AppointmentDetailDialog;
import com.dentalclinic.capstone.admin.dialog.SearchDentistDialog;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.Payment;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.dentalclinic.capstone.admin.utils.Utils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.dewinjm.monthyearpicker.Util;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchPatientFragment extends BaseFragment {
    private FloatingActionsMenu menuMultipleActions;
    private FloatingActionButton btnNewPatient, btnNewAppointment, btnNewPayment;
    private List<Patient> patients = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();
    private Staff currentStaff;
    private PatientSwiftAdapter mAdapter;
    private AppointmentSwift2Adapter mAdapter2;
    private RecyclerView recyclerView;
    private RecyclerView appointmentRecyclerView;
    private TextView textView,txtLabelAppointment;
    private LinearLayoutManager mLayoutManager;
    private String phone;
    public static final String PATIENT_INFO = "PATIENT_INFO";
    public static final String STAFF_INFO = "STAFF_INFO";
    public static final int REUQUEST_CREATE_APPOINTMENT = 102;
    public SearchPatientFragment() {
        // Required empty public constructor
    }

    private Toolbar toolbar;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        getActivity().setTitle("Tìm kiếm bệnh nhân");
        View view = inflater.inflate(R.layout.fragment_search_patient, container, false);
        textView = view.findViewById(R.id.txt_label_message);
        txtLabelAppointment = view.findViewById(R.id.txt_label_appointment);
//        int number = 0;
        txtLabelAppointment.setText(getResources().getString(R.string.label_patient_apppointment,0));

        //button newPatient
        getAllPhone();
        btnNewPatient = new FloatingActionButton(getContext());
        btnNewPatient.setTitle("Thêm mới bệnh nhân");
        btnNewPatient.setIconDrawable(getResources().getDrawable(R.drawable.ic_supervisor_account_white_24dp));
        btnNewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
//                showMessage("Thêm mới bệnh nhân");
                Intent intent = new Intent(getContext(), CreatePatientActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(AppConst.PHONE, phone);
                intent.putExtra(AppConst.BUNDLE, bundle);
                startActivity(intent);
            }
        });

        //button new Appointment
        btnNewAppointment = new FloatingActionButton(getContext());
        btnNewAppointment.setTitle("Đặt lịch cho bệnh nhân");
        btnNewAppointment.setIconDrawable(getResources().getDrawable(R.drawable.ic_schedule_white_24dp));
        btnNewAppointment.setColorNormal(getResources().getColor(R.color.color_green_500));
        btnNewAppointment.setColorPressed(getResources().getColor(R.color.color_green_800));
        btnNewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(Utils.isRceiption(getContext())){
                    intent = new Intent(getContext(), BookAppointmentReceptActivity.class);
                }else{
                    intent = new Intent(getContext(), BookAppointmentActivity.class);
                }
//                Intent intent = new Intent(getContext(), BookAppointmentReceptActivity.class);
                intent.putExtra(AppConst.PHONE, phone);
                startActivityForResult(intent,REUQUEST_CREATE_APPOINTMENT);
            }
        });


        //button payment
        btnNewPayment = new FloatingActionButton(getContext());
        btnNewPayment.setTitle("Thanh Toán");
        btnNewPayment.setIconDrawable(getResources().getDrawable(R.drawable.ic_payment_payment_24dp));
        btnNewPayment.setColorNormal(getResources().getColor(R.color.color_yellow_600));
        btnNewPayment.setColorPressed(getResources().getColor(R.color.color_yellow_800));
        btnNewPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPayment();
            }
        });

        menuMultipleActions = view.findViewById(R.id.multiple_actions);
//        if(Utils.isDentist(getContext())){
//            menuMultipleActions.setVisibility(View.GONE);
//        }
        menuMultipleActions.addButton(btnNewAppointment);
        menuMultipleActions.addButton(btnNewPayment);
        menuMultipleActions.addButton(btnNewPatient);
        removeAllButton();
        if (getContext() != null) {
            currentStaff = CoreManager.getStaff(getContext());
        }
//
        recyclerView = view.findViewById(R.id.listView);
        mAdapter = new PatientSwiftAdapter(getContext(), patients);
        mAdapter.setOnDelListener(new PatientSwiftAdapter.onSwipeListener() {
            @Override
            public void onTreatment(int pos) {

                if (pos >= 0 && pos < patients.size()) {
                    showLoading();
                    Patient crrPatient = patients.get(pos);
                    StaffService service = APIServiceManager.getService(StaffService.class);
                    service.receiveAppt(patients.get(pos).getId())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Response<SuccessResponse> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body() != null) {
                                            logError("ABC", "CCC");
                                            showSuccessMessage(response.body().getMessage());
                                            ((MainActivity) getActivity()).getPatienst(phone);
                                            appointmentRecyclerView.getLayoutManager().removeAllViews();
                                        }
                                    } else if (response.code() == 417) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext())
                                                .setTitle(getString(R.string.dialog_default_title))
                                                .setMessage("Bạn có muốn tạo lịch hẹn")
                                                .setPositiveButton("Đồng ý", (DialogInterface dialogInterface, int i) -> {
                                                    Intent intent = new Intent(getContext(), BookAppointmentReceptActivity.class);
                                                    intent.putExtra(SearchPatientFragment.PATIENT_INFO, crrPatient);
                                                    if (currentStaff != null) {
                                                        intent.putExtra(SearchPatientFragment.STAFF_INFO, currentStaff);
                                                    }
                                                    startActivityForResult(intent, REUQUEST_CREATE_APPOINTMENT);
                                                }).setNegativeButton("Không", (DialogInterface dialogInterface, int i) -> {
                                                       //Do nothing
                                                        }
                                                );
                                        alertDialog.show();
                                    } else if (response.code() == 500) {
                                        showFatalError(response.errorBody(), "mAdapter.setOnDelListener");
                                    } else if (response.code() == 401) {
                                        showErrorUnAuth();
                                    } else if (response.code() == 400) {
                                        showBadRequestError(response.errorBody(), "mAdapter.setOnDelListener");
                                    } else {
                                        showDialog(getString(R.string.error_message_api));
                                    }
                                    hideLoading();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    showErrorMessage(getString(R.string.error_message_api));
                                    hideLoading();
                                }
                            });
                }
            }

            @Override
            public void onItemClick(int pos) {
                if (pos < patients.size()) {
//                    SwipeBean swipeBean = mDatas.get(pos);
//                    mDatas.remove(swipeBean);
//                    mAdapter.notifyItemInserted(0);
//                    mDatas.add(0, swipeBean);
//                    mAdapter.notifyItemRemoved(pos + 1);
//                    if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
//                        mRv.scrollToPosition(0);
//                    }
                    //notifyItemRangeChanged(0,holder.getAdapterPosition()+1);
                    Intent intent = new Intent(getContext(), PatientDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AppConst.PATIENT_OBJ, patients.get(pos));
                    intent.putExtra(AppConst.BUNDLE, bundle);
                    startActivity(intent);

                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager = new GridLayoutManager(getContext(), 1));

        //6 2016 10 21 add , 增加viewChache 的 get()方法，
        // 可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。我个人觉得意义不大，
//        mRv.setOnTouchListener();
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                    if (null != viewCache) {
                        viewCache.smoothClose();
                    }
                }
                return false;
            }
        });

        recyclerView.setNestedScrollingEnabled(true);

        appointmentRecyclerView = view.findViewById(R.id.list_appointment);
        appointmentRecyclerView.setNestedScrollingEnabled(true);
        mAdapter2 = new AppointmentSwift2Adapter(getContext(), appointments);
        mAdapter2.setOnDelListener(new AppointmentSwift2Adapter.onSwipeListener() {
            @Override
            public void onChangeDoctorClick(int pos) {
//                showMessage("Change Doctor");
                callApiGetListDentist(pos);

            }

            @Override
            public void onCancleClick(int pos) {
//                showMessage("Cancle Appointment");
//                changeStatus(4, pos);
                showConfirmDelete(pos);
            }

            @Override
            public void onItemClick(int pos) {
              Appointment appointment = appointments.get(pos);
                AppointmentDetailDialog dialog = new AppointmentDetailDialog(getActivity(), appointment);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
        appointmentRecyclerView.setAdapter(mAdapter2);
        appointmentRecyclerView.setLayoutManager(mLayoutManager = new GridLayoutManager(getContext(), 1));

        //6 2016 10 21 add , 增加viewChache 的 get()方法，
        // 可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。我个人觉得意义不大，
//        mRv.setOnTouchListener();
        appointmentRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                    if (null != viewCache) {
                        viewCache.smoothClose();
                    }
                }
                return false;
            }
        });



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        ((MainActivity) getActivity()).getSearchView().setMenuItem(item);
        super.onCreateOptionsMenu(menu, inflater);

    }

    public void removeAllButton() {
//        btnNewPatient.setVisibility(View.GONE);
        if(Utils.isDentist(getContext())){
            btnNewPayment.setVisibility(View.GONE);
            btnNewPatient.setVisibility(View.GONE);
        }else{
            btnNewPayment.setVisibility(View.GONE);
        }
//        btnNewAppointment.setVisibility(View.GONE);
    }

    public void addButtonNewPatient() {
        btnNewPatient.setVisibility(View.VISIBLE);
    }

    public void removeButtonNewPatient() {
//        btnNewPatient.setEnabled(false);
        btnNewPatient.setVisibility(View.GONE);
    }

    public void addButtonPayment() {
        btnNewPayment.setVisibility(View.VISIBLE);

    }

    public void removeButtonPayment() {
        btnNewPayment.setVisibility(View.GONE);

    }

    public void addButtonAppointment() {
        btnNewAppointment.setVisibility(View.VISIBLE);
    }

    public void removeButtonAppointment() {
        btnNewAppointment.setVisibility(View.GONE);
    }

    public void enableAllButton() {
        btnNewPatient.setEnabled(true);
        btnNewPayment.setEnabled(true);
        btnNewAppointment.setEnabled(true);

    }

    private void prepareData() {
//        patients = new ArrayList<>();
        patients.add(new Patient("Vo Quoc Trinh", "1996-10-01", "MALE"));
        patients.add(new Patient("Vo Quoc Trinh", "1996-10-01", "FEMALE"));
    }

    private void prepareData2() {
        appointments.add(new Appointment("haha","vo quoc trinh",3,1));
        appointments.add(new Appointment("haha","vo quoc trinh",3,3));
        appointments.add(new Appointment("haha","vo quoc trinh",3,2));
        appointments.add(new Appointment("haha","vo quoc trinh",3,4));
    }

    public void setPatientsAndNotifiAdapter(List<Patient> patientList) {
        if (patientList.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
//            txtLabelAppointment.setVisibility(View.GONE);
        } else {
//            txtLabelAppointment.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
        patients.clear();
        patients.addAll(patientList);
        mAdapter.notifyDataSetChanged();
    }


    public void setAppointmentAndNotifiAdapter(List<Appointment> appointmentList) {
        txtLabelAppointment.setText(getResources().getString(R.string.label_patient_apppointment, appointmentList.size()));
        appointments.clear();
        appointments.addAll(appointmentList);
        mAdapter2.notifyDataSetChanged();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void changeStatus(int status, int position){
        showLoading();
        StaffService service = APIServiceManager.getService(StaffService.class);
        service.changeStatus(appointments.get(position).getId(), status)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Response<SuccessResponse> successResponseResponse) {
                        if (successResponseResponse.isSuccessful()) {
                            appointments.remove(position);
                            mAdapter2.notifyItemRemoved(position);
                            mAdapter2.notifyItemRangeChanged(position, appointments.size());
                        } else if (successResponseResponse.code() == 500) {
                            showFatalError(successResponseResponse.errorBody(), "appointmentService");
                        } else if (successResponseResponse.code() == 401) {
                            showErrorUnAuth();
                        } else if (successResponseResponse.code() == 400) {
                            showBadRequestError(successResponseResponse.errorBody(), "appointmentService");
                        } else {
                            showErrorMessage(getString(R.string.error_on_error_when_call_api));
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showWarningMessage(getResources().getString(R.string.error_on_error_when_call_api));
                        logError(BookAppointmentReceptActivity.class, "callApi", e.getMessage());
                        hideLoading();
                    }
                });
    }
    private Disposable disposable;

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
                            if (getActivity() != null && userResponse.body() !=null) {
                                ((MainActivity) getActivity()).setSugesstion(userResponse.body());
                            }
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

    private void getPayment(){
        showLoading();
        if (patients != null) {
            PaymentService paymentService = APIServiceManager.getService(PaymentService.class);
            paymentService.getByPhone(patients.get(0).getPhone())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Response<List<Payment>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Response<List<Payment>> listResponse) {
                            ArrayList<Payment> list = (ArrayList<Payment>) listResponse.body();
                            if (list != null) {
                                Intent intent = new Intent(getContext(), PatientPaymentActivity.class);
                                intent.putExtra(Utils.LIST_PAYMENT, list);
                                intent.putExtra(AppConst.PHONE, phone);
                                startActivity(intent);
                            } else if (listResponse.code() == 500) {
                                showFatalError(listResponse.errorBody(), "prepareData");
                            } else if (listResponse.code() == 401) {
                                showErrorUnAuth();
                            } else if (listResponse.code() == 400) {
                                showBadRequestError(listResponse.errorBody(), "prepareData");
                            }
                            hideLoading();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            showErrorMessage("Không thể kết nối đến server");
                            hideLoading();
                        }
                    });
//                List<Payment> listPayment = new ArrayList<>();
//                for (TreatmentHistory t : treatmentHistories) {
//
//                }
        }
    }
    private List<SearchDentistAdapter.SearchDentisItem> listItemDentist = new ArrayList<>();
    private List<Staff> listDentist = new ArrayList<>();
    private SearchDentistDialog searchDentistDialog;
    private void callApiGetListDentist(int appointmentPos) {
        StaffService service = APIServiceManager.getService(StaffService.class);
        Calendar c = Calendar.getInstance();
        String currentDate = DateUtils.getDate(c.getTime(), DateTimeFormat.DATE_TIME_DB_2);
        service.getCurrentFreeDentistAt(currentDate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<Staff>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<List<Staff>> listResponse) {
                        if (listResponse.isSuccessful()) {
                            if (listResponse.body() != null) {
                                listDentist.clear();
                                listItemDentist.clear();
                                listDentist.addAll(listResponse.body());
                                listItemDentist.addAll(convertListDentist(listDentist));
                                searchDentistDialog = new SearchDentistDialog(getActivity(), listItemDentist, "Tìm kiếm nha sĩ");
                                searchDentistDialog.setOnItemSelected(new SearchDentistDialog.OnSearchDentistItemSelected() {
                                    @Override
                                    public void onClick(int position, SearchDentistAdapter.SearchDentisItem searchListItem) {
//                                        showMessage("id" + listDentist.get(position).getId());
                                        changeDentist(appointments.get(appointmentPos).getId(),listDentist.get(position).getId(),appointmentPos, position);
                                    }
                                });
                                if (searchDentistDialog != null && listDentist != null && listDentist.size() > 0) {
                                    searchDentistDialog.show();
                                } else {
                                    showMessage("Danh sách nha sĩ trống");
                                }
                            }
                        } else if (listResponse.code() == 500) {
                            showFatalError(listResponse.errorBody(), "callApiGetListDentist");
                        } else if (listResponse.code() == 401) {
                            showErrorUnAuth();
                        } else if (listResponse.code() == 400) {
                            showBadRequestError(listResponse.errorBody(), "callApiGetListDentist");
                        } else {
                            showDialog(getString(R.string.error_message_api));
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showErrorMessage("Không thể kết nối đến máy chủ");
                    }
                });
    }
    private List<SearchDentistAdapter.SearchDentisItem> convertListDentist(List<Staff> list) {
        List<SearchDentistAdapter.SearchDentisItem> listItems = new ArrayList<>();
        for (Staff s : list) {
            SearchDentistAdapter.SearchDentisItem item =
                    new SearchDentistAdapter.SearchDentisItem(s.getId(), s.getName(), s.getStatus());
            listItems.add(item);
        }
        return listItems;
    }
    public void changeDentist(int appointmentId, int dentistId,int appointmentPos, int dentisPosition) {
        showLoading();
        StaffService service = APIServiceManager.getService(StaffService.class);
        service.changeDentist(appointmentId, dentistId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Response<SuccessResponse> successResponseResponse) {
                        if (successResponseResponse.isSuccessful()) {
                            showMessage("Đổi nha sĩ thành công!");
                            appointments.get(appointmentPos).setStaff(listDentist.get(dentisPosition));
                            mAdapter2.notifyDataSetChanged();
                        } else if (successResponseResponse.code() == 500) {
                            showFatalError(successResponseResponse.errorBody(), "appointmentService");
                        } else if (successResponseResponse.code() == 401) {
                            showErrorUnAuth();
                        } else if (successResponseResponse.code() == 400) {
                            showBadRequestError(successResponseResponse.errorBody(), "appointmentService");
                        } else {
                            showErrorMessage(getString(R.string.error_on_error_when_call_api));
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showWarningMessage(getResources().getString(R.string.error_on_error_when_call_api));
                        logError(BookAppointmentReceptActivity.class, "callApi", e.getMessage());
                        hideLoading();
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REUQUEST_CREATE_APPOINTMENT){
            if(resultCode == getActivity().RESULT_OK){
                ((MainActivity) getActivity()).getPatienst(phone);
                appointmentRecyclerView.getLayoutManager().removeAllViews();
                recyclerView.getLayoutManager().removeAllViews();
            }
        }
    }

    public void showConfirmDelete(int pos) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getContext())
                .setMessage("Bạn có muốn hủy lịch hẹn này?")
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
//                        changeStatus(4, pos);
                        ((MainActivity) getActivity()).getPatienst(phone);
                        appointmentRecyclerView.getLayoutManager().removeAllViews();
                    }
                });
        alertDialog.show();
    }
}
