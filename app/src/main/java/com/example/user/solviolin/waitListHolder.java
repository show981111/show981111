package com.example.user.solviolin;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class waitListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView tv_wl_userID;
    public TextView tv_wl_userBranch;
    public TextView tv_wl_courseTeacher;
    public TextView tv_wl_startDate;
    public TextView tv_wl_Time;

    public Button bt_accept;
    public Button bt_reject;

    private waitListAdapter waitListAdapter;


    public waitListHolder(@NonNull View itemView, waitListAdapter waitListAdapter) {
        super(itemView);
        this.waitListAdapter = waitListAdapter;
        tv_wl_userID =itemView.findViewById(R.id.wl_userID);
        tv_wl_userBranch =itemView.findViewById(R.id.wl_userBranch);
        tv_wl_courseTeacher =itemView.findViewById(R.id.wl_courseTeacher);
        tv_wl_startDate =itemView.findViewById(R.id.wl_startDate);
        tv_wl_Time =itemView.findViewById(R.id.wl_Time);

        bt_accept = itemView.findViewById(R.id.bt_accept);
        bt_reject = itemView.findViewById(R.id.bt_reject);

        bt_accept.setOnClickListener(this);
        bt_reject.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int position = getAdapterPosition();
        Log.d("postWaitListPos", String.valueOf(position));

        switch (view.getId()){
            case R.id.bt_accept:
                waitListAdapter.onAcceptClicked(position);
                break;
            case R.id.bt_reject:
                waitListAdapter.onRejectClicked(position);
                break;
        }

    }
}
