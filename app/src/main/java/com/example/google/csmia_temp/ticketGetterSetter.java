package com.example.google.csmia_temp;

public class ticketGetterSetter {
String datetime,type,Subject,ticketId;

    public String getdatetime() {
        return datetime;
    }
    public String gettype() {
        return type;
    }
    public String getSubject() {
        return Subject;
    }
    public String getticketId() {
        return ticketId;
    }

    public ticketGetterSetter(String datetime ,String type,String Subject,String ticketId)
    {

        this.datetime=datetime;
        this.type=type;
        this.Subject=Subject;
        this.ticketId=ticketId;



    }
}
