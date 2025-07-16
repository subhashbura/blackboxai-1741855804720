package com.provizit.qrscanner.Services;

import java.io.Serializable;
import java.util.ArrayList;

public class Items implements Serializable {
    private String comp_id;
    private String idnumber;
    private String name;
    private Integer status;
    private double start;
    private ArrayList<Double> starts;
    private double end;
    public ArrayList<SupplierDetail> supplier_details;
    public ArrayList<SupplierDetail> contractorsData;
    public ArrayList<SupplierDetail> subcontractorsData;
    public ArrayList<Invite> invites;

    public ArrayList<SupplierDetail> getSupplier_details() {
        return supplier_details;
    }

    public ArrayList<SupplierDetail> getContractorsData() {
        return contractorsData;
    }

    public ArrayList<SupplierDetail> getSubcontractorsData() {
        return subcontractorsData;
    }

    public ArrayList<Invite> getInvites() {
        return invites;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public String getName() {
        return name;
    }

    public double getStart() {
        return start;
    }

    public Integer getStatus() {
        return status;
    }

    public ArrayList<Double> getStarts() {
        return starts;
    }


    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }
    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }
}

