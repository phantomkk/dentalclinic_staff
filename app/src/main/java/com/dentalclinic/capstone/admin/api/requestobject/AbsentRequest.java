package com.dentalclinic.capstone.admin.api.requestobject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class AbsentRequest implements Serializable{
    private List<Calendar> calendars;
    private String note;

    public List<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(List<Calendar> calendars) {
        this.calendars = calendars;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
