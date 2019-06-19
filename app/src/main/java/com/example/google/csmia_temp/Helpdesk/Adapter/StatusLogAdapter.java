package com.example.google.csmia_temp.Helpdesk.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.google.csmia_temp.Helpdesk.Model.Ticket_Log;
import com.example.google.csmia_temp.R;

import java.util.ArrayList;

public class StatusLogAdapter extends RecyclerView.Adapter<StatusLogAdapter.ViewHolder> {
    private ArrayList<Ticket_Log> ticketLogArrayList;
    private Context context;
    public StatusLogAdapter(ArrayList<Ticket_Log> ticketLogArrayList, Context context) {
        this.ticketLogArrayList = ticketLogArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket_logs,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      //  holder.tx_username.setText(ticketLogArrayList.get(position).getEmployeeName());
        holder.tx_descp.setText(ticketLogArrayList.get(position).getDescription());
        holder.tx_status_log.setText(ticketLogArrayList.get(position).getLogText());
        holder.tx_createdat.setText(ticketLogArrayList.get(position).getCreatedAt());
        holder.tx_username.setText(ticketLogArrayList.get(position).getEmployeeName());
    }

    @Override
    public int getItemCount() {
        return ticketLogArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tx_username,tx_createdat,tx_status_log,tx_descp;
        public ViewHolder(View itemView) {
            super(itemView);
            tx_username = itemView.findViewById(R.id.tx_username);
            tx_createdat = itemView.findViewById(R.id.tx_createdat);
            tx_status_log = itemView.findViewById(R.id.tx_status_log);
            tx_descp = itemView.findViewById(R.id.tx_descp);
        }
    }
}
