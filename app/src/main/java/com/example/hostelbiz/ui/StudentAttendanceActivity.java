package com.example.hostelbiz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.hostelbiz.MainActivity;
import com.example.hostelbiz.R;

public class StudentAttendanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            Intent intent = new Intent(StudentAttendanceActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown( keyCode, event );
    }
}