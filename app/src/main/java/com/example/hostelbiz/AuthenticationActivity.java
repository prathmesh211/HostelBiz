package com.example.hostelbiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hostelbiz.ui.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class AuthenticationActivity extends FragmentActivity {
    private EditText nameEditText;
    private EditText mobileNumberEditText;
    private EditText addressEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button loginButton;
    private TextView textView;

    private RadioGroup radioGroup;
    private RadioButton selectedRadioButton;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        nameEditText = findViewById(R.id.edit_text_name);
        mobileNumberEditText = findViewById(R.id.edit_text_mobile_number);
        addressEditText = findViewById(R.id.edit_text_address);
        passwordEditText = findViewById(R.id.edit_text_password);
        confirmPasswordEditText = findViewById(R.id.edit_text_confirm_password);
        loginButton = findViewById(R.id.button_login);
        radioGroup = findViewById(R.id.radio_group_user_type);
        firebaseAuth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.loginTv);
        selectedRadioButton = findViewById(radioGroup.getChildAt(0).getId());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment mainFragment = new LoginFragment();

                // Get the FragmentManager and start a FragmentTransaction
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Replace the current fragment container with the new MainFragment
            //    fragmentTransaction.replace(R.id.fragmentContainer, mainFragment);

                // Add the transaction to the back stack (optional)
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEditText.getText().toString().trim();
                String mobileNumber = mobileNumberEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (name.isEmpty() || mobileNumber.isEmpty() || address.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(AuthenticationActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(AuthenticationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 8) {
                    Toast.makeText(AuthenticationActivity.this, "Password should be at least 8 characters", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(name, mobileNumber, address, password);
                }
            }
        });
    }


    private void registerUser(final String name, final String mobileNumber, final String address, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(mobileNumber + "@example.com", password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        User user = new User(name, mobileNumber, address);
                        FirebaseUser fuser = firebaseAuth.getCurrentUser();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                if(radioGroup.getCheckedRadioButtonId() == RadioGroup.NO_ID){
                                    Toast.makeText(AuthenticationActivity.this, "Please select whether you're Student or Staff.", Toast.LENGTH_SHORT).show();
                                } else {
                                    int selectedId = radioGroup.getCheckedRadioButtonId();
                                    selectedRadioButton = (RadioButton) findViewById(selectedId);
                                }

                            }
                        });

                        if(selectedRadioButton.getText().toString().equals("Student")){

                            db.collection("students").document(fuser.getUid()).set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                            } else {
                                                Toast.makeText(AuthenticationActivity.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            db.collection("staff").document(fuser.getUid()).set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                            } else {
                                                Toast.makeText(AuthenticationActivity.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }


                        if (fuser != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            fuser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(AuthenticationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                            navigateToMainActivity(fuser);

                                        }
                                    });
                        }
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(AuthenticationActivity.this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthUserCollisionException e) {
                            Toast.makeText(AuthenticationActivity.this, "User with this mobile number already exists", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(AuthenticationActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void navigateToMainActivity(FirebaseUser user) {

        if(selectedRadioButton.getText().toString().equals("Student")){
            Intent studentIntent = new Intent(AuthenticationActivity.this, MainActivity.class);
            startActivity(studentIntent);
        } else {
            startActivity((new Intent(AuthenticationActivity.this, MainActivityStaff.class)));
        }
    }


}
