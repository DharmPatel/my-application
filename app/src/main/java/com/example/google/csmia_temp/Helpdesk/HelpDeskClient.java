package com.example.google.csmia_temp.Helpdesk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HelpDeskClient {
    public static String BASE_URL="http://punctualiti.in/csmia/android/";

    public static Retrofit retrofit=null;
   static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    public static Retrofit getClient(){
        retrofit =new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retrofit;
    }
 /*   $ticket_code = $tkt_arr['ticket_code'];
    $user_id = $_REQUEST['user_id'];
    $site_id = $_REQUEST['site_id'];
    $building_id = $_REQUEST['building_id'];
    $floor_id = $_REQUEST['floor_id'];
    $room_id = $_REQUEST['room_id'];
    $asset_id = $_REQUEST['asset_id'];
    $category_id = $_REQUEST['category_id'];
    $sub_category_id = $_REQUEST['sub_category_id'];
    $issue_id = $_REQUEST['issue_id'];
    $level = $_REQUEST['level'];
    $user_type = $_REQUEST['user_type'];
    $status = $_REQUEST['status'];
    $mail_on_closure = $_REQUEST['mail_on_closure'];
    $desc = $_REQUEST['desc'];*/
//$created_at = $_REQUEST['created_at'];
//$record_status = $_REQUEST['record_status'];

    /*$main_db_name = $_REQUEST['main_db_name'];
    $selected_site_db_name = $_REQUEST['selected_site_db_name'];
*/
    /*public static void createUser(@FieldMap HashMap<String, String> params, Callback<JSONObject> callback) {
        Retrofit retrofit= HelpDeskClient.getClient();
        HelpdeskApi helpdeskApi=retrofit.create(HelpdeskApi.class);
        Call<JSONObject> userCall = (Call<JSONObject>) helpdeskApi.ticketInsert(String 'ticket_code',String 'user_id',String);
        userCall.enqueue(callback);
    }*/

}
