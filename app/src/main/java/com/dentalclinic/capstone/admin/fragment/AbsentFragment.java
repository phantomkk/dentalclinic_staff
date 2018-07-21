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
import com.dentalclinic.capstone.admin.api.requestobject.AbsentRequest;
import com.dentalclinic.capstone.admin.dialog.DatePickerAbsentDialog;
import com.dentalclinic.capstone.admin.dialog.ViewAbsentDialog;
import com.dentalclinic.capstone.admin.models.Absent;
import com.dentalclinic.capstone.admin.models.Staff;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    public AbsentFragment() {
        // Required empty public constructor
    }

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
                        monthSelected = month + 1;
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
                DatePickerAbsentDialog dialog = new DatePickerAbsentDialog(getActivity());
                dialog.setOnButtonClickListener(new DatePickerAbsentDialog.OnButtonClickListener() {
                    @Override
                    public void onSubmitClick(AbsentRequest absentRequest) {
                        showMessage(absentRequest.getNote());
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
                ViewAbsentDialog dialog = new ViewAbsentDialog(getActivity());
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
        absents = new ArrayList<>();
        Absent absent = new Absent("2018-07-10 23:02:56", "2018-07-10 23:02:56", "xin nghĩ");
        absents.add(absent);
        Absent absent1 = new Absent("2018-07-10 23:02:56", "2018-07-10 23:02:56", "xin nghĩ");
        absent1.setStaffApprove(new Staff());
        absent1.setStatus(0);
        absents.add(absent1);
        Absent absent2 = new Absent("2018-07-10 23:02:56", "2018-07-10 23:02:56", "xin nghĩ");
        absent2.setStaffApprove(new Staff());
        absent2.setStatus(1);
        absents.add(absent2);
    }

}
