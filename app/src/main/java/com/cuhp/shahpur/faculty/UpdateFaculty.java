package com.cuhp.shahpur.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView csiDepartment, mathsDepartment, libraryDepartment;
    private LinearLayout csiNoData, mathsNoData, libraryNoData;
    private List<FacultyData> list1,list2,list3;

    private FacultyAdapter adapter;

    private DatabaseReference reference, dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);

        csiDepartment = findViewById(R.id.csiDepartment);
        mathsDepartment = findViewById(R.id.mathsDepartment);
        libraryDepartment = findViewById(R.id.libraryDepartment);

        csiNoData = findViewById(R.id.csiNoData);
        mathsNoData = findViewById(R.id.mathsNoData);
        libraryNoData = findViewById(R.id.libraryNoData);

        reference = FirebaseDatabase.getInstance().getReference().child("Faculty");
        
        csiDepartment();
        mathsDepartment();
        libraryDepartment();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateFaculty.this,AddFaculty.class));
            }
        });

    }

    private void csiDepartment() {
        dbRef = reference.child("Computer Science and Informatics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    csiNoData.setVisibility(View.VISIBLE);
                    csiDepartment.setVisibility(View.GONE);
                }else {
                    csiNoData.setVisibility(View.GONE);
                    csiDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        FacultyData data = snapshot.getValue(FacultyData.class);
                        list1.add(data);
                    }
                    csiDepartment.setHasFixedSize(true);
                    csiDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new FacultyAdapter(list1,UpdateFaculty.this, "Computer Science and Informatics");
                    csiDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mathsDepartment() {
        dbRef =reference.child("Mathematics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    mathsNoData.setVisibility(View.VISIBLE);
                    mathsDepartment.setVisibility(View.GONE);
                }else {
                    mathsNoData.setVisibility(View.GONE);
                    mathsDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        FacultyData data = snapshot.getValue(FacultyData.class);
                        list2.add(data);
                    }
                    mathsDepartment.setHasFixedSize(true);
                    mathsDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new FacultyAdapter(list2,UpdateFaculty.this,"Mathematics");
                    mathsDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void libraryDepartment() {
        dbRef =reference.child("Library Science");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    libraryNoData.setVisibility(View.VISIBLE);
                    libraryDepartment.setVisibility(View.GONE);
                }else {
                    libraryNoData.setVisibility(View.GONE);
                    libraryDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        FacultyData data = snapshot.getValue(FacultyData.class);
                        list3.add(data);
                    }
                    libraryDepartment.setHasFixedSize(true);
                    libraryDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new FacultyAdapter(list3,UpdateFaculty.this,"Library Science");
                    libraryDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}