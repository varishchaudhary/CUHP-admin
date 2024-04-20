package com.cuhp.shahpur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cuhp.shahpur.notice.RemoveNotice;
import com.cuhp.shahpur.notice.UploadNotice;
import com.cuhp.shahpur.student.UpdateStudents;
import com.cuhp.shahpur.faculty.UpdateFaculty;

public class MainActivity extends AppCompatActivity {
    CardView addNotice,addFacultyDetails,addStudentDetails,removeNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNotice = findViewById(R.id.addNotice);
        addFacultyDetails = findViewById(R.id.addFacultyDetails);
        addStudentDetails = findViewById(R.id.addStudentDetails);
        removeNotice = findViewById(R.id.removeNotice);

        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UploadNotice.class));
            }
        });

        addFacultyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UpdateFaculty.class));
            }
        });

        addStudentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UpdateStudents.class));
            }
        });

        removeNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RemoveNotice.class));
            }
        });
    }
}