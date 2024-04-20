package com.cuhp.shahpur.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cuhp.shahpur.R;

public class UpdateStudents extends AppCompatActivity {
    private CardView csiStudents, mathsStudents, libraryStudents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_students);

        csiStudents = findViewById(R.id.csiStudents);
        mathsStudents = findViewById(R.id.mathsStudents);
        libraryStudents = findViewById(R.id.libraryStudents);

        csiStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateStudents.this, CsiStudents.class));
            }
        });
        mathsStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateStudents.this, MathsStudents.class));
            }
        });
        libraryStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateStudents.this, LibraryStudents.class));
            }
        });
    }
}