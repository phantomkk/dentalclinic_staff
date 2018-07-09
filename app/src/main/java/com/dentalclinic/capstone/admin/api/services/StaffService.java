package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.models.User;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface StaffService {
    @FormUrlEncoded
    @POST("api/staff/login")
    Single<Response<User>> login(@Field("phone") String phone, @Field("password")String password);
}
