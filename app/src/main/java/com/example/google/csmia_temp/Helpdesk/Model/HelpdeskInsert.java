package com.example.google.csmia_temp.Helpdesk.Model;

public class HelpdeskInsert {
    String Building,Floor,Room,Asset,Caregory,Issues,descrip;

    public String getBuilding() {
        return Building;
    }

    public void setBuilding(String building) {
        Building = building;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getAsset() {
        return Asset;
    }

    public void setAsset(String asset) {
        Asset = asset;
    }

    public String getCaregory() {
        return Caregory;
    }

    public void setCaregory(String caregory) {
        Caregory = caregory;
    }

    public String getIssues() {
        return Issues;
    }

    public void setIssues(String issues) {
        Issues = issues;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public HelpdeskInsert() {
    }

    public HelpdeskInsert(String building, String floor, String room, String asset, String caregory, String issues, String descrip) {
        Building = building;
        Floor = floor;
        Room = room;
        Asset = asset;
        Caregory = caregory;
        Issues = issues;
        this.descrip = descrip;
    }

    @Override
    public String toString() {
        return "HelpdeskInsert{" +
                "Building='" + Building + '\'' +
                ", Floor='" + Floor + '\'' +
                ", Room='" + Room + '\'' +
                ", Asset='" + Asset + '\'' +
                ", Caregory='" + Caregory + '\'' +
                ", Issues='" + Issues + '\'' +
                ", descrip='" + descrip + '\'' +
                '}';
    }
}
