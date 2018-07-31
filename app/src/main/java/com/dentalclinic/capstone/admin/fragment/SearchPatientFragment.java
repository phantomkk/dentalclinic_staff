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
import com.dentalclinic.capstone.admin.activities.BookAppointmentReceptActivity;
import com.dentalclinic.capstone.admin.activities.CreatePatientActivity;
import com.dentalclinic.capstone.admin.activities.LoginActivity;
import com.dentalclinic.capstone.admin.activities.MainActivity;
import com.dentalclinic.capstone.admin.activities.PatientDetailActivity;
import com.dentalclinic.capstone.admin.adapter.AppointmentSwift2Adapter;
import com.dentalclinic.capstone.admin.adapter.PatientAdapter;
import com.dentalclinic.capstone.admin.adapter.PatientSwiftAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.api.services.PatientService;
import com.dentalclinic.capstone.admin.api.services.UserService;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
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
        int number = 2;
        txtLabelAppointment.setText(getResources().getString(R.string.label_patient_apppointment, number));
        //button newPatient
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
                startActivity(new Intent(getContext(), BookAppointmentReceptActivity.class));
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
//                showMessage("Thanh Toán");
            }
        });

        menuMultipleActions = view.findViewById(R.id.multiple_actions);
        menuMultipleActions.addButton(btnNewAppointment);
        menuMultipleActions.addButton(btnNewPayment);
        menuMultipleActions.addButton(btnNewPatient);
        removeAllButton();
        if (getContext() != null) {
            currentStaff = CoreManager.getStaff(getContext());
        }
        prepareData();
        prepareData2();
//        mListVi
// ew = (SwipeMenuListView) view.findViewById(R.id.listView);
//        mAdapter = new PatientAdapter(getContext(), patients);
//        mListView.setAdapter(mAdapter);
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//            @Override
//            public void create(SwipeMenu menu) {
//                // create "open" item
//                SwipeMenuItem skipItem = new SwipeMenuItem(getContext());
//                // set item background
//                // set item width
//                skipItem.setWidth(dp2px(50));
//                // set item title
//                // set item title fontsize
//                skipItem.setIcon(R.drawable.ic_done_white_24dp);
//                skipItem.setTitleSize(18);
//                // set item title font color
//                skipItem.setTitleColor(Color.WHITE);
//                //set backgroup
//                skipItem.setBackground(R.color.color_green_500);
//                // add to menu
//                menu.addMenuItem(skipItem);
//
//                SwipeMenuItem editItem = new SwipeMenuItem(getContext());
//                editItem.setBackground(R.color.color_blue_500);
//                editItem.setWidth(dp2px(50));
//                editItem.setIcon(R.drawable.ic_edit_white_24dp);
//                editItem.setTitleSize(18);
//                editItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(editItem);
//
//                // create "delete" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
//                // set item background
//                deleteItem.setBackground(R.color.color_red_500);
//                // set item width
//                deleteItem.setWidth(dp2px(50));
//                // set a icon
//                deleteItem.setIcon(R.drawable.ic_delete_white_24dp);
//                // add to menu
//                menu.addMenuItem(deleteItem);
//            }
//        };
//        // set creator
//        mListView.setMenuCreator(creator);
//
//        // step 2. listener item click event
//        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                Patient item = patients.get(position);
//                switch (index) {
//                    case 0:
//                        // open
//                        showMessage("patient" + position);
//                        break;
//                    case 1:
//                        // delete
//                        showMessage("patient" + position);
//                        break;
//                    case 2:
//                        patients.remove(position);
//                        mAdapter.notifyDataSetChanged();
//                        break;
//                }
//                return false;
//            }
//        });
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getContext(), PatientDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(AppConst.PATIENT_OBJ, patients.get(i));
//                intent.putExtra(AppConst.BUNDLE, bundle);
//                startActivity(intent);
//            }
//        });

        recyclerView = view.findViewById(R.id.listView);
        mAdapter = new PatientSwiftAdapter(getContext(), patients);
        mAdapter.setOnDelListener(new PatientSwiftAdapter.onSwipeListener() {
            @Override
            public void onTreatment(int pos) {

                if (pos >= 0 && pos < patients.size()) {
//                    Toast.makeText(FullDelDemoActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
//                    patients.remove(pos);
//                    mAdapter.notifyItemRemoved(pos);//推荐用这个
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
//                    showMessage("delete");
//                }
                    showLoading();
                    Patient crrPatient = patients.get(pos);
                    PatientService service = APIServiceManager.getService(PatientService.class);
                    service.beginTreatment(patients.get(pos).getId())
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
                                                    startActivity(intent);
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
                showMessage("Change Doctor");
            }

            @Override
            public void onCancleClick(int pos) {
                showMessage("Cancle Appointment");
            }

            @Override
            public void onItemClick(int pos) {
                showMessage("show dialog");
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
        btnNewPatient.setVisibility(View.GONE);
        btnNewPayment.setVisibility(View.GONE);
        btnNewAppointment.setVisibility(View.GONE);
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
    }

    public void setPatientsAndNotifiAdapter(List<Patient> patientList) {
        if (patientList.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
        patients.clear();
        patients.addAll(patientList);
        mAdapter.notifyDataSetChanged();
    }

    public void setAppointmentAndNotifiAdapter(List<Appointment> appointmentList) {
//        if (appointmentList.isEmpty()) {
//            textView.setVisibility(View.VISIBLE);
//        } else {
//            textView.setVisibility(View.GONE);
//        }
        appointments.clear();
        appointments.addAll(appointmentList);
        mAdapter2.notifyDataSetChanged();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
