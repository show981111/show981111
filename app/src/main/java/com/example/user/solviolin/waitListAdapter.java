package com.example.user.solviolin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class waitListAdapter extends RecyclerView.Adapter<waitListHolder> {

    private Context context;
    private ArrayList<waitlist_item> waitlistItems;

    public waitListAdapter(Context context, ArrayList<waitlist_item> arrayList) {
        this.context = context;
        this.waitlistItems = arrayList;
    }

    @NonNull
    @Override
    public waitListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView=inflater.inflate(R.layout.waitlist, parent,false);
        //recyclerView full width!!
//        View baseView = View.inflate(context,R.layout.waitlist,null);
//        waitListHolder waitListHolder = new waitListHolder(baseView);
        return new waitListHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull waitListHolder holder, int position) {

        waitlist_item waitlist_item = waitlistItems.get(position);

        holder.tv_wl_userID.setText(waitlist_item.getWl_userID());
        holder.tv_wl_userBranch.setText(waitlist_item.getWl_userBranch());
        holder.tv_wl_courseTeacher.setText(waitlist_item.getWl_courseTeacher());
        holder.tv_wl_startDate.setText(waitlist_item.getWl_startDate());
        holder.tv_wl_Time.setText(waitlist_item.getWl_Time());
    }

    @Override
    public int getItemCount() {
        return waitlistItems.size();
    }
}
