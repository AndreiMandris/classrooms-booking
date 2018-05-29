package com.etti.classroomsbooking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends ArrayAdapter<String> {
    ArrayList<String> arrayList;
    private TextView textView;
    public EventsAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }


}
