package com.example.google.template;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listTitle;
    private Map<String,List<String>> listItems;
    ArrayList<Boolean> positionArray;
    SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    public ExpandableListViewAdapter(Context context, List<String> listTitle, Map<String, List<String>> listItems) {
        this.context = context;
        this.listTitle = listTitle;
        this.listItems = listItems;

        positionArray = new ArrayList<Boolean>(listItems.size());
        for(int i =0;i<listItems.size();i++){
            positionArray.add(false);
        }

    }

    @Override
    public int getGroupCount() {
        return listTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listItems.get(listTitle.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listItems.get(listTitle.get(groupPosition)).get(childPosition);
    }

    class ExpanableValue {
        String title;
        boolean isChecked;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String Title = (String)getGroup(groupPosition);
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_group_header,null);
        }
        TextView textViewTitle = (TextView)convertView.findViewById(R.id.headerTextview);
        textViewTitle.setText(Title);


        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {

        String Child = (String)getChild(groupPosition,childPosition);
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_group_child,null);
        }
        final TextView textViewChild = (TextView)convertView.findViewById(R.id.listItem);
        textViewChild.setText(Child);
        final CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.CheckedChild);
        checkBox.setTag(groupPosition);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (Integer)buttonView.getTag();
                Log.d("Jfnkjsdfkjsd",position+"");
                positionArray.add(position,isChecked);

            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
