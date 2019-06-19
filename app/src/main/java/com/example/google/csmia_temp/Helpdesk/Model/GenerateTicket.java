package com.example.google.csmia_temp.Helpdesk.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenerateTicket {

    @SerializedName("BuildingId")
    @Expose
    private String buildingId;
    @SerializedName("RoomId")
    @Expose
    private String roomId;
    @SerializedName("FloorId")
    @Expose
    private String floorId;
    @SerializedName("Asset_Name")
    @Expose
    private String assetId;
    @SerializedName("Descp")
    @Expose
    private String descp;
    @SerializedName("Site_Location_Id")
    @Expose
    private String siteLocationId;
    @SerializedName("User_Id")
    @Expose
    private String userId;
    @SerializedName("selected_site_db_name")
    @Expose
    private String selectedSiteDbName;
    @SerializedName("main_db_name")
    @Expose
    private String mainDbName;
    @SerializedName("employee_name")
    @Expose
    private String employeeName;
    @SerializedName("user_mailid")
    @Expose
    private String userMailid;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("ServiceDetails")
    @Expose
    private List<ServiceDetail> serviceDetails = null;

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getSiteLocationId() {
        return siteLocationId;
    }

    public void setSiteLocationId(String siteLocationId) {
        this.siteLocationId = siteLocationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSelectedSiteDbName() {
        return selectedSiteDbName;
    }

    public void setSelectedSiteDbName(String selectedSiteDbName) {
        this.selectedSiteDbName = selectedSiteDbName;
    }

    public String getMainDbName() {
        return mainDbName;
    }

    public void setMainDbName(String mainDbName) {
        this.mainDbName = mainDbName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getUserMailid() {
        return userMailid;
    }

    public void setUserMailid(String userMailid) {
        this.userMailid = userMailid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<ServiceDetail> getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(List<ServiceDetail> serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public GenerateTicket(String buildingId, String roomId, String floorId, String assetId, String descp, String siteLocationId, String userId, String selectedSiteDbName, String mainDbName, String employeeName, String userMailid, String userType, List<ServiceDetail> serviceDetails) {
        this.buildingId = buildingId;
        this.roomId = roomId;
        this.floorId = floorId;
        this.assetId = assetId;
        this.descp = descp;
        this.siteLocationId = siteLocationId;
        this.userId = userId;
        this.selectedSiteDbName = selectedSiteDbName;
        this.mainDbName = mainDbName;
        this.employeeName = employeeName;
        this.userMailid = userMailid;
        this.userType = userType;
        this.serviceDetails = serviceDetails;
    }

    @Override
    public String toString() {
        return "GenerateTicket{" +
                "buildingId='" + buildingId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", floorId='" + floorId + '\'' +
                ", assetId='" + assetId + '\'' +
                ", descp='" + descp + '\'' +
                ", siteLocationId='" + siteLocationId + '\'' +
                ", userId='" + userId + '\'' +
                ", selectedSiteDbName='" + selectedSiteDbName + '\'' +
                ", mainDbName='" + mainDbName + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", userMailid='" + userMailid + '\'' +
                ", userType='" + userType + '\'' +
                ", serviceDetails=" + serviceDetails +
                '}';
    }

    public GenerateTicket() {
    }
}

