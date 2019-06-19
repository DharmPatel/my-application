package com.example.google.csmia_temp.Helpdesk.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HkTicket {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sub_category_id")
    @Expose
    private String subCategoryId;
    @SerializedName("issue_id")
    @Expose
    private String issueId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("building_id")
    @Expose
    private String buildingId;
    @SerializedName("floor_id")
    @Expose
    private String floorId;
    @SerializedName("room_id")
    @Expose
    private String roomId;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("ticket_code")
    @Expose
    private String ticketCode;
    @SerializedName("rejected_at")
    @Expose
    private String rejectedAt;
    @SerializedName("onhold_till")
    @Expose
    private Object onholdTill;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("main_db_name")
    @Expose
    private String mainDbName;
    @SerializedName("closed_at")
    @Expose
    private Object closedAt;
    @SerializedName("selected_site_db_name")
    @Expose
    private String selectedSiteDbName;
    @SerializedName("site_id")
    @Expose
    private String siteId;
    @SerializedName("asset_id")
    @Expose
    private String assetId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
    @SerializedName("Building_Name")
    @Expose
    private String buildingName;
    @SerializedName("Building_Code")
    @Expose
    private String buildingCode;
    @SerializedName("Site_Location_Id")
    @Expose
    private String siteLocationId;
    @SerializedName("floor_name")
    @Expose
    private String floorName;
    @SerializedName("floor_code")
    @Expose
    private String floorCode;
    @SerializedName("Room_Area")
    @Expose
    private String roomArea;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("issues")
    @Expose
    private String issues;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("assigned_to")
    @Expose
    private Object assignedTo;
    @SerializedName("t1")
    @Expose
    private String t1;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ticket_log")
    @Expose
    private ArrayList<Ticket_Log> ticket_Log = null;
    @SerializedName("created_user")
    @Expose
    private String createdUser;

    public HkTicket(Parcel in) {
        id = in.readString();
        subCategoryId = in.readString();
        issueId = in.readString();
        userId = in.readString();
        buildingId = in.readString();
        floorId = in.readString();
        roomId = in.readString();
        level = in.readString();
        userType = in.readString();
        ticketCode = in.readString();
        desc = in.readString();
        area = in.readString();
        mainDbName = in.readString();
        selectedSiteDbName = in.readString();
        siteId = in.readString();
        assetId = in.readString();
        subCategoryName = in.readString();
        buildingName = in.readString();
        buildingCode = in.readString();
        siteLocationId = in.readString();
        floorName = in.readString();
        floorCode = in.readString();
        roomArea = in.readString();
        status = in.readString();
        issues = in.readString();
        createdAt = in.readString();
        assignedTo = in.readString();
        t1 = in.readString();
        groupName = in.readString();
        name = in.readString();
        ticket_Log = in.createTypedArrayList(Ticket_Log.CREATOR);
        createdUser = in.readString();
    }
    public static final Parcelable.Creator<HkTicket> CREATOR = new Parcelable.Creator<HkTicket>() {
        @Override
        public HkTicket createFromParcel(Parcel in) {
            return new HkTicket(in);
        }

        @Override
        public HkTicket[] newArray(int size) {
            return new HkTicket[size];
        }
    };

    public HkTicket() {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(String rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public Object getOnholdTill() {
        return onholdTill;
    }

    public void setOnholdTill(Object onholdTill) {
        this.onholdTill = onholdTill;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getMainDbName() {
        return mainDbName;
    }

    public void setMainDbName(String mainDbName) {
        this.mainDbName = mainDbName;
    }

    public Object getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Object closedAt) {
        this.closedAt = closedAt;
    }

    public String getSelectedSiteDbName() {
        return selectedSiteDbName;
    }

    public void setSelectedSiteDbName(String selectedSiteDbName) {
        this.selectedSiteDbName = selectedSiteDbName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getSiteLocationId() {
        return siteLocationId;
    }

    public void setSiteLocationId(String siteLocationId) {
        this.siteLocationId = siteLocationId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getFloorCode() {
        return floorCode;
    }

    public void setFloorCode(String floorCode) {
        this.floorCode = floorCode;
    }

    public String getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(String roomArea) {
        this.roomArea = roomArea;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Object assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String  getName( ) {
      return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ticket_Log> getTicketLog() {
        return ticket_Log;
    }

    public void setTicketLog(ArrayList<Ticket_Log> ticket_Log) {
        this.ticket_Log = ticket_Log;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

}
