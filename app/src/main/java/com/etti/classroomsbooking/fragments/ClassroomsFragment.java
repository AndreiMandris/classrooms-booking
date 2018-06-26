package com.etti.classroomsbooking.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.etti.classroomsbooking.MainActivity;
import com.etti.classroomsbooking.R;
import com.etti.classroomsbooking.model.Classroom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.HashMap;

import static com.etti.classroomsbooking.util.Constant.ROOM;
import static com.etti.classroomsbooking.util.Constant.ROOM_POSITION;

public class ClassroomsFragment extends Fragment {
    private static final String TAG = ClassroomsFragment.class.getSimpleName();
    ListAdapter adapter;
    ListView listView;

    public ClassroomsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classrooms, container, false);
        listView = view.findViewById(R.id.list_classrooms);
        String[] roomNames = {"A01", "A02", "A03", "B01", "B02", "B03"};
        adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1 , roomNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Bundle args = getArguments();
            args.putInt(ROOM_POSITION, position);
            Fragment eventsFragment = new EventsFragment();
            eventsFragment.setArguments(args);
            ((MainActivity) getActivity()).moveToFragment(eventsFragment);
        });


        return view;
    }

}
