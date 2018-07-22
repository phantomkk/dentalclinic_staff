package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.api.requestobject.PatientProfileRequest;
import com.dentalclinic.capstone.admin.api.requestobject.ReqAbsentRequest;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.models.Absent;
import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.RequestAbsent;
import com.dentalclinic.capstone.admin.models.Staff;
import com.dentalclinic.capstone.admin.models.User;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface StaffService {
    @FormUrlEncoded
    @POST("api/staff/login")
    Single<Response<Staff>> login(@Field("phone") String phone, @Field("password") String password);

    @Multipart
    @POST("api/user/changeAvatar")
    Single<Response<SuccessResponse>> changeAvatar(
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part id);

    @FormUrlEncoded
    @POST("api/user/changePassword")
    Single<Response<SuccessResponse>> changePassword(@Field("phone") String phone,
                                                     @Field("current_password") String currentPassword,
                                                     @Field("password") String password);


    @POST("api/staff/createPatient")
    Single<Response<Patient>> createProfile(@Body PatientProfileRequest requestObj);

    @GET("api/staff/getAppointmentByMonth")
    Single<Response<List<Appointment>>> getAppointments(@Query("dentist_id") int id, @Query("month") int month, @Query("year") int year);

    @GET("api/staff/getAvailableDentist")
    Single<Response<List<Staff>>> getAvailableDentist(@Query("date") String date);

    @GET("api/user/logout")
    Single<Response<SuccessResponse>> logout();

    @GET("api/staff/getListRequestAbsentByTime")
    Single<Response<List<Absent>>> getListRequestAbsent(
            @Query("staff_id") int staffid,
            @Query("month") int month,
            @Query("year") int year);

    @GET("api/requestAbsent/delete")
    Single<Response<List<SuccessResponse>>> deleteRequestAbsent(
            @Query("staff_id") int staffid,
            @Query("month") int month,
            @Query("year") int year);


    @GET("api/staff/getListRequestAbsent")
    Single<Response<List<RequestAbsent>>> getListRequestAbsent(@Query("staff_id") int staffid);

    @POST("api/staff/requestAbsent")
    Single<Response<Absent>> requestAbsent(@Body ReqAbsentRequest requestObj);

//    @POST("api/staff/updateStaffInfo")
//    Single<Response<SuccessResponse>> updateStaffInfo(@Body StaffProfileRequest requestObj);
}

