package com.dentalclinic.capstone.admin.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.activities.BookAppointmentReceptActivity;
import com.dentalclinic.capstone.admin.activities.PatientDetailActivity;
import com.dentalclinic.capstone.admin.activities.ShowTreatmentHistoryActivity;
import com.dentalclinic.capstone.admin.adapter.AppointmentAdapter;
import com.dentalclinic.capstone.admin.adapter.AppointmentSwiftAdapter;
import com.dentalclinic.capstone.admin.adapter.PatientSwiftAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.api.services.AppointmentService;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

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
public class AppointmentFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView;
    private RecyclerView mListView;
    private List<Appointment> appointments =new ArrayList<>();
    private AppointmentSwiftAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TextView txtDate, txtMessage;
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

        Calendar.getInstance();
        prepareData(DateUtils.getDate(Calendar.getInstance().getTime(), DateTimeFormat.DATE_TIME_DB_2));

//        mListView = view.findViewById(R.id.listView);
        mListView= view.findViewById(R.id.listView);
        mAdapter = new AppointmentSwiftAdapter(getContext(), appointments);

        mAdapter.setOnDelListener(new AppointmentSwiftAdapter.onSwipeListener() {
            @Override
            public void onStartClick(int pos) {
                changeStatus(2, pos);
            }

            @Override
            public void onTreatmentClick(int pos) {
                if(appointments.get(pos).getPatient()!=null) {
                    Patient patient = appointments.get(pos).getPatient();
                    if (patient != null) {
                        Intent intent = new Intent(getContext(), ShowTreatmentHistoryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConst.PATIENT_OBJ, patient);
                        intent.putExtra(AppConst.BUNDLE, bundle);
                        startActivity(intent);
                    }
                }else{
                    showDialog("Thông tin chưa được cập nhật");
                }
            }

            @Override
            public void onDoneClick(int pos) {
                changeStatus(3, pos);
            }

            @Override
            public void onItemClick(int pos) {
                if(appointments.get(pos).getPatient()!=null) {
                    Patient patient = appointments.get(pos).getPatient();
                    if (patient != null) {
                        Intent intent = new Intent(getContext(), PatientDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConst.PATIENT_OBJ, patient);
                        intent.putExtra(AppConst.BUNDLE, bundle);
                        startActivity(intent);
                    }
                }else{
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

    public void setData(){
        appointments= new ArrayList<>();
        appointments.add(new Appointment("haha","vo quoc trinh",3,1, new Patient()));
        appointments.add(new Appointment("haha","vo quoc trinh",3,2, new Patient()));
        appointments.add(new Appointment("haha","vo quoc trinh",3,3, new Patient()));
    }

    public void prepareData(String dateFormat){
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
                           appointments.addAll(response.body());
                           mAdapter.notifyDataSetChanged();
                           if(appointments.isEmpty()){
                               txtMessage.setVisibility(View.VISIBLE);
                           }else{
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

//    private int dp2px(int dp) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
//                getResources().getDisplayMetrics());
//    }

    public void callSwifData(String dateFormat){
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
                            appointments.addAll(response.body());
                            mAdapter.notifyDataSetChanged();
                            if(appointments.isEmpty()){
                                txtMessage.setVisibility(View.VISIBLE);
                            }else{
                                txtMessage.setVisibility(View.GONE);
                            }
                            swipeRefreshLayout.setRefreshing(false);
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

    public void changeStatus(int status, int position){
        showLoading();
        AppointmentService service = APIServiceManager.getService(AppointmentService.class);
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

}