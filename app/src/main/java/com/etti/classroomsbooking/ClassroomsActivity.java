package com.etti.classroomsbooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.etti.classroomsbooking.util.*;

import com.etti.classroomsbooking.model.Classroom;

import java.math.BigDecimal;
import java.util.HashMap;

import static com.etti.classroomsbooking.util.Constant.ROOM;

public class ClassroomsActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrooms);
        listView = findViewById(android.R.id.list);
        String[] roomNames = {"A01", "A02", "A03", "A04", "A05"};
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , roomNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = getIntent().getExtras();
                args.putString(ROOM, parent.getItemAtPosition(position).toString());
                startActivity(new Intent(getApplicationContext(), EventsActivity.class), args);
            }
        });

    }
}
