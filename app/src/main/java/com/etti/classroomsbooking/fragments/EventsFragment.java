package com.etti.classroomsbooking.fragments;

import com.etti.classroomsbooking.model.TimeFrame;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.etti.classroomsbooking.MainActivity;
import com.etti.classroomsbooking.R;
import com.etti.classroomsbooking.model.Classroom;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.etti.classroomsbooking.util.Constant.DATE_IN_MILLIS;
import static com.etti.classroomsbooking.util.Constant.ROOM_POSITION;
import static com.etti.classroomsbooking.util.Utility.getStringDateFromTimeMillis;

public class EventsFragment extends Fragment {

    private static final String TAG = EventsFragment.class.getSimpleName();
    Long dateInMillis;
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

        TextView classroomNametextView = view.findViewById(R.id.classroomNameView);
        TextView dateOfEvents = view.findViewById(R.id.dateOfEvents);
        classroomNametextView.setText("Classroom " + selectedClassroom.getName());
        dateOfEvents.setText(getStringDateFromTimeMillis(dateInMillis));
        db = FirebaseDatabase.getInstance();
        classroomsRef = db.getReference("rooms");

        auth = FirebaseAuth.getInstance();

        addEventButton.setOnClickListener(v -> {
            displayStartTimePicker(selectedClassroom, listView);
        });

        FloatingActionButton cancelMeetingsButton = view.findViewById(R.id.cancelSelectedMeetings);
        if (cancelMeetingsButton != null) {
                    cancelMeetingsButton.setOnClickListener(v -> {
                    int count = listView.getCount();
                    ArrayList<TimeFrame> intervalsFromSpecifiedDate = (ArrayList<TimeFrame>) selectedClassroom.getIntervals().stream().filter(interval -> interval.getDate().equals(getStringDateFromTimeMillis(selectedDateInMillis))).collect(Collectors.toList());
                    ArrayList<TimeFrame> otherIntervals = (ArrayList<TimeFrame>) selectedClassroom.getIntervals().stream().filter(interval -> !interval.getDate().equals(getStringDateFromTimeMillis(selectedDateInMillis))).collect(Collectors.toList());
                    for(int i=0; i<count; ++i) {
                        ViewGroup row = (ViewGroup) listView.getChildAt(i);
                        CheckBox check = row.findViewById(R.id.eventCheckBox);
                        if (check.isChecked()) {
                            intervalsFromSpecifiedDate.set(i, null);
                        }
                    }
                    intervalsFromSpecifiedDate.removeAll(Collections.singleton(null));
                    for (TimeFrame timeFrame : otherIntervals){
                        intervalsFromSpecifiedDate.add(timeFrame);
                    }
                    selectedClassroom.setIntervals(intervalsFromSpecifiedDate);
                    saveBookingToDB(selectedClassroom);
                    mainActivity.buildEventsListView(listView, selectedClassroom, dateInMillis);

            });
        }
        return view;
    }

    public void displayStartTimePicker(final Classroom classroom, ListView listView){
        String selectedDate = getStringDateFromTimeMillis(dateInMillis);
        TimePickerDialog tpd = TimePickerDialog.newInstance((view, hourOfDay, minute, second) -> {
                double startPickedTime = getComposedHour(hourOfDay, minute);
                displayEndTimePicker(startPickedTime, classroom, selectedDate, listView);
        }, true);

        boolean[] disabledHours = classroom.checkStartTimeAvailability(selectedDate);
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
        tpd.setTitle("Choose starting time");
        tpd.show(getActivity().getFragmentManager(), "Choose starting time");
    }

    public void displayEndTimePicker(double startPickedTime, Classroom classroom, String selectedDate, ListView listView){
        TimePickerDialog tpdE = TimePickerDialog.newInstance((view, hourOfDay, minute, second) -> {
                classroom.bookClassroom(startPickedTime, getComposedHour(hourOfDay, minute), auth.getCurrentUser().getEmail(), dateInMillis);
                saveBookingToDB(classroom);
                mainActivity.buildEventsListView(listView, classroom, dateInMillis);
                ((MainActivity)getActivity()).notifyAdapter();
        }, true);
        boolean[] disabledHoursE = classroom.checkEndTimeAvailability(selectedDate, startPickedTime);
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
        tpdE.setTitle("Choose ending time");
        tpdE.show(getActivity().getFragmentManager(), "Choose ending time");
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

    public static String getTAG() {
        return TAG;
    }
}
