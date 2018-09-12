package com.example.google.template;

/**
 * Created by Intel on 09-11-2016.
 */
public class AlertDataProvider
{

    private String Asset_Name,Schedule_Date,Task_Id,Activity_Name,Activity_Frequency_Id,Task_Start_At,AlertType,ViewFlag,UpdatedStatus;

    public String getUpdatedStatus() {
        return UpdatedStatus;
    }

    public void setUpdatedStatus(String updatedStatus) {
        UpdatedStatus = updatedStatus;
    }

    public String getAsset_Name() {
        return Asset_Name;
    }

    public void setAsset_Name(String asset_Name) {
        Asset_Name = asset_Name;
    }

    public String getSchedule_Date() {
        return Schedule_Date;
    }

    public void setSchedule_Date(String schedule_Date) {
        Schedule_Date = schedule_Date;
    }

    public String getTask_Id() {
        return Task_Id;
    }

    public void setTask_Id(String task_Id) {
        Task_Id = task_Id;
    }

    public String getActivity_Name() {
        return Activity_Name;
    }

    public void setActivity_Name(String activity_Name) {
        Activity_Name = activity_Name;
    }

    public String getActivity_Frequency_Id() {
        return Activity_Frequency_Id;
    }

    public void setActivity_Frequency_Id(String activity_Frequency_Id) {
        Activity_Frequency_Id = activity_Frequency_Id;
    }

    public String getTask_Start_At() {
        return Task_Start_At;
    }

    public void setTask_Start_At(String task_Start_At) {
        Task_Start_At = task_Start_At;
    }

    public String getAlertType() {
        return AlertType;
    }

    public void setAlertType(String alertType) {
        AlertType = alertType;
    }

    public String getViewFlag() {
        return ViewFlag;
    }

    public void setViewFlag(String viewFlag) {
        ViewFlag = viewFlag;
    }

    public AlertDataProvider(String Asset_Name, String Schedule_Date, String Task_Id, String Activity_Name, String Activity_Frequency_Id, String Task_Start_At, String AlertType, String ViewFlag,String UpdatedStatus)
    {

        this.Asset_Name=Asset_Name;
        this.Schedule_Date=Schedule_Date;
        this.Task_Id=Task_Id;
        this.Activity_Name=Activity_Name;
        this.Activity_Frequency_Id=Activity_Frequency_Id;
        this.Task_Start_At = Task_Start_At;
        this.AlertType = AlertType;
        this.ViewFlag =ViewFlag;
        this.UpdatedStatus =UpdatedStatus;

    }


}
