package com.etti.classroomsbooking;

import android.app.TimePickerDialog;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.etti.classroomsbooking.fragments.CalendarFragment;
import com.etti.classroomsbooking.fragments.EventsFragment;
import com.etti.classroomsbooking.fragments.ScannedRoomFragment;
import com.etti.classroomsbooking.login.LoginActivity;
import com.etti.classroomsbooking.model.Classroom;
import com.etti.classroomsbooking.model.TimeLapse;
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

import static com.etti.classroomsbooking.util.Constant.EVENT_CHECK_BOX;
import static com.etti.classroomsbooking.util.Constant.REQUEST_CODE_QR;
import static com.etti.classroomsbooking.util.Constant.TIME_INTERVAL;
import static com.etti.classroomsbooking.util.Constant.USER;
import static com.etti.classroomsbooking.util.Constant.formatTime;
import static com.etti.classroomsbooking.util.Constant.getStringDateFromTimeMillis;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private Button buttonLogout;
    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;
    SimpleAdapter adapter;


    private List<Classroom> classrooms;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED && data != null) {
            if (requestCode == REQUEST_CODE_QR) {
                moveToFragment(new ScannedRoomFragment());
            }
        }
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

        classrooms = new ArrayList<>();
        classrooms.add(new Classroom(0, "A01"));
        classrooms.add(new Classroom(1, "A02"));
        classrooms.add(new Classroom(2, "A03"));
        classrooms.add(new Classroom(3, "B01"));
        classrooms.add(new Classroom(4, "B02"));
        classrooms.add(new Classroom(5, "B03"));
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new CalendarFragment(), new CalendarFragment().getTag())
                .addToBackStack(null)
                .commit();
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
                    //startActivity(new Intent(getApplicationContext(), ScanQRActivity.class));
                    startActivityForResult(i, REQUEST_CODE_QR);
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
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
