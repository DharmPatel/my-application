package com.example.google.csmia_temp.Helpdesk.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.google.csmia_temp.DatabaseHelper;
import com.example.google.csmia_temp.Helpdesk.Model.HkTicket;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket;
import com.example.google.csmia_temp.R;

import java.util.ArrayList;

public class HelpdeskTicketAdapter extends RecyclerView.Adapter<HelpdeskTicketAdapter.HelpdeskHolder> {
    Context context;
    public final String MY_PREFS_NAME1 = "CheckTaskInsert";
    //    private final View.OnClickListener onClickListener = (View.OnClickListener) new MyOnClicklistener();
    DatabaseHelper databaseHelper;
    ArrayList<HkTicket> hkticketArrayList;

    public HelpdeskTicketAdapter(Context context, ArrayList<HkTicket> HkticketArrayList) {
        this.context = context;
        hkticketArrayList = HkticketArrayList;

    }

    @Override
    public HelpdeskHolder onCreateViewHolder(ViewGroup parent, int resource) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newticket, parent, false);
        final HelpdeskHolder helpdeskHolder = new HelpdeskHolder(view);
        return helpdeskHolder;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onBindViewHolder(HelpdeskHolder holder, int position) {
        String building,floor,room, location = null;
        HkTicket hkticket = hkticketArrayList.get(position);
        for (int i=0;i<hkticketArrayList.size();i++){
            building =hkticket.getBuildingName();
            floor=hkticket.getFloorName();
            room=hkticket.getRoomArea() ;

            /*if(building==null){
                building="";
            }if (floor==null || floor.equalsIgnoreCase(null) ){
                floor="";
            } if (room==null){
                room="";
            }*/
            location = " " + building + " " + floor + " " + room+ " " ;
            Log.d("Room Id : "+hkticketArrayList.get(i).getRoomId(),"id :" +i +"Location"+location);
            if (hkticket.getBuildingName()==null) {
                holder.tv_loc_name.setText("No Location Available ");

            }else {
                holder.tv_loc_name.setText(location);
            }
        }

        holder.tv_ticket_code.setText(hkticketArrayList.get(position).getTicketCode());
        holder.tv_status.setText(hkticket.getStatus());
        holder.tv_product_nm.setText(hkticket.getSubCategoryName());
        holder.tv_product_title.setText(hkticket.getIssues());
        holder.tv_createAt.setText(hkticket.getCreatedAt());
        holder.tv_dept_name.setText(hkticket.getLevel());
        holder.tv_loc_name.setText(location);

    }

    @Override
    public int getItemCount() {
        return hkticketArrayList.size();
    }

    public class HelpdeskHolder extends RecyclerView.ViewHolder {
        TextView tv_ticket_code, tv_status, tv_product_nm, tv_createAt, tv_product_title, tv_dept_name,tv_loc_name;

        public HelpdeskHolder(View itemView) {
            super(itemView);
            tv_ticket_code = itemView.findViewById(R.id.tv_ticket_code);
            tv_status=itemView.findViewById(R.id.tvStatus);
            tv_product_nm = itemView.findViewById(R.id.tv_product_name);
            tv_createAt = itemView.findViewById(R.id.tv_createdAt);
            tv_product_title = itemView.findViewById(R.id.tv_problem_title);
            tv_dept_name = itemView.findViewById(R.id.tvDeptNm);
            tv_loc_name=itemView.findViewById(R.id.tv_loc_name);
            }
    }

}
