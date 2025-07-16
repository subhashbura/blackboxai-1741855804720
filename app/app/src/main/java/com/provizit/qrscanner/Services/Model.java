package com.provizit.qrscanner.Services;



import java.io.Serializable;

public class Model implements Serializable {
    public Integer result;
    public Items items;
    public CompanyInfo incomplete_data;
    public Integer getResult() {
        return result;
    }
    public void setResult(Integer result) {
        this.result = result;
    }
    public Items getItems() {
        return items;
    }

    public CompanyInfo getIncomplete_data() {
        return incomplete_data;
    }
}
