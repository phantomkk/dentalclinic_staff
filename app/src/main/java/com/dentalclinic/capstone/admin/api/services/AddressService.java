package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.models.City;
import com.dentalclinic.capstone.admin.models.District;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AddressService {
    @GET("api/district/")
    Single<Response<List<District>>> getAllDistricts();

    @GET("api/city/all")
    Single<Response<List<City>>> getAllCities();
    @GET("api/city/{id}/districts")
    Single<Response<List<District>>> getDistrictByCityID(@Path("id") int id);
}
