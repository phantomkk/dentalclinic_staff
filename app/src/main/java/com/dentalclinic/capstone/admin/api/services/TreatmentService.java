package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.models.Treatment;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface TreatmentService {
    @GET("api/treatment/all")
    Single<Response<Treatment>> getAllTreatment();

}
