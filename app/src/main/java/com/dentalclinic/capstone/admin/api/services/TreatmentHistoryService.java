package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.models.TreatmentHistory;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface TreatmentHistoryService {

    @POST("api/treatmentHistory/create")
    Single<Response<SuccessResponse>> createTreatmentHistory(@Body MultipartBody data);

    @GET("api/treatmentHistory/getByPatientId/{id}")
    Single<Response<List<TreatmentHistory>>> getByPatientId(@Path("id") int id);
}
