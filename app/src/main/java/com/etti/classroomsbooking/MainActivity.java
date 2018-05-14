package com.etti.classroomsbooking;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.etti.classroomsbooking.fragments.CalendarFragment;
import com.etti.classroomsbooking.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.etti.classroomsbooking.util.Constant.TIME_INTERVAL;
import static com.etti.classroomsbooking.util.Constant.USER;

public class MainActivity extends AppCompatActivity{

    private Button buttonLogout;
    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu2);

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
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    public void buildListView(ListView listView){
        Map<String, String> eventsDetails = new HashMap<>();
        eventsDetails.put("09:00 - 10:00", "andrei.mandris@gmail.com");
        eventsDetails.put("10:00 - 10:30", "andrei.mandris@gmail.com");
        eventsDetails.put("11:00 - 12:00", "andrei.mandris@gmail.com");
        eventsDetails.put("14:00 - 14:30", "andrei.mandris@gmail.com");

        List<Map<String,String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.event_item,
                new String[]{TIME_INTERVAL, USER}, new int[]{R.id.textView1, R.id.textView2});
        Iterator iterator = eventsDetails.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            Map<String, String> pair = new HashMap<>();
            pair.put(TIME_INTERVAL, entry.getKey());
            pair.put(USER, entry.getValue());
            listItems.add(pair);
        }
        listView.setAdapter(adapter);
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
}
