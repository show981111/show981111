package com.example.user.solviolin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookedListAdapter extends RecyclerView.Adapter<BookedListHolder> {

    private Context context;
    private ArrayList<BookedList> bookedListArrayList ;
    private String option = "";


    public BookedListAdapter(Context context, ArrayList<BookedList> arrayList) {
        this.context = context;
        this.bookedListArrayList = arrayList;

    }
    public BookedListAdapter(Context context, ArrayList<BookedList> arrayList,String option) {
        this.context = context;
        this.bookedListArrayList = arrayList;
        this.option = option;
    }

    @NonNull
    @Override
    public BookedListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(option.equals("changeList"))
        {
            rowView=inflater.inflate(R.layout.changelist, parent,false);
        }else{
            rowView=inflater.inflate(R.layout.bookedlist, parent,false);
        }
        BookedListHolder bookedListHolder = new BookedListHolder(rowView,this);


        return bookedListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookedListHolder holder, int position) {

        BookedList bookedList = bookedListArrayList.get(position);
        holder.bookedTeacher.setText(bookedList.getBookedTeacher());
        holder.bookedBranch.setText(bookedList.getBookedBranch());
        if(option.equals("changeList"))
        {

            if(bookedList.getBookedEndDate().equals(""))//보강이 잡히지 않은 수업들
            {
                holder.bookedStartDate.setText(bookedList.getBookedStartDate());
                holder.bookedEndDate.setText(bookedList.getBookedEndDate());
            }else{
                //보강이 잡힌 수업. changeFrom 이 있는 수업
                holder.bookedStartDate.setText(bookedList.getBookedEndDate());
                holder.bookedEndDate.setText(bookedList.getBookedStartDate());
                return;
            }
        }

        holder.bookedStartDate.setText(bookedList.getBookedStartDate());
        holder.bookedEndDate.setText(bookedList.getBookedEndDate());
    }

    @Override
    public int getItemCount() {
        return bookedListArrayList.size();
    }
}
