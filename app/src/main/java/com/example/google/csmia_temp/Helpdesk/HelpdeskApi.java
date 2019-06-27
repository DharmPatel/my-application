package com.example.google.csmia_temp.Helpdesk;

import com.example.google.csmia_temp.Helpdesk.Model.GenerateTicket;
import com.example.google.csmia_temp.Helpdesk.Model.HkTicket;
import com.example.google.csmia_temp.Helpdesk.Model.MuiltiticketResponse;
import com.example.google.csmia_temp.Helpdesk.Model.NotificationResponse;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HelpdeskApi  {
    @GET("fetch.php")
    Call<ArrayList<Ticket>> getTicket(@Query("site") String site,@Query("Location") String location,@Query("SubLocation") String sublocation,@Query("ServiceArea") String servicearea);

    @GET("fetch.php")
    Call<ArrayList<Ticket>> getTicket(@Query("site") String site,@Query("Location") String location,@Query("SubLocation") String sublocation,@Query("ServiceArea") String servicearea,@Query("department") String department);

    @GET("fetch.php")
    Call<ArrayList<Ticket>> getAllTicket(@Query("site") String site);

    @GET("fetch.php")
    Call<ArrayList<Ticket>> getAllTicket(@Query("site") String site,@Query("department") String department);

    @GET("updatetkt.php")
    Call<ResponseBody> UpdatTicket(@Query("TicketID") String TicketID,@Query("Status") String Status,@Query("UpdatedOn") String UpdatedOn,@Query("UpdatedBy") String UpdatedBy,@Query("Comments") String Comments);

    @GET("TicketUpdateHelpdk.php")
    Call<ResponseBody> UpdatTickethelpdk(@Query("description_txt") String descp,@Query("ticket_id") String tktid,@Query("drpstatus") String drpstatus,@Query("curr_status") String currstatus,@Query("employee_name") String empname,@Query("ticket_code") String ticketcode,@Query("user_mailid") String mail,@Query("starton") String starton,@Query("userid") String userid);

    @Headers("Content-Type: application/json")
    @POST("ticketInsert_sweety.php")
    Call<ArrayList<MuiltiticketResponse>> sendMultiTicket(@Body GenerateTicket generateTicket);

    @POST("ticketData_helpdk.php")
    Call<ArrayList<HkTicket>> getHelpdkTkt(@Query("site_location_id") String site_location_id, @Query("main_db") String maindb, @Query("Login_user_for") String login_for, @Query("user_id") String userid);

    @FormUrlEncoded
    @POST("tokenupdate.php")
    Call<TokenResponse> TokenCall (@Field("Site_Location_Id") String site_location_id,
                                   @Field("macAddress") String macaddress,
                                   @Field("token") String token,
                                   @Field("user_role") String user_role);

    @Headers({"Accept: application/json"})
    @GET("notification.php")
    Call<NotificationResponse> TokenUpload (@Query("user_token") String site_location_id,
                                            @Query("message") String user_role,
                                            @Query("user_role_name") String group_name,
                                            @Query("Site_Location_Id") String Site_Location_Id );
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
