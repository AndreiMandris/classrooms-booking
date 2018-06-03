package com.etti.classroomsbooking.fragments;

import com.etti.classroomsbooking.model.TimeLapse;
import com.etti.classroomsbooking.util.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.etti.classroomsbooking.MainActivity;
import com.etti.classroomsbooking.R;
import com.etti.classroomsbooking.model.Classroom;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.etti.classroomsbooking.util.Constant.DATE_IN_MILLIS;
import static com.etti.classroomsbooking.util.Constant.ROOM_POSITION;
import static com.etti.classroomsbooking.util.Constant.getStringDateFromTimeMillis;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment {

    Long dateInMillis;
    DatabaseReference rootRef;
    DatabaseReference classroomsRef;
    DatabaseReference classroomRef;
    FirebaseDatabase db;
    FirebaseAuth auth;
    MainActivity mainActivity;


    public EventsFragment() {
        // Required empty public constructor
    }

    public static EventsFragment newInstance(Bundle bundle) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = bundle;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ListView listView = view.findViewById(R.id.list_events);
        FloatingActionButton addEventButton = view.findViewById(R.id.floatingActionButton2);

        Bundle args = getArguments();
        dateInMillis = getArguments().getLong(DATE_IN_MILLIS);
        int position = args.getInt(ROOM_POSITION);
        long selectedDateInMillis = args.getLong(DATE_IN_MILLIS);
        mainActivity = (MainActivity) getActivity();
        Classroom selectedClassroom = ((MainActivity) getActivity()).getClassrooms().get(position);
        mainActivity.buildEventsListView(listView, selectedClassroom, selectedDateInMillis);
        //hideCheckBoxes(listView);

        TextView classroomNametextView = view.findViewById(R.id.classroomNameView);
        TextView dateOfEvents = view.findViewById(R.id.dateOfEvents);
        classroomNametextView.setText("Classroom " + selectedClassroom.getName());
        dateOfEvents.setText(getStringDateFromTimeMillis(dateInMillis));
        db = FirebaseDatabase.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        classroomsRef = db.getReference("rooms");

        auth = FirebaseAuth.getInstance();

        addEventButton.setOnClickListener(v -> {
            displayStartTimePicker(selectedClassroom, listView);
        });

        Button cancelMeetingsButton = view.findViewById(R.id.cancelMeetings);
        if (cancelMeetingsButton != null) {
                    cancelMeetingsButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int count = listView.getCount();
                    ArrayList<TimeLapse> intervalsFromSpecifiedDate = (ArrayList<TimeLapse>) selectedClassroom.getIntervals().stream().filter(interval -> interval.getDate().equals(getStringDateFromTimeMillis(selectedDateInMillis))).collect(Collectors.toList());
                    ArrayList<TimeLapse> otherIntervals = (ArrayList<TimeLapse>) selectedClassroom.getIntervals().stream().filter(interval -> !interval.getDate().equals(getStringDateFromTimeMillis(selectedDateInMillis))).collect(Collectors.toList());
                    for(int i=0; i<count; ++i) {
                        ViewGroup row = (ViewGroup) listView.getChildAt(i);
                        CheckBox check = row.findViewById(R.id.eventCheckBox);
                        if (check.isChecked()) {
                            intervalsFromSpecifiedDate.set(i, null);
                        }
                    }
                    intervalsFromSpecifiedDate.removeAll(Collections.singleton(null));
                    for (TimeLapse timeLapse : otherIntervals){
                        intervalsFromSpecifiedDate.add(timeLapse);
                    }
                    selectedClassroom.setIntervals(intervalsFromSpecifiedDate);
                    saveBookingToDB(selectedClassroom);
                    mainActivity.buildEventsListView(listView, selectedClassroom, dateInMillis);
                }
            });
        }
        return view;
    }

//    public void tickCheckBoxes(ListView listView){
//        for (int i = 0; i < 5; ++i){
//            ViewGroup row = (ViewGroup) listView.getChildAt(i);
//            CheckBox checkBox = row.findViewById(R.id.eventCheckBox);
//            //checkBox.setChecked(Constant.checkedEventsList.get(i - 1));
//        }
//    }
//    public void hideCheckBoxes(ListView listView){
//        if(listView.getChildCount() != 0) {
//            int count = listView.getCount();
//            for (int i = 0; i < count; ++i) {
//                ViewGroup row = (ViewGroup) listView.getChildAt(i);
//                CheckBox checkBox = row.findViewById(R.id.eventCheckBox);
//                TextView userEmail = row.findViewById(R.id.textView2);
//                if (!userEmail.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
//                    checkBox.setVisibility(View.INVISIBLE);
//                }
//            }
//        }
//    }

    public void displayStartTimePicker(final Classroom classroom, ListView listView){
        String selectedDate = getStringDateFromTimeMillis(dateInMillis);
        TimePickerDialog tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                double startPickedTime = getComposedHour(hourOfDay, minute);
                displayEndTimePicker(startPickedTime, classroom, selectedDate, listView);
            }
        }, true);

        boolean[] disabledHours = classroom.checkAvailability(selectedDate);
        List<Timepoint> selectableTimes = new ArrayList<>();
        for (int i = 0; i < 48; i++){
            if (disabledHours[i] != true){
                double j = (double) i / 2;
                selectableTimes.add(new Timepoint((int) j, (int) ((j % 1) * 60)));
            }
        }

        Timepoint[] tps = new Timepoint[selectableTimes.size()];
        tps = selectableTimes.toArray(tps);

        tpd.setSelectableTimes(tps);
        tpd.setTitle("Pick meeting start time");
        tpd.show(getActivity().getFragmentManager(), "Pick meeting start time");
    }

    public void displayEndTimePicker(double startPickedTime, Classroom classroom, String selectedDate, ListView listView){
        TimePickerDialog tpdE = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                classroom.bookClassroom(startPickedTime, getComposedHour(hourOfDay, minute), auth.getCurrentUser().getEmail(), dateInMillis);
                saveBookingToDB(classroom);
                mainActivity.buildEventsListView(listView, classroom, dateInMillis);
                ((MainActivity)getActivity()).notifyAdapter();
            }
        }, true);
        boolean[] disabledHoursE = classroom.checkAvailabilityE(selectedDate, startPickedTime);
        List<Timepoint> selectableTimesE = new ArrayList<>();
        for (int i = 0; i < 48; i++){
            if (disabledHoursE[i] != true && (double) i / 2 > startPickedTime){
                double j = (double) i / 2;
                selectableTimesE.add(new Timepoint((int) j, (int) ((j % 1) * 60)));
            }
        }
        Timepoint[] tps = new Timepoint[selectableTimesE.size()];
        tps = selectableTimesE.toArray(tps);

        tpdE.setSelectableTimes(tps);
        tpdE.setTitle("Pick meeting end time");
        tpdE.show(getActivity().getFragmentManager(), "Pick meeting end time");
    }

    private void saveBookingToDB(Classroom classroom){
        classroom.sortIntervals();
        classroomRef = classroomsRef.child("classroom" + classroom.getId());
        classroomRef.setValue(classroom);
    }

    private double getComposedHour(int hourOfDay, int minute) {
        double minutes = minute == 0 ? 0 : 0.5;
        return hourOfDay + minutes;
    }
}
