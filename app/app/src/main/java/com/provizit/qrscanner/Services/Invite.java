package com.provizit.qrscanner.Services;

import java.io.Serializable;

public class Invite implements Serializable {

    public String email;
    public String mobile;
    public String name;
    public String id;
    public String company;


    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }
}
