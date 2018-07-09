package com.dentalclinic.capstone.admin.utils;

import android.content.Context;
import android.util.Log;

import com.dentalclinic.capstone.admin.models.User;


/**
 * Created by lucky on 15-Oct-17.
 */

public class CoreManager {
    private static User mUser = null;
//    private static Patient mCurrentPatient = null;
    private CoreManager() {

    }

    public static User getUser(Context context) {
        mUser = Utils.getUserInSharePref(context);
        if (mUser == null) {
            Log.d(AppConst.DEBUG_CORE_TAG, "get User from share prefef Null");
        }
        return mUser;
    }

    public static void setUser(Context context, User user) {
//        if (user != null) {
//            List<Patient> list = user.getPatients();
//            mUser = user;
//            if (list != null && list.size() > 0) {
//                user.setCurrentPatient(user.getPatients().get(0));
//            } else {
//                Log.d(AppConst.DEBUG_TAG, "class CoreManager.setUser(): LIST PATIENT IS NULL OR EMPTY PLEASE CHECK");
//            }
//        } else {
//            Log.d(AppConst.DEBUG_TAG, "class CoreManager.setUser(): USER IS NULL");
//        }
        Utils.saveUserInSharePref(context, user);

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
    public static void clearUser(Context context){
        Utils.saveUserInSharePref(context, null);
//         setCurrentPatient(-1, context);
         mUser=null;
    }



}
