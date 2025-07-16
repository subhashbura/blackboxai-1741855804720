package com.provizit.qrscanner.Services;




import java.io.Serializable;
import java.util.ArrayList;

public class Model1 implements Serializable {
    public Integer result;

    public Items getItems() {
        return items;
    }

    public  Items items;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }


}
