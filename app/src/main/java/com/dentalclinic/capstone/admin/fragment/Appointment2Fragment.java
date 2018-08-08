package com.dentalclinic.capstone.admin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.activities.BookAppointmentReceptActivity;
import com.dentalclinic.capstone.admin.adapter.AppointmentSwiftAdapter;
import com.dentalclinic.capstone.admin.adapter.AppointmentSwiftForReceipAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.services.AppointmentService;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
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
public class Appointment2Fragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView;
    private RecyclerView mListView;
    private List<Appointment> appointments = new ArrayList<>();
    private AppointmentSwiftForReceipAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TextView txtDate, txtMessage;

    public Appointment2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment2, container, false);
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
        return view;
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


}
