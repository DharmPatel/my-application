package com.example.google.csmia_temp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by dharm on 14/2/17.
 */

public class LoggerFile {

GPSTracker gpsTracker = new GPSTracker(new applicationClass().getContext());
    HomePage homePage = new HomePage();

    String MacAddres = homePage.getMacAddr();

 public String[] loggerFunction(String UserId){
     Calendar calendar = Calendar.getInstance();
     final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
     String rdate = simpleDateFormat.format(calendar.getTime());

     String Record_Status = "I";
     String Last_Date_Time = rdate;
     String Last_IP = MacAddres;
     String Last_User_Id = UserId;
     String Update_Location = "M";
     String Apk_Web_Version = BuildConfig.VERSION_NAME;
     String GeoLoc = gpsTracker.getLatitude() + " " +gpsTracker.getLongitude();
     return new String[]{Record_Status,Last_Date_Time,Last_IP,Last_User_Id,Update_Location,Apk_Web_Version,GeoLoc};

 }
}
