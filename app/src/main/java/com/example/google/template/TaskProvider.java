package com.example.google.template;

/**
 * Created by Intel on 27-03-2017.
 */
public class TaskProvider {

    String TaskId,Frequency_Id,Task_Start_At,Site_Location_Id,Assigned_To_User_Id ,Asset_Id,From_Id,StartDateTime,EndDateTime,
            Asset_Code,Asset_Name ,Asset_Location,Asset_Status,Activity_Name,Task_Status,UpdateStatus,Assigned_To_User_Group_Id,Group_Name;
    boolean NoTaskAssigned;
    public boolean getNoTaskAssigned() {
        return NoTaskAssigned;
    }
    public void setNoTaskAssigned(boolean NoTaskAssigned) {
        this.NoTaskAssigned = NoTaskAssigned;
    }
    public String getTaskId() {
        return TaskId;
    }
    public void setTaskId(String TaskId) {
        this.TaskId = TaskId;
    }
    public String getFrequency_Id() {
        return Frequency_Id;
    }
    public void setFrequency_Id(String Frequency_Id) {
        this.Frequency_Id = Frequency_Id;
    }
    public String getSite_Location_Id() {
        return Site_Location_Id;
    }
    public void setSite_Location_Id(String Site_Location_Id) {
        this.Site_Location_Id = Site_Location_Id;
    }
    public String getAssigned_To_User_Id() {
        return Assigned_To_User_Id;
    }
    public void getAssigned_To_User_Id(String Assigned_To_User_Id) {
        this.Assigned_To_User_Id = Assigned_To_User_Id;
    }
    public String getAsset_Id() {
        return Asset_Id;
    }
    public void setAsset_Id(String Asset_Id) {
        this.Asset_Id = Asset_Id;
    }
    public String getFrom_Id() {

        return From_Id;
    }
    public void setFrom_Id(String From_Id) {
        this.From_Id = From_Id;
    }
    public String getStartDateTime() {
        return StartDateTime;
    }
    public void setStartDateTime(String StartDateTime) {
        this.StartDateTime = StartDateTime;
    }
    public String getEndDateTime() {
        return EndDateTime;
    }
    public void setEndDateTime(String EndDateTime) {
        this.EndDateTime = EndDateTime;
    }
    public String getAsset_Code() {
        return Asset_Code;
    }
    public void setAsset_Codee(String Asset_Code) {
        this.Asset_Code = Asset_Code;
    }
    public String getAsset_Name() {
        return Asset_Name;
    }
    public void setAsset_Name(String Asset_Name) {
        this.Asset_Name = Asset_Name;
    }
    public String getAsset_Location() {
        return Asset_Location;
    }
    public void setAsset_Location(String Asset_Location) {
        this.Asset_Location = Asset_Location;
    }
    public void setAsset_Status(String Asset_Status) {
        this.Asset_Status = Asset_Status;
    }
    public String getAsset_Status() {
        return Asset_Status;
    }
    public void setActivity_Name(String Activity_Name) {
        this.Activity_Name = Activity_Name;
    }
    public String getActivity_Name() {
        return Activity_Name;
    }
    public void setTask_Status(String Task_Status) {
        this.Task_Status = Task_Status;
    }
    public String getTask_Status() {
        return Task_Status;
    }
    public void setUpdateStatus(String UpdateStatus) {
        this.UpdateStatus = UpdateStatus;
    }
    public String getUpdateStatus() {
        return UpdateStatus;
    }
    public String getGroup_Name() {
        return Group_Name;
    }
    public void setGroup_Name(String group_Name) {
        Group_Name = group_Name;
    }
    public  TaskProvider(){

    }

    public String getAssigned_To_User_Group_Id() {
        return Assigned_To_User_Group_Id;
    }

    public void setAssigned_To_User_Group_Id(String assigned_To_User_Group_Id) {
        Assigned_To_User_Group_Id = assigned_To_User_Group_Id;
    }

    public TaskProvider(String TaskId,String Frequency_Id,String Site_Location_Id,String Assigned_To_User_Id ,
                        String Asset_Id,String From_Id,String StartDateTime,String EndDateTime,
                        String Asset_Code,String Asset_Name ,String Asset_Location,String Asset_Status,
                        String Activity_Name,String Task_Status,String Group_Name,String Assigned_To_User_Group_Id,String UpdateStatus)
    {
        this.TaskId = TaskId;
        this.Frequency_Id=Frequency_Id;
        this.Site_Location_Id=Site_Location_Id;
        this.Assigned_To_User_Id=Assigned_To_User_Id;
        this.Assigned_To_User_Group_Id=Assigned_To_User_Group_Id;
        this.Asset_Id=Asset_Id;
        this.From_Id=From_Id;
        this.StartDateTime=StartDateTime;
        this.EndDateTime=EndDateTime;
        this.Asset_Code=Asset_Code;
        this.Asset_Name=Asset_Name;
        this.Asset_Location =Asset_Location;
        this.Asset_Status =Asset_Status;
        this.Activity_Name =Activity_Name;
        this.Task_Status =Task_Status;
        this.UpdateStatus = UpdateStatus;
        this.Group_Name = Group_Name;
    }

}
