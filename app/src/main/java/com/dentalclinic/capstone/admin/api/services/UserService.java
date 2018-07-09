package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.api.requestobject.LoginRequest;
import com.dentalclinic.capstone.admin.api.requestobject.UpdateUserRequest;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.models.User;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by lucky on 15-Oct-17.
 */

public interface UserService {
    @POST("/api/user/login")
    Single<Response<User>> login(@Body LoginRequest request);

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

    @POST("api/user/updatePatient")
    Single<Response<SuccessResponse>> changePatientInfo(@Body UpdateUserRequest request);

    @FormUrlEncoded
    @POST("api/user/updateNotifToken")
    Single<Response<String>> updateNotifyFirebaseToken(@Field("notif_token") String token, @Field("phone") String phone);

}
