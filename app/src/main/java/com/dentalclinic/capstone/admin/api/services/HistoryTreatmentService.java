package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.models.TreatmentHistory;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HistoryTreatmentService {
    @GET("api/treatmentHistory/all")
    Single<Response<List<TreatmentHistory>>> getAllHistoryTreatment();

    @GET("api/treatmentHistory/{phone}")
    Single<Response<List<TreatmentHistory>>> getHistoryTreatmentByPhone(@Path("phone") String phone);

    @GET("api/treatmentHistory/getById/{id}")
    Single<Response<List<TreatmentHistory>>> getHistoryTreatmentById(@Path("id") int id);
}
