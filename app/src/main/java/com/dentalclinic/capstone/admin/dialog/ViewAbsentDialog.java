package com.dentalclinic.capstone.admin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.Absent;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.utils.DateTimeFormat;
import com.dentalclinic.capstone.admin.utils.DateUtils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewAbsentDialog extends Dialog {
    public Activity c;
    public Dialog d;
    public TextView btnOk;
    private CircleImageView imgAvatar;
    private Absent absent;
    private TextView txtName, txtPhone, txtEmail, txtStatus, txtMessage, txtCreatedDate;

    public ViewAbsentDialog(Activity a, Absent absent) {
        super(a);
        this.c = a;
        this.absent = absent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_approve_absent);
        setTitle("Xác nhận từ Admin");
        imgAvatar = findViewById(R.id.img_staff_avatar);
        txtName = findViewById(R.id.txt_staff_name);
        txtPhone = findViewById(R.id.txt_staff_phone);
        txtEmail = findViewById(R.id.txt_email);
        txtStatus = findViewById(R.id.txt_status);
        txtMessage = findViewById(R.id.txt_message_from_staff);
        txtCreatedDate = findViewById(R.id.txt_created_date);

        if (absent != null) {
            if (absent.getStaffApprove() != null) {
                Staff staff = absent.getStaffApprove();
                if (staff.getAvatar() != null) {
                    Picasso.get().load(staff.getAvatar()).into(imgAvatar);
                }
                if (staff.getName() != null) {
                    txtName.setText(staff.getName());
                }
                if (staff.getPhone() != null) {
                    txtPhone.setText(staff.getPhone());
                }
                if (staff.getEmail() != null) {
                    txtEmail.setText(staff.getEmail());
                }
            }
            if (absent.getMessageFromStaff() != null) {
                txtMessage.setText(absent.getMessageFromStaff());
            }
            if(absent.isApproved()){
                txtStatus.setText("xác nhận");
                txtStatus.setTextColor(getContext().getResources().getColor(R.color.color_green_500));
            }else {
                txtStatus.setText("hủy");
                txtStatus.setTextColor(getContext().getResources().getColor(R.color.color_red_500));
            }
            if(absent.getCreatedTime()!=null){
                txtCreatedDate.setText(DateUtils.changeDateFormat(absent.getCreatedTime(), DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_FOTMAT));
            }

        }

        btnOk = findViewById(R.id.btn_confirm);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


    }
}
