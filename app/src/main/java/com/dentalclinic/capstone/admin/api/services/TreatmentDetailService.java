package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TreatmentDetailService {
    @POST("api/treatmentDetail/create")
    Single<Response<SuccessResponse>> createTreatmentDetail(@Body MultipartBody body);
}
