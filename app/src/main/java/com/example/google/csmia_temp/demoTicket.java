package com.example.google.csmia_temp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.google.csmia_temp.Helpdesk.Activity.NewTicketActivity;

public class demoTicket extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter__ticket, container, false);
        Intent intent = new Intent(getActivity(), NewTicketActivity.class);
        startActivity(intent);
        return view;
    }
}
