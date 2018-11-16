package com.example.google.template;

/**
 * Created by Intel on 09-11-2016.
 */
public class DataProvider
{

    private String Asset_Id,Task_State,Color,Asset_Code,Asset_Name,Asset_Location,Status,UpdateStatus,StartTime,TaskStatus,Task_Id,Asset_Status_Id;


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

    public String getAsset_Status_Id() {
        return Asset_Status_Id;
    }

    public void setAsset_Status_Id(String asset_Status_Id) {
        Asset_Status_Id = asset_Status_Id;
    }

    public String getTask_State() {
        return Task_State;
    }

    public void setTask_State(String task_State) {
        Task_State = task_State;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    /*public DataProvider(String Asset_Id ,String Asset_Code,String Asset_Name,String Asset_Location,String Status,String StartTime,String TaskStatus,String Task_Id)
    {

        this.Asset_Id=Asset_Id;
        this.Asset_Code=Asset_Code;
        this.Asset_Name=Asset_Name;
        this.Asset_Location=Asset_Location;
        this.Status=Status;
        this.StartTime = StartTime;
        this.TaskStatus = TaskStatus;
        this.Task_Id =Task_Id;


    }*/


    public DataProvider(String Asset_Id ,String Asset_Code,String Asset_Name,String Asset_Location,String Status,String StartTime,String TaskStatus,String Task_Id,String Asset_Status_Id,String Task_State,String Color)
    {

        this.Asset_Id=Asset_Id;
        this.Asset_Code=Asset_Code;
        this.Asset_Name=Asset_Name;
        this.Asset_Location=Asset_Location;
        this.Status=Status;
        this.StartTime = StartTime;
        this.TaskStatus = TaskStatus;
        this.Task_Id =Task_Id;
        this.Asset_Status_Id =Asset_Status_Id;
        this.Task_State= Task_State;
        this.Color= Color;


    }

}
