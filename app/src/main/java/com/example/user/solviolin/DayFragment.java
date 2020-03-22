package com.example.user.solviolin;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.user.solviolin.mMySQL.Downloader2;
import com.example.user.solviolin.mMySQL.Downloader3;
import com.example.user.solviolin.mMySQL.Downloader4;
import com.example.user.solviolin.mMySQL.Downloader5;
import com.example.user.solviolin.mMySQL.Downloader6;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.view.View.GONE;
import static com.example.user.solviolin.DayButtonGridAdapter.dayCredit;
import static com.example.user.solviolin.LoginActivity.InputURL;
import static com.example.user.solviolin.MainActivity.userBranch;
import static com.example.user.solviolin.MainActivity.userDuration;
import static com.example.user.solviolin.MainActivity.userID;
import static com.example.user.solviolin.MainActivity.userName;
import static com.example.user.solviolin.MainActivity.delaycredit;
import static com.example.user.solviolin.mMySQL.DataParser.courseDay;
import static com.example.user.solviolin.mMySQL.DataParser.courseID;
import static com.example.user.solviolin.mMySQL.DataParser.courseTeacher;
import static com.example.user.solviolin.mMySQL.DataParser.courseTime;
import static com.example.user.solviolin.mMySQL.DataParser1.BookedList;
import static com.example.user.solviolin.mMySQL.DataParser1.bookedCourseIDList_G;
import static com.example.user.solviolin.mMySQL.DataParser1.bookedCourseIDList_J;
import static com.example.user.solviolin.mMySQL.DataParser1.bookedCourseIDList_K;
import static com.example.user.solviolin.mMySQL.DataParser1.bookedCourseIDList_S;
import static com.example.user.solviolin.mMySQL.DataParser1.bookedCourseIDList_Y;
import static com.example.user.solviolin.mMySQL.DataParser3.DayBookedList;
import static com.example.user.solviolin.mMySQL.DataParser3.personalDayBookedList;
import static com.example.user.solviolin.mMySQL.DataParser3.personalDayBookedListcur;
import static com.example.user.solviolin.mMySQL.DataParser4.extendedDateList;
import static com.example.user.solviolin.mMySQL.DataParser5.exclusionList;


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

    //public static String canceledCourseDate;
    //public static String newlyBookedDate;
    //public static String delayDate;
    //public static boolean delayStatus = false;


    public void onActivityCreated(Bundle b) {
        /*Downloader3 download = new Downloader3(getContext(), "http://show981111.cafe24.com/DayBookedList.php");
        download.execute();
        super.onActivityCreated(b);

        final int curYear,curMonth,curDay;
        GregorianCalendar calendar = new GregorianCalendar();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH);
        curDay= calendar.get(Calendar.DAY_OF_MONTH);



        //final Button lookUpButton = (Button) getView().findViewById(R.id.lookUpButton);
        //lookUpButton.setVisibility(GONE);
        final Button delayButton = (Button) getView().findViewById(R.id.delayButton);
        delayButton.setEnabled(false);
        final Button delayDoneButton = (Button) getView().findViewById(R.id.delayDoneButton);
        if(delaycredit > 0)
        {
            delayDoneButton.setEnabled(true);
        }else
        {
            delayDoneButton.setEnabled(false);
        }
        final TextView selectedCancelDate = (TextView) getView().findViewById(R.id.selectedDate);
        final TextView canceltimetext = (TextView) getView().findViewById(R.id.canceltimetext);
        final Button chooseCancelDateButton = (Button) getView().findViewById(R.id.chooseCancelDate);

        final String[] cancelYear = new String[1];
        final String[] cancelMonth = new String[1];
        final String[] cMM = new String[1];
        final String[] cDD = new String[1];
        final String[] cancelDay = new String[1];
        final String[] cancelDayofWeek = new String[1];

        final Spinner cancelAvailableTime;
        final ArrayList<String> cancelTimeList = new ArrayList<>();
        cancelAvailableTime = (Spinner) getView().findViewById(R.id.cancelAvailableTimeSpinner);

        final TextView dayBookingTeacher = (TextView) getView().findViewById(R.id.dayBookingTeacher);
        if(BookedList.size() >= 1) {
            dayBookingTeacher.setText(BookedList.get(0).getBookedCourseTeacher()); // 예약된 선생님


            final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    cancelYear[0] = String.valueOf(year);
                    cancelMonth[0] = String.valueOf(month + 1);
                    cancelDay[0] = String.valueOf(dayOfMonth);

                    if (month < 9) {

                        cMM[0] = "0" + cancelMonth[0];

                    } else {
                        cMM[0] = String.valueOf(month + 1);
                    }
                    if (dayOfMonth < 10) {
                        cDD[0] = "0" + cancelDay[0];
                    } else {
                        cDD[0] = String.valueOf(dayOfMonth);
                    }

                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(Calendar.YEAR, year);
                    calendar1.set(Calendar.MONTH, month);
                    calendar1.set(Calendar.DATE, dayOfMonth);
                    switch (calendar1.get(Calendar.DAY_OF_WEEK)) {
                        case 1:
                            cancelDayofWeek[0] = "일";
                            break;
                        case 2:
                            cancelDayofWeek[0] = "월";
                            break;
                        case 3:
                            cancelDayofWeek[0] = "화";
                            break;
                        case 4:
                            cancelDayofWeek[0] = "수";
                            break;
                        case 5:
                            cancelDayofWeek[0] = "목";
                            break;
                        case 6:
                            cancelDayofWeek[0] = "금";
                            break;
                        case 7:
                            cancelDayofWeek[0] = "토";
                            break;

                    }
                    selectedCancelDate.setText(cancelYear[0] + "년 " + cancelMonth[0] + "월 " + cancelDay[0] + "일 " + cancelDayofWeek[0] + "요일");
                    // lookUpButton.setEnabled(true);

                    new Downloader2(getContext(), InputURL).execute();//시간대를 받기위한 데이터 파싱
                    delayButton.setEnabled(true);

                    cancelTimeList.clear();
                    Boolean timeout = false;
                    for (int i = 0; i < BookedList.size(); i++) {
                        int startDate = Integer.parseInt(BookedList.get(i).getStartDate().substring(0, 4) + BookedList.get(i).getStartDate().substring(5, 7) + BookedList.get(i).getStartDate().substring(8, 10));
                        int canceledDatenum = Integer.parseInt(cancelYear[0] + cMM[0] + cDD[0]);


                        if (cancelDayofWeek[0].equals(BookedList.get(i).getBookedCourseDay()) && startDate <= canceledDatenum) {
                            cancelTimeList.add(BookedList.get(i).getBookedCourseTime());
                        }
                        int curtime;

                        if (curYear == Integer.parseInt(cancelYear[0]) && (curMonth + 1) == Integer.parseInt(cancelMonth[0]) && curDay == Integer.parseInt(cancelDay[0])) {
                            Calendar calendar2 = Calendar.getInstance();

                            curtime = calendar2.get(Calendar.HOUR_OF_DAY) * 100 + calendar2.get(Calendar.MINUTE);
                            int point = BookedList.get(i).getBookedCourseTime().lastIndexOf(":");
                            int timeforcancel = Integer.parseInt(BookedList.get(i).getBookedCourseTime().substring(0, point)) * 100 + Integer.parseInt(BookedList.get(i).getBookedCourseTime().substring(point + 1, BookedList.get(i).getBookedCourseTime().length()));


                            if (timeforcancel - curtime < 400) {
                                cancelTimeList.remove(BookedList.get(i).getBookedCourseTime());
                                timeout = true;

                            }

                        }

                    }
                    for (int i = 0; i < personalDayBookedList.size(); i++) {
                        int point = personalDayBookedList.get(i).getCanceledCourseDate().lastIndexOf(" ");
                        if (!personalDayBookedList.get(i).getCanceledCourseDate().equals("null") && !DayBookedList.get(i).getCanceledCourseDate().isEmpty()) {
                            if (personalDayBookedList.get(i).getCanceledCourseDate().substring(0, point).equals(cancelYear[0] + "-" + cMM[0] + "-" + cDD[0])) {
                                cancelTimeList.remove(personalDayBookedList.get(i).getCanceledCourseDate().substring((point + 1), personalDayBookedList.get(i).getCanceledCourseDate().length()));
                            }
                        }
                    }
                    if (cancelTimeList.size() == 0) {
                        if (timeout) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            AlertDialog dialog = builder.setMessage("취소할 수 있는 레슨이 없습니다(4시간 전 까지만 변경이 가능합니다).")
                                    .setNegativeButton("확인", null)
                                    .create();
                            dialog.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            AlertDialog dialog = builder.setMessage("취소할 수 있는 레슨이 없습니다(예약현황을 확인해주세요).")
                                    .setNegativeButton("확인", null)
                                    .create();
                            dialog.show();
                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, cancelTimeList);
                    cancelAvailableTime.setAdapter(adapter);


                }

            }, curYear, curMonth, curDay

            );
            Calendar cal = calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.MONTH, mm);
            cal.set(Calendar.DAY_OF_MONTH, dd);
            cal.set(Calendar.YEAR, yy);
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis()); //달력 다이얼로그에서 선택가능한 미니멈 날짜
            Calendar c = Calendar.getInstance();
            c.set(Calendar.MONTH, mm + 1);
            c.set(Calendar.DAY_OF_MONTH, 7);
            c.set(Calendar.YEAR, yy);
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());


            chooseCancelDateButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    datePickerDialog.show();


                }
            });


            final String[] selectedCancelTime = new String[1];

            final Button chooseNewDateButton = (Button) getView().findViewById(R.id.chooseChangedDate);
            chooseNewDateButton.setEnabled(false);

            cancelAvailableTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedCancelTime[0] = cancelAvailableTime.getSelectedItem().toString();
                    canceledCourseDate = cancelYear[0] + "-" + cMM[0] + "-" + cDD[0] + " " + selectedCancelTime[0];
                    if (cancelTimeList.size() > 0) {
                        chooseNewDateButton.setEnabled(true);
                    } else {

                        chooseNewDateButton.setEnabled(false);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            delayDoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Downloader2(getContext(), InputURL).execute();
                    new Downloader3(getContext(), "http://show981111.cafe24.com/DayBookedList.php").execute();
                    chooseCancelDateButton.setEnabled(false);
                    chooseNewDateButton.setEnabled(true);
                    canceltimetext.setText("");
                    cancelAvailableTime.setVisibility(GONE);
                    for (int i = 0; i < personalDayBookedList.size(); i++) {

                        if (personalDayBookedList.get(i).getNewlyBookedDate().equals("다음달 중으로 할 예정(이월된 보강은 다음달까지만 가능합니다)")) {
                            selectedCancelDate.setText(personalDayBookedList.get(i).getCanceledCourseDate());

                            delayStatus = true;
                            break;
                        }
                    }
                }
            });


            final TextView selectedNewDate = (TextView) getView().findViewById(R.id.selectedNewDate);

            final String[] changedYear = new String[1];
            final String[] changedMonth = new String[1];
            final String[] MM = new String[1];
            final String[] DD = new String[1];
            final String[] changedDay = new String[1];
            final String[] changedDayofWeek = new String[1];

            final GridView newAvailableTimeGridView = (GridView) getView().findViewById(R.id.changingTimeGridView);
            //Button lookUpNewTimeButton = (Button) getView().findViewById(R.id.lookUpNewButton);
            final ArrayList<String> availableTimeList = new ArrayList<>();
            final ArrayList<String> TimeListforshow = new ArrayList<>();
            final ArrayList<Integer> timeIndex = new ArrayList<>();


            final DatePickerDialog datePickerDialog1 = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    changedYear[0] = String.valueOf(year);
                    changedMonth[0] = String.valueOf(month + 1);
                    changedDay[0] = String.valueOf(dayOfMonth);
                    if (month < 9) {

                        MM[0] = "0" + changedMonth[0];

                    } else {
                        MM[0] = String.valueOf(month + 1);
                    }
                    if (dayOfMonth < 10) {
                        DD[0] = "0" + changedDay[0];
                    } else {
                        DD[0] = String.valueOf(dayOfMonth);
                    }


                    //changedMonth[0] = String.valueOf(month + 1);

                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(Calendar.YEAR, year);
                    calendar1.set(Calendar.MONTH, month);
                    calendar1.set(Calendar.DATE, dayOfMonth);
                    switch (calendar1.get(Calendar.DAY_OF_WEEK)) {
                        case 1:
                            changedDayofWeek[0] = "일";
                            break;
                        case 2:
                            changedDayofWeek[0] = "월";
                            break;
                        case 3:
                            changedDayofWeek[0] = "화";
                            break;
                        case 4:
                            changedDayofWeek[0] = "수";
                            break;
                        case 5:
                            changedDayofWeek[0] = "목";
                            break;
                        case 6:
                            changedDayofWeek[0] = "금";
                            break;
                        case 7:
                            changedDayofWeek[0] = "토";
                            break;

                    }
                    selectedNewDate.setText(changedYear[0] + "년 " + changedMonth[0] + "월 " + changedDay[0] + "일 " + changedDayofWeek[0] + "요일");

                    //canceledCourseDate = cancelYear[0] + "-" + cMM[0] + "-" + cDD[0] + " " + selectedCancelTime[0];
                    newlyBookedDate = changedYear[0] + "-" + MM[0] + "-" + DD[0];
                    availableTimeList.clear();
                    timeIndex.clear();
                    for (int i = 0; i < courseTeacher.size(); i++) {
                        if (courseTeacher.get(i).equals(BookedList.get(0).getBookedCourseTeacher()) && courseDay.get(i).equals(changedDayofWeek[0])) {
                            timeIndex.add(i);
                        }
                    }

                    for (int j = 0; j < timeIndex.size(); j++) {
                        switch (userBranch) {
                            case "잠실":
                                if (!bookedCourseIDList_J.contains(courseID.get(timeIndex.get(j)))) {//정기예약이 된 것들을 거르는 과정이다.
                                    availableTimeList.add(courseTime.get(timeIndex.get(j)));
                                }
                                break;
                            case "여의도":
                                if (!bookedCourseIDList_Y.contains(courseID.get(timeIndex.get(j)))) {
                                    availableTimeList.add(courseTime.get(timeIndex.get(j)));
                                }
                                break;
                            case "시청":
                                if (!bookedCourseIDList_S.contains(courseID.get(timeIndex.get(j)))) {
                                    availableTimeList.add(courseTime.get(timeIndex.get(j)));
                                }
                                break;
                            case "교대":
                                if (!bookedCourseIDList_K.contains(courseID.get(timeIndex.get(j)))) {
                                    availableTimeList.add(courseTime.get(timeIndex.get(j)));
                                }
                                break;
                            case "광화문":
                                if (!bookedCourseIDList_G.contains(courseID.get(timeIndex.get(j)))) {
                                    availableTimeList.add(courseTime.get(timeIndex.get(j)));
                                }
                                break;
                        }
                    }
                    //이 다음 예약할 선생님과 새로 고른 날짜가 취소된 날짜와 취소된 선생과 일치하다면 그 시간 추가 그러나 그 선생,날짜.시간대가 새로 예약된 에 잇으면 빼주기기
                    //int point = 0;
                    int removepoint = 0;
                    for (int i = 0; i < DayBookedList.size(); i++) {
                        if (DayBookedList.get(i).getDayBookedCourseBranch().equals(userBranch) && DayBookedList.get(i).getBookedCourseTeacher().equals(BookedList.get(0).getBookedCourseTeacher())) {
                            if (!DayBookedList.get(i).getCanceledCourseDate().equals("null") && !DayBookedList.get(i).getCanceledCourseDate().isEmpty() && (DayBookedList.get(i).getCanceledCourseDate().length() == 16)) {
                                int point = DayBookedList.get(i).getCanceledCourseDate().lastIndexOf(" ");


                                if (newlyBookedDate.equals(DayBookedList.get(i).getCanceledCourseDate().substring(0, point))) {
                                    if (!availableTimeList.contains(DayBookedList.get(i).getCanceledCourseDate().substring(point + 1, DayBookedList.get(i).getCanceledCourseDate().length()))) {
                                        availableTimeList.add(DayBookedList.get(i).getCanceledCourseDate().substring(point + 1, DayBookedList.get(i).getCanceledCourseDate().length()));//리스트에 잇는 취소된 날짜와 사용자가 예약하려는 날짜가 같다면 그 날짜에 해당대는 시간을 추가해주기
                                    }
                                }
                            }
                            if (!DayBookedList.get(i).getNewlyBookedDate().equals("null") && !DayBookedList.get(i).getNewlyBookedDate().isEmpty() && DayBookedList.get(i).getNewlyBookedDate().length() > 4 && (DayBookedList.get(i).getNewlyBookedDate().length() == 16)) {
                                removepoint = DayBookedList.get(i).getNewlyBookedDate().lastIndexOf(" ");
                                if (newlyBookedDate.equals(DayBookedList.get(i).getNewlyBookedDate().substring(0, removepoint))) {
                                    if (availableTimeList.contains(DayBookedList.get(i).getNewlyBookedDate().substring(removepoint + 1, DayBookedList.get(i).getNewlyBookedDate().length()))) {
                                        availableTimeList.remove(DayBookedList.get(i).getNewlyBookedDate().substring(removepoint + 1, DayBookedList.get(i).getNewlyBookedDate().length()));
                                        //새로 예약하려는 날짜가 기존의 리스트에 있는 새롭게 예약된 날짜와 같다면 그 시간대를 지워줄것
                                    }
                                }
                            }

                            if (!DayBookedList.get(i).getendDate().equals("null") && !DayBookedList.get(i).getendDate().isEmpty() && (DayBookedList.get(i).getendDate().length() == 16)) {
                                int point = DayBookedList.get(i).getendDate().lastIndexOf(" ");


                                if (newlyBookedDate.equals(DayBookedList.get(i).getendDate().substring(0, point))) {

                                    String quarterTime = DayBookedList.get(i).getendDate().substring(removepoint + 1, DayBookedList.get(i).getendDate().length());
                                    if (quarterTime.substring(3, quarterTime.length()).equals("45") || quarterTime.substring(3, quarterTime.length()).equals("15")) {
                                        String quarterByHalf = String.valueOf(Integer.parseInt(quarterTime.substring(3, quarterTime.length())) - 15);
                                        String blockedTime = quarterTime.substring(0, 3) + quarterByHalf;
                                        if (availableTimeList.contains(blockedTime)) {
                                            availableTimeList.remove(blockedTime);

                                        }
                                    }


                                }
                            }

                        }
                    }
                    for (int i = 0; i < extendedDateList.size(); i++) {
                        if (extendedDateList.get(i).getExtendeduserBranch().equals(userBranch) && extendedDateList.get(i).getExtendedcourseTeacher().equals(BookedList.get(0).getBookedCourseTeacher())) {
                            //Toast.makeText(getContext(),"PASS", Toast.LENGTH_SHORT).show();
                            if (!extendedDateList.get(i).getExtendedDate().equals("null") && !extendedDateList.get(i).getExtendedDate().isEmpty()) {
                                //Toast.makeText(getContext(),"PASS Again", Toast.LENGTH_SHORT).show();
                                int point = extendedDateList.get(i).getExtendedDate().lastIndexOf(" ");
                                if (newlyBookedDate.equals(extendedDateList.get(i).getExtendedDate().substring(0, point))) {
                                    String extendedTime = extendedDateList.get(i).getExtendedDate().substring(point + 1, extendedDateList.get(i).getExtendedDate().length());
                                    int blockedMinute = Integer.parseInt(extendedTime.substring(3, extendedTime.length())) + 30;

                                    if (blockedMinute >= 60) {
                                        blockedMinute = blockedMinute - 60;
                                    }
                                    String blockedTime = extendedTime.substring(0, 3) + String.valueOf(blockedMinute);
                                    if (blockedMinute == 0) {
                                        blockedTime = String.valueOf(Integer.parseInt(extendedTime.substring(0, 2)) + 1) + ":00";
                                    }

                                    if (availableTimeList.contains(blockedTime)) {
                                        availableTimeList.remove(blockedTime);

                                    }
                                }
                            }


                        }
                    }

                    for (int i = 0; i < exclusionList.size(); i++) //클로즈 거르는 부분
                    {

                        if ((exclusionList.get(i).getClosedBranch().equals(userBranch) || exclusionList.get(i).getClosedBranch().equals("전체")) && (exclusionList.get(i).getClosedTeacher().equals(BookedList.get(0).getBookedCourseTeacher()) || exclusionList.get(i).getClosedTeacher().equals("전체"))) {

                            if (!exclusionList.get(i).getClosedStartDate().equals("null") && !exclusionList.get(i).getClosedStartDate().isEmpty() && !exclusionList.get(i).getClosedEndDate().equals("null") && !exclusionList.get(i).getClosedEndDate().isEmpty()) {
                                //Toast.makeText(getContext(),Integer.toString(exclusionList.size()), Toast.LENGTH_SHORT).show();


                                int closestartDate = Integer.parseInt(exclusionList.get(i).getClosedStartDate().substring(0, 4) + exclusionList.get(i).getClosedStartDate().substring(5, 7) + exclusionList.get(i).getClosedStartDate().substring(8, 10));
                                int closeendDate = Integer.parseInt(exclusionList.get(i).getClosedEndDate().substring(0, 4) + exclusionList.get(i).getClosedEndDate().substring(5, 7) + exclusionList.get(i).getClosedEndDate().substring(8, 10));
                                int newlyBookednum = Integer.parseInt(changedYear[0] + MM[0] + DD[0]);
                                //LocalDate BookedDateNewly = LocalDate.parse(newlyBookedDate);


                                if (newlyBookednum >= closestartDate && newlyBookednum <= closeendDate) {

                                    if (availableTimeList.size() > 0) {
                                        for (int j = 0; j < availableTimeList.size(); j++) {

                                            if (newlyBookednum == closestartDate || newlyBookednum == closeendDate) {
                                                //Toast.makeText(getContext(),"PASS SEC", Toast.LENGTH_SHORT).show();
                                                String removedTime = availableTimeList.get(j);
                                                int endindex = availableTimeList.get(j).length();
                                                int preventredun = 0;
                                                int getTestedTime = Integer.parseInt(availableTimeList.get(j).substring(0, 2)) * 100 + Integer.parseInt(availableTimeList.get(j).substring(3, endindex));
                                                int closestartTime = Integer.parseInt(exclusionList.get(i).getClosedStartDate().substring(11, 13) + exclusionList.get(i).getClosedStartDate().substring(14, 16));
                                                int closeendTime = Integer.parseInt(exclusionList.get(i).getClosedEndDate().substring(11, 13) + exclusionList.get(i).getClosedEndDate().substring(14, 16));
                                                if (getTestedTime > closestartTime && newlyBookednum == closestartDate)//클로즈 시작날과 겹쳣을떄
                                                {
                                                    availableTimeList.remove(removedTime);
                                                    j = j - 1;
                                                    preventredun = 1;
                                                }
                                                if (getTestedTime < closeendTime && newlyBookednum == closeendDate)//클로즈 끝나는 날과 겹쳣을떄
                                                {
                                                    if (preventredun == 1) continue;
                                                    availableTimeList.remove(removedTime);
                                                    j = j - 1;


                                                }
                                            } else {
                                                availableTimeList.clear();
                                            }


                                        }
                                    }


                                }

                            }
                        }
                    }

                    if (Integer.parseInt(changedYear[0]) == curYear && Integer.parseInt(changedMonth[0]) == (curMonth + 1) && Integer.parseInt(changedDay[0]) == curDay) {
                        TimeListforshow.clear();
                        int curtime = 0;
                        int timeforbook = 0;
                        for (int i = 0; i < availableTimeList.size(); i++) {
                            Calendar calendar2 = Calendar.getInstance();

                            curtime = calendar2.get(Calendar.HOUR_OF_DAY) * 100 + calendar2.get(Calendar.MINUTE);
                            int devide = availableTimeList.get(i).lastIndexOf(":");
                            timeforbook = Integer.parseInt(availableTimeList.get(i).substring(0, devide)) * 100 + Integer.parseInt(availableTimeList.get(i).substring(devide + 1, availableTimeList.get(i).length()));

                            if (timeforbook - curtime > 400) {
                                TimeListforshow.add(availableTimeList.get(i));
                            } else
                                continue;
                        }
                        DayButtonGridAdapter buttonGridAdapter = new DayButtonGridAdapter(getContext(), TimeListforshow, DayFragment.this);
                        buttonGridAdapter.notifyDataSetChanged();
                        newAvailableTimeGridView.setAdapter(buttonGridAdapter);


                    } else {


                        DayButtonGridAdapter buttonGridAdapter = new DayButtonGridAdapter(getContext(), availableTimeList, DayFragment.this);
                        buttonGridAdapter.notifyDataSetChanged();
                        newAvailableTimeGridView.setAdapter(buttonGridAdapter);
                    }


                    canceledCourseDate = cancelYear[0] + "-" + cMM[0] + "-" + cDD[0] + " " + selectedCancelTime[0];


                }

            }, curYear, curMonth, curDay

            );
            datePickerDialog1.getDatePicker().setMinDate(cal.getTimeInMillis()); //달력 다이얼로그에서 선택가능한 미니멈 날짜
            datePickerDialog1.getDatePicker().setMaxDate(c.getTimeInMillis());


            chooseNewDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Downloader3(getContext(), "http://show981111.cafe24.com/DayBookedList.php").execute();
                    new Downloader4(getContext(), "http://show981111.cafe24.com/extensionList.php").execute();
                    new Downloader5(getContext(), "http://show981111.cafe24.com/closedDate.php").execute();
                    new Downloader6(getContext(), "http://show981111.cafe24.com/openDate.php").execute();
                    Boolean validCancel = false;
                    if (!delayStatus) {
                        if (cancelTimeList.size() != 0) {
                            for (int i = 0; i < BookedList.size(); i++) {
                                if (BookedList.get(i).getBookedCourseDay().equals(cancelDayofWeek[0]) && BookedList.get(i).getBookedCourseTime().equals(selectedCancelTime[0])) {
                                    validCancel = true;
                                }
                            }
                            if (validCancel) {
                                datePickerDialog1.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                AlertDialog dialog = builder.setMessage("취소하실 레슨을 다시한번 확인해주세요!")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            AlertDialog dialog = builder.setMessage("취소하실 레슨을 다시한번 확인해주세요!")
                                    .setNegativeButton("확인", null)
                                    .create();
                            dialog.show();
                        }
                    } else {
                        datePickerDialog1.show();
                    }
                }
            });

            delayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    AlertDialog dialog = builder.setMessage("보강을 다음으로 미루시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (cancelTimeList.size() == 0) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        AlertDialog dialog1 = builder.setMessage("취소하실 레슨을 다시 한 번 확인해 주세요!")
                                                .setNegativeButton("확인", null)
                                                .create();
                                        dialog1.show();
                                    }

                                    dayCredit = 2;
                                    if (!userDuration.equals("null") && !userDuration.isEmpty()) {
                                        if (userDuration.equals("60")) {
                                            dayCredit = 4;
                                        }
                                    }
                                    int count = personalDayBookedListcur.size();
                                    dayCredit = dayCredit - count;
                                    if (dayCredit > 0) {
                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    boolean success = jsonResponse.getBoolean("success");
                                                    if (success) {
                                                        Toast.makeText(getActivity(), "보강이 이월되었습니다.", Toast.LENGTH_LONG).show();
                                                        Downloader3 download = new Downloader3(getContext(), "http://show981111.cafe24.com/DayBookedList.php");
                                                        download.execute();
                                                        getActivity().finish();


                                                    } else {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                        AlertDialog dialog = builder.setMessage("보강을 이월할 수 없습니다.")
                                                                .setNegativeButton("확인", null)
                                                                .create();
                                                        dialog.show();

                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        };
                                        String dataStatus = "cur";
                                        ChangeRequest changeRequest = new ChangeRequest(canceledCourseDate, "다음달 중으로 할 예정(이월된 보강은 다음달까지만 가능합니다)", userID, userName, BookedList.get(0).getBookedCourseTeacher(), userBranch, dataStatus, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                                        queue.add(changeRequest);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        AlertDialog dialog1 = builder.setMessage("레슨을 변경할 수 없습니다(변경가능 횟수가 초과하였습니다.)")
                                                .setNegativeButton("확인", null)
                                                .create();
                                        dialog1.show();
                                    }
                                }


                            })
                            .setNegativeButton("취소", null)
                            .create();
                    dialog.show();
                }
            });


        }*/
        super.onActivityCreated(b);

        TextView tv_canceledTeacher = (TextView) getView().findViewById(R.id.tv_canceledTeacher);
        Spinner sp_canceledDate = (Spinner) getView().findViewById(R.id.sp_canceledDate);
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
