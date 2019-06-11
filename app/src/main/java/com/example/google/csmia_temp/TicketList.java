package com.example.google.csmia_temp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

//import com.example.google.microland.Helper.DatabaseHelper;

public class TicketList extends AppCompatActivity {

    ticketAdapter ticketAdapter;
    ListView lvtickect;
    String dateTime,Subject,Type,User_Id,SiteId,Scan_Type,Auto_Id;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    String User_Group_Id="";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        myDb = new DatabaseHelper(getApplicationContext());
        User_Id = settings.getString("userId", null);
        SiteId = myDb.Site_Location_Id(User_Id);
        Scan_Type = myDb.ScanType(User_Id);
        User_Group_Id = myDb.UserGroupId(User_Id);

        Log.d("testingvalue",User_Id+"");
        /*if(User_Id.equalsIgnoreCase("7c8b9c73-a5ca-11e8-97e2-00163c089164")){
            getUser="M&E";
        }else if (User_Id.equalsIgnoreCase("add4cd93-a5ca-11e8-97e2-00163c089164")) {
            getUser="HouseKeeping";
        }*/

        //if(User_Id.equalsIgnoreCase(""))


//        lvtickect = (ListView)findViewById(R.id.ticket);

        ticketAdapter = new ticketAdapter(getApplicationContext(), R.layout.asset_list_item);
        lvtickect.setAdapter(ticketAdapter);

        lvtickect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {

                            dateTime = ticketAdapter.getItem(position).gettype();
                            Subject = ticketAdapter.getItem(position).getSubject();
                            Type = ticketAdapter.getItem(position).getdatetime();
                            Auto_Id = ticketAdapter.getItem(position).getticketId();

                    Intent intent = new Intent(TicketList.this, viewTicket.class);
                    intent.putExtra("TicketId", Auto_Id);

                    startActivity(intent);
                    finish();



                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("LoG@",e.getMessage());
                }

            }
        });
        loadList(SiteId,User_Id,User_Group_Id);
    }

    public void loadList(String SiteId, String UserId,String User_Group_Id){
        try {
                db = myDb.getReadableDatabase();
            String Query="SELECT * from Ticket_Created where Site_Location_Id ='" + SiteId + "' AND Created_User_Id ='"+UserId+"' ";
            Cursor cursor= db.rawQuery(Query, null);
            Log.d("Testing","AlertQuery"+Query);
            if(cursor.getCount() != 0){
            if (cursor.moveToFirst()) {
                do {
                    String Created_DateTime=cursor.getString(cursor.getColumnIndex("Created_DateTime"));
                    String Ticket_Type=cursor.getString(cursor.getColumnIndex("Ticket_Type"));
                    String Ticket_Subject=cursor.getString(cursor.getColumnIndex("Ticket_Subject"));
                    String Auto_Id=cursor.getString(cursor.getColumnIndex("Auto_Id"));


                    ticketGetterSetter assetDataProvider = new ticketGetterSetter(Created_DateTime,Ticket_Type,Ticket_Subject,Auto_Id);
                    ticketAdapter.add(assetDataProvider);
                }
                while (cursor.moveToNext());
            }
                cursor.close();
            }else {
                String assignedGroup = "SELECT tc.* FROM Ticket_Created tc\n" +
                        "LEFT JOIN Form_Ticket ft ON \n" +
                        "tc.Form_Ticket_Id = ft.Auto_Id \n" +
                        "WHERE ft.Assigned_To IN ("+User_Group_Id+ ")";

                Cursor cursorAssigned= db.rawQuery(assignedGroup, null);
                Log.d("testingValue",cursorAssigned.getCount()+"");
                if (cursorAssigned.moveToFirst()) {
                    do {
                        String Created_DateTime=cursorAssigned.getString(cursorAssigned.getColumnIndex("Created_DateTime"));
                        String Ticket_Type=cursorAssigned.getString(cursorAssigned.getColumnIndex("Ticket_Type"));
                        String Ticket_Subject=cursorAssigned.getString(cursorAssigned.getColumnIndex("Ticket_Subject"));
                        String Auto_Id=cursorAssigned.getString(cursorAssigned.getColumnIndex("Auto_Id"));


                        ticketGetterSetter assetDataProvider = new ticketGetterSetter(Created_DateTime,Ticket_Type,Ticket_Subject,Auto_Id);
                        ticketAdapter.add(assetDataProvider);
                    }
                    while (cursorAssigned.moveToNext());
                }
                cursorAssigned.close();
            }

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TicketList.this, HomePage.class);
        intent.putExtra("User_Id", User_Id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
}
