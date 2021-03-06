package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.models.Medicine;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface MedicineService {
    @GET("api/medicine/all")
    Single<Response<List<Medicine>>> getAllMedicine();
}
