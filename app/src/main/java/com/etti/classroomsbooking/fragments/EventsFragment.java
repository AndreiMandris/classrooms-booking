package com.etti.classroomsbooking.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.etti.classroomsbooking.R;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment {



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
        ListView eventsListView = (ListView) view.findViewById(R.id.list_events);
        HashMap<String, String> events = new HashMap<>();

        return view;
    }

}
