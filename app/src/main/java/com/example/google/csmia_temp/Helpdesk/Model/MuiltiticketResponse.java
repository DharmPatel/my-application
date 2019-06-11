package com.example.google.csmia_temp.Helpdesk.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MuiltiticketResponse {
    @SerializedName("respone")
    @Expose
    private String respone;

    public String getRespone() {
        return respone;
    }

    public void setRespone(String respone) {
        this.respone = respone;
    }
}
