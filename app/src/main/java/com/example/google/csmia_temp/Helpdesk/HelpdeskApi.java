package com.example.google.csmia_temp.Helpdesk;

import com.example.google.csmia_temp.Helpdesk.Model.GenerateTicket;
import com.example.google.csmia_temp.Helpdesk.Model.MuiltiticketResponse;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket;
import com.example.google.csmia_temp.Helpdesk.Model.TicketIdResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface HelpdeskApi  {
    @GET("fetch.php")
    Call<ArrayList<Ticket>> getTicket(@Query("site") String site,@Query("Location") String location,@Query("SubLocation") String sublocation,@Query("ServiceArea") String servicearea);

    @GET("fetch.php")
    Call<ArrayList<Ticket>> getTicket1(@Query("site") String site,@Query("Location") String location,@Query("SubLocation") String sublocation,@Query("ServiceArea") String servicearea,@Query("department") String department);

    @GET("fetch.php")
    Call<ArrayList<Ticket>> getAllTicket(@Query("site") String site);

    @GET("updatetkt.php")
    Call<ResponseBody> UpdatTicket(@Query("TicketID") String TicketID,@Query("Status") String Status,@Query("UpdatedOn") String UpdatedOn,@Query("UpdatedBy") String UpdatedBy,@Query("Comments") String Comments);

    @Headers("Content-Type: application/json")
    @POST("ticketInsert_sweety.php")
    Call<MuiltiticketResponse> sendMultiTicket(@Body GenerateTicket generateTicket);

    @GET("ticketInsert.php")
    Call<TicketIdResponse> ticketInsert(@Query("user_id") HashSet<String> user_id,
                                        @Query("site_id") HashSet<String> site_id,
                                        @Query("building_id") HashSet<String> building_id,
                                        @Query("floor_id") HashSet<String> floor_id,
                                        @Query("room_id") HashSet<String> room_id,
                                        @Query("asset_id") HashSet<String> asset_id,
                                        @Query("category_id") HashSet<String> category_id,
                                        @Query ("sub_category_id") HashSet<String> sub_category_id,
                                        @Query("issue_id") HashSet<String> issue_id,
                                        @Query("area") String area,
                                        @Query("user_type") String user_type,
                                        @Query("mail_on_closure") int mail_on_closure,
                                        @Query("desc") HashSet<String> desc,
                                        @Query("selected_site_db_name") HashSet<String> selected_site_db_name,
                                        @Query("main_db_name") HashSet<String> main_db_name,
                                        @Query("size") int size,
                                        @Query("employee_name")HashSet<String> employee_name ,
                                        @Query("user_mailid")HashSet<String> user_mailid);

/*
    @GET("updatetkt.php")
    Call<> UpdatTicket(@Query("TicketID") String TicketID,@Query("Status") String Status,@Query("UpdatedOn") String UpdatedOn,@Query("UpdatedBy") String UpdatedBy,@Query("Comments") String Comments);
*/

    /*

    @Multipart
    @POST("ticketInsert.php") //Your login url should look like this
    Call<ResponseBody> ticketInsert(@Part("user_id") RequestBody user_id,
                                    @Part("site_id") RequestBody site_id,
                                    @Part("building_id") RequestBody building_id,
                                    @Part("floor_id") RequestBody floor_id,
                                    @Part("room_id") RequestBody room_id,
                                    @Part("asset_id") RequestBody asset_id,
                                    @Part("category_id") int category_id,
                                    @Part("sub_category_id") int sub_category_id,
                                    @Part("issue_id") int issue_id,
                                    @Part("area") RequestBody area,
                                    @Part("user_type") RequestBody user_type,
                                    @Part("mail_on_closure") int mail_on_closure,
                                    @Part("desc") RequestBody desc,
                                    @Part("selected_site_db_name") RequestBody selected_site_db_name,
                                    @Part("main_db_name") RequestBody main_db_name,
                                    @Part List<MultipartBody.Part> file,
                                    @Part("size") int size,
                                    @Part("employee_name") RequestBody employee_name,
                                    @Part("employee_name") RequestBody user_mailid);


    @FormUrlEncoded
    @POST("registerUser.php")
    Call<ResponseBody> Register(@Field("name") String name,
                                @Field("mailid") String mailid,
                                @Field("pwd") String pwd,
                                @Field("type") String type,
                                @Field("site") String site,
                                @Field("contact") String contact);

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> Login(@Query("mailid") String mailid,
                             @Query("pwd") String pwd);



    @FormUrlEncoded
    @POST("TicketUpdate.php")
    Call<ResponseBody> StatusUpdate(@Field("description_txt") String desc,
                                    @Field("drpstatus") String drpstatus,
                                    @Field("ticket_id") int ticket_id,
                                    @Field("curr_status") String curr_Status,
                                    @Field("userid") String userid,
                                    @Field("employee_name") String employee_name,
                                    @Field("ticket_code") String ticket_code,
                                    @Field("user_mailid") String user_mailid,
                                    @Field("starton") String starton);

   */
}
