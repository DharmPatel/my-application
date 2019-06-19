package com.example.google.csmia_temp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Notification extends AppCompatActivity {
    RecyclerView recyclerView;
    public List<NotificationProvider> listItems;
    RecyclerView.Adapter adapter;
    NotificationProvider notificationProvider;
    String msg, hrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView = (RecyclerView)findViewById(R.id.notification_list);
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();
        adapter = new NotificationAdapter(listItems,getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                notificationProvider = listItems.get(position);
                msg = notificationProvider.getMessage();
                hrs = notificationProvider.getHours();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}
