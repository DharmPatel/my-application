package com.example.google.csmia_temp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ticketAdapter extends ArrayAdapter<ticketGetterSetter> {
    List<ticketGetterSetter> list = new ArrayList<ticketGetterSetter>();
    private LayoutInflater mInflater;
    public ticketAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    public class LayoutHandler
    {
        TextView DateTime,Subject,Type;
    }
    @Override
    public void add(ticketGetterSetter object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ticketGetterSetter getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ticketAdapter.LayoutHandler layoutHandler;
        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.ticketlist,parent,false);
            layoutHandler = new ticketAdapter.LayoutHandler();
            layoutHandler.DateTime = (TextView)row.findViewById(R.id.tvDate);
            layoutHandler.Subject=(TextView)row.findViewById(R.id.tvSubject);
            layoutHandler.Type=(TextView)row.findViewById(R.id.tvtype);
            //layoutHandler.ASSET_STATUS=(TextView)row.findViewById(R.id.tvAssetStatus);
            row.setTag(layoutHandler);
        }
        else
        {
            layoutHandler =(ticketAdapter.LayoutHandler)row.getTag();
        }
        ticketGetterSetter dataProvider = (ticketGetterSetter)this.getItem(position);
        int number = position+1;

        layoutHandler.DateTime.setText(dataProvider.getdatetime());
        layoutHandler.Subject.setText(dataProvider.getSubject());
        layoutHandler.Type.setText(dataProvider.gettype());
        //layoutHandler.ASSET_STATUS.setText(dataProvider.getStatus());

        return row;

    }

}
