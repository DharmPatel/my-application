package com.example.google.template;

/**
 * Created by Intel on 10-11-2016.
 */

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
public class ListDataAdapter extends ArrayAdapter<DataProvider> {
    List<DataProvider> list = new ArrayList<DataProvider>();
    private LayoutInflater mInflater;
    public ListDataAdapter(Context context, int resource) {
        super(context, resource);
    }

    public class LayoutHandler
    {
        TextView ASSET_ID,ASSET_NAME,ASSET_LOCATION,ASSET_STATUS, Sr_No;
    }
    @Override
    public void add(DataProvider object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DataProvider getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutHandler layoutHandler;
        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.asset_list_item,parent,false);
            layoutHandler = new LayoutHandler();
            layoutHandler.Sr_No = (TextView)row.findViewById(R.id.Sr_No);
            layoutHandler.ASSET_NAME=(TextView)row.findViewById(R.id.textName);
            layoutHandler.ASSET_ID=(TextView)row.findViewById(R.id.textId);
            layoutHandler.ASSET_LOCATION=(TextView)row.findViewById(R.id.tvAssetLocation);
            //layoutHandler.ASSET_STATUS=(TextView)row.findViewById(R.id.tvAssetStatus);
            row.setTag(layoutHandler);
        }
        else
        {
            layoutHandler =(LayoutHandler)row.getTag();
        }
        DataProvider dataProvider = (DataProvider)this.getItem(position);
        int number = position+1;
        layoutHandler.Sr_No.setText(number+"");
        layoutHandler.ASSET_ID.setText(dataProvider.getAsset_Code());
        layoutHandler.ASSET_NAME.setText(dataProvider.getAsset_Name());
        layoutHandler.ASSET_LOCATION.setText(dataProvider.getAsset_Location());
        //layoutHandler.ASSET_STATUS.setText(dataProvider.getStatus());
        if(dataProvider.getColor()!= null) {
            row.setBackgroundColor(Color.parseColor(dataProvider.getColor()));
        }

        /*try {
            if(dataProvider.getStatus().equals(" "))
            {
                row.setBackgroundColor(Color.TRANSPARENT);

            }
            else if(dataProvider.getStatus().equals("WORKING"))
            {
                row.setBackgroundColor(Color.parseColor("#82E0AA"));

            }
            else if(dataProvider.getStatus().equals("STAND BY"))
            {
                row.setBackgroundColor(Color.parseColor("#F9E79F"));

            }
            else if(dataProvider.getStatus().equals("MAINTENANCE"))
            {
                row.setBackgroundColor(Color.parseColor("#85C1E9"));

            }
            else if(dataProvider.getStatus().equals("NOT WORKING"))
            {
                row.setBackgroundColor(Color.parseColor("#D98880"));

            }
            else
            {
                row.setBackgroundColor(Color.parseColor("#76D7C4"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
        return row;

    }
   
}


