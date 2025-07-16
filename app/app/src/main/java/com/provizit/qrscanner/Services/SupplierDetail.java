package com.provizit.qrscanner.Services;

import java.io.Serializable;

public class SupplierDetail implements Serializable {
    public String contact_person;
    public String name;
    public String supplier_email;

    public String email;
    public String mobile;
    public String id_number;
    public String nationality;
    public String vehicle_no;
    public String vehicle_type;
    public boolean disabledStatus;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getContact_person() {
        return contact_person;
    }

    public String getSupplier_email() {
        return supplier_email;
    }

    public String getId_number() {
        return id_number;
    }

    public String getNationality() {
        return nationality;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public boolean isDisabledStatus() {
        return disabledStatus;
    }
}
