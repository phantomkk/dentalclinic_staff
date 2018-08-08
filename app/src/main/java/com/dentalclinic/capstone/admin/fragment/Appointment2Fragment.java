package com.dentalclinic.capstone.admin.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.activities.BookAppointmentActivity;
import com.dentalclinic.capstone.admin.activities.BookAppointmentReceptActivity;
import com.dentalclinic.capstone.admin.activities.CreatePatientActivity;
import com.dentalclinic.capstone.admin.adapter.AppointmentSwift2Adapter;
import com.dentalclinic.capstone.admin.adapter.AppointmentSwiftAdapter;
import com.dentalclinic.capstone.admin.adapter.AppointmentSwiftForReceipAdapter;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.api.services.AppointmentService;
import com.dentalclinic.capstone.admin.api.services.PatientService;
import com.dentalclinic.capstone.admin.dialog.AppointmentDetailDialog;
import com.dentalclinic.capstone.admin.dialog.ChoosePatientForAppointmentDialog;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

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
    private ChoosePatientForAppointmentDialog dialog;
    private int posAppointment = -1;

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
        Calendar.getInstance();
        prepareData(DateUtils.getDate(Calendar.getInstance().getTime(), DateTimeFormat.DATE_TIME_DB_2));
        mListView = view.findViewById(R.id.list_appointment);
        mListView.setNestedScrollingEnabled(true);
        mAdapter = new AppointmentSwiftForReceipAdapter(getContext(), appointments);
        mAdapter.setOnDelListener(new AppointmentSwiftForReceipAdapter.onSwipeListener() {
            @Override
            public void onTreatment(int pos) {
                posAppointment = pos;
                getPatientsByPhone(appointments.get(pos).getPhone());
            }

            @Override
            public void onChangeDoctorClick(int pos) {
                showMessage("Change Doctor");
            }

            @Override
            public void onCancleClick(int pos) {
//                showMessage("Cancle Appointment");
                showConfirmDelete(pos);
            }

            @Override
            public void onItemClick(int pos) {
                Appointment appointment = appointments.get(pos);
                AppointmentDetailDialog dialog = new AppointmentDetailDialog(getActivity(), appointment);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
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

    private List<Patient> patients;

    private void getPatientsByPhone(String phone) {
        showLoading();
        PatientService service = APIServiceManager.getService(PatientService.class);
        service.getPatientsByPhone(phone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<Patient>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Response<List<Patient>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().isEmpty()) {
                                Intent intent = new Intent(getContext(), CreatePatientActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(AppConst.PHONE, phone);
                                intent.putExtra(AppConst.BUNDLE, bundle);
                                startActivityForResult(intent, AppConst.REQUEST_CREATENEW_PATIENT);
                            } else {
                                dialog = new ChoosePatientForAppointmentDialog(getActivity(), response.body());
                                dialog.setListener(new ChoosePatientForAppointmentDialog.OnButtonClickListener() {
                                    @Override
                                    public void onItemClick(Patient patient) {
                                        receiveAppointmentManually(appointments.get(posAppointment).getId(), patient.getId());
                                    }
                                });
                                dialog.setCanceledOnTouchOutside(true);
                                dialog.show();
                            }
                        } else if (response.code() == 500) {
                            showFatalError(response.errorBody(), "callApiGetPatient");
                        } else if (response.code() == 401) {
                            showErrorUnAuth();
                        } else if (response.code() == 400) {
                            showBadRequestError(response.errorBody(), "callApiGetPatient");
                        } else {
                            showErrorMessage(getString(R.string.error_on_error_when_call_api));
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

    public void receiveAppointmentManually(int appointmentId, int patientId) {
        showLoading();
        AppointmentService service = APIServiceManager.getService(AppointmentService.class);
        service.receiAppointmentManual(patientId, appointmentId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<SuccessResponse> response) {
                        if (response.isSuccessful()) {
                            showMessage("Nhận bệnh thành công!");
                            if(dialog!=null){
                                dialog.dismiss();
                            }
                            prepareData(DateUtils.getDate(Calendar.getInstance().getTime(), DateTimeFormat.DATE_TIME_DB_2));
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

    public void prepareData(String dateFormat) {
        showLoading();
        AppointmentService service = APIServiceManager.getService(AppointmentService.class);
        service.getApppointmentByDate(dateFormat)
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
                            mListView.getRecycledViewPool().clear();
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

    public void changeStatus(int status, int position) {
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
//                            appointments.get(position).setStatus(status);
                            mAdapter.notifyItemRemoved(position);
//                            mAdapter.notifyDataSetChanged();
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
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        showWarningMessage(getResources().getString(R.string.error_on_error_when_call_api));
                        logError(BookAppointmentReceptActivity.class, "callApi", e.getMessage());
                        hideLoading();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    public void callSwifData(String dateFormat) {
        AppointmentService service = APIServiceManager.getService(AppointmentService.class);
        service.getApppointmentByDate(dateFormat)
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
                            mListView.getRecycledViewPool().clear();
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

//        List<Appointment> list1 = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getStatus() == 2) {
//                list1.add(list.get(i));
////                list.remove(i);
//            }
//        }
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getStatus() == 1) {
//                list1.add(list.get(i));
////                list.remove(i);
//            }
//        }
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getStatus() == 0) {
//                list1.add(list.get(i));
////                list.remove(i);
//            }
//        }
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getStatus() == 3) {
//                list1.add(list.get(i));
////                list.remove(i);
//            }
//        }
//        list1.addAll(list);
        return list;
    }

    public void showConfirmDelete(int pos) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getContext())
                .setMessage("Bạn có muốn hủy lịch hẹn này?")
                .setTitle(getString(R.string.dialog_default_title))
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppConst.REQUEST_CREATENEW_PATIENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Bundle bundle = data.getBundleExtra(AppConst.BUNDLE);
                if (bundle != null) {
                    Patient patient = (Patient) bundle.getSerializable(AppConst.PATIENT_OBJ);
                    if (patient.getId() != -1) {
                        receiveAppointmentManually(appointments.get(posAppointment).getId(), patient.getId());
                    }
                }
            }
        }
    }
}
