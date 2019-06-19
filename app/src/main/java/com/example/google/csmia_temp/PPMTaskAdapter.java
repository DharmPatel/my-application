package com.example.google.csmia_temp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class PPMTaskAdapter extends RecyclerView.Adapter<PPMTaskAdapter.ViewHolder> {
    private List<PpmTaskProvider> taskProviders;
    private Context context;

    public PPMTaskAdapter(List<PpmTaskProvider> listItems, Context pendingPPMTask) {
        this.taskProviders = listItems;
        this.context = pendingPPMTask;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PpmTaskProvider taskProvider = taskProviders.get(position);

        if(taskProvider.getupdatedStatus() == null)
        {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);

        }
        else if(taskProvider.getupdatedStatus().equals("no"))
        {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);

        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#76D7C4"));
        }
        holder.Time.setText(taskProvider.getTask_Scheduled_Date());
        holder.Asset_Name.setText(taskProvider.getAsset_Name());
        holder.Activity_Name.setText(taskProvider.getActivity_Name());
        holder.ENDTIME.setText(taskProvider.getTaskEndTime());
        holder.Asset_Location.setText(taskProvider.getAsset_Location());
        holder.User_Group.setText(taskProvider.getGroup_Name());
        //holder.Asset_Id.setText(taskProvider.getAsset_Id());
        holder.Asset_Code.setText(taskProvider.getAsset_Code());

    }

    @Override
    public int getItemCount() {
        return taskProviders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Time, Asset_Name, Activity_Name, Asset_Code, Asset_Id, ENDTIME, Asset_Location, User_Group;
        public ViewHolder(View itemView) {
            super(itemView);
            Time = (TextView)itemView.findViewById(R.id.tvTime);
            Asset_Name = (TextView)itemView.findViewById(R.id.tvAssetName);
            Activity_Name = (TextView)itemView.findViewById(R.id.tvActivity_name);
            ENDTIME = (TextView)itemView.findViewById(R.id.graceDuration2);
            Asset_Location = (TextView)itemView.findViewById(R.id.tvAssetLocation);
            User_Group = (TextView)itemView.findViewById(R.id.tvUserGroup);
            Asset_Code=(TextView)itemView.findViewById(R.id.tvAssetCode );
            Asset_Id=(TextView)itemView.findViewById(R.id.tvAsset_Id);
        }
    }
}
