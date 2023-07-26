package com.example.hostelbiz;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivityStaff extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List<String> studentNamesList;
    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_staff);
        drawerLayout = findViewById(R.id.drawer_layout);

        recyclerView = findViewById(R.id.recycler_view_students);

        Toolbar toolbar = findViewById(R.id.toolbar);
  //      setSupportActionBar(toolbar);


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

   //     navigationView.setNavigationItemSelectedListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("students");

        studentNamesList = new ArrayList<>();
        studentAdapter = new StudentAdapter(studentNamesList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(studentAdapter);

        fetchStudentNames();
    }

    private void fetchStudentNames() {
        databaseReference.get().addOnSuccessListener(dataSnapshot -> {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                String name = snapshot.child("name").getValue(String.class);
                if (name != null) {
                    studentNamesList.add(name);
                }
            }
            studentAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(MainActivityStaff.this, "Failed to fetch student names", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_profile) {
            // Handle profile menu item click
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_contact_admin) {
            // Handle contact admin menu item click
            Toast.makeText(this, "Contact Admin clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_logout) {
            // Handle logout menu item click
            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_notifications) {
            // Handle notifications menu item click
            Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show();
        }



        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
