package com.dentalclinic.capstone.admin.api.services;

import com.dentalclinic.capstone.admin.models.District;
import com.dentalclinic.capstone.admin.models.PieChartData;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChartService {
    @GET("api/treatmentHistory/getTreatmentHistoryReport/")
    Single<Response<List<PieChartData>>> getDataPiechart(@Query("staff_id") int id,@Query("month") int month,@Query("year") int year);
}
