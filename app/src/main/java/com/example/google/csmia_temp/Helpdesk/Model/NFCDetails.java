package com.example.google.csmia_temp.Helpdesk.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NFCDetails {

    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("SubLocation")
    @Expose
    private String subLocation;
    @SerializedName("ServiceArea")
    @Expose
    private String serviceArea;
    @SerializedName("department")
    @Expose
    private String department;

    public NFCDetails(String site, String location, String subLocation, String serviceArea, String department) {
        this.site = site;
        this.location = location;
        this.subLocation = subLocation;
        this.serviceArea = serviceArea;
        this.department = department;
    }

    public NFCDetails(String site, String location, String subLocation, String serviceArea) {
        this.site = site;
        this.location = location;
        this.subLocation = subLocation;
        this.serviceArea = serviceArea;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubLocation() {
        return subLocation;
    }

    public void setSubLocation(String subLocation) {
        this.subLocation = subLocation;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

}