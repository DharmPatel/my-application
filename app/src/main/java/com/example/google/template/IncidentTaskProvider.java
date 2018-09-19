package com.example.google.template;

public class IncidentTaskProvider {

    String IncidentName;
    String IncidentLocation;
    String Activity;
    String Asset;
    String Activity_Frequency_Id;
    String Scan_Id ;
    String Section_Id;
    String Task_Id;
    int sid;
    String UpdatedStatus;

    public String getIncidentName() {
        return IncidentName;
    }

    public void setIncidentName(String incidentName) {
        IncidentName = incidentName;
    }

    public String getIncidentLocation() {
        return IncidentLocation;
    }

    public void setIncidentLocation(String incidentLocation) {
        IncidentLocation = incidentLocation;
    }

    public String getAsset() {
        return Asset;
    }

    public void setAsset(String asset) {
        Asset = asset;
    }

    public String getActivity() {

        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }

    public String getActivity_Frequency_Id() {
        return Activity_Frequency_Id;
    }

    public void setActivity_Frequency_Id(String activity_Frequency_Id) {
        Activity_Frequency_Id = activity_Frequency_Id;
    }

    public String getScan_Id() {
        return Scan_Id;
    }

    public void setScan_Id(String scan_Id) {
        Scan_Id = scan_Id;
    }

    public String getSection_Id() {
        return Section_Id;
    }

    public void setSection_Id(String section_Id) {
        Section_Id = section_Id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTask_Id() {
        return Task_Id;
    }

    public void setTask_Id(String task_Id) {
        Task_Id = task_Id;
    }

    public String getUpdatedStatus() {
        return UpdatedStatus;
    }

    public void setUpdatedStatus(String updatedStatus) {
        UpdatedStatus = updatedStatus;
    }

    public IncidentTaskProvider(String IncidentName, String IncidentLocation,String Activity,String Asset,String Activity_Frequency_Id,String Scan_Id,String Section_Id,int sid,String Task_Id,String UpdatedStatus)
    {
        this.IncidentName = IncidentName;
        this.IncidentLocation=IncidentLocation;
        this.Activity=Activity;
        this.Asset=Asset;
        this.Activity_Frequency_Id=Activity_Frequency_Id;
        this.Scan_Id=Scan_Id;
        this.Section_Id=Section_Id;
        this.sid=sid;
        this.Task_Id=Task_Id;

        this.UpdatedStatus=UpdatedStatus;


    }

}

