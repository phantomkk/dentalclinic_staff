package com.dentalclinic.capstone.admin.fragment;


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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.activities.ShowTreatmentHistoryActivity;
import com.dentalclinic.capstone.admin.adapter.AppointmentAdapter;
import com.dentalclinic.capstone.admin.adapter.AppointmentSwiftAdapter;
import com.dentalclinic.capstone.admin.adapter.PatientSwiftAdapter;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.List;

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
//        mAdapter = new AppointmentAdapter(getContext(), appointments);
//        mListView.setAdapter(mAdapter);
//
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//            @Override
//            public void create(SwipeMenu menu) {
//
//                switch (menu.getViewType()) {
//                    case 0:
//                        createMenu1(menu);
//                        break;
//                    case 1:
//                        createMenu2(menu);
//                        break;
//                }
//
//
//            }
//        };
//        // set creator
//        mListView.setMenuCreator(creator);
//        mListView.setSmoothScrollbarEnabled(true);
//        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
////                ApplicationInfo item = mAppList.get(position);
//                Appointment appointment = appointments.get(position);
//                if(appointment!=null){
//                    Patient patient = appointment.getPatient();
//                    if(patient==null){
//                        showMessage("Thông Tin Chưa Được Cập Nhật");
//                    }else{
//                        switch (index) {
//                            case 0:
//                                break;
//                            case 1:
//                                showMessage(1 + "");
//                                break;
//                            case 2:
//                                showMessage(2 + "");
//                                Intent intent = new Intent(getContext(), ShowTreatmentHistoryActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable(AppConst.PATIENT_OBJ, patient);
//                                intent.putExtra(AppConst.BUNDLE,bundle);
//                                startActivity(intent);
//                                break;
//                        }
//                    }
//                }
//                return false;
//            }
//        });

        mListView= view.findViewById(R.id.listView);
        mAdapter = new AppointmentSwiftAdapter(getContext(), appointments);
        mAdapter.setOnDelListener(new AppointmentSwiftAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < appointments.size()) {
//                    Toast.makeText(FullDelDemoActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                    appointments.remove(pos);
                    mAdapter.notifyItemRemoved(pos);//推荐用这个
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
                    showMessage("delete");
                }
            }

            @Override
            public void onTop(int pos) {
                if (pos > 0 && pos < appointments.size()) {
//                    SwipeBean swipeBean = mDatas.get(pos);
//                    mDatas.remove(swipeBean);
//                    mAdapter.notifyItemInserted(0);
//                    mDatas.add(0, swipeBean);
//                    mAdapter.notifyItemRemoved(pos + 1);
//                    if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
//                        mRv.scrollToPosition(0);
//                    }
                    //notifyItemRangeChanged(0,holder.getAdapterPosition()+1);
                    showMessage("top");
                }
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(mLayoutManager = new GridLayoutManager(getContext(), 1));

        //6 2016 10 21 add , 增加viewChache 的 get()方法，
        // 可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。我个人觉得意义不大，
//        mRv.setOnTouchListener();
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
