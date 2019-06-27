package com.example.google.csmia_temp.notification;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import com.example.google.csmia_temp.ConstantList.Config;
import com.example.google.csmia_temp.Helpdesk.Fragment.HelpdeskFilter_Tkt;
import com.example.google.csmia_temp.R;
import com.example.google.csmia_temp.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = "Notic";
    String user = "deesha";
    String UserType_G = "UserType_G";
    NotificationUtils notificationUtils;
    SharedPreferences settings,settings_token;
    SharedPreferences.Editor editor,editor_token;
    //  Showing notification with text only
    Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    // This is the Notification Channel ID. More about this in the next section
    public static final String CHANNEL_ID = "notification_infocity";
    //User visible Channel Name
    public static final String CHANNEL_NAME = "Notification Channel";
    // Importance applicable to all the notifications in this Channel

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

//  Showing notification with text and image

    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        final Intent intent = new Intent("tokenReceiver");
        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        intent.putExtra("token",s);
        broadcastManager.sendBroadcast(intent);
        storeRegIdInPref(s);
    }
    private void storeRegIdInPref(String token) {
        settings_token = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        editor_token = settings_token.edit();
        editor_token.putString("token", token);
        editor_token.apply();
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = null,body=null;
        //handle the data message here
           /* Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setSmallIcon(R.drawable.eurestservices)
                    .build();
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(123, notification);*/

        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);
//        String obj=object.getString("data");
        try {
            Log.e("JSON OBJECT", object.getString("data"));
            String data =object.getString("data");
            JSONObject jsonObject= new JSONObject(data);
            title=jsonObject.getString("title");
            body=jsonObject.getString("body");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //rest of the code
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        UserType_G = settings.getString("UserType_G", null);
        if (settings.contains("UserType_G")) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            try {
                Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("body"));
                Intent intent = new Intent(this, HelpdeskFilter_Tkt.class);
//                handleIntent(intent);
//                intent.putExtra("Firebase_Reconfig", "Firebase_Reconfig");
                   /* HomePage homePage=HomePage.getInstance();
                    homePage.Reconfig_AlertDialog();*/
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//                String channelId = createNotificationChannel(this);
//                String title = titi;
//                String body = remoteMessage.getData().get("body");

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    builder.setSmallIcon(R.drawable.applogo)
                            .setColor(getResources().getColor(R.color.colorAccent))
                            .setContentTitle(title)
                            .setContentText(body)
                            .setAutoCancel(true)
                            .setNumber(1)
                            .setSound(soundUri)
                            .setContentIntent(pendingIntent);
                    manager.notify(NOTIFICATION_ID, builder.build());

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AudioAttributes att = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                    CharSequence name = "deesha_chanel_name";
                    String description = "deesha_desc";
                    int importance = NotificationManager.IMPORTANCE_HIGH ;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                    channel.setDescription(description);
                    channel.setSound(soundUri,att);
                    channel.setName("Infocity");
                    channel.setShowBadge(true);
                    channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    channel.enableLights(true);
                    NotificationManager notificationManager =getSystemService(NotificationManager.class);
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(channel);
                 Notification.Builder notification =new Notification.Builder(this,CHANNEL_ID)
                            .setSmallIcon(R.drawable.applogo)
                            .setContentTitle(title) // title for notification
                            .setContentText(body) // message for notification
                            .setAutoCancel(true)
                            .setSound(soundUri)
                            .setNumber(3)
                            .setOnlyAlertOnce(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setContentIntent(pendingIntent);
                    manager.notify(NOTIFICATION_ID, notification.build());
                } else {
                    builder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setContentText(remoteMessage.getNotification()
                                    .getBody())
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent);
                    manager.notify(NOTIFICATION_ID, builder.build());
                }


            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}


/*

    //Dharam Code Backup
 @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        editor1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        UserType_G=settings.getString("UserType_G",null);

  if(settings.contains("UserType_G")){
        if(UserType_G.equalsIgnoreCase("Regular")) {
            Log.e(TAG, "From: " + remoteMessage.getFrom());
            // Check if message contains a notification payload.

            if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                handleNotification(remoteMessage.getNotification().getBody());
            }

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

                try {
                    JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    handleDataMessage(json);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }

  }
      }
    }

    private void handleNotification(String message) {
        Log.d("testasdasdasdasd",message);
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);

            Bundle extras = new Bundle();
            extras.putString("message", message);


            Log.d("testasdasdasdasd234",message);
            pushNotification.putExtras(extras);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                Log.d("teasdasdasdasdasd",message +"");
                Bundle extras = new Bundle();
                extras.putString("message", message);


                pushNotification.putExtras(extras);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
                resultIntent.putExtra("message", message);


                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


*
     * Showing notification with text only

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }


*
     * Showing notification with text and image

    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
*/
