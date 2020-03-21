package com.example.user.solviolin;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;


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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_month, container, false);
        return v;

    }


    public static String selectedTeacher; //선택된 선생님
    public static int selectedYear = 0;
    public static int selectedMonth = 0;
    public static int selectedDayNum = 0;
    public static String selectedDay; //선택된 날짜
    public static String startDate;

    /*to make teacherspinner*/
    private  Spinner teacherSpinner;
    public static ArrayList<courseTimeLine> courseTimeLineArrayList;
    /* end */

    /*to make dayspinner*/
    private Spinner daySpinner;
    private ArrayList<String> dayList = new ArrayList<>();
    /* end */

    String startYear;
    String startMonth;
    String sm;
    String sd;
    String startDay ;
    String startDayofWeek;

    private int isDayMatched( int year, int month, int dayOfMonth, Button searchButton, TextView startDateTextView, GridView timeButtonGrid)
    {
        startYear = String.valueOf(year);
                startMonth = String.valueOf(month + 1);
                startDay = String.valueOf(dayOfMonth);

                if(month < 9){

                    sm = "0" + startMonth;

                }else
                {
                    sm = String.valueOf(month + 1);
                }
                if(dayOfMonth < 10)
                {
                    sd = "0" + startDay;
                }else
                {
                    sd = String.valueOf(dayOfMonth);
                }

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, dayOfMonth);
                switch (calendar1.get(Calendar.DAY_OF_WEEK)){
                    case 1:
                        startDayofWeek = "일";
                        break;
                    case 2:
                        startDayofWeek = "월";
                        break;
                    case 3:
                        startDayofWeek = "화";
                        break;
                    case 4:
                        startDayofWeek = "수";
                        break;
                    case 5:
                        startDayofWeek = "목";
                        break;
                    case 6:
                        startDayofWeek = "금";
                        break;
                    case 7:
                        startDayofWeek = "토";
                        break;

                }
                if(!startDayofWeek.equals(selectedDay))
                {
                    searchButton.setEnabled(false);
                    startDateTextView.setText("");
                    timeButtonGrid.setVisibility(View.GONE);
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                    androidx.appcompat.app.AlertDialog dialog = builder.setMessage("시작 요일과 수업 요일이 일치하지 않습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create();
                    dialog.show();
                    return 0;
                }else
                {
                    startDateTextView.setText(startYear + "년 "+ startMonth+ "월 " + startDay+"일 " + startDayofWeek +"요일");
                    searchButton.setEnabled(true);
                    startDate = startYear+ "-" +sm+ "-"+ sd ;
                    //timeButtonGrid.setVisibility(View.VISIBLE);

                    return 1;
                }
    }

    public void onActivityCreated(Bundle b){
        super.onActivityCreated(b);

        final Button searchButton = (Button) getView().findViewById(R.id.searchButton);
        final Button startDateButton = (Button) getView().findViewById(R.id.startDateButton);
        final TextView startDateTextView = (TextView) getView().findViewById(R.id.startDateTextView);
        final GridView timeButtonGrid = (GridView) getView().findViewById(R.id.ButtonGridView);

        daySpinner = (Spinner) getView().findViewById(R.id.courseDaySpinner);
        searchButton.setEnabled(false);
        timeButtonGrid.setVisibility(View.GONE);

        /* teacher spinner 세팅 시작하는 지점 */
        teacherSpinner = (Spinner) getView().findViewById(R.id.courseTeacherSpinner);
        final fetchTeacherForSpinner fetchCourseTimeLine = new fetchTeacherForSpinner(teacherSpinner,getContext());
        fetchCourseTimeLine.execute("http://show981111.cafe24.com/getCourseTimeLine.php");
        /*spinner setting end*/


        /*For DatePickerDialog*/
        final int curYear,curMonth,curDay;
        GregorianCalendar calendar = new GregorianCalendar();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH);
        curDay= calendar.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                timeButtonGrid.setVisibility(View.GONE);
                selectedYear = year;
                selectedMonth = month;
                selectedDayNum = dayOfMonth;

                isDayMatched(year,month,dayOfMonth,searchButton, startDateTextView, timeButtonGrid);
            }

        },curYear,curMonth,curDay
        );


        teacherSpinner.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                timeButtonGrid.setVisibility(View.GONE);
                courseTimeLineArrayList = fetchCourseTimeLine.getCourseTimeLineArrayList();
                Log.d("spinnerafter", String.valueOf(courseTimeLineArrayList.size()));

                dayList.clear();
                String item = teacherSpinner.getSelectedItem().toString();
                selectedTeacher = item;

                for(int i = 0; i < courseTimeLineArrayList.size(); i++)
                {
                    if(courseTimeLineArrayList.get(i).getCourseTeacher().equals(selectedTeacher))
                    {
                        dayList.add(courseTimeLineArrayList.get(i).getCourseDay());
                    }
                }

                HashSet<String> hashSet = new HashSet<String>(dayList);
                ArrayList<String> HashedDayList = new ArrayList<String>(hashSet);


                ArrayAdapter adapter1 = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,HashedDayList);
                daySpinner.setAdapter(adapter1);
                Log.d("selectedyear",String.valueOf(selectedYear));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }));


        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                timeButtonGrid.setVisibility(View.GONE);
                String day = daySpinner.getSelectedItem().toString();
                selectedDay = day;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int res = isDayMatched(selectedYear,selectedMonth,selectedDayNum,searchButton,startDateTextView,timeButtonGrid);
                /*button grid 세팅 시작하는 지점 */
                if(res == 1)
                {
                    fetchTimeForMonth fetchTimeForMonth = new fetchTimeForMonth(timeButtonGrid,getContext(),MonthFragment.this,selectedDay,selectedTeacher,startDate);
                    fetchTimeForMonth.execute("http://show981111.cafe24.com/getTimeForMonth.php");
                    /*button grid setting end*/
                    timeButtonGrid.setVisibility(View.VISIBLE);
                }
            }
        });

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
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
