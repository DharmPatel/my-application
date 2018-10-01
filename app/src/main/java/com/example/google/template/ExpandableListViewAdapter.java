package com.example.google.template;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listTitle;
    private Map<String,List<String>> listItems;
    private ArrayList<ArrayList<Boolean>> checkboxStatus = new ArrayList<ArrayList<Boolean>>();
    TextView textViewChild;
    CheckBox checkBox;
    Boolean [] CheckSelected;

    public ExpandableListViewAdapter(Context context, List<String> listTitle, Map<String, List<String>> listItems) {
        this.context = context;
        this.listTitle = listTitle;
        this.listItems = listItems;

        /*for (int i = 0; i<listTitle.size(); i++){
            ArrayList<Boolean> childStatus = new ArrayList<Boolean>();
            for (int j = 0; j <listItems.get(listTitle.get(i)).size(); j++){
                childStatus.add(false);
            }
            checkboxStatus.add(childStatus);
        }*/
        //CheckSelected = new boolean[listItems.get(listTitle.get(groupPosition)).size()];
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
         textViewChild = (TextView)convertView.findViewById(R.id.listItem);
        textViewChild.setText(Child);


        //Log.d("sdfsdf","1: "+checkboxStatus+"");

        checkBox = (CheckBox)convertView.findViewById(R.id.CheckedChild);
        //Log.d("sdfsdf","2: "+checkboxStatus.get(groupPosition).get(childPosition)+"");
       /* checkBox.setChecked(checkboxStatus.get(groupPosition).get(childPosition));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkboxStatus.get(groupPosition).set(childPosition,isChecked);
                    Log.d("sdfsdf","3: "+textViewChild.getText().toString()+"");
                    Log.d("sdfsdf","4: "+checkboxStatus.get(groupPosition).get(childPosition)+" "+groupPosition);

            }
        });*/


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
