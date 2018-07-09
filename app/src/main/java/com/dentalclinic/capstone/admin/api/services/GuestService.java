package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.api.requestobject.RegisterRequest;
import com.dentalclinic.capstone.admin.models.User;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GuestService {
    @POST("/api/user/register")
    Single<Response<User>> register(@Body RegisterRequest requestObj);

}
