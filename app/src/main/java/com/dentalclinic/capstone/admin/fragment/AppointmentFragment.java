package com.dentalclinic.capstone.admin.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.activities.ShowTreatmentHistoryActivity;
import com.dentalclinic.capstone.admin.adapter.AppointmentAdapter;
import com.dentalclinic.capstone.admin.models.Appointment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView;
    private SwipeMenuListView mListView;
    private List<Appointment> appointments =new ArrayList<>();
    private AppointmentAdapter mAdapter;
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                showMessage("refresh");
//                prepareData();
//                mAdapter.notifyDataSetChanged();
            }
        });
        prepareData();
        mListView = view.findViewById(R.id.listView);
        mAdapter = new AppointmentAdapter(getContext(), appointments);
        mListView.setAdapter(mAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                switch (menu.getViewType()) {
                    case 0:
                        createMenu1(menu);
                        break;
                    case 1:
                        createMenu2(menu);
                        break;
                }


            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                ApplicationInfo item = mAppList.get(position);
                switch (index) {
                    case 0:
                        // open
                        showMessage(0+"");
                        break;
                    case 1:
                        // delete
//					delete(item);
                        showMessage(1+"");
                        break;
                    case 2:
                        showMessage(2+"");
                        Intent intent = new Intent(getContext(), ShowTreatmentHistoryActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        return view;
    }

    public void createMenu1(SwipeMenu menu){
        // create "open" item
        SwipeMenuItem editItem = new SwipeMenuItem(getContext());
        editItem.setBackground(R.color.color_blue_500);
        editItem.setWidth(dp2px(70));
        editItem.setIcon(R.drawable.ic_edit_white_24dp);
        editItem.setTitleSize(18);
        editItem.setTitleColor(Color.WHITE);
        // add to menu
        menu.addMenuItem(editItem);

        // create "delete" item
        SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
        // set item background
        deleteItem.setBackground(R.color.color_red_500);
        // set item width
        deleteItem.setWidth(dp2px(70));
        // set a icon
        deleteItem.setIcon(R.drawable.ic_delete_white_24dp);
        // add to menu
        menu.addMenuItem(deleteItem);
    }
    public void createMenu2(SwipeMenu menu){
        // create "open" item


        SwipeMenuItem editItem = new SwipeMenuItem(getContext());
        editItem.setBackground(R.color.color_blue_500);
        editItem.setWidth(dp2px(70));
        editItem.setIcon(R.drawable.ic_edit_white_24dp);
        editItem.setTitleSize(18);
        editItem.setTitleColor(Color.WHITE);
        // add to menu
        menu.addMenuItem(editItem);

        // create "delete" item
        SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
        // set item background
        deleteItem.setBackground(R.color.color_red_500);
        // set item width
        deleteItem.setWidth(dp2px(70));
        // set a icon
        deleteItem.setIcon(R.drawable.ic_delete_white_24dp);
        // add to menu
        menu.addMenuItem(deleteItem);


        SwipeMenuItem skipItem = new SwipeMenuItem(getContext());
        // set item background
        // set item width
        skipItem.setWidth(dp2px(70));
        // set item title
        // set item title fontsize
        skipItem.setIcon(R.drawable.ic_done_white_24dp);
        skipItem.setTitleSize(18);
        // set item title font color
        skipItem.setTitleColor(Color.WHITE);
        //set backgroup
        skipItem.setBackground(R.color.color_green_500);
        // add to menu
        menu.addMenuItem(skipItem);
    }
    public void prepareData(){
        appointments= new ArrayList<>();
        appointments.add(new Appointment("haha","vo quoc trinh",3,1));
        appointments.add(new Appointment("haha","vo quoc trinh",3,0));
        appointments.add(new Appointment("haha","vo quoc trinh",3,0));
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
