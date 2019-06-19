package com.example.google.csmia_temp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listTitle;
    private Map<String,List<String>> listItems;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    String value, User_Id, refreshVal;
    int groupVal, childVal;
    AssetsActivity assetsActivity = new AssetsActivity();
    SharedPreferences settings;
    List<String> LocationList = new ArrayList<String>();
    List<String> TypeList = new ArrayList<String>();

    public ExpandableListViewAdapter(Context context, List<String> listTitle, Map<String, List<String>> listItems) {
        this.context = context;
        this.listTitle = listTitle;
        this.listItems = listItems;
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
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        groupVal = groupPosition;
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
        childVal = childPosition;
        String Child = (String)getChild(groupPosition,childPosition);
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_group_child,null);
        }
        final CheckedTextView checkedTextView = (CheckedTextView)convertView.findViewById(R.id.checkedTextView);
        checkedTextView.setText(Child);
        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckedTextView)v).isChecked();
                if (checked){
                    checkedTextView.setCheckMarkDrawable(null);
                    checkedTextView.setChecked(false);
                    setValue(null);
                    Log.d("checkedval","1: "+checked+" "+checkedTextView.getText().toString());
                }else {
                    checkedTextView.setCheckMarkDrawable(R.drawable.checked);
                    checkedTextView.setChecked(true);
                    setValue(checkedTextView.getText().toString());
                    Log.d("checkedval","2: "+checked+" "+checkedTextView.getText().toString()+"  "+groupVal);
                    StringBuilder stringBuilder = new StringBuilder();;
                    stringBuilder.append(checkedTextView.getText().toString()+",");
                    Log.d("fbgkndfgmd",stringBuilder.toString());
                }
                Log.d("checkedval","3: "+checked+" "+checkedTextView.getText().toString());
                myDb = new DatabaseHelper(context);
                db = myDb.getReadableDatabase();
                settings = PreferenceManager.getDefaultSharedPreferences(context);
                User_Id = settings.getString("userId", null);
                LocationList = myDb.getAssetLocation((myDb.UserGroupId(User_Id)));
                TypeList = myDb.getAssetType((myDb.UserGroupId(User_Id)));
                /*if (groupPosition == 0){
                    List<String> UpdatedTypeList = new ArrayList<String>();
                    UpdatedTypeList = myDb.assetLocation(myDb.UserGroupId(User_Id),getValue());
                    Log.d("ChildList1",UpdatedTypeList+"");
                    assetsActivity.updateMenuData(LocationList,UpdatedTypeList);

                }else {

                    //myDb.assetType(User_Group_Id,CheckedValue);
                    List<String> UpdatedLocationList = new ArrayList<String>();
                    UpdatedLocationList = myDb.assetType(myDb.UserGroupId(User_Id),getValue());
                    Log.d("ChildList2",UpdatedLocationList+"");
                    assetsActivity.updateMenuData(UpdatedLocationList,TypeList);
                }*/
            }
        });

        return convertView;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public int getChildTypeCount() {
        return listItems.size();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
