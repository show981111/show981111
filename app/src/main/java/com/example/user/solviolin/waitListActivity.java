package com.example.user.solviolin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class waitListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wait_list);
        RecyclerView rv_waitList = findViewById(R.id.rv_waitList);
        fetchWaitList fetchWaitList = new fetchWaitList(this,rv_waitList);
        fetchWaitList.execute("http://show981111.cafe24.com/getWaitList.php");



    }

    class fetchWaitList extends AsyncTask<String, Void, waitlist_item[]>
    {
        private Context context;
        private waitListAdapter waitListAdapterinFetch;
        private RecyclerView rv_waitList;

        private ArrayList<waitlist_item> waitListItems = new ArrayList<>();
        public fetchWaitList(Context context, RecyclerView rv_waitList) {
            this.context = context;
            this.rv_waitList = rv_waitList;
        }

        @Override
        protected waitlist_item[] doInBackground(String... strings) {
            String url= strings[0];

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                Log.d("waitLIstFetch", "try");
                Gson gson = new Gson();
                waitlist_item[] waitlist_item = gson.fromJson(response.body().charStream(), waitlist_item[].class);
                return waitlist_item;

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("waitLIstFetch", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(waitlist_item[] waitlist_items) {
            super.onPostExecute(waitlist_items);
            waitListItems.clear();

            for(waitlist_item waitlist_item : waitlist_items)
            {
                waitListItems.add(waitlist_item);
                Log.d("waitlist",waitlist_item.getWl_userID());
            }

            Log.d("waitlistSize", String.valueOf(waitListItems.size()));

            waitListAdapterinFetch = new waitListAdapter(context,waitListItems);
            Log.d("waitlistA", String.valueOf(waitListAdapterinFetch.getItemCount()));
            rv_waitList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
            rv_waitList.setAdapter(waitListAdapterinFetch);

        }
    }
}
