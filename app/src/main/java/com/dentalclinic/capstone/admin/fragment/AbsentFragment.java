package com.dentalclinic.capstone.admin.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.activities.DatePickerActivity;
import com.dentalclinic.capstone.admin.adapter.AbsentSwiftAdapter;
import com.dentalclinic.capstone.admin.adapter.AppointmentSwiftAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.requestobject.AbsentRequest;
import com.dentalclinic.capstone.admin.api.requestobject.ReqAbsentRequest;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.api.services.StaffService;
import com.dentalclinic.capstone.admin.dialog.DatePickerAbsentDialog;
import com.dentalclinic.capstone.admin.dialog.ViewAbsentDialog;
import com.dentalclinic.capstone.admin.models.Absent;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
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
public class AbsentFragment extends BaseFragment {
    FloatingActionButton button;
    TextView txtMonthPicker;
    RecyclerView recyclerView;
    private AbsentSwiftAdapter mAdapter;
    private List<Absent> absents;
    private LinearLayoutManager mLayoutManager;
    private DatePickerAbsentDialog dialog;
    public AbsentFragment() {
        // Required empty public constructor
    }
    private ReqAbsentRequest reqAbsentRequest;
    private int yearSelected;
    private int monthSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_absent, container, false);
        button = view.findViewById(R.id.btn_request);
        txtMonthPicker = view.findViewById(R.id.txt_date_picker);
        recyclerView = view.findViewById(R.id.recyclerView);
        Calendar calendar = Calendar.getInstance();
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);
        String value = "Tháng " + (monthSelected + 1) + " năm " + yearSelected;
        txtMonthPicker.setText(value);
        txtMonthPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(monthSelected, yearSelected);
//                dialogFragment.getDialog().setCanceledOnTouchOutside(true);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int month) {
//                        showMessage("month="+ month+", year=" + year);
                        String value = "Tháng " + (month + 1) + " năm " + year;
                        monthSelected = month;
                        yearSelected = year;
                        txtMonthPicker.setText(value);
                    }
                });
                dialogFragment.show(getFragmentManager(), null);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new DatePickerAbsentDialog(getActivity());
                dialog.setOnButtonClickListener(new DatePickerAbsentDialog.OnButtonClickListener() {
                    @Override
                    public void onSubmitClick(ReqAbsentRequest absentRequest){
                        ReqAbsentRequest request = absentRequest;
                        request.setStaffId(CoreManager.getStaff(getContext()).getId());
                        createNewRequestAbsent(request);
                    }
                });
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();


            }
        });
        prepateData();
        mAdapter = new AbsentSwiftAdapter(getContext(), absents);
        mAdapter.setOnDelListener(new AbsentSwiftAdapter.onSwipeListener() {
            @Override
            public void onViewClick(int pos) {
                Absent absent = absents.get(pos);
                absent.setStaffApprove(new Staff("Vo Quoc Trinh","mr.trinhvo1996@gmail.com","01685149049","https://cdn2.vectorstock.com/i/1000x1000/01/66/businesswoman-character-avatar-icon-vector-12800166.jpg"));
                absent.setMessageFromStaff("nghi vui ve");
                ViewAbsentDialog dialog = new ViewAbsentDialog(getActivity(), absent);

                dialog.show();
            }

            @Override
            public void onDelete(int pos) {
                showMessage("delete");
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager = new GridLayoutManager(getContext(), 1));
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

        return view;
    }

    private void prepateData() {
//        absents = new ArrayList<>();
//        Absent absent = new Absent("2018-07-10 23:02:56", "2018-07-10 23:02:56", "xin nghĩ");
//        absents.add(absent);
//        Absent absent1 = new Absent("2018-07-10 23:02:56", "2018-07-10 23:02:56", "xin nghĩ");
//        absent1.setStaffApprove(new Staff());
//        absent1.setApproved(false);
//        absent1.setCreatedTime("2018-07-10 23:02:56");
//        absents.add(absent1);
//        Absent absent2 = new Absent("2018-07-10 23:02:56", "2018-07-10 23:02:56", "xin nghĩ");
//        absent2.setStaffApprove(new Staff());
//        absent2.setApproved(true);
//        absent2.setCreatedTime("2018-07-10 23:02:56");
//        absents.add(absent2);

        showLoading();
        StaffService service = APIServiceManager.getService(StaffService.class);
        service.getListRequestAbsent(CoreManager.getStaff(getContext()).getId(), monthSelected, yearSelected)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Response<List<Absent>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Response<List<Absent>> response) {
                if(response.isSuccessful()){
                    absents.clear();
                    absents.addAll(response.body());
                    mAdapter.notifyDataSetChanged();
                } else if (response.code() == 500) {
                    showFatalError(response.errorBody(), "logoutOnServer");
                } else if (response.code() == 401) {
                    showErrorUnAuth();
                } else if (response.code() == 400) {
                    showBadRequestError(response.errorBody(), "logoutOnServer");
                } else {
                    showDialog(getString(R.string.error_message_api));
                }
                hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                hideLoading();
            }
        });

    }


    private void createNewRequestAbsent(ReqAbsentRequest request){
        showLoading();
        StaffService service = APIServiceManager.getService(StaffService.class);
        service.requestAbsent(request).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Response<Absent>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Response<Absent> response) {
                if(response.isSuccessful()){
                    dialog.dismiss();
                    showSuccessMessage("Gửi Yêu Cầu Thành Công");
                    absents.add(response.body());
                    mAdapter.notifyDataSetChanged();
                } else if (response.code() == 500) {
                    showFatalError(response.errorBody(), "logoutOnServer");
                } else if (response.code() == 401) {
                    showErrorUnAuth();
                } else if (response.code() == 400) {
                    showBadRequestError(response.errorBody(), "logoutOnServer");
                } else {
                    showDialog(getString(R.string.error_message_api));
                }
                hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                hideLoading();
            }
        });
    }




}
