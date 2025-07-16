package com.provizit.qrscanner.Services;

import java.io.Serializable;
 import java.util.ArrayList;

public class CompanyInfo implements Serializable {
    private String comp_id;
    private ArrayList<String> pic;

    public ArrayList<String> getPic() {
        return pic;
    }

    public void setPic(ArrayList<String> pic) {
        this.pic = pic;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }
}
