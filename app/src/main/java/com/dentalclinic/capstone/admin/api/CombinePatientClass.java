package com.dentalclinic.capstone.admin.api;

import com.dentalclinic.capstone.admin.models.Appointment;
import com.dentalclinic.capstone.admin.models.Patient;

import java.util.List;

import retrofit2.Response;

public class CombinePatientClass {
    private Response<List<Patient>> patients;
    private Response<List<Appointment>> appointments;

    public CombinePatientClass(Response<List<Patient>> patients, Response<List<Appointment>> appointments) {
        this.patients = patients;
        this.appointments = appointments;
    }

    public Response<List<Patient>> getPatients() {
        return patients;
    }

    public void setPatients(Response<List<Patient>> patients) {
        this.patients = patients;
    }

    public Response<List<Appointment>> getAppointments() {
        return appointments;
    }

    public void setAppointments(Response<List<Appointment>> appointments) {
        this.appointments = appointments;
    }
}
