package com.dentalclinic.capstone.admin.fragment;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.activities.BookAppointmentActivity;
import com.dentalclinic.capstone.admin.activities.BookAppointmentReceptActivity;
import com.dentalclinic.capstone.admin.activities.CreatePatientActivity;
import com.dentalclinic.capstone.admin.activities.MainActivity;
import com.dentalclinic.capstone.admin.activities.PatientDetailActivity;
import com.dentalclinic.capstone.admin.activities.ShowTreatmentHistoryActivity;
import com.dentalclinic.capstone.admin.adapter.AppointmentAdapter;
import com.dentalclinic.capstone.admin.adapter.AppointmentSwiftAdapter;
import com.dentalclinic.capstone.admin.adapter.PatientSwiftAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.api.services.AppointmentService;
import com.dentalclinic.capstone.admin.api.services.StaffService;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView;
    private RecyclerView mListView;
    private List<Appointment> appointments = new ArrayList<>();
    private AppointmentSwiftAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TextView txtDate, txtMessage;
    private final int REQUEST_BOOK_APPOINTMENT = 902;
    private Button btnLoad;
    public AppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(getContext().getResources().getString(R.string.appointment_title));
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        textView = view.findViewById(R.id.txt_label_message);
        btnLoad = view.findViewById(R.id.btn_load);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        txtDate = view.findViewById(R.id.lb_date);
        txtDate.setText(DateUtils.getCurrentDateFormat());
        txtMessage = view.findViewById(R.id.lb_message);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callSwifData(DateUtils.getDate(Calendar.getInstance().getTime(), DateTimeFormat.DATE_TIME_DB_2));
            }
        });
//        setData();
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareData(DateUtils.getDate(Calendar.getInstance().getTime(), DateTimeFormat.DATE_TIME_DB_2));
                btnLoad.setVisibility(View.GONE);
            }
        });

        Calendar.getInstance();
        prepareData(DateUtils.getDate(Calendar.getInstance().getTime(), DateTimeFormat.DATE_TIME_DB_2));

//        mListView = view.findViewById(R.id.listView);
        mListView = view.findViewById(R.id.listView);
        mAdapter = new AppointmentSwiftAdapter(getContext(), appointments);

        mAdapter.setOnDelListener(new AppointmentSwiftAdapter.onSwipeListener() {
            @Override
            public void onStartClick(int pos) {
                changeStatus(2, pos);
            }

            @Override
            public void onTreatmentClick(int pos) {
                if (appointments.get(pos).getPatient() != null) {
                    Patient patient = appointments.get(pos).getPatient();
                    if (patient != null) {
                        Intent intent = new Intent(getContext(), ShowTreatmentHistoryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConst.PATIENT_OBJ, patient);
                        intent.putExtra(AppConst.BUNDLE, bundle);
                        startActivity(intent);
                    }else{

                    }
                } else {
                    showDialog("Thông tin chưa được cập nhật");
                }
            }

            @Override
            public void onDoneClick(int pos) {
                showConfirmDialog(pos);
            }

            @Override
            public void onItemClick(int pos) {
                if (appointments.get(pos).getPatient() != null) {
                    Patient patient = appointments.get(pos).getPatient();
                    if (patient != null) {
                        Intent intent = new Intent(getContext(), PatientDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConst.PATIENT_OBJ, patient);
                        intent.putExtra(AppConst.BUNDLE, bundle);
                        startActivity(intent);
                    }
                } else {
                    showDialog("Thông tin chưa được cập nhật");
                }
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(mLayoutManager = new GridLayoutManager(getContext(), 1));
        mListView.setOnTouchListener(new View.OnTouchListener() {
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

    public void showConfirmDialog(int pos) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getContext())
                .setMessage("Bạn có muốn đặt lịch hẹn lại cho bệnh nhân này?")
                .setTitle(getString(R.string.dialog_default_title))
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        changeStatus(3, pos);
                    }
                })
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getContext(), BookAppointmentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConst.PATIENT_OBJ, appointments.get(pos).getPatient());
                        bundle.putInt("POSITION_APPOINTMENT",pos);
                        intent.putExtra(AppConst.BUNDLE, bundle);
                        startActivityForResult(intent, REQUEST_BOOK_APPOINTMENT);
                    }
                });
        alertDialog.show();
    }

    public void setData() {
        appointments = new ArrayList<>();
        appointments.add(new Appointment("haha", "vo quoc trinh", 3, 1, new Patient()));
        appointments.add(new Appointment("haha", "vo quoc trinh", 3, 2, new Patient()));
        appointments.add(new Appointment("haha", "vo quoc trinh", 3, 3, new Patient()));
    }

    public void prepareData(String dateFormat) {
        showLoading();
        AppointmentService service = APIServiceManager.getService(AppointmentService.class);
        service.getApppointmentByDate(CoreManager.getStaff(getContext()).getId(), dateFormat)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<Appointment>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<List<Appointment>> response) {
                        if (response.isSuccessful()) {
                            appointments.clear();
                            appointments.addAll(sortList(response.body()));
                            mAdapter.notifyDataSetChanged();
                            if (appointments.isEmpty()) {
                                txtMessage.setVisibility(View.VISIBLE);
                            } else {
                                txtMessage.setVisibility(View.GONE);
                            }

                        } else if (response.code() == 500) {
                            showFatalError(response.errorBody(), "appointmentService");
                        } else if (response.code() == 401) {
                            showErrorUnAuth();
                        } else if (response.code() == 400) {
                            showBadRequestError(response.errorBody(), "appointmentService");
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


    public void callSwifData(String dateFormat) {
        AppointmentService service = APIServiceManager.getService(AppointmentService.class);
        service.getApppointmentByDate(CoreManager.getStaff(getContext()).getId(), dateFormat)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<Appointment>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<List<Appointment>> response) {
                        if (response.isSuccessful()) {
                            appointments.clear();
                            appointments.addAll(sortList(response.body()));
                            mAdapter.notifyDataSetChanged();
                            if (appointments.isEmpty()) {
                                txtMessage.setVisibility(View.VISIBLE);
                            } else {
                                txtMessage.setVisibility(View.GONE);
                            }
                            swipeRefreshLayout.setRefreshing(false);
                            mListView.getLayoutManager().removeAllViews();
                            btnLoad.setVisibility(View.GONE);
                        } else if (response.code() == 500) {
                            showFatalError(response.errorBody(), "appointmentService");
                        } else if (response.code() == 401) {
                            showErrorUnAuth();
                        } else if (response.code() == 400) {
                            showBadRequestError(response.errorBody(), "appointmentService");
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

    public void changeStatus(int status, int position) {
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
                            appointments.get(position).setStatus(status);
                            mAdapter.notifyDataSetChanged();
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


    private List<Appointment> sortList(List<Appointment> appointments) {
        List<Appointment> list = appointments;
        list.sort(Comparator.comparing(Appointment::getStatus).thenComparing(Appointment::getNumericalOrder));

        List<Appointment> list1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus() == 2) {
                list1.add(list.get(i));
//                list.remove(i);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus() == 1) {
                list1.add(list.get(i));
//                list.remove(i);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus() == 0) {
                list1.add(list.get(i));
//                list.remove(i);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus() == 3) {
                list1.add(list.get(i));
//                list.remove(i);
            }
        }
//        list1.addAll(list);
        return list1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BOOK_APPOINTMENT) {
            if (resultCode == getActivity().RESULT_OK) {
                int pos = data.getIntExtra("POSITION_APPOINTMENT", -1);
                if (pos != -1) {
                    changeStatus(3, pos);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance
                    (getActivity())
                    .registerReceiver(mMessageReceiver, new IntentFilter(AppConst.ACTION_RELOAD));
        }
        Log.d("DEBUG_TAG", "ACTION_RELOAD_APPOINTMENT REGISTER");
    }

    @Override
    public void onPause() {
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity())
                    .unregisterReceiver(mMessageReceiver);
        }
        Log.d("DEBUG_TAG", "ACTION_RELOAD_APPOINTMENT PAUSE");
        super.onPause();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("DEBUG_TAG", "Got message mMessageReceiver");
//            showMessage("APPOINTMENT RELOAD");
            String reloadType = intent.getStringExtra(AppConst.ACTION_RELOAD_TYPE);
            if (reloadType.equals(AppConst.ACTION_RELOAD_DENTIST_APPOINTMENT)) {
                btnLoad.setVisibility(View.VISIBLE);
//                prepareData(DateUtils.getDate(Calendar.getInstance().getTime(), DateTimeFormat.DATE_TIME_DB_2));
            }

        }
    };

    public void showConfirmDelete(int pos) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getContext())
                .setMessage("Bạn có muốn hủy lịch hẹn này?")
                .setTitle(getString(R.string.dialog_default_title))
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeStatus(4, pos);
                    }
                });
        alertDialog.show();
    }
}
