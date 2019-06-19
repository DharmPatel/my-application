package com.example.google.csmia_temp;

import android.content.Context;
import android.os.Handler;
//import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chandan on 11/24/2017.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<IncidentTaskProvider> taskProviders;
    private Context context;
    Handler mHandler;


    public MyAdapter(List<IncidentTaskProvider> taskProviders, Context context) {
        this.taskProviders = taskProviders;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_recycler,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final IncidentTaskProvider taskProvider = taskProviders.get(position);

        holder.IncidentName.setText(taskProvider.getIncidentName());
        holder.IncidentDateTime.setText(taskProvider.getIncidentLocation());
        holder.Activity.setText(taskProvider.getActivity());
        holder.Asset.setText(taskProvider.getAsset());
        /*if(taskProvider.getUpdatedStatus() == null)
        {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
        else if(taskProvider.getUpdatedStatus().equals("no"))
        {
            holder.itemView.setBackgroundColor(Color.WHITE);

        }
        else
        {
            //holder.itemView.setBackgroundColor(Color.parseColor("#76D7C4"));
            holder.cardView.setBackgroundColor(Color.parseColor("#ECEFF1"));

        }*/

    }

    @Override
    public int getItemCount() {
        return taskProviders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView IncidentName,IncidentDateTime,Activity,Asset;
      /*  public CardView cardView;*/
        public ViewHolder(View itemView) {
            super(itemView);
            IncidentName= (TextView) itemView.findViewById(R.id.tvIncidentName);
            IncidentDateTime= (TextView) itemView.findViewById(R.id.tvIncidentDate);
            Activity= (TextView) itemView.findViewById(R.id.tvActivity);
            Asset= (TextView) itemView.findViewById(R.id.tvAsset);
         //   cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }
}

