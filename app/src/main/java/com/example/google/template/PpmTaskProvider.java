package com.example.google.template;

public class PpmTaskProvider {
    String TaskId, Site_Location_Id,form_id,activity_frequency,updatedStatus,TaskScheduledDate,TaskEndTime,task_status,asset_activity_linking_id,timestartson,activity_duration,grace_duration_before,grace_duration_after,Activity_Name,Asset_Code,Asset_Name,Asset_Location,Asset_Type,Status,Assigned_To_User_Group_Id,Group_Name;

    public String setTaskId(String TaskId){
        return TaskId;
    }
    public String getTaskId(){
        return TaskId;
    }
    public String setForm_id(String form_id){
        return form_id;
    }
    public String getForm_id(){
        return form_id;
    }
    public String setSite_Location_Id(String Site_Location_Id){
        return Site_Location_Id;
    }
    public String getSite_Location_Id(){
        return Site_Location_Id;
    }
    public String setActivity_frequency(String activity_frequency){
        return activity_frequency;
    }
    public String getActivity_frequency(){
        return activity_frequency;
    }
    public String setTask_date(String task_date){
        return task_date;
    }
    public String getTask_Scheduled_Date(){
        return TaskScheduledDate;
    }
    public String getTaskEndTime(){
        return TaskEndTime;
    }
    public String setTask_status(String task_status){
        return task_status;
    }
    public String getTask_status(){
        return task_status;
    }
    public String setAsset_activity_linking_id(String asset_activity_linking_id){
        return asset_activity_linking_id;
    }
    public String getAsset_activity_linking_id(){
        return asset_activity_linking_id;
    }
    public String setTimestartson(String timestartson){
        return timestartson;
    }
    public String getTimestartson(){
        return timestartson;
    }
    public String setActivity_duration(String activity_duration){
        return activity_duration;
    }
    public String getActivity_duration(){
        return activity_duration;
    }
    public String setGrace_duration_before(String grace_duration_before){
        return grace_duration_before;
    }
    public String getGrace_duration_before(){
        return grace_duration_before;
    }
    public String setGrace_duration_after(String grace_duration_after){
        return grace_duration_after;
    }
    public String getGrace_duration_after(){
        return grace_duration_after;
    }
    public String setActivity_Name(String Activity_Name){
        return Activity_Name;
    }
    public String getActivity_Name(){
        return Activity_Name;
    }
    public String setAsset_Code(String Asset_Code){
        return Asset_Code;
    }
    public String getAsset_Code(){
        return Asset_Code;
    }
    public String setAsset_Name(String Asset_Name){
        return Asset_Name;
    }
    public String getAsset_Name(){
        return Asset_Name;
    }
    public String setAsset_Location(String Asset_Location){
        return Asset_Location;
    }
    public String getAsset_Location(){
        return Asset_Location;
    }
    public String setAsset_Type(String Asset_Type){
        return Asset_Type;
    }
    public String getAsset_Type(){
        return Asset_Location;
    }
    public String setStatus(String Status){
        return Status;
    }
    public String getStatus(){
        return Status;
    }
    public String setAssigned_To_User_Group_Id(String Assigned_To_User_Group_Id){
        return Assigned_To_User_Group_Id;
    }
    public String getAssigned_To_User_Group_Id(){
        return Assigned_To_User_Group_Id;
    }
    public String setGroup_Name(String Group_Name){
        return Group_Name;
    }
    public String getGroup_Name(){
        return Group_Name;
    }
    public String getupdatedStatus(){
        return updatedStatus;
    }

    public PpmTaskProvider(String TaskId, String Site_Location_Id, String activity_frequency, String TaskScheduledDate, String task_status, String asset_activity_linking_id, String TaskEndTime, String Activity_Name, String Form_Id, String Asset_Code, String Asset_Name, String Asset_Location, String Asset_Type, String Status, String Assigned_To_User_Group_Id, String Group_Name,String updatedStatus){
        this.TaskId = TaskId;
        this.Site_Location_Id = Site_Location_Id;
        this.activity_frequency = activity_frequency;
        //this.task_date = task_date;
        this.task_status = task_status;
        this.TaskScheduledDate = TaskScheduledDate;
        this.TaskEndTime = TaskEndTime;

        this.asset_activity_linking_id = asset_activity_linking_id;
        //this.timestartson = timestartson;
        //this.activity_duration = activity_duration;
        //this.grace_duration_before = grace_duration_before;
        //this.grace_duration_after = grace_duration_after;
        this.Activity_Name = Activity_Name;
        this.form_id = Form_Id;
        this.Asset_Code = Asset_Code;
        this.Asset_Name = Asset_Name;
        this.Asset_Location = Asset_Location;
        this.Asset_Type = Asset_Type;
        this.Status = Status;
        this.Assigned_To_User_Group_Id = Assigned_To_User_Group_Id;
        this.Group_Name = Group_Name;
        this.updatedStatus = updatedStatus;
    }
}
