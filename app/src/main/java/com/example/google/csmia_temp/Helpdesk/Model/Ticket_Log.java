package com.example.google.csmia_temp.Helpdesk.Model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressLint("ParcelCreator")
public class Ticket_Log implements Parcelable {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("log_text")
    @Expose
    private String logText;
    @SerializedName("Employee_Name")
    @Expose
    private String employeeName;

    protected Ticket_Log(Parcel in) {
        userId = in.readString();
        description = in.readString();
        createdAt = in.readString();
        logText = in.readString();
        employeeName = in.readString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(description);
        dest.writeString(createdAt);
        dest.writeString(logText);
        dest.writeString(employeeName);
    }

    public static final Creator<Ticket_Log> CREATOR = new Creator<Ticket_Log>() {
        @Override
        public Ticket_Log createFromParcel(Parcel in) {
            return new Ticket_Log(in);
        }

        @Override
        public Ticket_Log[] newArray(int size) {
            return new Ticket_Log[size];
        }
    };


    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }


}
