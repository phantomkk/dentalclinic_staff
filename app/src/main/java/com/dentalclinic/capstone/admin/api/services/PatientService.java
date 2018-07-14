package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.api.requestobject.PatientProfileRequest;
import com.dentalclinic.capstone.admin.models.Patient;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PatientService {
    @GET("api/patient/getByPhone")
    Single<Response<List<Patient>>> getPatientsByPhone(@Query("phone") String phone);
}
