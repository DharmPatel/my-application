package com.example.google.template;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.example.google.template.ConstantList.DatabaseColumn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Intel on 23-03-2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    static final boolean LOG = new applicationClass().checkLog();
    applicationClass applicationClass = new applicationClass();
    public DatabaseHelper(Context context) {
        super(context, "facilitymate.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE Activity_Frequency (Id INTEGER PRIMARY KEY AUTOINCREMENT,Site_Location_Id TEXT,Frequency_Auto_Id TEXT,YearStartson TEXT,TimeStartson TEXT,TimeEndson TEXT,Activity_Duration INTEGER,Grace_Duration_Before INTEGER,Grace_Duration_After INTEGER,RepeatEveryDay INTEGER,RepeatEveryMin INTEGER,RepeatEveryMonth TEXT,Verified INTEGER,Assign_Days TEXT,Asset_Activity_Linking_Id TEXT,RecordStatus TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Asset_Activity_Linking (Id INTEGER PRIMARY KEY AUTOINCREMENT,Site_Location_Id TEXT,Auto_Id TEXT,Asset_Id TEXT,Activity_Id TEXT,RecordStatus TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Activity_Master (Id INTEGER PRIMARY KEY AUTOINCREMENT,Site_Location_Id TEXT,Auto_Id TEXT,Form_Id TEXT,Activity_Name TEXT,Activity_Type TEXT,RecordStatus TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Asset_Activity_AssignedTo (Id INTEGER PRIMARY KEY AUTOINCREMENT,Site_Location_Id TEXT,Auto_Id TEXT,Assigned_To_User_Id TEXT,Assigned_To_User_Group_Id TEXT,Asset_Activity_Linking_Id TEXT,RecordStatus TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Login_Details (Id INTEGER PRIMARY KEY,User_Id TEXT,Employee_Id TEXT, Company_Customer_Id TEXT, Username TEXT,Password TEXT,Employee_Name TEXT,Asset_View INT,Verify INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE User_Group (Id INTEGER PRIMARY KEY,User_Group_Id TEXT,Group_Name TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Site_Details (Id INTEGER PRIMARY KEY ,Auto_Id TEXT,Site_Name_Label TEXT,Site_Name_Value TEXT,Assigned_To_User_Id TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Form_Structure (Id INTEGER PRIMARY KEY,Field_Id TEXT,Site_Location_Id TEXT,Form_Id TEXT,Field_Label TEXT,Field_Type TEXT,Field_Options TEXT,FixedValue Text,Mandatory INTEGER,sid INTEGER,sections TEXT,Display_Order INTEGER,SafeRange INTEGER,Calculation INTEGER,Record_Status TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Asset_Details (Id INTEGER PRIMARY KEY ,Asset_Id TEXT,Site_Location_Id TEXT,Asset_Code TEXT,Asset_Name TEXT,Asset_Location TEXT,Asset_Status_Id TEXT,Asset_Type TEXT,Status TEXT,Manual_Time TEXT, Asset_Update_Time TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Asset_Status (Id INTEGER PRIMARY KEY,Asset_Status_Id TEXT,Status TEXT,Task_State TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Parameter (Id INTEGER PRIMARY KEY,Site_Location_Id TEXT,Activity_Frequency_Id TEXT, Form_Id TEXT,Form_Structure_Id TEXT, Field_Limit_From TEXT,Field_Limit_To TEXT,Threshold_From TEXT,Threshold_To TEXT,Validation_Type TEXT,Critical INTEGER,Field_Option_Id TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Meter_Reading(Id INTEGER PRIMARY KEY,Site_Location_Id TEXT,Task_Id TEXT,Asset_Id TEXT,Form_Structure_Id TEXT,Reading TEXT,UOM TEXT,Reset INTEGER,Activity_Frequency_Id TEXT,Task_Start_At TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Data_Posting(Id INTEGER PRIMARY KEY,Site_Location_Id TEXT,Task_Id TEXT,Form_Id TEXT,Form_Structure_Id TEXT,Parameter_Id TEXT,Value TEXT,UOM Text,Remark TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Task_Details (Id INTEGER PRIMARY KEY,Auto_Id TEXT,Company_Customer_Id TEXT,Site_Location_Id TEXT,Activity_Frequency_Id TEXT,Asset_Activity_Linking_Auto_Id TEXT,Activity_Master_Auto_Id TEXT,Asset_Activity_AssignedTo_Auto_Id TEXT,Task_Start_At TEXT,Task_Scheduled_Date TEXT,Task_Status TEXT,Scan_Type TEXT,Assigned_To TEXT,Assigned_To_User_Id TEXT,Incident TEXT,Assigned_To_User_Group_Id TEXT,Asset_Id TEXT,From_Id TEXT,EndDateTime TEXT,Asset_Code TEXT,Asset_Name TEXT,Asset_Location TEXT,Asset_Status TEXT, Activity_Name TEXT,Activity_Type TEXT,Remarks Text,Verified INTEGER,RecordStatus TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Ticket_Master (ID INTEGER PRIMARY KEY ,Site_Location_Id TEXT,Company_Customer_Id TEXT,Created_Source TEXT,Created_At TEXT,ticket_Subject TEXT,ticket_Content TEXT,ticket_Priority TEXT,ticket_Type TEXT,Task_Type TEXT,Ticket_Raise_By TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE AlertMaster (ID INTEGER PRIMARY KEY ,Site_Location_Id TEXT,Task_Id TEXT,Form_Id TEXT,Form_Structure_Id TEXT,Alert_Type TEXT,Asset_Name TEXT,Activity_Name TEXT,Activity_Frequency_Id TEXT,Task_Status TEXT,Task_Start_At TEXT,Task_Scheduled_Date TEXT,Created_By_Id TEXT,Assigned_To_User_Group_Id TEXT,Critical TEXT,TaskType TEXT,ViewFlag TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Task_Details_Server (Id INTEGER PRIMARY KEY,Task_Id TEXT,Site_Location_Id TEXT,Activity_Frequency_Id TEXT,Task_Scheduled_Date TEXT,Task_Status TEXT,Assigned_To_User_Id TEXT,Task_Start_At TEXT,Assigned_To_User_Group_Id TEXT,Remarks TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Task_Selfie (ID INTEGER PRIMARY KEY ,Site_Location_Id TEXT, Task_Id TEXT,Task_Server_Id TEXT, Image_Selfie BLOB,Image_Type TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE AssetStatusLog (Id INTEGER PRIMARY KEY ,Asset_Id TEXT,Site_Location_Id TEXT,Previous_Asset_Status_Id TEXT,Asset_Status_Id TEXT,Manual_Time TEXT,Asset_Updated_Time TEXT,Remark TEXT,Assigned_To_User_Id TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Settings (Id INTEGER PRIMARY KEY,User_Id TEXT,Site_Location_Id TEXT,User_Group_Id TEXT,Notification TEXT,Scan_Type TEXT,Site_Name TEXT,Site_URL TEXT,Asset_View INT)");
        sqLiteDatabase.execSQL("CREATE TABLE AutoSync (Id INTEGER PRIMARY KEY,Site_Location_Id TEXT,SyncStatus TEXT,SyncFreq TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE RefrenseTable (Id INTEGER PRIMARY KEY,Site_Location_Id TEXT,Auto_Id TEXT,Record_Id TEXT,Table_Name TEXT,Updated_Id TEXT,DateTime TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE UserSiteLinking (Id INTEGER PRIMARY KEY,Linking_Auto_Id TEXT,User_Id TEXT,Site_Location_Id TEXT,User_Group_Id TEXT,User_Role_Id TEXT,User_Right_Id TEXT,Created_DateTime TEXT,Deleted_DateTime TEXT,Record_Status TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE site_imagelist (Id INTEGER PRIMARY KEY,Auto_Id TEXT,Site_Location_Id TEXT,Image_Name TEXT,Record_Status TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE PPM_Task (Id INTEGER PRIMARY KEY AUTOINCREMENT,Auto_Id TEXT,Site_Location_Id TEXT,Activity_Frequency TEXT, Task_Date TEXT,Task_End_Date TEXT,Task_Status TEXT,Task_Done_At TEXT,Scan_Type TEXT, Asset_Activity_Linking_Id TEXT,Assigned_To_User_Id TEXT,Assigned_To_User_Group_Id TEXT,Timestartson TEXT,Activity_Duration INTEGER,Grace_Duration_Before INTEGER,Grace_Duration_After INTEGER,Record_Status TEXT,UpdatedStatus TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE pun_score (Id INTEGER PRIMARY KEY, Score_Auto_Id TEXT, Form_Structure_Id TEXT, Option_value TEXT, Option_Id TEXT, Score TEXT, Total TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Measurement_Conversion (Id INTEGER Primary key,Conversion_Auto_Id TEXT, Source_UOM TEXT, Multiplication_Factor TEXT, Add_Factor TEXT, Subtraction_Factor TEXT, Division_Factor TEXT, Target_UOM TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE feedback_score(Id INTEGER PRIMARY KEY AUTOINCREMENT,Feedbaack_Auto_Id TEXT,Score TEXT,FeedBackName TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Notification(Id INTEGER PRIMARY KEY AUTOINCREMENT,Notification_Auto_Id TEXT,Message TEXT,Update_Type TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if (newVersion > oldVersion) {
            sqLiteDatabase.execSQL("ALTER TABLE Task_Frequency ADD COLUMN RepeatEveryMonth TEXT");
        }
    }

    public int feedbackScore(String FeedBackName){
        int Score=0;
        try {
            String query = "SELECT Score FROM feedback_score Where FeedBackName ='"+FeedBackName+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    Score=res.getInt(res.getColumnIndex("Score"));
                } while (res.moveToNext());
            }
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Score;
    }

    public String emailList(String SiteId){
        String emailquery = "Select Site_Location_Id, Employee_Email from EmailList where Site_Location_Id = '"+SiteId+"'";
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor emailList = database.rawQuery(emailquery, null);
        StringBuilder sb =new StringBuilder();

        if(emailList.moveToFirst()){
            do{
                sb.append(emailList.getString(emailList.getColumnIndex("Employee_Email"))+",");
            }while (emailList.moveToNext());
        }

        return sb.deleteCharAt(sb.lastIndexOf(",")).toString();
    }

    public String UserGroupName(String UserGroupId){
        String Username="";
        try {
            String query = "SELECT Group_Name FROM User_Group Where User_Group_Id ='"+UserGroupId+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    Username=res.getString(0);
                } while (res.moveToNext());
            }
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Username;
    }

    public String UserName(String User_Id){
        String Username="";
        try {
            String query = "SELECT Employee_Name FROM Login_Details Where User_Id ='"+User_Id+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    Username=res.getString(0);
                } while (res.moveToNext());
            }
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Username;
    }

    public String EmployeeName(String User_Id){
        String EmployeeName="";
        try {
            String query = "SELECT Employee_Name FROM Login_Details Where User_Id ='"+User_Id+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    EmployeeName=res.getString(0);
                } while (res.moveToNext());
            }
            res.close();
            // db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EmployeeName;
    }

    public int formScore(String form_structure_id, int Option_id){
        int Score=0;
        try {
            String query = "SELECT Score FROM pun_score Where Form_Structure_Id ='"+form_structure_id+"'and Option_Id = '"+Option_id+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    Score=res.getInt(res.getColumnIndex("Score"));
                } while (res.moveToNext());
            }
            Log.d("scoreval", String.valueOf(Score));

            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Score;
    }

    public String getPPMCount(String TaskStatus,String UserGroupId,String SiteLocationId){
        SQLiteDatabase database = this.getWritableDatabase();

        String query =  "SELECT   DISTINCT ppm.Auto_Id , \n" +
                "          ppm.Site_Location_Id , \n" +
                "          ppm.Activity_Frequency , \n" +
                "          ppm.Task_Date , \n" +
                "          ppm.Task_End_Date , \n" +
                "          ppm.Task_Status , \n" +
                "          ppm.Asset_Activity_Linking_Id , \n" +
                "          ppm.Timestartson , \n" +
                "          ppm.Activity_Duration , \n" +
                "          ppm.Grace_Duration_Before , \n" +
                "          ppm.Grace_Duration_After,\n" +
                "          am.Activity_Name,\n" +
                "          ad.Asset_Code,\n" +
                "          ad.Asset_Name,\n" +
                "          ad.Asset_Location,\n" +
                "          ad.Asset_Type,\n" +
                "          ad.Status,\n" +
                "          aaa.Assigned_To_User_Group_Id,\n" +
                "          ug.Group_Name\n" +
                "FROM      ppm_task ppm \n" +
                "LEFT JOIN asset_activity_assignedto aaa \n" +
                "ON        aaa.Asset_Activity_Linking_Id = ppm.Asset_Activity_Linking_Id\n" +
                "LEFT JOIN asset_activity_linking aal \n" +
                "ON        aal.Auto_Id = ppm.Asset_Activity_Linking_Id \n" +
                "LEFT JOIN asset_details ad \n" +
                "ON        ad.Asset_Id = aal.Asset_Id \n" +
                "LEFT JOIN activity_master am \n" +
                "ON        am.Auto_Id = aal.Activity_Id \n" +
                "LEFT JOIN User_Group ug\n" +
                "ON                ug.User_Group_Id = aaa.Assigned_To_User_Group_Id \n" +
                "WHERE ppm.task_status = '"+TaskStatus+"' and aaa.Assigned_To_User_Group_Id = "+UserGroupId+" AND  ppm.site_location_id ='"+SiteLocationId+"'";

        int count = 0;

        Cursor cursor = database.rawQuery(query, null);
        count = cursor.getCount();
        return count+"";
    }
    public String lastFinalReading(String fieldId,String Activity_Frequency_Id){
        StringBuffer buffer = new StringBuffer();
        String Task_Id = "";
        String reading ="";
        String uomValue ="";
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "SELECT Auto_Id FROM Task_Details WHERE Task_Status IN ('Completed','Unplanned') AND Activity_Frequency_Id = '"+Activity_Frequency_Id+"' ORDER BY Id DESC LIMIT 1";
        Log.d("TestingUrl",updateQuery);
        Cursor cursor = database.rawQuery(updateQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task_Id =  cursor.getString(0);

            } while (cursor.moveToNext());

        }
        cursor.close();

        String valueQuery = "Select Reading,UOM  FROM Meter_Reading WHERE Form_Structure_Id='"+fieldId+"' and Task_Id ='"+Task_Id+"'  ORDER BY Id DESC LIMIT 1";
        Log.d("TestingUrl",updateQuery);
        Cursor cursorDataPosting = database.rawQuery(valueQuery, null);
        if (cursorDataPosting.moveToFirst()) {
            do {
                reading =  cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Reading"));
                uomValue =  cursorDataPosting.getString(cursorDataPosting.getColumnIndex("UOM"));
//                Log.d("ATASdasdasd",uomValue);

            } while (cursorDataPosting.moveToNext());

        }
        cursorDataPosting.close();

        database.close();
        //Select Value,UOM  FROM Data_Posting WHERE Form_Structure_Id='"+fieldId+"' and Task_Id ='"++"'  ORDER BY Id DESC LIMIT 1


        if(reading.equals("")){
            return "No Previous Reading";
        }else {
            String[] finalReading = reading.split(",");
            String[] finalUOM = uomValue.split(",");
            return finalReading[1] +","+finalUOM[1];
            //return buffer.toString();
        }

    }


    public int getassetCount(String assetCode){
        int count = 0;
        String selectQuery = "SELECT Asset_Id FROM Asset_Details where Asset_Code = '"+assetCode+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        cursor.close();
        database.close();
        return count;
    }

    public String getassetStatus(int Asset_Id){
        String Status = "";
        String selectQuery = "SELECT Asset_Status FROM Asset_Details where Asset_Id = "+Asset_Id+"";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.getCount()!=0){
            if (cursor.moveToFirst()) {
                do {
                    Status = cursor.getString(0);
                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        database.close();
        return Status;
    }


    public int getRecord(String Site_Location_Id){
        int record_id =0;
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            String query="SELECT Record_Id FROM RefrenseTable WHERE Record_Id = (SELECT MAX(Record_Id) FROM RefrenseTable) AND Site_Location_Id='"+ Site_Location_Id+"' ";
            Cursor res =database.rawQuery(query, null);

            if(res.getCount()!=0){
                if (res.moveToFirst()) {
                    do {
                        record_id = res.getInt(0);
                    } while (res.moveToNext());
                }

            }
            res.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return record_id;
    }


    public boolean insertRadioBitmap(Bitmap selfie, String taskID,String SiteId,String Form_Structure_Id)  {
        boolean image = false;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        selfie.compress(Bitmap.CompressFormat.JPEG, 100, out);

        ByteArrayOutputStream meterOut = new ByteArrayOutputStream();
        byte[] selfieBuffer=out.toByteArray();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        try
        {
            values = new ContentValues();
            values.put("Image_Selfie", selfieBuffer);
            values.put("Task_Id", taskID);
            values.put("UpdatedStatus","no");
            values.put("Site_Location_Id",SiteId);
            //values.put("Form_Structure_Id",Form_Structure_Id);
            long i = db.insert("Task_Selfie", null, values);
            if(i == -1)
                image =  false;
            else
                image =  true;
            Log.i("Insert", i + "");
            db.close();
            // Insert into database successfully.
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();

        }
        return image;


    }

    public int refrenseTableCount(String site_Id){
        int count =0;
        SQLiteDatabase database = this.getWritableDatabase();
        String query="select * from RefrenseTable where Site_Location_Id='"+site_Id+"' AND UpdatedStatus='no'";
        Cursor res =database.rawQuery(query, null);
        count=res.getCount();

        return count;
    }





    public boolean insertVal (String uid,String SiteId, String User_Group_Id, String Notification,String Scan_Type,String Site_Name,String Site_URL,int Asset_View){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("User_Id",uid);
        contentValues.put("Site_Location_Id",SiteId);
        contentValues.put("User_Group_Id",User_Group_Id);
        contentValues.put("Notification", Notification);
        contentValues.put("Scan_Type", Scan_Type);
        contentValues.put("Site_Name", Site_Name);
        contentValues.put("Site_URL", Site_URL);
        contentValues.put("Asset_View", Asset_View);
        long result = db.insert("Settings", null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertAutoSync (String SiteId,String SyncStatus,int SyncFreq){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Site_Location_Id",SiteId);
        contentValues.put("SyncStatus",SyncStatus);
        contentValues.put("SyncFreq",SyncFreq);
        long result = db.insert("AutoSync", null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public String ScanType(String User_Id){
        String Scan_Type="";
        try {
            String query = "SELECT Scan_Type FROM Settings Where User_Id ='"+User_Id+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    Scan_Type=res.getString(0);
                } while (res.moveToNext());
            }
            res.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Scan_Type;
    }

    public String UserGroupId(String User_Id){
        String UserGroupId="";
        try {
            String query = "SELECT User_Group_Id FROM Settings Where User_Id ='"+User_Id+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    UserGroupId=res.getString(0);
                } while (res.moveToNext());
            }
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UserGroupId;
    }

    public int assetView(){
        int value=0;
        try {
            String query = "SELECT Asset_View from Login_Details";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    value=res.getInt(0);
                } while (res.moveToNext());
            }
            res.close();
            Log.d(TAG,"assetView value"+value);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public List<String> assetType(String GroupId,String AssetType){
        final List<String> spinnerArray = new ArrayList<String>();
        try {
            String query = "SELECT DISTINCT(Asset_Location) from Asset_Details asm Left Join Asset_Activity_Linking aal " +
                    "on aal.Asset_Id =  asm.Asset_Id " +
                    "Left Join Asset_Activity_AssignedTo aaa " +
                    " on aal.Auto_Id = aaa.Asset_Activity_Linking_Id " +
                    "where aaa.Assigned_To_User_Group_Id IN ("+GroupId+") and asm.Asset_Type = '"+AssetType+"'";
            SQLiteDatabase db = getWritableDatabase();
            Log.d("assetLocationQuery",query);
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    spinnerArray.add(res.getString(0));
                } while (res.moveToNext());
            }
            res.close();
            db.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return spinnerArray;
    }

    public List<String> getAssetLocation(String GroupId){
        final List<String> spinnerArray = new ArrayList<String>();
        try {
            String query = "SELECT DISTINCT(Asset_Location) from Asset_Details asm Left Join Asset_Activity_Linking aal " +
                    "on aal.Asset_Id =  asm.Asset_Id " +
                    "Left Join Asset_Activity_AssignedTo aaa " +
                    " on aal.Auto_Id = aaa.Asset_Activity_Linking_Id " +
                    "where aaa.Assigned_To_User_Group_Id IN ("+GroupId+")";
            SQLiteDatabase db = getWritableDatabase();
            Log.d("getAssetLocationQuery",query);
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    spinnerArray.add(res.getString(0));
                } while (res.moveToNext());
            }
            res.close();
            db.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return spinnerArray;
    }

    public List<String> getTaskLocation(String GroupId, String SiteId){
        final List<String> spinnerArray = new ArrayList<String>();
        try {
            String query = "  SELECT DISTINCT td.Asset_Location\n" +
                    "  FROM Task_Details td \n" +
                    "  LEFT JOIN User_Group ug ON \n" +
                    "  ug.User_Group_Id=td.Assigned_To_User_Group_Id \n" +
                    "  WHERE td.Assigned_To_User_Group_Id IN ("+GroupId+") \n" +
                    "  AND td.Site_Location_Id='"+SiteId+"' AND td.Asset_Status= 'WORKING'  AND td.Task_Status='Pending' AND td.RecordStatus != 'D'";
            SQLiteDatabase db = getWritableDatabase();
            Log.d("getTaskLocationQuery",query);
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    spinnerArray.add(res.getString(0));
                } while (res.moveToNext());
            }
            res.close();
            db.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return spinnerArray;
    }

    public  List<String> getAssetType(String GroupId){
        final List<String> spinnerArray = new ArrayList<String>();
        String value="";
        try {
            String query = "SELECT DISTINCT(Asset_Type) from Asset_Details asm Left Join Asset_Activity_Linking aal " +
                    "on aal.Asset_Id =  asm.Asset_Id " +
                    "Left Join Asset_Activity_AssignedTo aaa " +
                    " on aal.Auto_Id = aaa.Asset_Activity_Linking_Id " +
                    "where aaa.Assigned_To_User_Group_Id IN ("+GroupId+")";
            SQLiteDatabase db = getWritableDatabase();
            Log.d("assetTypeQuery",query);
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    //value=res.getString(0);
                    spinnerArray.add(res.getString(0));
                } while (res.moveToNext());
            }
            res.close();
            db.close();
            //Log.d(TAG,"assetView Type"+value);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return spinnerArray;
    }

    public  List<String> assetLocation(String GroupId, String AssetLocation){
        final List<String> spinnerArray = new ArrayList<String>();
        String value="";
        try {
            String query = "SELECT DISTINCT(Asset_Type) from Asset_Details asm Left Join Asset_Activity_Linking aal " +
                    "on aal.Asset_Id =  asm.Asset_Id " +
                    "Left Join Asset_Activity_AssignedTo aaa " +
                    " on aal.Auto_Id = aaa.Asset_Activity_Linking_Id " +
                    "where aaa.Assigned_To_User_Group_Id IN ("+GroupId+")and asm.Asset_Location = '"+AssetLocation+"'";
            SQLiteDatabase db = getWritableDatabase();
            Log.d("assetTypeQuery",query);
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    //value=res.getString(0);
                    spinnerArray.add(res.getString(0));
                } while (res.moveToNext());
            }
            res.close();
            db.close();
            //Log.d(TAG,"assetView Type"+value);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return spinnerArray;
    }

    public String Notification(String User_Id){
        String notification="";
        try {
            String query = "SELECT Notification FROM Settings Where User_Id ='"+User_Id+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    notification=res.getString(0);
                } while (res.moveToNext());
            }
            res.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notification;
    }

    public String SiteName(String User_Id){
        String Site_Name="";
        try {
            String query = "SELECT Site_Name FROM Settings Where User_Id ='"+User_Id+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    Site_Name=res.getString(0);
                } while (res.moveToNext());
            }
            res.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Site_Name;
    }
    public String SiteURL(String User_Id){
        String Site_URL="";
        try {
            String query = "SELECT Site_URL FROM Settings Where User_Id ='"+User_Id+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    Site_URL=res.getString(0);

                } while (res.moveToNext());
            }
            res.close();
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Site_URL;
    }

    public String Site_IdUserSiteLinking(String User_Id){
        String Site_Location_Id="";
        try {
            String query = "SELECT Site_Location_Id FROM UserSiteLinking Where User_Id ='"+User_Id+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    Site_Location_Id=res.getString(0);
                } while (res.moveToNext());
            }
            res.close();
            // db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Site_Location_Id;
    }

    public String Site_Location_Id(String User_Id){
        String Site_Location_Id="";
        try {
            String query = "SELECT Site_Location_Id FROM Settings Where User_Id ='"+User_Id+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    Site_Location_Id=res.getString(0);
                } while (res.moveToNext());
            }
            res.close();
           // db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Site_Location_Id;
    }

    public void UpdateSiteName(String Site_Name,String UserGroupId,String User_Id,String Site_URL,String Site_Location_Id){
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        contentValues.put("Site_Name", Site_Name);
        contentValues.put("Site_URL", Site_URL);
        contentValues.put("User_Group_Id", UserGroupId);
        contentValues.put("Site_Location_Id", Site_Location_Id);
        db.update("Settings", contentValues, "User_Id ='" + User_Id + "'", null);
        db.close();
    }

    public String AutoSyncSetting(){
        String Autosync="";
        try {
            String query = "SELECT * FROM AutoSync";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    Autosync=res.getString(res.getColumnIndex("SyncStatus"));

                } while (res.moveToNext());
            }
            res.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Autosync;
    }

    public String AutoSyncFreq(){
        String Autosync="";
        try {
            String query = "SELECT * FROM AutoSync";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    Autosync=res.getString(res.getColumnIndex("SyncFreq"));

                } while (res.moveToNext());
            }
            res.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Autosync;
    }

    public int Verify(String User_Id){
        int Verify=0;
        try {
            String query = "SELECT * FROM Login_Details where User_Id='"+User_Id+"'";
            SQLiteDatabase db = getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    Verify=res.getInt(res.getColumnIndex("Verify"));
                } while (res.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Verify;
    }
    public boolean insertTicket(String siteLocationId,String Company_Customer_Id,String createdSource,String CreatedAt,String ticketSubject,String ticketContent,String ticketPriority,String ticket_type,String updated_Status,String Ticket_Raise_By) {

        SQLiteDatabase db =this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Site_Location_Id", siteLocationId);
        contentValues.put("Company_Customer_Id", Company_Customer_Id);
        contentValues.put("Created_Source", createdSource);
        contentValues.put("Created_At", CreatedAt);
        contentValues.put("ticket_Subject", ticketSubject);
        contentValues.put("ticket_Content", ticketContent);
        contentValues.put("ticket_Priority", ticketPriority);
        contentValues.put("ticket_Type", ticket_type);
        contentValues.put("Task_Type", "");
        contentValues.put("Ticket_Raise_By", Ticket_Raise_By);
        contentValues.put("UpdatedStatus", updated_Status);

        long resultset = db.insert("Ticket_Master", null, contentValues);
        if(resultset == -1){
            return false;}
        else{
            return true;}
    }

    public int getassetTaskCount(String assetCode,String UserGroupId){
        int count = 0;
        String selectQuery = "Select * FROM Task_Details WHERE Asset_Code ='"+assetCode+"' and Assigned_To_User_Group_Id IN ("+UserGroupId+") AND RecordStatus = 'I'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        cursor.close();
        database.close();
        return count;
    }

    public String getassetStatus(String assetCode){
        String assetStatus = "";
        String selectQuery = "SELECT Task_Status FROM Task_Details WHERE Asset_Code = '"+assetCode+"' AND Task_Scheduled_Date <='"+ applicationClass.yymmddhhmm()+"' AND EndDateTime >= '"+ applicationClass.yymmddhhmm()+"' AND Task_Status !='Unplanned' AND RecordStatus = 'I'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.getCount() > 0){
            if (cursor.moveToFirst()) {
                do {
                    assetStatus = cursor.getString(0);
                    if(assetStatus.equalsIgnoreCase("Pending")){
                        return assetStatus;
                    }
                } while (cursor.moveToNext());
            }
        }else {
            String Query = "SELECT Task_Status FROM Task_Details WHERE Asset_Code = '"+assetCode+"' AND Task_Status !='Unplanned'";
            Cursor cursorforCount = database.rawQuery(Query, null);
            if(cursorforCount.getCount() !=0) {
                if (cursorforCount.moveToNext()) {
                    assetStatus = cursorforCount.getString(0);
                }
            } else{
                assetStatus = "Not found";
            }
        }

        return assetStatus;
    }

    public String getPPMTaskCount(String Asset_Code){
        String assetStatus = "";
        String selectQuery = "select ppm.task_Status\n" +
                "from ppm_task ppm \n" +
                "LEFT JOIN asset_activity_assignedto aaa \n" +
                "    ON        aaa.Asset_Activity_Linking_Id = ppm.Asset_Activity_Linking_Id\n" +
                "    LEFT JOIN asset_activity_linking aal \n" +
                "    ON        aal.Auto_Id = ppm.Asset_Activity_Linking_Id \n" +
                "    LEFT JOIN asset_details ad \n" +
                "    ON        ad.Asset_Id = aal.Asset_Id \n" +
                "    LEFT JOIN activity_master am \n" +
                "    ON        am.Auto_Id = aal.Activity_Id \n" +
                "    where ad.Asset_Code = '"+Asset_Code+"' and ppm.Task_Date <= '"+applicationClass.yymmddhhmm()+"' and ppm.Task_End_Date >= '"+applicationClass.yymmddhhmm()+"' and ppm.Task_Status NOT IN ('Completed', 'Missed')";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        Log.d("queryprint",selectQuery+"   Count:"+cursor.getCount());
        if(cursor.getCount() !=0){
            if(cursor.moveToNext()){
                assetStatus = cursor.getString(0);
            }
        }else {
            String selectQuery1 = "select ppm.task_Status\n" +
                    "from ppm_task ppm \n" +
                    "LEFT JOIN asset_activity_assignedto aaa \n" +
                    "    ON        aaa.Asset_Activity_Linking_Id = ppm.Asset_Activity_Linking_Id\n" +
                    "    LEFT JOIN asset_activity_linking aal \n" +
                    "    ON        aal.Auto_Id = ppm.Asset_Activity_Linking_Id \n" +
                    "    LEFT JOIN asset_details ad \n" +
                    "    ON        ad.Asset_Id = aal.Asset_Id \n" +
                    "    LEFT JOIN activity_master am \n" +
                    "    ON        am.Auto_Id = aal.Activity_Id \n" +
                    "    where ad.Asset_Code = '"+Asset_Code+"'";

            Cursor cursorforCount = database.rawQuery(selectQuery1, null);
            if(cursorforCount.getCount() !=0) {
                if (cursorforCount.moveToNext()) {
                    assetStatus = cursorforCount.getString(0);
                }
            } else{
                assetStatus = "Not found";
            }
        }
        Log.d("bvhgvbn",assetStatus);
        return assetStatus;
    }


    /*public String getassetStatus(String assetCode){
        String assetStatus = "";
        String selectQuery = "SELECT Task_Status FROM Task_Details WHERE Asset_Code = '"+assetCode+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToNext()){
            assetStatus = cursor.getString(0);
            Log.d("taskStatus","1 "+cursor.getCount()+" 2 "+cursor.getString(0));

        }
        return assetStatus;
    }*/
    public String getfieldId(int id) {
        String formType = " ";
        String selectQuery = "SELECT Field_Id FROM Form_Structure WHERE Id = '"+id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.moveToNext()){
            formType = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return formType;
    }

    public String getFormId(String Field_Id) {
        String Form_Id = " ";
        String selectQuery = "SELECT Form_Id FROM Form_Structure WHERE Field_Id = '"+Field_Id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.moveToNext()){
            Form_Id = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return Form_Id;
    }

    public String getfieldLabel(int id) {
        String formType = " ";
        String selectQuery = "SELECT Field_Label FROM Form_Structure WHERE Id = '"+id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.moveToNext()){
            formType = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return formType;
    }

    public String lastMultiMeterReading(String autoId,String fieldId){
        StringBuffer buffer = new StringBuffer();
        String reading ="";
        String uomValue ="";
        String fieldType = "";
        SQLiteDatabase database = this.getWritableDatabase();
        String formQuery = "select Field_Type from Form_Structure where Field_Id = '"+fieldId+"'";
        Cursor c = database.rawQuery(formQuery,null);
        if(c.moveToFirst()){
            do{
                fieldType = c.getString(c.getColumnIndex("Field_Type"));
            } while (c.moveToNext());

           Log.d("Field_Type"," "+fieldId+" "+fieldType);

        }

        String updateQuery = "Select Reading, UOM,Task_Start_At  FROM Meter_Reading WHERE  Asset_Id = '"+autoId+"' AND Form_Structure_Id='"+fieldId+"'  ORDER BY Id DESC LIMIT 1";
        Cursor cursor = database.rawQuery(updateQuery, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    reading =  cursor.getString(0);
                    uomValue =  cursor.getString(1);
                    buffer.append(reading);
                    buffer.append(" ");
                    buffer.append(uomValue);
                } while (cursor.moveToNext());

            }
        }
        cursor.close();
        database.close();
        if(reading.equals("")){
            return "No Previous Reading";
        }else {
            return buffer.toString();
        }

    }

    public Double Conversion(Double secondVal,String firstRadio, String secondRadio){
        SQLiteDatabase db = this.getWritableDatabase();
        String ConversionQuery = "Select * from Measurement_Conversion where Source_UOM='"+firstRadio+"' AND Target_UOM='"+secondRadio+"'";
        Cursor conversion = db.rawQuery(ConversionQuery,null);
        Double calc=0.0;
        Double subFactor=0.0, addFactor=0.0, multiFactor=1.0, divFactor=1.0;

        try{
            if (conversion.moveToFirst()) {
                do {
                    subFactor = Double.parseDouble(conversion.getString(conversion.getColumnIndex("Subtraction_Factor")));
                    divFactor = Double.parseDouble(conversion.getString(conversion.getColumnIndex("Division_Factor")));
                    multiFactor = Double.parseDouble(conversion.getString(conversion.getColumnIndex("Multiplication_Factor")));
                    addFactor = Double.parseDouble(conversion.getString(conversion.getColumnIndex("Add_Factor")));
                } while (conversion.moveToNext());
                conversion.close();
                db.close();
            }
            calc  = (((secondVal - subFactor) / divFactor) * multiFactor) + addFactor;
            if(LOG) Log.d("Convertor"," "+calc+" "+secondVal+" "+subFactor+" "+divFactor+" "+multiFactor+" "+addFactor);
        } catch (Exception e){
            e.printStackTrace();
        }
        return calc;
    }


    public boolean updatedTaskDetails(String Auto_Id,String Task_Status, String Conditional_Status,String Task_Start_At,String Scan_Type,String userId,String Remarks,int incident){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Task_Status", Task_Status);
        contentValues.put("Task_Start_At", Task_Start_At);
        contentValues.put("Scan_Type", Scan_Type);
        contentValues.put("Assigned_To_User_Id",userId);
        contentValues.put("UpdatedStatus", "no");
        contentValues.put("Remarks", Remarks);
        contentValues.put("Incident", incident);
        long resultset = database.update("Task_Details", contentValues, "Auto_Id ='" + Auto_Id + "' AND  Task_Status='"+Conditional_Status+"'", null);
        database.close();
        if(resultset == -1)
            return false;
        else
            return true;

    }

    public boolean insertTaskDetails(String Auto_Id,String companyId,String SiteId,String frequencyId,String Task_Status, String Conditional_Status,String Task_Start_At,String Asset_Name,String AssetId, String Form_IdIntent, String assetCode, String Asset_Location, String Asset_Status, String Activity_Name, String Scan_Type,String userId,String User_Group_Id,String type,String Remarks){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Auto_Id", Auto_Id);
        contentValues.put("Company_Customer_Id", companyId);
        contentValues.put("Site_Location_Id", SiteId);
        contentValues.put("Activity_Frequency_Id", frequencyId);
        contentValues.put("Task_Scheduled_Date", "0000-00-00 00:00:00");
        contentValues.put("Task_Status", Task_Status);
        contentValues.put("Task_Start_At", Task_Start_At);
        contentValues.put("Assigned_To", "U");
        contentValues.put("Asset_Name", Asset_Name);
        contentValues.put("Asset_Id",AssetId);
        contentValues.put("From_Id",Form_IdIntent);
        contentValues.put("Asset_Code",assetCode);
        contentValues.put("Asset_Location", Asset_Location);
        contentValues.put("Asset_Status", Asset_Status);
        contentValues.put("Activity_Name", Activity_Name);
        contentValues.put("Activity_Type", type);
        contentValues.put("Assigned_To_User_Id", userId);
        contentValues.put("Assigned_To_User_Group_Id",User_Group_Id);
        contentValues.put("Scan_Type", Scan_Type);
        contentValues.put("UpdatedStatus", "no");
        contentValues.put("Remarks", Remarks);
        long resultset = database.insert("Task_Details", null, contentValues);
        database.close();
        if(resultset == -1)
            return false;
        else
            return true;

    }

    public boolean updatedTaskDetails(String Auto_Id,String Task_Status,String Task_Start_At,String Scan_Type,String userId,String Remarks,int incident){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Task_Status", Task_Status);
        contentValues.put("Task_Start_At", Task_Start_At);
        contentValues.put("Scan_Type", Scan_Type);
        contentValues.put("Assigned_To_User_Id",userId);
        contentValues.put("UpdatedStatus", "no");
        contentValues.put("Remarks", Remarks);
        contentValues.put("Incident", incident);
        long resultset = database.update("Task_Details", contentValues, "Auto_Id ='" + Auto_Id + "' AND  Task_Status='Pending'", null);
        database.close();
        if(resultset == -1)
            return false;
        else
            return true;

    }


    public boolean updatedPPMTaskDetails(String Auto_Id,String Task_Status, String Conditional_Status,String Task_Start_At,String Scan_Type,String userId,String Assigned_To_User_Group_Id,String Remarks){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Task_Status", Task_Status);
        contentValues.put(DatabaseColumn.Task_Done_At, Task_Start_At);
        contentValues.put("Assigned_To_User_Id",userId);

        contentValues.put(DatabaseColumn.Assigned_To_User_Group_Id,Assigned_To_User_Group_Id);
        contentValues.put(DatabaseColumn.Scan_Type,Scan_Type);
        contentValues.put("UpdatedStatus", "no");

        long resultset = database.update("ppm_task", contentValues, "Auto_Id ='" + Auto_Id + "' AND  Task_Status='"+Conditional_Status+"'", null);
        database.close();
        if(resultset == -1)
            return false;
        else
            return true;

    }

/*
    public boolean updatedTaskDetails(String Auto_Id,String Task_Status,String Task_Start_At,String Scan_Type,String userId,String Remarks){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Task_Status", Task_Status);
        contentValues.put("Task_Start_At", Task_Start_At);
        contentValues.put("Scan_Type", Scan_Type);
        contentValues.put("Assigned_To_User_Id",userId);
        contentValues.put("UpdatedStatus", "no");
        contentValues.put("Remarks", Remarks);
        long resultset = database.update("Task_Details", contentValues, "Auto_Id ='" + Auto_Id + "' AND  Task_Status='Pending'", null);
        database.close();
        if(resultset == -1)
            return false;
        else
            return true;

    }
*/

    public boolean updatedTaskCancelled(String Asset_Id,String Task_Status,String Task_Start_At,String Scan_Type,String Asset_Status){
        SimpleDateFormat YMDHMDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calenderCurrent = Calendar.getInstance();

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Task_Status",Task_Status);
        contentValues.put("Task_Start_At",Task_Start_At);
        contentValues.put("Scan_Type",Scan_Type);
        contentValues.put("Asset_Status",Asset_Status);
        long resultset = -1;
        if(Task_Status.equals("Cancelled")) {
            resultset = database.update("Task_Details",contentValues, "Asset_Id ='" + Asset_Id + "' AND  Task_Status='Pending' ", null);
            database.close();

        }else if(Task_Status.equals("Pending")) {
            resultset = database.update("Task_Details",contentValues, "Asset_Id ='" + Asset_Id + "' AND  Task_Status='Cancelled'  AND datetime(EndDateTime) >= datetime('"+YMDHMDateFormat.format(calenderCurrent.getTime())+"')", null);
            database.close();
        }
        if (resultset == -1)
            return false;
        else
            return true;

    }

    public boolean updatedTaskCancelledCart(String TaskId){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UpdatedStatus", "no");
        long resultset = database.update("Task_Details", contentValues, "Auto_Id ='" + TaskId + "' AND  Task_Status='Cancelled' AND UpdatedStatus IS null", null);
        database.close();
        if(resultset == -1)
            return false;
        else
            return true;
    }

    public boolean updateRemarkValue(String TaskId,String StartTime, String EndTime, String remark){
        Log.d("InUpdatedValue","11"+TaskId+":"+remark);
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Remarks",remark);
        long resultset = database.update("Task_Details", contentValues, "Auto_Id ='" + TaskId+"' AND Task_Scheduled_Date = '"+StartTime+"' AND EndDateTime = '"+EndTime+"' AND Task_Status='Cancelled'", null);
        Log.d("resultRemark",resultset+"");
        database.close();
        if(resultset == -1)
            return false;
        else
            return true;
    }


    public JSONArray composeJSONfortaskDetailsNew(String UserId,String Task_Status){
        JSONArray UploadArray = new JSONArray();
        JSONObject TaskDetails = new JSONObject();
        JSONObject MeterReading = new JSONObject();
        JSONObject DataValue = new JSONObject();
        JSONObject AlertData = new JSONObject();

        JSONArray TaskJsonArray = new JSONArray();
        JSONArray MeterReadingArray = new JSONArray();
        JSONArray DataPostingArray = new JSONArray();
        JSONArray AlertArray = new JSONArray();
        try {


            String[]loggerData = new LoggerFile().loggerFunction(UserId);

            String selectQuery = "SELECT  * FROM Task_Details where UpdatedStatus = 'no' and Assigned_To_User_Id='"+UserId+"' and Task_Status='"+Task_Status+"' ORDER BY Task_Scheduled_Date LIMIT 5" ;
            SQLiteDatabase database = this.getWritableDatabase();

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject TaskJsonObject = new JSONObject();
                    String TaskID= cursor.getString(cursor.getColumnIndex("Auto_Id"));
                    TaskJsonObject.put("Auto_Id", cursor.getString(cursor.getColumnIndex("Auto_Id")));
                    TaskJsonObject.put("Company_Customer_Id",cursor.getString(cursor.getColumnIndex("Company_Customer_Id")));
                    TaskJsonObject.put("Site_Location_Id",cursor.getString(cursor.getColumnIndex("Site_Location_Id")));
                    TaskJsonObject.put("Activity_Frequency_Id", cursor.getString(cursor.getColumnIndex("Activity_Frequency_Id")));
                    TaskJsonObject.put("Task_Start_At", cursor.getString(cursor.getColumnIndex("Task_Start_At")));
                    TaskJsonObject.put("Task_Scheduled_Date", cursor.getString(cursor.getColumnIndex("Task_Scheduled_Date")));
                    TaskJsonObject.put("Task_Status", cursor.getString(cursor.getColumnIndex("Task_Status")));
                    TaskJsonObject.put("Asset_Id", cursor.getString(cursor.getColumnIndex("Asset_Id")));
                    TaskJsonObject.put("Scan_Type", cursor.getString(cursor.getColumnIndex("Scan_Type")));
                    TaskJsonObject.put("Assigned_To", cursor.getString(cursor.getColumnIndex("Assigned_To")));
                    TaskJsonObject.put("Assigned_To_User_Id", cursor.getString(cursor.getColumnIndex("Assigned_To_User_Id")));
                    TaskJsonObject.put("Assigned_To_User_Group_Id", cursor.getString(cursor.getColumnIndex("Assigned_To_User_Group_Id")));
                    TaskJsonObject.put("Record_Status",loggerData[0] );
                    TaskJsonObject.put("Last_Date_Time",loggerData[1]);
                    TaskJsonObject.put("Last_IP",loggerData[2]);
                    TaskJsonObject.put("Last_User_Id",loggerData[3]);
                    TaskJsonObject.put("Update_Location",loggerData[4]);
                    TaskJsonObject.put("Apk_Web_Version", loggerData[5]);
                    TaskJsonObject.put("GeoLoc", loggerData[6]);


                    String selectQuery1 = "SELECT  Meter_Reading.* FROM Meter_Reading LEFT JOIN  Task_Details ON Meter_Reading.Task_Id = Task_Details.Auto_Id WHERE  Meter_Reading.Task_Id ='"+TaskID+"' AND Meter_Reading.UpdatedStatus = 'no'  " ;

                    Cursor cursorMeterReading = database.rawQuery(selectQuery1, null);

                    if (cursorMeterReading.moveToFirst()) {
                        do {
                            JSONObject MeterReadingJsonObject = new JSONObject();

                            MeterReadingJsonObject.put("Task_Id",cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Task_Id")));
                            MeterReadingJsonObject.put("Asset_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Asset_Id")));
                            MeterReadingJsonObject.put("Form_Structure_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Form_Structure_Id")));
                            MeterReadingJsonObject.put("Reading", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Reading")));
                            MeterReadingJsonObject.put("UOM", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("UOM")));
                            MeterReadingJsonObject.put("Reset", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Reset")));
                            MeterReadingJsonObject.put("Activity_Frequency_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Activity_Frequency_Id")));
                            MeterReadingJsonObject.put("Record_Status",loggerData[0] );
                            MeterReadingJsonObject.put("Last_Date_Time",loggerData[1]);
                            MeterReadingJsonObject.put("Last_IP",loggerData[2]);
                            MeterReadingJsonObject.put("Last_User_Id",loggerData[3]);
                            MeterReadingJsonObject.put("Update_Location",loggerData[4]);
                            MeterReadingJsonObject.put("Apk_Web_Version", loggerData[5]);
                            MeterReadingJsonObject.put("GeoLoc", loggerData[6]);
                            MeterReadingArray.put(MeterReadingJsonObject);
                        } while (cursorMeterReading.moveToNext());

                    }
                    cursorMeterReading.close();

                    String selectQueryData = "SELECT  Data_Posting.* FROM Data_Posting LEFT JOIN  Task_Details ON Data_Posting.Task_Id = Task_Details.Auto_Id WHERE Data_Posting.UpdatedStatus = 'no' AND Data_Posting.Task_Id ='"+TaskID+"' ";
                    Cursor cursorDataPosting = database.rawQuery(selectQueryData, null);
                    if (cursorDataPosting.moveToFirst()) {
                        do {
                            JSONObject DatPostingJsonObject = new JSONObject();
                            DatPostingJsonObject.put("Task_Id", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Task_Id")));
                            DatPostingJsonObject.put("Form_Id",cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Form_Id")));
                            DatPostingJsonObject.put("Form_Structure_Id",cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Form_Structure_Id")));
                            DatPostingJsonObject.put("Parameter_Id", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Parameter_Id")));
                            DatPostingJsonObject.put("Value", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Value")));
                            DatPostingJsonObject.put("Record_Status",loggerData[0] );
                            DatPostingJsonObject.put("Last_Date_Time",loggerData[1]);
                            DatPostingJsonObject.put("Last_IP",loggerData[2]);
                            DatPostingJsonObject.put("Last_User_Id",loggerData[3]);
                            DatPostingJsonObject.put("Update_Location",loggerData[4]);
                            DatPostingJsonObject.put("Apk_Web_Version", loggerData[5]);
                            DatPostingJsonObject.put("GeoLoc", loggerData[6]);
                            DataPostingArray.put(DatPostingJsonObject);
                        } while (cursorDataPosting.moveToNext());
                    }
                    cursorDataPosting.close();

                    String selectQueryAlert = "SELECT * FROM AlertMaster WHERE Task_Id='"+TaskID+"'";
                    Cursor cursorAlert = database.rawQuery(selectQueryAlert, null);
                    if (cursorAlert.moveToFirst()) {
                        do {
                            JSONObject AlertJsonObject = new JSONObject();
                            AlertJsonObject.put("Task_Id",cursorAlert.getString(cursorAlert.getColumnIndex("Task_Id")));
                            AlertJsonObject.put("Form_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Form_Id")));
                            AlertJsonObject.put("Form_Structure_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Form_Structure_Id")));
                            AlertJsonObject.put("Alert_Type", cursorAlert.getString(cursorAlert.getColumnIndex("Alert_Type")));
                            AlertJsonObject.put("Created_By_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Created_By_Id")));
                            AlertJsonObject.put("TaskType", cursorAlert.getString(cursorAlert.getColumnIndex("TaskType")));
                            AlertJsonObject.put("Critical", cursorAlert.getString(cursorAlert.getColumnIndex("Critical")));
                            AlertJsonObject.put("Record_Status",loggerData[0] );
                            AlertJsonObject.put("Last_Date_Time",loggerData[1]);
                            AlertJsonObject.put("Last_IP",loggerData[2]);
                            AlertJsonObject.put("Last_User_Id",loggerData[3]);
                            AlertJsonObject.put("Update_Location",loggerData[4]);
                            AlertJsonObject.put("Apk_Web_Version", loggerData[5]);
                            AlertJsonObject.put("GeoLoc", loggerData[6]);
                            AlertArray.put(AlertJsonObject);
                        } while (cursorAlert.moveToNext());
                    }
                    cursorAlert.close();
                    TaskJsonArray.put(TaskJsonObject);
                } while (cursor.moveToNext());
            }cursor.close();
            database.close();

        }catch (Exception E){
            E.printStackTrace();
        }

        try {

            TaskDetails.put("TaskDetails",TaskJsonArray);
            MeterReading.put("MeterReading",MeterReadingArray);
            DataValue.put("DataValue",DataPostingArray);
            AlertData.put("AlertData",AlertArray);

            UploadArray.put(TaskDetails);
            UploadArray.put(MeterReading);
            UploadArray.put(DataValue);
            UploadArray.put(AlertData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return UploadArray;
    }


    public JSONArray composeJSONfortaskDetailsReconfigNew(String UserId,String Task_Status){
        JSONArray UploadArray = new JSONArray();
        JSONObject TaskDetails = new JSONObject();
        JSONObject MeterReading = new JSONObject();
        JSONObject DataValue = new JSONObject();
        JSONObject AlertData = new JSONObject();

        JSONArray TaskJsonArray = new JSONArray();
        JSONArray MeterReadingArray = new JSONArray();
        JSONArray DataPostingArray = new JSONArray();
        JSONArray AlertArray = new JSONArray();
        try {


            String[]loggerData = new LoggerFile().loggerFunction(UserId);

            String selectQuery = "SELECT  * FROM Task_Details where UpdatedStatus = 'no' and Task_Status='"+Task_Status+"' ORDER BY Task_Scheduled_Date LIMIT 5" ;
            SQLiteDatabase database = this.getWritableDatabase();

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject TaskJsonObject = new JSONObject();
                    String TaskID= cursor.getString(cursor.getColumnIndex("Auto_Id"));
                    TaskJsonObject.put("Auto_Id", cursor.getString(cursor.getColumnIndex("Auto_Id")));
                    TaskJsonObject.put("Company_Customer_Id",cursor.getString(cursor.getColumnIndex("Company_Customer_Id")));
                    TaskJsonObject.put("Site_Location_Id",cursor.getString(cursor.getColumnIndex("Site_Location_Id")));
                    TaskJsonObject.put("Activity_Frequency_Id", cursor.getString(cursor.getColumnIndex("Activity_Frequency_Id")));
                    TaskJsonObject.put("Task_Start_At", cursor.getString(cursor.getColumnIndex("Task_Start_At")));
                    TaskJsonObject.put("Task_Scheduled_Date", cursor.getString(cursor.getColumnIndex("Task_Scheduled_Date")));
                    TaskJsonObject.put("Task_Status", cursor.getString(cursor.getColumnIndex("Task_Status")));
                    TaskJsonObject.put("Asset_Id", cursor.getString(cursor.getColumnIndex("Asset_Id")));
                    TaskJsonObject.put("Scan_Type", cursor.getString(cursor.getColumnIndex("Scan_Type")));
                    TaskJsonObject.put("Assigned_To", cursor.getString(cursor.getColumnIndex("Assigned_To")));
                    TaskJsonObject.put("Assigned_To_User_Id", cursor.getString(cursor.getColumnIndex("Assigned_To_User_Id")));
                    TaskJsonObject.put("Assigned_To_User_Group_Id", cursor.getString(cursor.getColumnIndex("Assigned_To_User_Group_Id")));
                    TaskJsonObject.put("Record_Status",loggerData[0] );
                    TaskJsonObject.put("Last_Date_Time",loggerData[1]);
                    TaskJsonObject.put("Last_IP",loggerData[2]);
                    TaskJsonObject.put("Last_User_Id",loggerData[3]);
                    TaskJsonObject.put("Update_Location",loggerData[4]);
                    TaskJsonObject.put("Apk_Web_Version", loggerData[5]);
                    TaskJsonObject.put("GeoLoc", loggerData[6]);


                    String selectQuery1 = "SELECT  Meter_Reading.* FROM Meter_Reading LEFT JOIN  Task_Details ON Meter_Reading.Task_Id = Task_Details.Auto_Id WHERE Meter_Reading.Task_Id ='"+TaskID+"' AND Meter_Reading.UpdatedStatus = 'no'  " ;

                    Cursor cursorMeterReading = database.rawQuery(selectQuery1, null);

                    if (cursorMeterReading.moveToFirst()) {
                        do {
                            JSONObject MeterReadingJsonObject = new JSONObject();

                            MeterReadingJsonObject.put("Task_Id",cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Task_Id")));
                            MeterReadingJsonObject.put("Asset_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Asset_Id")));
                            MeterReadingJsonObject.put("Form_Structure_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Form_Structure_Id")));
                            MeterReadingJsonObject.put("Reading", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Reading")));
                            MeterReadingJsonObject.put("UOM", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("UOM")));
                            MeterReadingJsonObject.put("Reset", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Reset")));
                            MeterReadingJsonObject.put("Activity_Frequency_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Activity_Frequency_Id")));
                            MeterReadingJsonObject.put("Record_Status",loggerData[0] );
                            MeterReadingJsonObject.put("Last_Date_Time",loggerData[1]);
                            MeterReadingJsonObject.put("Last_IP",loggerData[2]);
                            MeterReadingJsonObject.put("Last_User_Id",loggerData[3]);
                            MeterReadingJsonObject.put("Update_Location",loggerData[4]);
                            MeterReadingJsonObject.put("Apk_Web_Version", loggerData[5]);
                            MeterReadingJsonObject.put("GeoLoc", loggerData[6]);
                            MeterReadingArray.put(MeterReadingJsonObject);
                        } while (cursorMeterReading.moveToNext());

                    }
                    cursorMeterReading.close();

                    String selectQueryData = "SELECT  Data_Posting.* FROM Data_Posting LEFT JOIN  Task_Details ON Data_Posting.Task_Id = Task_Details.Auto_Id WHERE Data_Posting.UpdatedStatus = 'no' AND Data_Posting.Task_Id ='"+TaskID+"' ";
                    Cursor cursorDataPosting = database.rawQuery(selectQueryData, null);
                    if (cursorDataPosting.moveToFirst()) {
                        do {
                            JSONObject DatPostingJsonObject = new JSONObject();
                            DatPostingJsonObject.put("Task_Id", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Task_Id")));
                            DatPostingJsonObject.put("Form_Id",cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Form_Id")));
                            DatPostingJsonObject.put("Form_Structure_Id",cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Form_Structure_Id")));
                            DatPostingJsonObject.put("Parameter_Id", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Parameter_Id")));
                            DatPostingJsonObject.put("Value", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Value")));
                            DatPostingJsonObject.put("Record_Status",loggerData[0] );
                            DatPostingJsonObject.put("Last_Date_Time",loggerData[1]);
                            DatPostingJsonObject.put("Last_IP",loggerData[2]);
                            DatPostingJsonObject.put("Last_User_Id",loggerData[3]);
                            DatPostingJsonObject.put("Update_Location",loggerData[4]);
                            DatPostingJsonObject.put("Apk_Web_Version", loggerData[5]);
                            DatPostingJsonObject.put("GeoLoc", loggerData[6]);
                            DataPostingArray.put(DatPostingJsonObject);
                        } while (cursorDataPosting.moveToNext());
                    }
                    cursorDataPosting.close();

                    String selectQueryAlert = "SELECT * FROM AlertMaster WHERE Task_Id='"+TaskID+"'";
                    Cursor cursorAlert = database.rawQuery(selectQueryAlert, null);
                    if (cursorAlert.moveToFirst()) {
                        do {
                            JSONObject AlertJsonObject = new JSONObject();
                            AlertJsonObject.put("Task_Id",cursorAlert.getString(cursorAlert.getColumnIndex("Task_Id")));
                            AlertJsonObject.put("Form_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Form_Id")));
                            AlertJsonObject.put("Form_Structure_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Form_Structure_Id")));
                            AlertJsonObject.put("Alert_Type", cursorAlert.getString(cursorAlert.getColumnIndex("Alert_Type")));
                            AlertJsonObject.put("Created_By_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Created_By_Id")));
                            AlertJsonObject.put("TaskType", cursorAlert.getString(cursorAlert.getColumnIndex("TaskType")));
                            AlertJsonObject.put("Critical", cursorAlert.getString(cursorAlert.getColumnIndex("Critical")));
                            AlertJsonObject.put("Record_Status",loggerData[0] );
                            AlertJsonObject.put("Last_Date_Time",loggerData[1]);
                            AlertJsonObject.put("Last_IP",loggerData[2]);
                            AlertJsonObject.put("Last_User_Id",loggerData[3]);
                            AlertJsonObject.put("Update_Location",loggerData[4]);
                            AlertJsonObject.put("Apk_Web_Version", loggerData[5]);
                            AlertJsonObject.put("GeoLoc", loggerData[6]);
                            AlertArray.put(AlertJsonObject);
                        } while (cursorAlert.moveToNext());
                    }
                    cursorAlert.close();
                    TaskJsonArray.put(TaskJsonObject);
                } while (cursor.moveToNext());
            }cursor.close();
            database.close();

        }catch (Exception E){
            E.printStackTrace();
        }

        try {

            TaskDetails.put("TaskDetails",TaskJsonArray);
            MeterReading.put("MeterReading",MeterReadingArray);
            DataValue.put("DataValue",DataPostingArray);
            AlertData.put("AlertData",AlertArray);

            UploadArray.put(TaskDetails);
            UploadArray.put(MeterReading);
            UploadArray.put(DataValue);
            UploadArray.put(AlertData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return UploadArray;
    }


    public JSONArray composeJSONforTicket(String UserId){
        JSONArray TicketJsonArray = new JSONArray();

        String[]loggerData = new LoggerFile().loggerFunction(UserId);
        String selectQuery = "SELECT  * FROM Ticket_Master  WHERE UpdatedStatus = 'no' AND Ticket_Raise_By='"+UserId+"'";

        try {
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject TicketJsonObject = new JSONObject();
                    TicketJsonObject.put("ID", cursor.getString(cursor.getColumnIndex("ID")));
                    TicketJsonObject.put("Site_Location_Id",  cursor.getString(cursor.getColumnIndex("Site_Location_Id")));
                    TicketJsonObject.put("Company_Customer_Id",  cursor.getString(cursor.getColumnIndex("Company_Customer_Id")));
                    TicketJsonObject.put("Created_Source",  cursor.getString(cursor.getColumnIndex("Created_Source")));
                    TicketJsonObject.put("Created_At",  cursor.getString(cursor.getColumnIndex("Created_At")));
                    TicketJsonObject.put("ticket_Subject",  cursor.getString(cursor.getColumnIndex("ticket_Subject")));
                    TicketJsonObject.put("ticket_Content",  cursor.getString(cursor.getColumnIndex("ticket_Content")));
                    TicketJsonObject.put("ticket_Priority",  cursor.getString(cursor.getColumnIndex("ticket_Priority")));
                    TicketJsonObject.put("Ticket_Raise_By", cursor.getString(cursor.getColumnIndex("Ticket_Raise_By")));
                    TicketJsonArray.put(TicketJsonObject);
                } while (cursor.moveToNext());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return TicketJsonArray;
    }



    public JSONArray composeJSONforAssets(String UserId){
        JSONArray AssetJsonArray = new JSONArray();
        String selectQuery = "SELECT  * FROM Asset_Details where UpdatedStatus = 'no'";
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    String[]loggerData = new LoggerFile().loggerFunction(UserId);
                    JSONObject AssetJsonObject = new JSONObject();
                    AssetJsonObject.put("Asset_Id", cursor.getString(cursor.getColumnIndex("Asset_Id")));
                    AssetJsonObject.put("Asset_Status_Id", cursor.getString(cursor.getColumnIndex("Asset_Status_Id")));
                    AssetJsonObject.put("Manual_Time", cursor.getString(cursor.getColumnIndex("Manual_Time")));
                    AssetJsonObject.put("Asset_Update_Time", cursor.getString(cursor.getColumnIndex("Asset_Update_Time")));
                    AssetJsonArray.put(AssetJsonObject);
                } while (cursor.moveToNext());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return AssetJsonArray;
    }

    public JSONArray composeJSONforAssetsStatusChange(String SiteId,String UserId){
        JSONArray AssetJsonArray = new JSONArray();
        String selectQuery = "SELECT  * FROM AssetStatusLog where Site_Location_Id='"+SiteId+"' AND Assigned_To_User_Id='"+UserId+"' AND UpdatedStatus = 'no'";
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    String[]loggerData = new LoggerFile().loggerFunction(UserId);
                    JSONObject AssetJsonObject1 = new JSONObject();
                    AssetJsonObject1.put("ID", cursor.getString(cursor.getColumnIndex("Id")));
                    AssetJsonObject1.put("Asset_Id", cursor.getString(cursor.getColumnIndex("Asset_Id")));
                    AssetJsonObject1.put("Site_Location_Id", cursor.getString(cursor.getColumnIndex("Site_Location_Id")));
                    AssetJsonObject1.put("Previous_Asset_Status_Id", cursor.getString(cursor.getColumnIndex("Previous_Asset_Status_Id")));
                    AssetJsonObject1.put("Asset_Status_Id", cursor.getString(cursor.getColumnIndex("Asset_Status_Id")));
                    AssetJsonObject1.put("Manual_Time", cursor.getString(cursor.getColumnIndex("Manual_Time")));
                    AssetJsonObject1.put("Asset_Updated_Time", cursor.getString(cursor.getColumnIndex("Asset_Updated_Time")));
                    AssetJsonObject1.put("Remark", cursor.getString(cursor.getColumnIndex("Remark")));
                    AssetJsonObject1.put("Assigned_To_User_Id",cursor.getString(cursor.getColumnIndex("Assigned_To_User_Id")));
                    AssetJsonArray.put(AssetJsonObject1);
                } while (cursor.moveToNext());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return AssetJsonArray;
    }

    public JSONArray SyncInfo(String UserId,int Battery_Percentage,int Signal_Strength,Character Network_Type,String CarrierName,String DeviceDetails){

        JSONArray TicketJsonArray = new JSONArray();
        String[]loggerData = new LoggerFile().loggerFunction(UserId);
        try {
            JSONObject TicketJsonObject = new JSONObject();
            TicketJsonObject.put("Sync_Date_Time",loggerData[1]);
            TicketJsonObject.put("MacAddress",loggerData[2]);
            TicketJsonObject.put("User_Id",loggerData[3]);
            TicketJsonObject.put("Apk_Web_Version", loggerData[5]);
            TicketJsonObject.put("GeoLoc", loggerData[6]);
            TicketJsonObject.put("Signal_Strength",Signal_Strength);
            TicketJsonObject.put("Battery_Percentage",Battery_Percentage);
            TicketJsonObject.put("Network_Type",Network_Type);
            TicketJsonObject.put("Carrier_Name",CarrierName);
            TicketJsonObject.put("Device_Name",DeviceDetails);
            TicketJsonArray.put(TicketJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return TicketJsonArray;
    }

    public boolean insertBitmap(Bitmap selfie, String taskID,String Type,String SiteId)  {
        boolean image = false;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        selfie.compress(Bitmap.CompressFormat.JPEG, 100, out);

        ByteArrayOutputStream meterOut = new ByteArrayOutputStream();
        byte[] selfieBuffer=out.toByteArray();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        try
        {
            values = new ContentValues();
            values.put("Image_Selfie", selfieBuffer);
            values.put("Task_Id", taskID);
            values.put("Image_Type", Type);
            values.put("UpdatedStatus","no");
            values.put("Site_Location_Id",SiteId);
            long i = db.insert("Task_Selfie", null, values);
            if(i == -1)
                image =  false;
            else
                image =  true;
            Log.i("Insert", i + "");
            // Insert into database successfully.
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();

        }
        return image;


    }

    public HashMap<String,String> Images(String TaskId){
        byte[] image = null;
        String image_str,type;
        HashMap<String,String> images = new HashMap<>();
        String selectQuery = "SELECT  * FROM Task_Selfie where Task_Id='" +TaskId + "'";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                image = cursor.getBlob(cursor.getColumnIndex("Image_Selfie"));
                image_str = Base64.encodeToString(image, Base64.DEFAULT);
                type = cursor.getString(cursor.getColumnIndex("Image_Type"));
                images.put(image_str,type);
            } while (cursor.moveToNext());
        }
        return images;
    }

    public void updateImages(String TaskId, String status){
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            String updateQuery = "Update Task_Selfie set UpdatedStatus = '"+ status +"' where Task_Id ='"+ TaskId +"'";
            database.execSQL(updateQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
