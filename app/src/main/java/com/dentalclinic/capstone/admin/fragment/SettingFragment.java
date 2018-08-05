package com.dentalclinic.capstone.admin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.dentalclinic.capstone.admin.R;
import com.dentalclinic.capstone.admin.models.Setting;
import com.dentalclinic.capstone.admin.utils.SettingManager;

public class SettingFragment extends Fragment {
    private Switch switchVibrate;
    private Switch switchPromote;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        switchVibrate = v.findViewById(R.id.switch_vibrate);
        switchPromote = v.findViewById(R.id.switch_promote);

        Setting setting = SettingManager.getSettingPref(getContext());
        if(setting!=null){
            switchPromote.setChecked(setting.isPromote());
            switchVibrate.setChecked(setting.isVibrate());
        }
        switchPromote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (getContext() != null) {
                    setting.setPromote(checked);
                    SettingManager.saveSetting(getContext(), setting);
                    Log.d("AAABBB", "onCheckedChanged: setPromote");
                }
            }
        });
        switchVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (getContext() != null) {
                    setting.setVibrate(checked);
                    SettingManager.saveSetting(getContext(), setting);
                    Log.d("AAABBB", "onCheckedChanged: setVibrate");}
            }
        });
        return v;

    }
}
