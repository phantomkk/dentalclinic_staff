package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.api.requestobject.PatientProfileRequest;
import com.dentalclinic.capstone.admin.api.requestobject.UpdatePatientRequest;
import com.dentalclinic.capstone.admin.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.admin.models.Patient;
import com.dentalclinic.capstone.admin.models.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PatientService {
    @GET("api/patient/getListPatientByPhone")
    Single<Response<User>> getPatientsByPhone(@Query("phone") String phone);

    @POST("api/patient/updatePatient")
    Single<Response<SuccessResponse>> changePatientInfo(@Body PatientProfileRequest request);
    @FormUrlEncoded
    @POST("api/patient/receive")
    Single<Response<SuccessResponse>> beginTreatment(@Field("patient_id") int patientId);
}
