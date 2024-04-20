package com.cuhp.shahpur.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cuhp.shahpur.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LibraryStudents extends AppCompatActivity {

    FloatingActionButton fab3;
    private RecyclerView firstYear,finalYear;
    private LinearLayout firstYearNoData,finalYearNoData;
    private List<StudentData> list1,list2;

    private StudentAdapter adapter;

    private DatabaseReference reference, dbRef;

    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_students);

        firstYear = findViewById(R.id.firstYear);
        finalYear = findViewById(R.id.finalYear);

        firstYearNoData = findViewById(R.id.firstYearNoData);
        finalYearNoData = findViewById(R.id.firstYearNoData);

        reference = FirebaseDatabase.getInstance().getReference().child("Student").child("Library Science");

        firstYear();
        finalYear();

        fab3 = findViewById(R.id.fab3);

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LibraryStudents.this, AddStudent.class));
            }
        });
    }
    private void firstYear() {
        dbRef = reference.child("First Year");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    firstYearNoData.setVisibility(View.VISIBLE);
                    firstYear.setVisibility(View.GONE);
                }else {
                    firstYearNoData.setVisibility(View.GONE);
                    firstYear.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        StudentData data = snapshot.getValue(StudentData.class);
                        list1.add(data);
                    }
                    firstYear.setHasFixedSize(true);
                    firstYear.setLayoutManager(new LinearLayoutManager(LibraryStudents.this));
                    adapter = new StudentAdapter(list1,LibraryStudents.this, "Library Science","First Year");
                    firstYear.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LibraryStudents.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void finalYear() {
        dbRef = reference.child("Final Year");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    finalYearNoData.setVisibility(View.VISIBLE);
                    finalYear.setVisibility(View.GONE);
                }else {
                    finalYearNoData.setVisibility(View.GONE);
                    finalYear.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        StudentData data = snapshot.getValue(StudentData.class);
                        list2.add(data);
                    }
                    finalYear.setHasFixedSize(true);
                    finalYear.setLayoutManager(new LinearLayoutManager(LibraryStudents.this));
                    adapter = new StudentAdapter(list2,LibraryStudents.this, "Library Science","Final Year");
                    finalYear.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LibraryStudents.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}