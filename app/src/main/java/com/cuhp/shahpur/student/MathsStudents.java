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

public class MathsStudents extends AppCompatActivity {

    FloatingActionButton fab2;
    private RecyclerView firstYear,finalYear;
    private LinearLayout firstYearNoData,finalYearNoData;
    private List<StudentData> list1,list2;

    private StudentAdapter adapter;

    private DatabaseReference reference, dbRef;

    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_students);

        firstYear = findViewById(R.id.firstYear);
        finalYear = findViewById(R.id.finalYear);

        firstYearNoData = findViewById(R.id.firstYearNoData);
        finalYearNoData = findViewById(R.id.firstYearNoData);

        reference = FirebaseDatabase.getInstance().getReference().child("Student").child("Mathematics");

        firstYear();
        finalYear();

        fab2 =findViewById(R.id.fab2);

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MathsStudents.this, AddStudent.class));
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
                    firstYear.setLayoutManager(new LinearLayoutManager(MathsStudents.this));
                    adapter = new StudentAdapter(list1,MathsStudents.this, "Mathematics","First Year");
                    firstYear.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MathsStudents.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
                    finalYear.setLayoutManager(new LinearLayoutManager(MathsStudents.this));
                    adapter = new StudentAdapter(list2,MathsStudents.this, "Mathematics","Final Year");
                    finalYear.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MathsStudents.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}