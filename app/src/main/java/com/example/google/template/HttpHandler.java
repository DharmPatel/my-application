package com.example.google.template;

/**
 * Created by MegaVision01 on 11/2/2016.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.google.template.ConstantList.UrlList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class HttpHandler {
    static final boolean LOG = new applicationClass().checkLog();
    DatabaseHelper myDb = new DatabaseHelper(new applicationClass().getContext());
    private static final String TAG = HttpHandler.class.getSimpleName();
    private String URLSTRING = new applicationClass().urlString()+"android/";
    public HttpHandler() {
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String LoginDetails(String username,String password) {
        String response = null;
        try {
            String reqUrl =  URLSTRING+"LoginDetails.php";
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            SSLCerts.sslreq();
            conn.setRequestMethod("POST");
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String data = URLEncoder.encode("Username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+"&"+
                    URLEncoder.encode("Password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") ;
            Log.d("ASdASDASDWAS",reqUrl +"?"+data);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
            Log.d("checkResponse","-->"+response.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public String apkDetails(int apkversion,String UserId) {
        String response = "";
        Log.d("StringDataForm",apkversion + "Value");

        try {
            //String reqUrl = new applicationClass().urlString()+"android/checkapkupdate.php";
            String reqUrl =URLSTRING+"checkapkupdate.php";
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            SSLCerts.sslreq();
            conn.setRequestMethod("POST");
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String data = URLEncoder.encode("apk", "UTF-8") + "=" + URLEncoder.encode(""+apkversion, "UTF-8");

            if(LOG)Log.d("Stringasfawe", data+url);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream in = new BufferedInputStream(conn.getInputStream());

            response = convertStreamToString(in);

            if(LOG)Log.d("String_Value", response.toString());
        } catch (MalformedURLException e) {
            Log.e("fsd", "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e("fdsa", "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e("fdasf", "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e("fds", "Exception: " + e.getMessage());
        }
        return response;
    }

    public String downloadRefrense(String Site_Id,int record_id) {
        String response = "";
        try {
            if(LOG)Log.d("fdasfdddds","f"+Site_Id+record_id);
            String reqUrl = URLSTRING+"DownloadSettings.php";
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            SSLCerts.sslreq();
            conn.setRequestMethod("POST");
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String data = URLEncoder.encode("Record_Key", "UTF-8") + "=" + URLEncoder.encode(""+record_id, "UTF-8")+"&"+
                    URLEncoder.encode("Site_Location_Id", "UTF-8") + "=" + URLEncoder.encode(""+Site_Id, "UTF-8");
            if(LOG)Log.d("Stringasfawe", reqUrl+"?"+data);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream in = new BufferedInputStream(conn.getInputStream());

            response = convertStreamToString(in);

            if(LOG)Log.d("String_Value", response.toString());
        } catch (MalformedURLException e) {
            if(LOG)Log.e("fsd", "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            if(LOG)Log.e("fdsa", "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            if(LOG)Log.e("fdasf", "IOException: " + e.getMessage());
        } catch (Exception e) {
            if(LOG)Log.e("fds", "Exception: " + e.getMessage());
        }
        return response;
    }

    public String downloadAssetChanges(String Site_Id,String Updated_Ids,String TableName) {
        String response = "";
        try {
            String reqUrl = URLSTRING+"DownloadUpdatedAsset.php";
            if(LOG)Log.d("reqUrl34343","1"+reqUrl);
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            SSLCerts.sslreq();
            conn.setRequestMethod("POST");
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data = URLEncoder.encode("Updated_Ids", "UTF-8") + "=" + URLEncoder.encode(""+Updated_Ids, "UTF-8")+"&"+
                    URLEncoder.encode("Table_Name", "UTF-8") + "=" + URLEncoder.encode(""+TableName, "UTF-8")+"&"+
                    URLEncoder.encode("Site_Location_Id", "UTF-8") + "=" + URLEncoder.encode(""+Site_Id, "UTF-8") ;
            if(LOG)Log.d("Stringasfawe12", data);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream in = new BufferedInputStream(conn.getInputStream());

            response = convertStreamToString(in);

            if(LOG)Log.d("String_Value", response.toString());
        } catch (MalformedURLException e) {
            if(LOG)Log.e("fsd", "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            if(LOG)Log.e("fdsa", "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            if(LOG)Log.e("fdasf", "IOException: " + e.getMessage());
        } catch (Exception e) {
            if(LOG)Log.e("fds", "Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    public Bitmap bannerImage(String profilename) {

        String reqUrl =  new applicationClass().urlString()+"Images/appimages/"+profilename;

        if(LOG)Log.d("ASDASDaSDASdasdasda",reqUrl);
        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(reqUrl).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }


    public String imageList() {
        String response = null;
        try {
            String reqUrl =  URLSTRING+"imageslist.php";
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            SSLCerts.sslreq();
            conn.setRequestMethod("POST");
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            /*String data = URLEncoder.encode("Username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+"&"+
                    URLEncoder.encode("Password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") ;
            bufferedWriter.write(data);*/
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
            if(LOG)Log.d("checkResponse","-->"+response.toString());
        } catch (MalformedURLException e) {
            if(LOG)Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            if(LOG)Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            if(LOG)Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            if(LOG)Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public String taskDataCall(String User_Group_Id, String SiteID,String URL) {
        String response = null;
        try {
            String reqUrl =  URLSTRING+"DataDownload.php";
            if(LOG)Log.d("fdsaf",reqUrl);
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            SSLCerts.sslreq();
            conn.setRequestMethod("POST");
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String data = URLEncoder.encode("User_Group_Id", "UTF-8") + "=" + URLEncoder.encode(User_Group_Id, "UTF-8")+"&"+
                    URLEncoder.encode("Site_Location_Id", "UTF-8") + "=" + URLEncoder.encode(SiteID, "UTF-8");

            if(LOG)Log.d("Fsdafasdfads", "" + reqUrl + "?" + data);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());

            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }


    public String getPPmTask(String User_Group_Id, String SiteID) {
        String response = null;
        try {
            //String reqUrl =  URLSTRING+"DataDownload.php";
            //if(LOG)Log.d("fdsaf",reqUrl);
            URL url = new URL(UrlList.PPMTASKURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            SSLCerts.sslreq();
            conn.setRequestMethod("POST");
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String data = URLEncoder.encode("User_Group_Id", "UTF-8") + "=" + URLEncoder.encode(User_Group_Id, "UTF-8")+"&"+
                    URLEncoder.encode("Site_Location_Id", "UTF-8") + "=" + URLEncoder.encode(SiteID, "UTF-8");

            if(LOG)Log.d("PPMURLData", "" + UrlList.PPMTASKURL + "?" + data);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());

            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }
    public String getTaskDetailsServer(String userID,String date,String Site_Id) {
        String response = null;
        try {
            String reqUrl =  URLSTRING+"TaskDetailsServerNewv1.0.1.php";
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            SSLCerts.sslreq();
            conn.setRequestMethod("POST");
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String data = URLEncoder.encode("Assigned_To_User_Group_Id", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8")+"&"+
                    URLEncoder.encode("dateTime", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")+"&"+
                    URLEncoder.encode("Site_Location_Id", "UTF-8") + "=" + URLEncoder.encode(""+Site_Id, "UTF-8");

            if(LOG)Log.d("Datadownload",reqUrl+data);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());

            response = convertStreamToString(in);


        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

}