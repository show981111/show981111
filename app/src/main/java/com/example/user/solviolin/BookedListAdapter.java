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


    public BookedListAdapter(Context context, ArrayList<BookedList> arrayList) {
        this.context = context;
        this.bookedListArrayList = arrayList;

    }

    @NonNull
    @Override
    public BookedListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView=inflater.inflate(R.layout.bookedlist, parent,false);
        BookedListHolder bookedListHolder = new BookedListHolder(rowView,this);

//        View rowView;
//        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        rowView=inflater.inflate(R.layout.waitlist, parent,false);
//        waitListHolder waitListHolder = new waitListHolder(rowView,this);
        return bookedListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookedListHolder holder, int position) {
        Log.d("bookedListArrayADap",String.valueOf(bookedListArrayList.size())+"notjin?");
        Log.d("bookedListArraypos",position+"notjin?");
        BookedList bookedList = bookedListArrayList.get(position);
        Log.d("bookedListArrayADap",bookedList.getBookedTeacher()+"notjin?");
        holder.bookedTeacher.setText(bookedList.getBookedTeacher());
        holder.bookedBranch.setText(bookedList.getBookedBranch());
        holder.bookedStartDate.setText(bookedList.getBookedStartDate());
        holder.bookedEndDate.setText(bookedList.getBookedEndDate());
    }

    @Override
    public int getItemCount() {
        return bookedListArrayList.size();
    }
}
