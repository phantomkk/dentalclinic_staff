package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestAbsentService {
    @POST("api/requestAbsent/changeStatusDelete/{id}")
    Single<Response<SuccessResponse>> delete(@Path("id") int reqAbsentId);
}
