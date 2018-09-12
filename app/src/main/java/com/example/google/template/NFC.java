package com.example.google.template;
import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

/**
 * Created by user on 29-03-2017.
 */
public class NFC extends Application {

    Context context;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    String RES="";
    String tagcontent="";
    public static final String TAG = "NfcDemo";

    @Override
    public void onCreate() {
        try {
            super.onCreate();
            context = this;
        }catch (Exception e){
            e.printStackTrace();
            Log.d("NfcOnCreate",e.getMessage());
            Log.d("aa39","ERROR==" + e);
            Toast.makeText(context, "Error code: aa39", Toast.LENGTH_SHORT).show();
        }
    }

    public void  setupForegroundDispatch(Activity activity, NfcAdapter adapter) {
        try {
            final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            final PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);

            IntentFilter[] filters = new IntentFilter[1];
            String[][] techList = new String[][]{};

            // Notice that this is the same filter as in our manifest.
            filters[0] = new IntentFilter();
            filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
            filters[0].addCategory(Intent.CATEGORY_DEFAULT);
            try {
                filters[0].addDataType(MIME_TEXT_PLAIN);
            } catch (IntentFilter.MalformedMimeTypeException e) {
                throw new RuntimeException("Check your mime type.");
            }
            adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
        }catch (Exception e){
            e.printStackTrace();
            Log.d("NfcSetup",e.getMessage());
            Log.d("aa68", "ERROR==" + e);
        }
    }

    public void stopForegroundDispatch(Activity activity, NfcAdapter adapter) {
        try {
            adapter.disableForegroundDispatch(activity);
        }catch (Exception e){
            e.printStackTrace();
            Log.d("NfcStop",e.getMessage());
            Log.d("aa79","ERROR==" + e);
            Toast.makeText(context, "Error code: aa79", Toast.LENGTH_SHORT).show();
        }
    }

    public void readnfc(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecord=ndefMessage.getRecords();
        if(ndefRecord !=null && ndefRecord.length>0){
            NdefRecord ndefRecords=ndefRecord[0];
            tagcontent =readText(ndefRecords);
            Log.d("fdasfdasf",""+tagcontent);
            //Toast.makeText(context,""+tagcontent, Toast.LENGTH_SHORT).show();
        }
    }

    private String readText(NdefRecord record) {
        try {
            byte[] payload = record.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageCodeLength = payload[0] & 0063;
            try {
                return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("aa169","ERROR==" + e);
            Toast.makeText(context, "Error code: aa169", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
