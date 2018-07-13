package com.dentalclinic.capstone.admin.utils;

import android.content.Context;
import android.util.Log;

import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.models.User;
import com.dentalclinic.capstone.admin.model.FingerAuthObj;

import java.util.List;


/**
 * Created by lucky on 15-Oct-17.
 */

public class CoreManager {
    private static Staff mStaff = null;
    private static FingerAuthObj fingerAuthObj = null;
    //    private static Patient mCurrentPatient = null;
    private CoreManager() {

    }

    public static Staff getStaff(Context context) {
        mStaff = Utils.getStaffInSharePref(context);
        if (mStaff == null) {
            Log.d(AppConst.DEBUG_CORE_TAG, "get User from share prefef Null");
        }
        return mStaff;
    }

    public static void setStaff(Context context, Staff staff) {
        Utils.saveStaffInSharePref(context, staff);

    }
    public static FingerAuthObj getFingerAuthObj(Context context) {
        fingerAuthObj = Utils.getFingerAuthInSharePref(context);
        return fingerAuthObj;
    }

    public static void setFingerAuthObj(Context context,FingerAuthObj fingerAuthObj) {
        if(fingerAuthObj!=null){
            if(fingerAuthObj.getPhone()!=null && !fingerAuthObj.getPhone().isEmpty()){
                if(fingerAuthObj.getPassword()!=null && !fingerAuthObj.getPassword().isEmpty()){
                    Utils.saveFingerAuthInSharePref(context,fingerAuthObj);
                }
            }else{
                Utils.saveFingerAuthInSharePref(context,null);
            }
        }
    }


//    public static Patient getCurrentPatient(Context context){
//        return  Utils.getUserInSharePref(context).getCurrentPatient();
//    }
////    public static void saveAvatar(Context context, String link){
////        User user = CoreManager.getUser(context);
////        Patient patient = user.getCurrentPatient();
////        List<Patient> patients = user.getPatients();
////        patient.setAvatar(link);
////        for (Patient patient1: patients) {
////            if(patient1.getId() == patient.getId()){
////                patient1.setAvatar(link);
////            }
////        }
////        Utils.saveUserInSharePref(context, user);
////    }
////    public static void savePatient(Context context, UpdatePatientRequest  request){
////        DatabaseHelper helper = new DatabaseHelper(context);
////        District district = helper.getDistrictFromId(request.getDistrictId());
////        City city = helper.getCityFromId(district.getCityId());
////        User user = CoreManager.getUser(context);
////        Patient currentPatient = user.getCurrentPatient();
////        List<Patient> patients = user.getPatients();
////        currentPatient.setName(request.getName());
////        currentPatient.setAddress(request.getAddress());
////        currentPatient.setGender(request.getGender());
////        currentPatient.setDistrict(district);
////        currentPatient.setCity(city);
////        currentPatient.setDateOfBirth(request.getBirthday());
////        for (Patient patient1: patients) {
////            if(patient1.getId() == currentPatient.getId()){
////                patient1.setName(request.getName());
////                patient1.setAddress(request.getAddress());
////                patient1.setGender(request.getGender());
////                patient1.setDistrict(district);
////                patient1.setCity(city);
////                patient1.setDateOfBirth(request.getBirthday());
////            }
////        }
////        Utils.saveUserInSharePref(context, user);
////    }
////
////
////
////    public static void setCurrentPatient(int patientId, Context context) {
////        if(patientId==-1){
////            mCurrentPatient=null;
////            return;
////        }
////        if(mUser!=null){
////            if(mUser.getPatients()!=null && mUser.getPatients().size()>0){
////                for (Patient patient1: mUser.getPatients()) {
////                    if (patient1.getId() == patientId){
////                        mUser.setCurrentPatient(patient1);
////                        break;
////                    }
////                }
////            }
////        }
////        Utils.saveUserInSharePref(context, mUser);
////    }
    public static void clearStaff(Context context){
        Utils.saveStaffInSharePref(context, null);
//         setCurrentPatient(-1, context);
         mStaff=null;
    }



}
