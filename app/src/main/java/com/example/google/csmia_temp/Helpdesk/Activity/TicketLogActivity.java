package com.example.google.csmia_temp.Helpdesk.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import com.example.google.csmia_temp.Helpdesk.Adapter.StatusLogAdapter;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket_Log;
import com.example.google.csmia_temp.R;
import java.util.ArrayList;

public class TicketLogActivity extends AppCompatActivity {
    RecyclerView rc_tktLog;
    StatusLogAdapter statusLogAdapter;
    ArrayList<Ticket_Log> ticketLogArrayList;
    Intent intent;
    ImageButton imgbackpress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketlog);
        rc_tktLog = (RecyclerView) findViewById(R.id.rc_tktlog);
        intent=getIntent();
        ticketLogArrayList = intent.getParcelableArrayListExtra("ticket_logList");
         statusLogAdapter = new StatusLogAdapter(ticketLogArrayList,TicketLogActivity.this);
        rc_tktLog.setHasFixedSize(true);
        rc_tktLog.setLayoutManager(new LinearLayoutManager(TicketLogActivity.this));
        rc_tktLog.setAdapter(statusLogAdapter);
        rc_tktLog.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        imgbackpress = (ImageButton) findViewById(R.id.btn_back);
        imgbackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
