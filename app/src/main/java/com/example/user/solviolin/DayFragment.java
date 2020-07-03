package com.example.user.solviolin;


import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.solviolin.Data.userData;
import com.example.user.solviolin.getData.FetchTermTask;
import com.example.user.solviolin.getData.fetchCanceledListTask;
import com.example.user.solviolin.getData.fetchTimeForDayTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.user.solviolin.MainActivity.userBranch;
import static com.example.user.solviolin.MainActivity.userDuration;
import static com.example.user.solviolin.MainActivity.userID;
import static com.example.user.solviolin.MainActivity.userName;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DayFragment newInstance(String param1, String param2) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    String send_userID = userID;
    String send_userDuration = userDuration ;
    String send_userBranch = userBranch ;

    class getUserDataTask extends AsyncTask<String, Void, userData[]> {

        private Context context;
        private String get_userID;
        private String get_userBranch;
        private ArrayList<userData> userDataArrayList = new ArrayList<>();
        Button button;

        private TextView tv_canceledTeacher;
        private TextView tv_canceledBranch;
        private TextView tv_canceledDate ;
        private Button bt_chooseDate;
        Boolean isnull;

        public getUserDataTask(Context context,String get_userID, String get_userBranch, Button button,TextView tv_canceledTeacher,TextView tv_canceledBranch,TextView tv_canceledDate,Button bt_chooseDate, Boolean isnull  ) {
            this.get_userID = get_userID;
            this.get_userBranch = get_userBranch;
            this.context = context;
            this.button = button;
            this.tv_canceledTeacher = tv_canceledTeacher;
            this.tv_canceledBranch = tv_canceledBranch;
            this.tv_canceledDate = tv_canceledDate;
            this.bt_chooseDate = bt_chooseDate;
            this.isnull = isnull;
        }

        @Override
        protected userData[] doInBackground(String... strings) {
            String url= strings[0];

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("userID", get_userID)
                    .add("userBranch", get_userBranch)
                    .build();
            //Log.d("getUserDataTask",get_userBranch);

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                //Log.d("getUserDataTask", "res");
                Gson gson = new Gson();
                userData[] userData = gson.fromJson(response.body().charStream(), userData[].class);
                return userData;

            } catch (IOException e) {
                e.printStackTrace();
                //Log.d("FetchUSer", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(userData[] userData) {
            super.onPostExecute(userData);
            userDataArrayList.clear();
            for(userData user : userData)
            {
                userDataArrayList.add(user);
            }
            //Log.d("getUserTasksize", String.valueOf(userDataArrayList.size()));
            if(userDataArrayList.size() == 1)
            {
                Toast.makeText(context,"매치되었습니다.",Toast.LENGTH_SHORT).show();
                send_userID = get_userID;
                send_userDuration = userDataArrayList.get(0).getUserDuration();
                send_userBranch = userDataArrayList.get(0).getUserBranch();
                if(!isnull) {
                    fetchCanceledListTask = new fetchCanceledListTask(getContext(), send_userID, tv_canceledTeacher, tv_canceledBranch, tv_canceledDate, "cancelAll", bt_chooseDate);
                    fetchCanceledListTask.execute("http://show981111.cafe24.com/getBookedList.php");
                }
                button.setEnabled(true);
            }else if(userDataArrayList.size() > 1)
            {
                Toast.makeText(context,"해당하는 유저가 여러명입니다.",Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(context,"해당하는 유저가 없습니다.",Toast.LENGTH_SHORT).show();
            }

        }
    }
    fetchCanceledListTask fetchCanceledListTask;
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.layout_adminDayBook);
        final EditText et_userID = getView().findViewById(R.id.et_adminDayUser);
        final EditText et_courseTeacher = getView().findViewById(R.id.et_adminTeacher);
        final EditText et_userBranch = getView().findViewById(R.id.et_adminDayBranch);
        Button bt_searchUser = getView().findViewById(R.id.bt_adminDaySearch);

        final TextView tv_canceledTeacher = (TextView) getView().findViewById(R.id.tv_canceledTeacher);
        final TextView tv_canceledBranch = (TextView) getView().findViewById(R.id.tv_canceledBranch);
        final TextView tv_canceledDate = (TextView) getView().findViewById(R.id.tv_canceledDate);
        final Button bt_chooseDate = getView().findViewById(R.id.chooseChangedDate);

        final CheckBox cb_isNull = getView().findViewById(R.id.cb_isNull);

        if(!userName.equals("admin"))
        {
            linearLayout.setVisibility(View.GONE);
        }
        bt_chooseDate.setEnabled(false);
        bt_searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserDataTask getUserDataTask = new getUserDataTask(getContext(),et_userID.getText().toString(),et_userBranch.getText().toString(),bt_chooseDate, tv_canceledTeacher,tv_canceledBranch,tv_canceledDate,bt_chooseDate,cb_isNull.isChecked());
                getUserDataTask.execute("http://show981111.cafe24.com/getUserData.php");
            }
        });


        final TextView tv_selectedNewDate = getView().findViewById(R.id.selectedNewDate);

        final GridView gv_chooseTime = getView().findViewById(R.id.changingTimeGridView);
        //fetchCanceledListTask(Context context, TextView tv_cancelTeacher, TextView tv_cancelBranch, TextView tv_cancelDate, String option)
        if(!userName.equals("admin")) {
            fetchCanceledListTask = new fetchCanceledListTask(getContext(), send_userID, tv_canceledTeacher, tv_canceledBranch, tv_canceledDate, "cancelAll", bt_chooseDate);
            fetchCanceledListTask.execute("http://show981111.cafe24.com/getBookedList.php");
        }

        /*get current Date*/
        final int curYear,curMonth,curDay;
        GregorianCalendar calendar = new GregorianCalendar();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH);
        curDay= calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String startYear = String.valueOf(year);
                String startMonth = String.valueOf(month + 1);
                String startDay = String.valueOf(dayOfMonth);

                String m,d;

                if(month < 9){

                    m = "0" + startMonth;

                }else
                {
                    m = String.valueOf(month + 1);
                }
                if(dayOfMonth < 10)
                {
                    d = "0" + startDay;
                }else
                {
                    d = String.valueOf(dayOfMonth);
                }
                String textDate =startYear+"년 "+startMonth + "월 "+ startDay+ "일";
                tv_selectedNewDate.setText(textDate);

                String selectedDate = startYear+"-"+m+"-"+d;
                String canceledDate;
                if(cb_isNull.isChecked())
                {
                    canceledDate = "admin";
                }else{
                    canceledDate =  fetchCanceledListTask.getCanceledDate();
                }
                if(userName.equals("admin")){
                    fetchTimeForDayTask fetchTimeForDayTask = new fetchTimeForDayTask(getContext(), DayFragment.this, gv_chooseTime, send_userID,send_userBranch, et_courseTeacher.getText().toString(),send_userDuration, selectedDate, canceledDate);
                    fetchTimeForDayTask.execute("http://show981111.cafe24.com/getTimeForDay.php");
                }else {
                    fetchTimeForDayTask fetchTimeForDayTask = new fetchTimeForDayTask(getContext(), DayFragment.this, gv_chooseTime, send_userID, send_userDuration, selectedDate, canceledDate);
                    //fetchTimeForDayTask(Context context, Fragment parent,GridView gridView, String userID, String courseBranch, String courseTeacher, String userDuration, String selectedDate,String canceledDate) {
                    fetchTimeForDayTask.execute("http://show981111.cafe24.com/getTimeForDay.php");
                }

            }

        },curYear,curMonth,curDay
        );

        FetchTermTask fetchTermTask = new FetchTermTask(datePickerDialog);
        fetchTermTask.execute("http://show981111.cafe24.com/getTermList.php");

        bt_chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
