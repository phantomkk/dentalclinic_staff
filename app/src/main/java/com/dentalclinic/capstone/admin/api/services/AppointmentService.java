package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.models.Appointment;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AppointmentService {
    @GET("api/city/{id}/districts")
    Single<Response<List<Appointment>>> getAppointment(@Query("dentist_id") int id, @Query("month") int month, @Query("year") int year);
}
