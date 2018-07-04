package com.dentalclinic.capstone.admin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.calendar.Badge;
import com.dentalclinic.capstone.admin.calendar.CalenderDate;
import com.dentalclinic.capstone.admin.calendar.ClickInterface;
import com.dentalclinic.capstone.admin.calendar.CustomCalendar;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends BaseWeekViewFragment {


    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_calendar, container, false);
//        calendarView = view.findViewById(R.id.calendar_view);
//        calendarView.setFullScreenWidth(true);
//
//
//        //adding badges to the dates.
//        List<Badge> badges = new ArrayList<>();
//
//        Badge badge = new Badge(2,11,12);//parameters: countOnBadge,dayOfMonth, Month as 1 to 12;
//
//        badges.add(badge);
//        badges.add(new Badge(1,2,7));
//        badges.add(new Badge(3,3,7));
//        badges.add(new Badge(2,4,7));
//
//        calendarView.setBadgeDateList(badges);
//
//        // implementing onClickListener for dates.
//        calendarView.setOnClickDate(new ClickInterface() {
//            @Override
//            public void setDateClicked(CalenderDate date) {
//
//                //displaying selected date in Toast (dd/mm/yyyy).
//                StyleableToast.makeText(getContext(), "Hello World!", Toast.LENGTH_LONG, R.style.succeToast).show();
//            }
//        });


        return view;
    }

}
