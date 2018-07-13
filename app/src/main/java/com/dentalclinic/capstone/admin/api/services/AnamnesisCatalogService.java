package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.models.AnamnesisCatalog;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface AnamnesisCatalogService {

    @GET("api/anamnesisCatalog/all")
    Single<Response<List<AnamnesisCatalog>>> getAllCatalog();
}
