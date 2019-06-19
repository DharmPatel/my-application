package com.example.google.csmia_temp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationProvider> notificationProviders;
    private Context context;

    public NotificationAdapter(List<NotificationProvider> listItems, Context context) {
        this.notificationProviders = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationProvider provider = notificationProviders.get(position);
        holder.Message.setText(provider.getMessage());
        holder.Hours.setText(provider.getHours());
    }

    @Override
    public int getItemCount() {
        return notificationProviders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Message, Hours;
        public ViewHolder(View itemView) {
            super(itemView);
            Message = (TextView)itemView.findViewById(R.id.messageId);
            Hours = (TextView)itemView.findViewById(R.id.timeTv);
        }
    }
}
