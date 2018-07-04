package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.api.requestobject.LoginRequest;
import com.dentalclinic.capstone.admin.api.requestobject.UpdateUserRequest;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.model.User;

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
    //    @GET("/api/users/{id}")
//    Call<User> getByID(@Path("id") int id);
//    @POST("/api/users/login")
//    Call<User> login(@Body User user);
//    @POST("/api/users/register")
//    Call<User> register(@Body User user);
//    @PUT("/api/users/{id}")
//    Call<User> update(@Path("id") int id, @Body User user);

//    @FormUrlEncoded
//     @POST("/api/users/updatepwd")
//    Call<User> updatePassword(@Field("id") int id, @Field("password") String password);

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
