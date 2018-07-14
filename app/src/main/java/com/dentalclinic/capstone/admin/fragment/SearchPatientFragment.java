package com.dentalclinic.capstone.admin.fragment;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.activities.LoginActivity;
import com.dentalclinic.capstone.admin.activities.MainActivity;
import com.dentalclinic.capstone.admin.adapter.PatientAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.services.UserService;
import com.dentalclinic.capstone.admin.models.Patient;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
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
    private PatientAdapter mAdapter;
    private SwipeMenuListView mListView;
    private TextView textView;

    public SearchPatientFragment() {
        // Required empty public constructor
    }

    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        getActivity().setTitle("Tìm kiếm bệnh nhân");
        View view = inflater.inflate(R.layout.fragment_search_patient, container, false);
        textView = view.findViewById(R.id.txt_label_message);
        btnNewPatient = new FloatingActionButton(getContext());
        btnNewPatient.setTitle("Thêm mới bệnh nhân");
        btnNewPatient.setIconDrawable(getResources().getDrawable(R.drawable.ic_supervisor_account_white_24dp));
        btnNewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                showMessage("Thêm mới bệnh nhân");
            }
        });

        btnNewAppointment = (FloatingActionButton) view.findViewById(R.id.btn_new_appointment);
        btnNewAppointment.setTitle("Đặt lịch cho bệnh nhân");
        btnNewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Đặt lịch cho bệnh nhân");
                menuMultipleActions.addButton(btnNewPatient);
            }
        });

        btnNewPayment = (FloatingActionButton) view.findViewById(R.id.btn_new_payment);
        btnNewPayment.setTitle("Thanh Toán");
        btnNewPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Thanh Toán");
            }
        });

        menuMultipleActions = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions);

//        prepareData();
        mListView = (SwipeMenuListView) view.findViewById(R.id.listView);
        mAdapter = new PatientAdapter(getContext(), patients);
        mListView.setAdapter(mAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem skipItem = new SwipeMenuItem(getContext());
                // set item background
                // set item width
                skipItem.setWidth(dp2px(50));
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

                SwipeMenuItem editItem = new SwipeMenuItem(getContext());
                editItem.setBackground(R.color.color_blue_500);
                editItem.setWidth(dp2px(50));
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
                deleteItem.setWidth(dp2px(50));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_white_24dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Patient item = patients.get(position);
                switch (index) {
                    case 0:
                        // open
                        showMessage("patient" + position);
                        break;
                    case 1:
                        // delete
                        showMessage("patient" + position);
                        break;
                    case 2:
                        patients.remove(position);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showMessage("position" + i);
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

    private void prepareData() {
//        patients = new ArrayList<>();
        patients.add(new Patient("Vo Quoc Trinh", "1996-10-01", "MALE"));
        patients.add(new Patient("Vo Quoc Trinh", "1996-10-01", "FEMALE"));
        patients.add(new Patient("Vo Quoc Trinh", "1996-10-01", "OTHER"));
    }

    public void setPatientsAndNotifiAdapter(List<Patient> patientList){
        if(patientList.isEmpty()){
            textView.setVisibility(View.VISIBLE);
        }else{
            textView.setVisibility(View.GONE);
        }
        patients.clear();
        patients.addAll(patientList);
        mAdapter.notifyDataSetChanged();
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
