package com.example.google.csmia_temp;

/**
 * Created by Intel on 10-11-2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by MegaVision01 on 10/6/2016.
 */
public class TaskDataAdapter extends ArrayAdapter<TaskProvider> {
    List<TaskProvider> list = new ArrayList<TaskProvider>();
    private LayoutInflater mInflater;
    public TaskDataAdapter(Context context, int resource) {
        super(context, resource);
    }

    public class LayoutHandler
    {
        TextView TIME,ASSET_NAME,USER_GROUP,ACTIVITY_NAME,ASSET_CODE,ASSET_ID,UPDATESTATUS,ASSET_LOCATION,STARTTIME,ENDTIME,TASKSTATUS;
    }
    @Override
    public void add(TaskProvider object) {
        super.add(object);
        list.add(object);
    }

    public static Comparator<String> StringAscComparator = new Comparator<String>() {

        public int compare(String app1, String app2) {

            String stringName1 = app1;
            String stringName2 = app2;

            return stringName1.compareToIgnoreCase(stringName2);
        }
    };
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public TaskProvider getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutHandler layoutHandler;
        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_item,parent,false);
            layoutHandler = new LayoutHandler();
            layoutHandler.TIME=(TextView)row.findViewById(R.id.tvTime);
            layoutHandler.ASSET_NAME=(TextView)row.findViewById(R.id.tvAssetName);
            layoutHandler.ACTIVITY_NAME=(TextView)row.findViewById(R.id.tvActivity_name );
            layoutHandler.ASSET_CODE=(TextView)row.findViewById(R.id.tvAssetCode );
            layoutHandler.ASSET_ID=(TextView)row.findViewById(R.id.tvAsset_Id);
            layoutHandler.UPDATESTATUS=(TextView)row.findViewById(R.id.tvUpdateStatus);
            layoutHandler.ASSET_LOCATION=(TextView)row.findViewById(R.id.tvAssetLocation);
            layoutHandler.USER_GROUP=(TextView)row.findViewById(R.id.tvUserGroup);
            //layoutHandler.STARTTIME=(TextView)row.findViewById(R.id.graceDuration);
            layoutHandler.ENDTIME=(TextView)row.findViewById(R.id.graceDuration2);
            layoutHandler.TASKSTATUS=(TextView)row.findViewById(R.id.tvTaskType);
            row.setTag(layoutHandler);
        }
        else
        {
            layoutHandler =(LayoutHandler)row.getTag();
        }
        try {
            TaskProvider taskProvider = (TaskProvider)this.getItem(position);
            SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            layoutHandler.UPDATESTATUS.setText(taskProvider.getUpdateStatus());
            if(taskProvider.getUpdateStatus() == null)
            {
                row.setBackgroundColor(Color.TRANSPARENT);

            }
            else if(taskProvider.getUpdateStatus().equals("no"))
            {
                row.setBackgroundColor(Color.TRANSPARENT);

            }
            else
            {
                row.setBackgroundColor(Color.parseColor("#76D7C4"));
            }
            Log.d("fasfdasfdasf",""+taskProvider.getGroup_Name());
            layoutHandler.TIME.setText(taskProvider.getStartDateTime());
            layoutHandler.ASSET_NAME.setText(taskProvider.getAsset_Name());
            layoutHandler.ACTIVITY_NAME.setText(taskProvider.getActivity_Name());
            layoutHandler.ASSET_CODE.setText(taskProvider.getAsset_Code());
            layoutHandler.ASSET_ID.setText(taskProvider.getAsset_Id());
            layoutHandler.ASSET_LOCATION.setText(taskProvider.getAsset_Location());
            layoutHandler.ENDTIME.setText(taskProvider.getEndDateTime());
            layoutHandler.USER_GROUP.setText(taskProvider.getGroup_Name());
            // layoutHandler.TASKSTATUS.setText(taskProvider.getTaskStatus());

        }catch (Exception e){
            e.printStackTrace();
        }
        return row;

    }
}


