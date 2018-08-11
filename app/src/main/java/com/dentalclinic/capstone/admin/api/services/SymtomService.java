package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.models.Symptom;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface SymtomService {
    @GET("api/symptom/all")
    Single<Response<List<Symptom>>> getAll();
}
