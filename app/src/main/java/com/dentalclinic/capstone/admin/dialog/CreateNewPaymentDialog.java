package com.dentalclinic.capstone.admin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.api.requestobject.ReqAbsentRequest;
import com.dentalclinic.capstone.admin.models.Payment;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CreateNewPaymentDialog extends Dialog {
    public Activity c;
    public Dialog d;
    public TextView btnSave;
    private Payment payment;
    private TextView txtTreatment, txtTotal, txtNotPayYet;
    private AutoCompleteTextView edtMoney;
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

    private String prePrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_payment_detail);
        txtTreatment = findViewById(R.id.txt_treatment);
        txtTotal = findViewById(R.id.txt_total_money);
        txtNotPayYet = findViewById(R.id.txt_not_pay_yet);
        edtMoney = findViewById(R.id.edt_money);

        edtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(prePrice)) {
//if(current)
                    String cleanString = charSequence.toString().replaceAll("[đ,.]", "");
                    Log.d("DEBUG", "BEFORE: " + cleanString);
//            double parsed = Double.parseDouble(cleanString);
                    String formatted = formatVnCurrence(cleanString);
                    Log.d("DEBUG", "AFTER: " + formatted);
                    prePrice = formatted;
                    edtMoney.setText(formatted);
                    edtMoney.setSelection(formatted.length() > 1 ? formatted.length() - 1 : formatted.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnSave = findViewById(R.id.btn_save);

        if (payment != null) {
            if (payment.getTreatmentNames() != null) {
                String rs = "";
                for (int i = 0; i < payment.getTreatmentNames().size(); i++) {
                    if (i == payment.getTreatmentNames().size() - 1) {
                        rs += payment.getTreatmentNames().get(i);
                    } else {
                        rs += payment.getTreatmentNames().get(i) + ", ";
                    }
                }
                txtTreatment.setText(rs);
            }
            if (payment.getTotalPrice() != null) {
                txtTotal.setText(Utils.formatMoney(payment.getTotalPrice()) + getContext().getResources().getString(R.string.current_unit));
                if (payment.getPaid() != null) {
                    txtNotPayYet.setText(Utils.formatMoney(payment.getTotalPrice() - payment.getPaid()) + getContext().getResources().getString(R.string.current_unit));
                }
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price = edtMoney.getText().toString().replaceAll("[đ,.]", "").trim();
                if (isValid(price)) {
                    listener.onSave(Float.parseFloat(price), payment);
                }
            }
        });

    }

    public static String formatVnCurrence(String price) {
        NumberFormat format =
                new DecimalFormat("#,##0.00");// #,##0.00 ¤ (¤:// Currency symbol)
        format.setCurrency(Currency.getInstance(Locale.US));//Or default locale

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = format.format(Double.parseDouble(price));
        price = price.replaceAll(",", "\\.");

        if (price.endsWith(".00")) {
            int centsIndex = price.lastIndexOf(".00");
            if (centsIndex != -1) {
                price = price.substring(0, centsIndex);
            }
        }
        price = String.format("%sđ", price);
        return price;
    }

    private boolean isValid(String price) {
        boolean rs = true;
        Long money = new Long(price);
        Long notePay = payment.getTotalPrice() - payment.getPaid();
        if (money > notePay) {
            edtMoney.setError("Vui lòng nhập tiền nhỏ hơn hoặc bằng số nợ");
            rs = false;
        }
        return rs;
    }
}

