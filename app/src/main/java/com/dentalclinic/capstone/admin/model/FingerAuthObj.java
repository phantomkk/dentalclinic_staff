package com.dentalclinic.capstone.admin.model;

import java.io.Serializable;

public class FingerAuthObj implements Serializable {
    private String phone;
    private String password;

    public FingerAuthObj(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
