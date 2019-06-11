package com.example.google.csmia_temp.Helpdesk.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.google.csmia_temp.DatabaseHelper;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket;
import com.example.google.csmia_temp.R;

import java.util.ArrayList;

public class HelpdeskAdapter extends RecyclerView.Adapter<HelpdeskAdapter.HelpdeskHolder> {
    Context context;
    public final String MY_PREFS_NAME1 = "CheckTaskInsert";
    //    private final View.OnClickListener onClickListener = (View.OnClickListener) new MyOnClicklistener();
    DatabaseHelper databaseHelper;
    ArrayList<Ticket> ticketArrayList;

    public HelpdeskAdapter(Context context, ArrayList<Ticket> ticketArrayList) {
        this.context = context;
        this.ticketArrayList = ticketArrayList;

    }

    @Override
    public HelpdeskHolder onCreateViewHolder(ViewGroup parent, int resource) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newticket, parent, false);
        final HelpdeskHolder helpdeskHolder = new HelpdeskHolder(view);
        return helpdeskHolder;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onBindViewHolder(HelpdeskHolder holder, int position) {
        Ticket ticket = ticketArrayList.get(position);
        String sub_loc=ticketArrayList.get(position).getSUBLOCATIONNAME();
        if(ticketArrayList.get(position).getSUBLOCATIONNAME()==null){
            sub_loc="";
        }
        holder.tv_ticket_code.setText(ticketArrayList.get(position).getTICKETID());
        holder.tv_loc_name.setText(ticketArrayList.get(position).getLOCATIONNAME()+" "+sub_loc);
        holder.tv_status.setText(ticketArrayList.get(position).getSTATUS());
        holder.tv_product_nm.setText(ticket.getPRODUCTNAME());
        holder.tv_dept_name.setText(ticket.getDEPARTMENTNAME());
        holder.tv_product_title.setText(ticket.getPROBLEMTITLE());
        holder.tv_createAt.setText(ticket.getCREATEDON());

    }

    @Override
    public int getItemCount() {
        return ticketArrayList.size();
    }

    public class HelpdeskHolder extends RecyclerView.ViewHolder {
        TextView tv_ticket_code, tv_status, tv_product_nm, tv_createAt, tv_product_title, tv_dept_name,tv_loc_name;
        CardView cardView;

        public HelpdeskHolder(View itemView) {
            super(itemView);
            tv_ticket_code = itemView.findViewById(R.id.tv_ticket_code);
            tv_status=itemView.findViewById(R.id.tvStatus);
            tv_product_nm = itemView.findViewById(R.id.tv_product_name);
            tv_createAt = itemView.findViewById(R.id.tv_createdAt);
            tv_product_title = itemView.findViewById(R.id.tv_problem_title);
            tv_dept_name = itemView.findViewById(R.id.tvDeptNm);
            tv_loc_name=itemView.findViewById(R.id.tv_loc_name);
            cardView=itemView.findViewById(R.id.cardView);
            }
    }

}
