package com.example.hostelbiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthenticationActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText mobileNumberEditText;
    private EditText addressEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button loginButton;
    private TextView textView;

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

        firebaseAuth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.skipTv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
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
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(AuthenticationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                            navigateToMainActivity(user);
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
        String userType = getUserType(user);
       // if (userType.equals("student")) {
            Intent studentIntent = new Intent(AuthenticationActivity.this, MainActivity.class);
            startActivity(studentIntent);
     //   } else if (userType.equals("staff")) {
     //       Intent staffIntent = new Intent(AuthenticationActivity.this, MainActivityStaff.class);
     //       startActivity(staffIntent);
     //   }
        ;
    }

    private String getUserType(FirebaseUser user) {
        // You can implement your own logic to determine the user type based on user data or Firebase user properties.
        // For example, you could use custom user claims, a separate "users" collection in Firestore, or a specific naming convention in user email addresses.
        // Here, we are assuming that the user type is determined based on the email address.
        String email = user.getEmail();
        if (email != null && email.endsWith("@student.example.com")) {
            return "student";
        } else if (email != null && email.endsWith("@staff.example.com")) {
            return "staff";
        }
        return "";
    }
}
