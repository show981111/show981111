package com.example.user.solviolin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.user.solviolin.MainActivity.userBranch;
import static com.example.user.solviolin.LoginActivity.InputURL;

import static com.example.user.solviolin.mMySQL.DataParser.courseID;
import static com.example.user.solviolin.mMySQL.DataParser.courseTeacher;
import static com.example.user.solviolin.mMySQL.DataParser.courseDay;
import static com.example.user.solviolin.mMySQL.DataParser1.bookedCourseIDList_G;
import static com.example.user.solviolin.mMySQL.DataParser1.bookedCourseIDList_J;
import static com.example.user.solviolin.mMySQL.DataParser1.bookedCourseIDList_K;
import static com.example.user.solviolin.mMySQL.DataParser1.bookedCourseIDList_Y;
import static com.example.user.solviolin.mMySQL.DataParser1.bookedCourseIDList_S;



import static com.example.user.solviolin.mMySQL.DataParser.courseTime;
import static com.example.user.solviolin.mMySQL.DataParser.courseBranch;




import com.example.user.solviolin.mMySQL.Downloader;
import com.example.user.solviolin.mMySQL.Downloader1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MonthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthFragment newInstance(String param1, String param2) {
        MonthFragment fragment = new MonthFragment();
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

    private  Spinner teacherSpinner;
    private Spinner daySpinner;




   /* final static String urlAddress_J = "http://show981111.cafe24.com/CourseList_J.php";
    final static String urlAddress_Y = "http://show981111.cafe24.com/CourseList_Y.php";
    final static String urlAddress_S = "http://show981111.cafe24.com/CourseList_S.php";
    final static String urlAddress_K = "http://show981111.cafe24.com/CourseList_K.php";*/

    // new Downloader1(MainActivity.this, "http://show981111.cafe24.com/BookedCourseList.php").execute();



   static String urlAddressForCourseList = "http://show981111.cafe24.com/BookedCourseList.php";


    public static ArrayList<Integer> Timeindex =new ArrayList<>(); // 시간대를 뽑아낼 수 있는 인덱스
    public static String selectedTeacher; //선택된 선생님
    public static String selectedDay; //선택된 날짜(데이터 보낼 목적)
    public static int dow;
    public static String startDate;

    public void onActivityCreated(Bundle b){
        super.onActivityCreated(b);
        switch (userBranch) {
            case "잠실":
                urlAddressForCourseList = "http://show981111.cafe24.com/BookedCourseList_J.php";
                break;
            case "여의도":
                urlAddressForCourseList = "http://show981111.cafe24.com/BookedCourseList_Y.php";
                break;
            case "시청":
                urlAddressForCourseList = "http://show981111.cafe24.com/BookedCourseList_S.php";
                break;
            case "교대":
                urlAddressForCourseList = "http://show981111.cafe24.com/BookedCourseList_K.php";
                break;
            case "광화문":
                urlAddressForCourseList = "http://show981111.cafe24.com/BookedCourseList_G.php";
                break;
        }




        daySpinner = (Spinner) getView().findViewById(R.id.courseDaySpinner);
        final ArrayList<String> dayList = new ArrayList<>();
        final ArrayList<String> dayListForShow = new ArrayList<>();
        final ArrayList<Integer> index =new ArrayList<>(); // 강사기준으로 분류하는 인덱스
        //final ArrayList<Integer> Timeindex =new ArrayList<>(); // 시간대를 뽑아낼 수 있는 인덱스
        final ArrayList<String> courseTimeList = new ArrayList<>(); //최종 모든 선택 기준에 알맞는 시간대




        //라디오그룹이랑 스피너 어댑터 접촉
        teacherSpinner = (Spinner) getView().findViewById(R.id.courseTeacherSpinner);
        final GridView timeButtonGrid = (GridView) getView().findViewById(R.id.ButtonGridView);
        final Button searchButton = (Button) getView().findViewById(R.id.searchButton);
        searchButton.setEnabled(false);
        timeButtonGrid.setVisibility(View.GONE);
        new Downloader(getContext(),InputURL,teacherSpinner).execute();

        new Downloader1(getContext(), urlAddressForCourseList).execute();

        final Button startDateButton = (Button) getView().findViewById(R.id.startDateButton);
        final TextView startDateTextView = (TextView) getView().findViewById(R.id.startDateTextView);




        //new Downloader(getContext(),InputURL,teacherSpinner).execute();
        /*positionindex.clear();teacherList.clear();courseDayList.clear();courseTimeList.clear();
        int counting = 0;
        int s = courseTeacher.size();
        while(counting < s)
        {

            if (courseBranch.get(counting).equals(userBranch.toString())) {
                positionindex.add(counting); }

            counting++;
        }
        for(int i = 0; i < positionindex.size();i++) //포지션인덱스는 지점별로 나누는 인덱스
        {
            teacherList.add(courseTeacher.get(positionindex.get(i)));
            courseDayList.add(courseDay.get(positionindex.get(i)));
            courseTimeList.add(courseTime.get(positionindex.get(i)));
        }*/

        final int curYear,curMonth,curDay;
        GregorianCalendar calendar = new GregorianCalendar();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH);
        curDay= calendar.get(Calendar.DAY_OF_MONTH);

        final String[] startYear = new String[1];
        final String[] startMonth = new String[1];
        final String[] sm = new String[1];
        final String[] sd = new String[1];
        final String[] startDay = new String[1];
        final String[] startDayofWeek = new String[1];



        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startYear[0] = String.valueOf(year);
                startMonth[0] = String.valueOf(month + 1);
                startDay[0] = String.valueOf(dayOfMonth);

                if(month < 9){

                    sm[0] = "0" + startMonth[0];

                }else
                {
                    sm[0] = String.valueOf(month + 1);
                }
                if(dayOfMonth < 10)
                {
                    sd[0] = "0" + startDay[0];
                }else
                {
                    sd[0] = String.valueOf(dayOfMonth);
                }

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, dayOfMonth);
                switch (calendar1.get(Calendar.DAY_OF_WEEK)){
                    case 1:
                        startDayofWeek[0] = "일";
                        break;
                    case 2:
                        startDayofWeek[0] = "월";
                        break;
                    case 3:
                        startDayofWeek[0] = "화";
                        break;
                    case 4:
                        startDayofWeek[0] = "수";
                        break;
                    case 5:
                        startDayofWeek[0] = "목";
                        break;
                    case 6:
                        startDayofWeek[0] = "금";
                        break;
                    case 7:
                        startDayofWeek[0] = "토";
                        break;

                }
                if(!startDayofWeek[0].equals(selectedDay))
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                    android.support.v7.app.AlertDialog dialog = builder.setMessage("시작 요일과 수업 요일이 일치하지 않습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create();
                    dialog.show();
                }else
                {
                    startDateTextView.setText(startYear[0] + "년 "+ startMonth[0]+ "월 " + startDay[0]+"일 " + startDayofWeek[0] +"요일");
                    searchButton.setEnabled(true);
                    startDate = startYear[0]+ "-" +sm[0]+ "-"+ sd[0] ;
                }






            }

        },curYear,curMonth,curDay

        );


        teacherSpinner.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dayList.clear();
                index.clear();
                dayListForShow.clear();
                String item = teacherSpinner.getSelectedItem().toString();
                selectedTeacher = item;

                int count = 0;
                while(count < courseTeacher.size())
                {

                    if (courseTeacher.get(count).equals(item)) {
                        index.add(count);
                    }

                    count++;
                }
                for(int j =0; j < index.size(); j++)
                {
                    dayList.add(courseDay.get(index.get(j)).toString());
                }
                for(int k = 0; k < index.size(); k++)
                {
                    dayListForShow.add(courseDay.get(index.get(k)));
                }
                HashSet hs1 = new HashSet();
                hs1.addAll(dayListForShow);
                dayListForShow.clear();
                dayListForShow.addAll(hs1);


                ArrayAdapter adapter1 = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,dayListForShow);
                daySpinner.setAdapter(adapter1);





            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }));


        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                

                courseTimeList.clear();
                Timeindex.clear();
                String Teacher = teacherSpinner.getSelectedItem().toString();
                String day = daySpinner.getSelectedItem().toString();
                selectedDay = day;
                if(selectedDay.equals("월"))
                {
                    dow = 1;
                }
                else if(selectedDay.equals("화"))
                {
                    dow = 2;
                }
                else if(selectedDay.equals("수"))
                {
                    dow = 3;
                }
                else if(selectedDay.equals("목"))
                {
                    dow = 4;
                }
                else if(selectedDay.equals("금"))
                {
                    dow = 5;
                }
                else if(selectedDay.equals("토"))
                {
                    dow = 6;
                }


                int count = 0;
                while(count < courseTeacher.size())
                {
                    if(courseTeacher.get(count).equals(Teacher) && courseDay.get(count).equals(day))
                    {
                        Timeindex.add(count);
                    }
                    count++;
                }

                for(int j =0; j < Timeindex.size(); j++)
                {
                    switch (userBranch){
                        case "잠실":
                            if(!bookedCourseIDList_J.contains(courseID.get(Timeindex.get(j)))){
                                courseTimeList.add(courseTime.get(Timeindex.get(j)));
                            }
                            break;
                        case "여의도":
                            if(!bookedCourseIDList_Y.contains(courseID.get(Timeindex.get(j)))){
                                courseTimeList.add(courseTime.get(Timeindex.get(j)));
                            }
                            break;
                        case "시청":
                            if(!bookedCourseIDList_S.contains(courseID.get(Timeindex.get(j)))){
                                courseTimeList.add(courseTime.get(Timeindex.get(j)));
                            }
                            break;
                        case "교대":
                            if(!bookedCourseIDList_K.contains(courseID.get(Timeindex.get(j)))){
                                courseTimeList.add(courseTime.get(Timeindex.get(j)));
                            }
                            break;
                        case "광화문":
                            if(!bookedCourseIDList_G.contains(courseID.get(Timeindex.get(j)))){
                                courseTimeList.add(courseTime.get(Timeindex.get(j)));
                            }
                            break;
                    }



                }




                ButtonGridAdapter buttonGridAdapter = new ButtonGridAdapter(getContext(), courseTimeList, MonthFragment.this);
                buttonGridAdapter.notifyDataSetChanged();
                timeButtonGrid.setAdapter(buttonGridAdapter);




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeButtonGrid.setVisibility(View.VISIBLE);


            }
        });

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });












       // daySpinner = (Spinner) getView().findViewById(R.id.courseDaySpinner);
        // courseList = new ArrayList<Course>();
        //dayList = new ArrayList<String>();
        //adapter = new CourseListAdapter(getContext().getApplicationContext(), courseList);
        //daysAdapter = new ArrayAdapter<>(getContext().getApplicationContext(), R.layout.daylist, R.id.courseDaySpinnerText, dayList);
        //daySpinner.setAdapter(daysAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_month, container, false);
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






    /*class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;
        protected void onPreExecute() {
            try{
                target = "http://show981111.cafe24.com/courseListKorean.php"+ URLEncoder.encode(courseTeacher); //해당 php 파일 , 첫번쨰 스피너에서 선택된 선생 이름

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        protected  String doInBackground(Void... voids){
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();


            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        public void onPostExecute(String result){
            try{
                courseList.clear();
                dayList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String courseTeacher, courseDay, courseTime;
                int courseID;
                while(count< jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    courseID = object.getInt("courseID");
                    courseTeacher = object.getString("courseTeacher");
                    courseDay = object.getString("courseDay");
                    courseTime = object.getString("courseTime");
                    Course course = new Course(courseID, courseTeacher, courseDay, courseTime);//생성자를 통해 값 넣어주기
                    courseList.add(course);
                    dayList.add(course.getCourseDay());
                    count++;
                }
                if(count == 0)
                {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MonthFragment.this.getActivity());
                    dialog = builder.setMessage("조회된 레슨이 없습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                }
                adapter.notifyDataSetChanged();
                daysAdapter.notifyDataSetChanged();

            }catch (Exception e ){
                e.printStackTrace();
            }
        }



    }*/


}
