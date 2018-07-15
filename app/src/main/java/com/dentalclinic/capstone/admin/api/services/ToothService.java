package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.models.Tooth;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface ToothService {
    @GET("api/tooth/all")
    Single<Response<List<Tooth>>> getAllTooth();
}
