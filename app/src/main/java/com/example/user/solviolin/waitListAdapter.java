package com.example.user.solviolin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class waitListAdapter extends RecyclerView.Adapter<waitListHolder> {

    private Context context;
    private ArrayList<waitlist_item> waitlistItems;
    private String pt_courseTeacher;
    private String pt_courseBranch;
    private String pt_userID;
    private String pt_startTime;
    private String pt_startDateAndDow;


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
        waitListHolder waitListHolder = new waitListHolder(rowView,this);
        //recyclerView full width!!
//        View baseView = View.inflate(context,R.layout.waitlist,null);
//        waitListHolder waitListHolder = new waitListHolder(baseView);
        return waitListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull waitListHolder holder, int position) {

        waitlist_item waitlist_item = waitlistItems.get(position);

        holder.tv_wl_userID.setText(waitlist_item.getWl_userID());
        holder.tv_wl_userBranch.setText(waitlist_item.getWl_userBranch());
        holder.tv_wl_courseTeacher.setText(waitlist_item.getWl_courseTeacher());
        holder.tv_wl_startDate.setText(waitlist_item.getWl_startDate());
        holder.tv_wl_Time.setText(waitlist_item.getWl_Time());

//        pt_courseTeacher = waitlist_item.getWl_courseTeacher();
//        pt_courseBranch = waitlist_item.getWl_userBranch();
//        pt_userID = waitlist_item.getWl_userID();
//        pt_startTime = waitlist_item.getWl_Time() ;
//        pt_startDateAndDow = waitlist_item.getWl_startDate();
        Log.d("postWaitListPosi", String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return waitlistItems.size();
    }

    public void onAcceptClicked(int position) {

        waitlist_item waitlist_item = waitlistItems.get(position);
        pt_courseTeacher = waitlist_item.getWl_courseTeacher();
        pt_courseBranch = waitlist_item.getWl_userBranch();
        pt_userID = waitlist_item.getWl_userID();
        pt_startTime = waitlist_item.getWl_Time() ;
        pt_startDateAndDow = waitlist_item.getWl_startDate();

        acceptRegularTask acceptRegularTask = new acceptRegularTask(context,pt_courseTeacher, pt_courseBranch,pt_userID, pt_startTime, pt_startDateAndDow,"accept",this,position,waitlistItems);
        acceptRegularTask.execute("http://show981111.cafe24.com/acceptRegular.php");
        notifyItemRemoved(position);

    }

    public void onRejectClicked(int position) {
        waitlist_item waitlist_item = waitlistItems.get(position);
        pt_courseTeacher = waitlist_item.getWl_courseTeacher();
        pt_courseBranch = waitlist_item.getWl_userBranch();
        pt_userID = waitlist_item.getWl_userID();
        pt_startTime = waitlist_item.getWl_Time() ;
        pt_startDateAndDow = waitlist_item.getWl_startDate();

        acceptRegularTask acceptRegularTask = new acceptRegularTask(context,pt_courseTeacher, pt_courseBranch,pt_userID, pt_startTime, pt_startDateAndDow,"reject",this,position,waitlistItems);
        acceptRegularTask.execute("http://show981111.cafe24.com/acceptRegular.php");
        notifyItemRemoved(position);

    }
}
