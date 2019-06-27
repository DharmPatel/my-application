package com.example.google.csmia_temp.Helpdesk.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MuiltiticketResponse {
    @SerializedName("ticket_code")
    @Expose
    private String ticketCode;

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }
}
