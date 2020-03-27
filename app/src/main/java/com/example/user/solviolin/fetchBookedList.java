package com.example.user.solviolin;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.user.solviolin.helper.MyButtonClickListener;
import com.example.user.solviolin.helper.mySwipeHelper;
import com.google.gson.Gson;

import static com.example.user.solviolin.MainActivity.userID;

public class fetchBookedList extends AsyncTask<String, Void, BookedList[]> {

    private Context context;
    private RecyclerView recyclerView;
    private static ArrayList<BookedList> cur_bookedListArrayList = new ArrayList<>();
    private static ArrayList<BookedList> past_bookedListArrayList = new ArrayList<>();
    private static ArrayList<BookedList> change_bookedListArrayList = new ArrayList<>();
    private String option;
    private BookedListAdapter bookedListAdapter;

    public fetchBookedList(Context context, RecyclerView recyclerView, String option) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.option = option;
    }

    @Override
    protected BookedList[] doInBackground(String... strings) {

        String url = strings[0];

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("userID", userID )
                .add("option", option)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.d("fetchBookedList", "res");
            Gson gson = new Gson();
            BookedList[] bookedLists = gson.fromJson(response.body().charStream(), BookedList[].class);
            return bookedLists;

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("fetchBookedList", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(BookedList[] bookedLists) {
        super.onPostExecute(bookedLists);
        if(option.equals("cur"))
        {
            cur_bookedListArrayList.clear();
            for(BookedList bookedListitem : bookedLists)
            {
                cur_bookedListArrayList.add(bookedListitem);
            }
            bookedListAdapter = new BookedListAdapter(context,cur_bookedListArrayList);
        }else if(option.equals("past")){
            past_bookedListArrayList.clear();
            for(BookedList bookedListitem : bookedLists)
            {
                past_bookedListArrayList.add(bookedListitem);
            }
            bookedListAdapter = new BookedListAdapter(context,past_bookedListArrayList);
        }else{
            change_bookedListArrayList.clear();
            for(BookedList bookedListitem : bookedLists)
            {
                change_bookedListArrayList.add(bookedListitem);
            }
            bookedListAdapter = new BookedListAdapter(context,change_bookedListArrayList,"changeList");
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(bookedListAdapter);

        if(option.equals("cur")) {
            final mySwipeHelper swipeHelper = new mySwipeHelper(context, recyclerView, 200) {
                @Override
                public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<mySwipeHelper.MyButton> buffer) {
                    buffer.add(new MyButton(context, "취소", 60, 0, Color.parseColor("#FF3C30"), new MyButtonClickListener() {
                        @Override
                        public void onClick(int pos) {
                            //Toast.makeText(context, "delete Click"+pos, Toast.LENGTH_SHORT).show();
                            Log.d("delte",cur_bookedListArrayList.get(pos).getBookedStartDate());
                            fetchCanceledListTask fetchCanceledListTask = new fetchCanceledListTask(context,pos,userID,cur_bookedListArrayList.get(pos).getBookedTeacher(),
                                    cur_bookedListArrayList.get(pos).getBookedBranch(), cur_bookedListArrayList.get(pos).getBookedStartDate(),
                                    cur_bookedListArrayList.get(pos).getBookedEndDate(),bookedListAdapter,cur_bookedListArrayList,"cancel_cur");
                            fetchCanceledListTask.execute("http://show981111.cafe24.com/getBookedList.php");

                        }
                    }));
                    buffer.add(new MyButton(context, "연장", 60, 0, Color.parseColor("#FF9502"), new MyButtonClickListener() {
                        @Override
                        public void onClick(int pos) {
                            Log.d("extend",cur_bookedListArrayList.get(pos).getBookedStartDate());
                            //Context context ,String userID, String extendTeacher, String extendBranch, String extendStartDate , String extendEndDate,ArrayList<BookedList> bookedLists, BookedListAdapter bookedListAdapter, int pos
                            extendRequestTask extendRequestTask = new extendRequestTask(context,userID,cur_bookedListArrayList.get(pos).getBookedTeacher(),
                                    cur_bookedListArrayList.get(pos).getBookedBranch(), cur_bookedListArrayList.get(pos).getBookedStartDate(),
                                    cur_bookedListArrayList.get(pos).getBookedEndDate(),cur_bookedListArrayList,bookedListAdapter,pos);
                            extendRequestTask.execute("http://show981111.cafe24.com/extendRequest.php");
                        }
                    }));
                }
            };

        }

    }
}
