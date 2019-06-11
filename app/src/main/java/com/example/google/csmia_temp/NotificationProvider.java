package com.example.google.csmia_temp;

public class NotificationProvider {

    String Message, hours;
    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public NotificationProvider(String Message, String Hours){
        this.Message = Message;
        this.hours = Hours;
    }

}
