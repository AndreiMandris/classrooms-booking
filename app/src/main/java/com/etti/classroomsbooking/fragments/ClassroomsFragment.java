package com.etti.classroomsbooking.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.etti.classroomsbooking.EventsActivity;
import com.etti.classroomsbooking.R;
import com.etti.classroomsbooking.model.Classroom;

import java.math.BigDecimal;
import java.util.HashMap;

import static com.etti.classroomsbooking.util.Constant.ROOM;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ClassroomsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassroomsFragment extends Fragment {

    static {
        HashMap<Integer, Classroom> classrooms = new HashMap<>();
        classrooms.put(0, new Classroom(new BigDecimal(0), "A01"));
        classrooms.put(1, new Classroom(new BigDecimal(1), "A02"));
        classrooms.put(2, new Classroom(new BigDecimal(2), "A03"));
        classrooms.put(4, new Classroom(new BigDecimal(3), "A05"));
        classrooms.put(5, new Classroom(new BigDecimal(4), "A06"));
    }
    ListAdapter adapter;
    ListView listView;


    public ClassroomsFragment() {
        // Required empty public constructor
    }

    public static ClassroomsFragment newInstance(String param1, String param2) {
        ClassroomsFragment fragment = new ClassroomsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classrooms, container, false);
        listView = view.findViewById(R.id.list_classrooms);
        String[] roomNames = {"A01", "A02", "A03", "A04", "A05"};
        adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1 , roomNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Bundle args = getArguments();
            args.putString(ROOM, parent.getItemAtPosition(position).toString());
            Fragment fragment = new ClassroomsFragment();
            fragment.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new EventsFragment(), new EventsFragment().getTag())
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }

}
