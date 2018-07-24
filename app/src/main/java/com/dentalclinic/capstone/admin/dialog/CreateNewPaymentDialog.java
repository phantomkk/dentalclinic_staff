package com.dentalclinic.capstone.admin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.api.requestobject.ReqAbsentRequest;
import com.dentalclinic.capstone.admin.models.Payment;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.Utils;

public class CreateNewPaymentDialog extends Dialog {
    public Activity c;
    public Dialog d;
    public TextView btnSave;
    private Payment payment;
    private TextView txtTreatment, txtTotal, txtNotPayYet;
    private EditText edtMoney;
    private OnButtonClickListener listener;
    public CreateNewPaymentDialog(Activity a, Payment payment) {
        super(a);
        this.c = a;
        this.payment = payment;
    }
    public interface OnButtonClickListener {
        void onSave(float money, Payment payment);
    }

    public OnButtonClickListener getListener() {
        return listener;
    }

    public void setListener(OnButtonClickListener listener) {
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_payment_detail);
        txtTreatment = findViewById(R.id.txt_treatment);
        txtTotal = findViewById(R.id.txt_total_money);
        txtNotPayYet = findViewById(R.id.txt_not_pay_yet);
        edtMoney = findViewById(R.id.edt_money);


        btnSave = findViewById(R.id.btn_save);

        if (payment != null) {
           if(payment.getTreatmentNames() !=null){
               String rs = "";
               for (int i=0; i<payment.getTreatmentNames().size();i++){
                   if(i == payment.getTreatmentNames().size()-1){
                       rs+= payment.getTreatmentNames().get(i);
                   }else{
                       rs+=payment.getTreatmentNames().get(i)+", ";
                   }
               }
           }
           if(payment.getTotalPrice()!=null){
               txtTotal.setText(payment.getTotalPrice()+" "+ getContext().getResources().getString(R.string.current_unit));
               if(payment.getPaid()!=null){
                   txtNotPayYet.setText((payment.getTotalPrice()- payment.getPaid())+" "+ getContext().getResources().getString(R.string.current_unit));
               }
           }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSave(Float.parseFloat(edtMoney.getText().toString()), payment);
            }
        });

    }



}

