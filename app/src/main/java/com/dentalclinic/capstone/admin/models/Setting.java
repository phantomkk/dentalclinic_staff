package com.dentalclinic.capstone.admin.models;

import java.io.Serializable;

public class Setting implements Serializable{
    private boolean vibrate;
    private boolean promote;

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean isPromote() {
        return promote;
    }

    public void setPromote(boolean promote) {
        this.promote = promote;
    }
}
