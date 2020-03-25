package com.example.user.solviolin;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookedListHolder extends RecyclerView.ViewHolder{
    public TextView bookedTeacher;
    public TextView bookedBranch;
    public TextView bookedStartDate;
    public TextView bookedEndDate;

    private BookedListAdapter BookedListAdapter;

    public BookedListHolder(@NonNull View itemView, BookedListAdapter BookedListAdapter) {
        super(itemView);
        this.BookedListAdapter = BookedListAdapter;


        this.bookedTeacher = itemView.findViewById(R.id.tv_bookedTeacher);
        this.bookedBranch = itemView.findViewById(R.id.tv_bookedBranch);
        this.bookedStartDate = itemView.findViewById(R.id.tv_bookedStartDate);
        this.bookedEndDate = itemView.findViewById(R.id.tv_bookedEndDate);
    }
}
