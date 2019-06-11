package com.example.google.csmia_temp.Helpdesk.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ticket {
    @SerializedName("TICKETID")
    @Expose
    private String tICKETID;
    @SerializedName("DEPARTMENT_NAME")
    @Expose
    private String dEPARTMENTNAME;
    @SerializedName("SITE_NAME")
    @Expose
    private String sITENAME;
    @SerializedName("LOCATION_NAME")
    @Expose
    private String lOCATIONNAME;
    @SerializedName("SUB_LOCATION_NAME")
    @Expose
    private String sUBLOCATIONNAME;
    @SerializedName("SERVICEAREA_NAME")
    @Expose
    private String sERVICEAREANAME;
    @SerializedName("PRODUCT_NAME")
    @Expose
    private String pRODUCTNAME;
    @SerializedName("COMPONENT_NAME")
    @Expose
    private String cOMPONENTNAME;
    @SerializedName("PROBLEM_TITLE")
    @Expose
    private String pROBLEMTITLE;
    @SerializedName("RISK_PRIORITY")
    @Expose
    private String rISKPRIORITY;
    @SerializedName("CREATEDON")
    @Expose
    private String cREATEDON;
    @SerializedName("UPDATEDON")
    @Expose
    private String uPDATEDON;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;
    @SerializedName("INCIDENT_TYPE")
    @Expose
    private String iNCIDENTTYPE;
    @SerializedName("RESOLUTION_TIME")
    @Expose
    private String rESOLUTIONTIME;
    @SerializedName("Affected_Party")
    @Expose
    private String affectedParty;
    @SerializedName("Incident_Description")
    @Expose
    private String incidentDescription;
    @SerializedName("Update_Remarks")
    @Expose
    private String updateRemarks;
    @SerializedName("CREATED_BY")
    @Expose
    private String cREATEDBY;
    @SerializedName("UPDATED_BY")
    @Expose
    private String uPDATEDBY;

    public String getTICKETID() {
        return tICKETID;
    }

    public void setTICKETID(String tICKETID) {
        this.tICKETID = tICKETID;
    }

    public String getDEPARTMENTNAME() {
        return dEPARTMENTNAME;
    }

    public void setDEPARTMENTNAME(String dEPARTMENTNAME) {
        this.dEPARTMENTNAME = dEPARTMENTNAME;
    }

    public String getSITENAME() {
        return sITENAME;
    }

    public void setSITENAME(String sITENAME) {
        this.sITENAME = sITENAME;
    }

    public String getLOCATIONNAME() {
        return lOCATIONNAME;
    }

    public void setLOCATIONNAME(String lOCATIONNAME) {
        this.lOCATIONNAME = lOCATIONNAME;
    }

    public String getSUBLOCATIONNAME() {
        return sUBLOCATIONNAME;
    }

    public void setSUBLOCATIONNAME(String sUBLOCATIONNAME) {
        this.sUBLOCATIONNAME = sUBLOCATIONNAME;
    }

    public String getSERVICEAREANAME() {
        return sERVICEAREANAME;
    }

    public void setSERVICEAREANAME(String sERVICEAREANAME) {
        this.sERVICEAREANAME = sERVICEAREANAME;
    }

    public String getPRODUCTNAME() {
        return pRODUCTNAME;
    }

    public void setPRODUCTNAME(String pRODUCTNAME) {
        this.pRODUCTNAME = pRODUCTNAME;
    }

    public String getCOMPONENTNAME() {
        return cOMPONENTNAME;
    }

    public void setCOMPONENTNAME(String cOMPONENTNAME) {
        this.cOMPONENTNAME = cOMPONENTNAME;
    }

    public String getPROBLEMTITLE() {
        return pROBLEMTITLE;
    }

    public void setPROBLEMTITLE(String pROBLEMTITLE) {
        this.pROBLEMTITLE = pROBLEMTITLE;
    }

    public String getRISKPRIORITY() {
        return rISKPRIORITY;
    }

    public void setRISKPRIORITY(String rISKPRIORITY) {
        this.rISKPRIORITY = rISKPRIORITY;
    }

    public String getCREATEDON() {
        return cREATEDON;
    }

    public void setCREATEDON(String cREATEDON) {
        this.cREATEDON = cREATEDON;
    }

    public String getUPDATEDON() {
        return uPDATEDON;
    }

    public void setUPDATEDON(String uPDATEDON) {
        this.uPDATEDON = uPDATEDON;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    public String getINCIDENTTYPE() {
        return iNCIDENTTYPE;
    }

    public void setINCIDENTTYPE(String iNCIDENTTYPE) {
        this.iNCIDENTTYPE = iNCIDENTTYPE;
    }

    public String getRESOLUTIONTIME() {
        return rESOLUTIONTIME;
    }

    public void setRESOLUTIONTIME(String rESOLUTIONTIME) {
        this.rESOLUTIONTIME = rESOLUTIONTIME;
    }

    public String getAffectedParty() {
        return affectedParty;
    }

    public void setAffectedParty(String affectedParty) {
        this.affectedParty = affectedParty;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    public String getUpdateRemarks() {
        return updateRemarks;
    }

    public void setUpdateRemarks(String updateRemarks) {
        this.updateRemarks = updateRemarks;
    }

    public String getCREATEDBY() {
        return cREATEDBY;
    }

    public void setCREATEDBY(String cREATEDBY) {
        this.cREATEDBY = cREATEDBY;
    }

    public String getUPDATEDBY() {
        return uPDATEDBY;
    }

    public void setUPDATEDBY(String uPDATEDBY) {
        this.uPDATEDBY = uPDATEDBY;
    }

}