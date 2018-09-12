package com.example.google.template;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MegaVision01 on 10/6/2016.
 */
public class AlertDataAdapter extends ArrayAdapter<AlertDataProvider> {
    List<AlertDataProvider> list = new ArrayList<AlertDataProvider>();
    private LayoutInflater mInflater;
    public AlertDataAdapter(Context context, int resource) {
        super(context, resource);
    }

    public class LayoutHandler
    {
        TextView ASSET_NAME,DATETIME,ALET_TYPE,ACTIVITY_NAME,TASK_SCHEDULE_DATE,UPDATED_STATUS;
    }
    @Override
    public void add(AlertDataProvider object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AlertDataProvider getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutHandler layoutHandler;
        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.alert_list_item,parent,false);
            layoutHandler = new LayoutHandler();
            layoutHandler.ASSET_NAME=(TextView)row.findViewById(R.id.tvAssetName);
            layoutHandler.DATETIME=(TextView)row.findViewById(R.id.graceDuration2);
            layoutHandler.ALET_TYPE=(TextView)row.findViewById(R.id.tvAssetLocation);
            layoutHandler.ACTIVITY_NAME=(TextView)row.findViewById(R.id.tvActivity_name);
            layoutHandler.TASK_SCHEDULE_DATE=(TextView)row.findViewById(R.id.tvTime);
            layoutHandler.UPDATED_STATUS=(TextView)row.findViewById(R.id.tvUpdateStatus);
            row.setTag(layoutHandler);
        }
        else
        {
            layoutHandler =(LayoutHandler)row.getTag();
        }
        AlertDataProvider dataProvider = (AlertDataProvider)this.getItem(position);
        layoutHandler.UPDATED_STATUS.setText(dataProvider.getUpdatedStatus());
        if(dataProvider.getUpdatedStatus() == null)
        {
            row.setBackgroundColor(Color.TRANSPARENT);
        }
        else if(dataProvider.getUpdatedStatus().trim().equals("yes"))
        {
            row.setBackgroundColor(Color.parseColor("#76D7C4"));
        }
        else
        {
            row.setBackgroundColor(Color.TRANSPARENT);
        }
        if(dataProvider.getViewFlag().trim().equals("yes"))
        {
            row.setBackgroundColor(Color.WHITE);
        }
        layoutHandler.ASSET_NAME.setText(dataProvider.getAsset_Name());
        layoutHandler.DATETIME.setText(dataProvider.getTask_Start_At());
        layoutHandler.ALET_TYPE.setText(dataProvider.getAlertType());
        layoutHandler.ACTIVITY_NAME.setText(dataProvider.getActivity_Name());
        layoutHandler.TASK_SCHEDULE_DATE.setText(dataProvider.getSchedule_Date());

        return row;

    }

}


