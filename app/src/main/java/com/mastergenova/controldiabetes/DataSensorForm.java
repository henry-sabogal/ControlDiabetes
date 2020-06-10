package com.mastergenova.controldiabetes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataSensorForm {

    @SerializedName("datalist")
    private List<DataList> datalist = null;

    public List<DataList> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<DataList> datalist) {
        this.datalist = datalist;
    }

}
