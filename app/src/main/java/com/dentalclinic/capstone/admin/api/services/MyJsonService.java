package com.dentalclinic.capstone.admin.api.services;

import com.alamkanak.weekview.WeekViewEvent;
import com.dentalclinic.capstone.admin.api.requestobject.AppointmentRequest;
import com.dentalclinic.capstone.admin.api.responseobject.Event;
import com.dentalclinic.capstone.admin.models.Appointment;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;


/**
 * Created by Raquib-ul-Alam Kanak on 1/3/16.
 * Website: http://alamkanak.github.io
 */
public interface MyJsonService {

    @GET("/1kpjf")
    void listEvents(Callback<List<Appointment>> eventsCallback);

}
