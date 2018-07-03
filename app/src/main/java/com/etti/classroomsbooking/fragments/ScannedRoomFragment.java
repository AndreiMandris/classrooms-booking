package com.etti.classroomsbooking.fragments;

import com.etti.classroomsbooking.ScanQRActivity;
import com.etti.classroomsbooking.model.TimeLapse;
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

import static com.etti.classroomsbooking.util.Utility.getStringDateFromTimeMillis;

public class ScannedRoomFragment extends Fragment {
    private static final String TAG = ScannedRoomFragment.class.getSimpleName();
    Long dateInMillis;
    DatabaseReference rootRef;
    DatabaseReference classroomsRef;
    DatabaseReference classroomRef;
    FirebaseDatabase db;
    FirebaseAuth auth;
    MainActivity mainActivity;

    public ScannedRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dateInMillis = System.currentTimeMillis();
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ListView listView = view.findViewById(R.id.list_events);
        TextView classroomNametextView = view.findViewById(R.id.classroomNameView);
        TextView dateOfEvents = view.findViewById(R.id.dateOfEvents);
        FloatingActionButton addEventButton = view.findViewById(R.id.floatingActionButton2);
        FloatingActionButton cancelMeetingsButton = view.findViewById(R.id.cancelSelectedMeetings);

        long selectedDateInMillis = System.currentTimeMillis();
        mainActivity = (MainActivity) getActivity();
        Classroom selectedClassroom = ((MainActivity) getActivity()).getClassrooms().get(ScanQRActivity.scannedQRCode);
        mainActivity.buildEventsListView(listView, selectedClassroom, selectedDateInMillis);

        classroomNametextView.setText("Classroom " + selectedClassroom.getName());
        dateOfEvents.setText(getStringDateFromTimeMillis(dateInMillis));
        db = FirebaseDatabase.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        classroomsRef = db.getReference("rooms");

        auth = FirebaseAuth.getInstance();

        addEventButton.setOnClickListener(v -> {
            displayStartTimePicker(selectedClassroom, listView);
        });

        if (cancelMeetingsButton != null) {
            cancelMeetingsButton.setOnClickListener(v -> {
                    int count = listView.getCount();
                    ArrayList<TimeLapse> intervalsFromSpecifiedDate = (ArrayList<TimeLapse>) selectedClassroom.getIntervals().stream().filter(interval -> interval.getDate().equals(getStringDateFromTimeMillis(selectedDateInMillis))).collect(Collectors.toList());
                    ArrayList<TimeLapse> otherIntervals = (ArrayList<TimeLapse>) selectedClassroom.getIntervals().stream().filter(interval -> !interval.getDate().equals(getStringDateFromTimeMillis(selectedDateInMillis))).collect(Collectors.toList());
                    for (int i = 0; i < count; ++i) {
                        ViewGroup row = (ViewGroup) listView.getChildAt(i);
                        CheckBox check = row.findViewById(R.id.eventCheckBox);
                        if (check.isChecked()) {
                            intervalsFromSpecifiedDate.set(i, null);
                        }
                    }
                    intervalsFromSpecifiedDate.removeAll(Collections.singleton(null));
                    for (TimeLapse timeLapse : otherIntervals) {
                        intervalsFromSpecifiedDate.add(timeLapse);
                    }
                    selectedClassroom.setIntervals(intervalsFromSpecifiedDate);
                    saveBookingToDB(selectedClassroom);
                    mainActivity.buildEventsListView(listView, selectedClassroom, dateInMillis);
                }
            );
        }
        return view;
    }

    public void displayStartTimePicker(final Classroom classroom, ListView listView) {
        String selectedDate = getStringDateFromTimeMillis(dateInMillis);
        TimePickerDialog startTimePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                double startPickedTime = getComposedHour(hourOfDay, minute);
                displayEndTimePicker(startPickedTime, classroom, selectedDate, listView);
            }
        }, true);

        boolean[] disabledHours = classroom.checkStartTimeAvailability(selectedDate);
        List<Timepoint> selectableTimes = new ArrayList<>();
        for (int i = 0; i < 48; i++) {
            if (disabledHours[i] != true) {
                double j = (double) i / 2;
                selectableTimes.add(new Timepoint((int) j, (int) ((j % 1) * 60)));
            }
        }

        Timepoint[] selectableStartTimes = new Timepoint[selectableTimes.size()];
        selectableStartTimes = selectableTimes.toArray(selectableStartTimes);

        startTimePickerDialog.setSelectableTimes(selectableStartTimes);
        startTimePickerDialog.setTitle("Pick meeting start time");
        startTimePickerDialog.show(getActivity().getFragmentManager(), "Pick meeting start time");
    }

    public void displayEndTimePicker(double startPickedTime, Classroom classroom, String selectedDate, ListView listView) {
        TimePickerDialog endTimePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                classroom.bookClassroom(startPickedTime, getComposedHour(hourOfDay, minute), auth.getCurrentUser().getEmail(), dateInMillis);
                saveBookingToDB(classroom);
                mainActivity.buildEventsListView(listView, classroom, dateInMillis);
                ((MainActivity) getActivity()).notifyAdapter();
            }
        }, true);
        boolean[] disabledHoursEndTime = classroom.checkEndTimeAvailability(selectedDate, startPickedTime);
        List<Timepoint> selectableTimesE = new ArrayList<>();
        for (int i = 0; i < 48; i++) {
            if (disabledHoursEndTime[i] != true && (double) i / 2 > startPickedTime) {
                double j = (double) i / 2;
                selectableTimesE.add(new Timepoint((int) j, (int) ((j % 1) * 60)));
            }
        }
        Timepoint[] selectableEndTimes = new Timepoint[selectableTimesE.size()];
        selectableEndTimes = selectableTimesE.toArray(selectableEndTimes);

        endTimePickerDialog.setSelectableTimes(selectableEndTimes);
        endTimePickerDialog.setTitle("Pick meeting end time");
        endTimePickerDialog.show(getActivity().getFragmentManager(), "Pick meeting end time");
    }

    private void saveBookingToDB(Classroom classroom) {
        classroom.sortIntervals();
        classroomRef = classroomsRef.child("classroom" + classroom.getId());
        classroomRef.setValue(classroom);
    }

    private double getComposedHour(int hourOfDay, int minute) {
        double minutes = minute == 0 ? 0 : 0.5;
        return hourOfDay + minutes;
    }
}
