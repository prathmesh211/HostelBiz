package com.example.hostelbiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hostelbiz.ui.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<User> studentNamesList;
    private StudentAdapter studentAdapter;

    private CollectionReference usersCollectionRef;

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(MainActivityStaff.this, MainActivity.class));
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_staff);
        drawerLayout = findViewById(R.id.drawer_layout);
        firebaseFirestore = FirebaseFirestore.getInstance();
        usersCollectionRef = firebaseFirestore.collection("students");
        recyclerView = findViewById(R.id.recycler_view_students);

        Toolbar toolbar = findViewById(R.id.toolbar);
  //      setSupportActionBar(toolbar);


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

   //     navigationView.setNavigationItemSelectedListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("students");

        studentNamesList = new ArrayList<>();
        studentAdapter = new StudentAdapter(this, studentNamesList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(studentAdapter);

        fetchStudentNames();
    }

    private void fetchStudentNames() {
        usersCollectionRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Clear the existing list of studentNames
                            studentNamesList.clear();

                            // Loop through the documents and add User objects to the list
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                User user = documentSnapshot.toObject(User.class);
                                studentNamesList.add(user);
                            }

                            // Notify the adapter that the data has changed
                            studentAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivityStaff.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
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

}
