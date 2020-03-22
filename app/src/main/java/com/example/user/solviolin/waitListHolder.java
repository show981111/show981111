package com.example.user.solviolin;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class waitListHolder extends RecyclerView.ViewHolder {
    public TextView tv_wl_userID;
    public TextView tv_wl_userBranch;
    public TextView tv_wl_courseTeacher;
    public TextView tv_wl_startDate;
    public TextView tv_wl_Time;

    public Button bt_accept;
    public Button bt_reject;


    public waitListHolder(@NonNull View itemView) {
        super(itemView);

        tv_wl_userID =itemView.findViewById(R.id.wl_userID);
        tv_wl_userBranch =itemView.findViewById(R.id.wl_userBranch);
        tv_wl_courseTeacher =itemView.findViewById(R.id.wl_courseTeacher);
        tv_wl_startDate =itemView.findViewById(R.id.wl_startDate);
        tv_wl_Time =itemView.findViewById(R.id.wl_Time);

        bt_accept = itemView.findViewById(R.id.bt_accept);
        bt_reject = itemView.findViewById(R.id.bt_reject);
    }
}
