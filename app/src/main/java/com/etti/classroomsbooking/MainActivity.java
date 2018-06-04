package com.etti.classroomsbooking;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.etti.classroomsbooking.fragments.CalendarFragment;
import com.etti.classroomsbooking.fragments.ClassroomsFragment;
import com.etti.classroomsbooking.fragments.EventsFragment;
import com.etti.classroomsbooking.fragments.ScannedRoomFragment;
import com.etti.classroomsbooking.login.LoginActivity;
import com.etti.classroomsbooking.model.Classroom;
import com.etti.classroomsbooking.model.TimeLapse;
import com.etti.classroomsbooking.util.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.etti.classroomsbooking.ScanQRActivity.scannedQRCode;
import static com.etti.classroomsbooking.util.Constant.EVENT_CHECK_BOX;
import static com.etti.classroomsbooking.util.Constant.REQUEST_CODE_QR;
import static com.etti.classroomsbooking.util.Constant.TIME_INTERVAL;
import static com.etti.classroomsbooking.util.Constant.USER;
import static com.etti.classroomsbooking.util.Utility.formatTime;
import static com.etti.classroomsbooking.util.Utility.getStringDateFromTimeMillis;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;
    private SimpleAdapter adapter;
    private boolean mReturningWithResult = false;
    private TextView navHeaderTitle;
    private List<Classroom> classrooms;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE_QR && scannedQRCode != -1) {
                mReturningWithResult = true;
            }
        }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mReturningWithResult) {
            moveToFragment(new ScannedRoomFragment());
        }
        mReturningWithResult = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu2);
        firebaseAuth = FirebaseAuth.getInstance();

        classrooms = new ArrayList<>();
        classrooms.add(new Classroom(0, "A01"));
        classrooms.add(new Classroom(1, "A02"));
        classrooms.add(new Classroom(2, "A03"));
        classrooms.add(new Classroom(3, "B01"));
        classrooms.add(new Classroom(4, "B02"));
        classrooms.add(new Classroom(5, "B03"));
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        moveToFragment(new ClassroomsFragment());
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch(id){
                case (R.id.nav_logout):
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish(); break;
                case (R.id.scan_qr):
                    Intent i = new Intent(this, ScanQRActivity.class);
                    startActivityForResult(i, REQUEST_CODE_QR); break;
                case (R.id.view_calendar):
                    moveToFragment(new CalendarFragment());
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
        View header = navigationView.getHeaderView(0);
        navHeaderTitle = header.findViewById(R.id.nav_header_title);
        navHeaderTitle.setText("Welcome, \n" + firebaseAuth.getCurrentUser().getEmail());
    }

    public List<Classroom> getClassrooms() {
        return this.classrooms;
    }

    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public void buildEventsListView(ListView listView, Classroom classroom, long selectedDateInMillis){
        ArrayList<TimeLapse> classroomIntervals = classroom.getIntervals();
        if (classroomIntervals == null){
            return;
        }
        classroom.sortIntervals();
        Map<String, String> eventsDetails = new LinkedHashMap<>();
        for (TimeLapse interval : classroomIntervals){

            if (getStringDateFromTimeMillis(selectedDateInMillis).equals(interval.getDate())) {
                double startTime = interval.getStartTime();
                double endTime = interval.getEndTime();
                String startTimeFormatted = formatTime((int) startTime) + ":" + (startTime % 1 == 0.0 ? "00" : "30");
                String endTimeFormatted = formatTime((int) endTime) + ":" + (endTime % 1 == 0.0 ? "00" : "30");
                String timeInterval = "" + startTimeFormatted + " - " + endTimeFormatted;
                eventsDetails.put(timeInterval, interval.getUserName());
            }
        }
        List<Map<String,String>> listItems = new ArrayList<>();
        adapter = new SimpleAdapter(this, listItems, R.layout.event_item,
                new String[]{TIME_INTERVAL, USER, "CHECKBOX"}, new int[]{R.id.textView1, R.id.textView2, R.id.eventCheckBox});
        Iterator iterator = eventsDetails.entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            Map<String, String> pair = new HashMap<>();
            pair.put(TIME_INTERVAL, entry.getKey());
            pair.put(USER, entry.getValue());
            pair.put(USER, entry.getValue());
            listItems.add(pair);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Constant.checkedEventsList.set(position, true);
                ViewGroup row = (ViewGroup) listView.getChildAt(position);
                CheckBox checkBox = row.findViewById(R.id.eventCheckBox);
                checkBox.setChecked(true);
            }
        });
        listView.setAdapter(adapter);
    }

    public void notifyAdapter(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void moveToFragment(Fragment fragment){
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment, fragment.getTag())
                .addToBackStack(fragment.getTag())
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void handleResult(Result result) {

    }
}
