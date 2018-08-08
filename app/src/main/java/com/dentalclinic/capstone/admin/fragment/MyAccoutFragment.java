package com.dentalclinic.capstone.admin.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.activities.EditAccoutActivity;
import com.dentalclinic.capstone.admin.activities.EditPasswordActivity;
import com.dentalclinic.capstone.admin.activities.MainActivity;
import com.dentalclinic.capstone.admin.api.APIServiceManager;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.api.services.StaffService;
import com.dentalclinic.capstone.admin.api.services.UserService;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.CoreManager;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.dentalclinic.capstone.admin.utils.GenderUtils;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccoutFragment extends BaseFragment implements View.OnClickListener {
    public static int REQUEST_CHANGE_PASSWORD = 10000;
    public static int REQUEST_CHANGE_ACCOUNT = 10001;
    Button btnChangeAvatar, btnEdit, btnChangePhone, btnChangePassword;
    CircleImageView cvAvatar;
    TextView txtName, txtGender, txtPhone, txtAddress, txtDateOfBirth, txtEmail, txtDegree;
    Staff staff = new Staff();
    LinearLayout llEditPassword;

    public MyAccoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_accout, container, false);
        btnChangeAvatar = v.findViewById(R.id.btn_change_avatar);
        btnChangeAvatar.setOnClickListener(this);
        btnEdit = v.findViewById(R.id.btn_edit_accout);
        btnEdit.setOnClickListener(this);
        llEditPassword = v.findViewById(R.id.ln_edit_password);
        llEditPassword.setOnClickListener(this);
//        btnChangePassword = v.findViewById(R.id.btn_edit_password);
//        btnChangePassword.setOnClickListener(this);
        cvAvatar = v.findViewById(R.id.img_avatar_user);
        txtName = v.findViewById(R.id.txt_name);
        txtDateOfBirth = v.findViewById(R.id.txt_date_of_birth);
        txtGender = v.findViewById(R.id.txt_gender);
        txtPhone = v.findViewById(R.id.txt_phone);
        txtAddress = v.findViewById(R.id.txt_address);
        txtEmail = v.findViewById(R.id.txt_email);
        txtDegree = v.findViewById(R.id.txt_degree);
//        setData(CoreManager.getCurrentuser(getContext()));
        staff = CoreManager.getStaff(getContext());
        if (staff != null) {
            setData(staff);
        }
        return v;
    }

    private void setData(Staff user) {
        if (user != null) {
            if (user.getAvatar() != null) {
//                Picasso.get().invalidate(user.getAvatar());
                Picasso.get().load(user.getAvatar()).into(cvAvatar);
            }
            if (user.getName() != null) {
                txtName.setText(user.getName());
            }
            if (user.getEmail() != null) {
                txtEmail.setText(user.getEmail());
            }
            if (user.getDegree() != null) {
                txtDegree.setText(user.getDegree());
            }
            if (user.getDateOfBirth() != null) {
                txtDateOfBirth.setText(DateUtils.changeDateFormat(user.getDateOfBirth(), DateTimeFormat.DATE_TIME_DB_2, DateTimeFormat.DATE_APP));
            }
            if (user.getGender() != null) {
                txtGender.setText(GenderUtils.toString(user.getGender()));
            }
            if (user.getPhone() != null) {
                txtPhone.setText(user.getPhone());
            }
            if (user.getAddress() != null) {
                String address = user.getAddress();
                if (user.getDistrict() != null) {
                    address += ", " + user.getDistrict().getName();
                    if (user.getCity() != null) {
                        address += ", " + user.getCity().getName();
                    }
                }
                txtAddress.setText(address);
            }
        }
    }

//    private void prepareData() {
////        user = new User("Vo Quoc Trinh", "Quang Trung p11", "01695149049", "1996-06-30 00:00:00", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS8lVVU1zu2SCLibBztyJFTTqCkOaNFg97S3RHyDpjbDbseoBOY");
////        user.setDistrict(new District("Go Vap", new City("Ho Chi Minh")));
//        user = new User();
//        user.setPhone("01685149049");
//        user.setAddress("Quang trung p11");
//        user.setDegree("Bác sĩ");
//        user.setEmail("mr.trinhvo1996@gmail.com");
//        user.setDateOfBirth("1996-30-06");
//        user.setGender("MALE");
//        user.setCity(new City(1));
//        user.setDistrict(new District(1));
//        user.setAvatar("https://cdn.iconscout.com/public/images/icon/free/png-512/avatar-user-hacker-3830b32ad9e0802c-512x512.png");
//        user.setName("Võ Quốc Trịnh");
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    private StaffService staffService = APIServiceManager.getService(StaffService.class);
    private Disposable userServiceDisposable;

    private void uploadImage(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        MultipartBody.Part id = MultipartBody.Part.createFormData("id", CoreManager.getStaff(getContext()).getId() + "");
        //cột username đang bị null hết chỉ có 2 record dc add vào: luc2, luc12345678
        staffService.changeAvatar(image, id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        userServiceDisposable = d;
                    }

                    @Override
                    public void onSuccess(Response<SuccessResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
//                                CoreManager.saveAvatar(getContext(), (String) response.body().getData());
//                                MainActivity.resetHeader(getContext());
//                                response.body().getData();
                                Staff staff = CoreManager.getStaff(getContext());
                                staff.setAvatar((String)response.body().getData());
                                ((MainActivity) getActivity()).setDataHeader(staff);
                                showMessage(getResources().getString(R.string.success_message_api));
                            }
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                                    .setMessage(getResources().getString(R.string.error_message_api))
                                    .setPositiveButton("Thử lại", (DialogInterface dialogInterface, int i) -> {
                                    });
                            alertDialog.show();
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        e.printStackTrace();
                        Toast.makeText(getContext(), getResources().getString(R.string.error_on_error_when_call_api), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        Bundle bundle = null;
        switch (view.getId()) {
            case R.id.btn_change_avatar:
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1)
                        .start(getContext(), this);
                break;
            case R.id.btn_edit_accout:
                intent = new Intent(getActivity(), EditAccoutActivity.class);
                bundle = new Bundle();
//                bundle.putSerializable(AppConst.USER_OBJ, CoreManager.getCurrentuser(getContext()));
                bundle.putSerializable(AppConst.STAFF_OBJ, staff);
                intent.putExtra(AppConst.BUNDLE, bundle);
                startActivityForResult(intent, REQUEST_CHANGE_ACCOUNT);
                break;
            case R.id.ln_edit_password:
                intent = new Intent(getActivity(), EditPasswordActivity.class);
                bundle = new Bundle();
                bundle.putSerializable(AppConst.STAFF_OBJ, staff);
//                bundle.putSerializable(AppConst.USER_OBJ, CoreManager.getCurrentuser(getContext()));
                intent.putExtra(AppConst.BUNDLE, bundle);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    InputStream is = getActivity().getContentResolver().openInputStream(resultUri);
//                    showLoading();
                    uploadImage(getBytes(is));
                    cvAvatar.setImageURI(resultUri);
//                    showMessage("update success!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cvAvatar.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CHANGE_PASSWORD) {
            if (resultCode == getActivity().RESULT_OK) {
                if (getContext() != null) {
                    setData(CoreManager.getStaff(getContext()));
                }
            }
        } else if (requestCode == REQUEST_CHANGE_ACCOUNT) {
            if (resultCode == getActivity().RESULT_OK) {
                if (getContext() != null) {
                    Staff staff = CoreManager.getStaff(getContext());
                    setData(staff);
                    ((MainActivity) getActivity()).setDataHeader(staff);
                }
            }
        }
    }


}
