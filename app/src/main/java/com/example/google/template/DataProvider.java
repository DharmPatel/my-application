package com.example.google.template;

/**
 * Created by Intel on 09-11-2016.
 */
public class DataProvider
{

    private String Asset_Id,Asset_Code,Asset_Name,Asset_Location,Status,UpdateStatus,StartTime,TaskStatus,Task_Id;


    public String getAsset_Id() {
        return Asset_Id;
    }

    public void setAsset_Id(String asset_Id) {
        Asset_Id = asset_Id;
    }

    public String getAsset_Code() {
        return Asset_Code;
    }

    public void setAsset_Code(String asset_Code) {
        Asset_Code = asset_Code;
    }

    public String getAsset_Name() {
        return Asset_Name;
    }

    public void setAsset_Name(String asset_Name) {
        Asset_Name = asset_Name;
    }

    public String getAsset_Location() {
        return Asset_Location;
    }

    public void setAsset_Location(String asset_Location) {
        Asset_Location = asset_Location;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        StartTime = StartTime;
    }

    public String getTaskStatus() {
        return TaskStatus;
    }

    public void setTaskStatus(String TaskStatus) {
        TaskStatus = TaskStatus;
    }

    public String getTask_Id() {
        return Task_Id;
    }

    public void setTask_Id(String FormId) {
        FormId = FormId;
    }

    public DataProvider(String Asset_Id ,String Asset_Code,String Asset_Name,String Asset_Location,String Status,String StartTime,String TaskStatus,String Task_Id)
    {

        this.Asset_Id=Asset_Id;
        this.Asset_Code=Asset_Code;
        this.Asset_Name=Asset_Name;
        this.Asset_Location=Asset_Location;
        this.Status=Status;
        this.StartTime = StartTime;
        this.TaskStatus = TaskStatus;
        this.Task_Id =Task_Id;


    }


}
