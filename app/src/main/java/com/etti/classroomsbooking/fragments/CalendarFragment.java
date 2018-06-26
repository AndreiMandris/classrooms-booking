package com.etti.classroomsbooking.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.etti.classroomsbooking.MainActivity;
import com.etti.classroomsbooking.R;
import com.etti.classroomsbooking.model.Classroom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static com.etti.classroomsbooking.util.Constant.DATE_IN_MILLIS;
import static java.util.Calendar.MONDAY;

public class CalendarFragment extends Fragment{
    private static final String TAG = CalendarFragment.class.getSimpleName();
    DatePicker datePicker;
    FloatingActionButton floatingActionButton;
    Long dateInMillis;
    DatabaseReference roomsRef;
    FirebaseDatabase db;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        datePicker = (DatePicker) view.findViewById(R.id.calendarDatePicker);
        datePicker.setMinDate(System.currentTimeMillis() - 10000);
        datePicker.setFirstDayOfWeek(MONDAY);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        MainActivity mainActivity = (MainActivity) getActivity();
        floatingActionButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            dateInMillis = getDateInstance(datePicker).getTimeInMillis();
            args.putLong(DATE_IN_MILLIS, dateInMillis);
            Fragment classroomsFragment = new ClassroomsFragment();
            classroomsFragment.setArguments(args);
            mainActivity.moveToFragment(classroomsFragment);
        });

        db = FirebaseDatabase.getInstance();
        roomsRef = db.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot iterator : dataSnapshot.getChildren()){
                    Classroom classroomFromDB = iterator.getValue(Classroom.class);
                   if(mainActivity.getClassrooms() == null){
                       return;
                   }
                    mainActivity.getClassrooms().set(classroomFromDB.getId(), classroomFromDB);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public Calendar getDateInstance(DatePicker datePicker){
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        return calendar;
    }

}
