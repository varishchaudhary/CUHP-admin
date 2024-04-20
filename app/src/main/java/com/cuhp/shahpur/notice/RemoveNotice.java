package com.cuhp.shahpur.notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cuhp.shahpur.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RemoveNotice extends AppCompatActivity {

    private RecyclerView deleteNoticeRecycler;
    private ProgressBar progressBar;
    private ArrayList<NoticeData>list;
    private NoticeAdapter adapter;

    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_notice);

        deleteNoticeRecycler = findViewById(R.id.deleteNoticeRecycler);
        progressBar = findViewById(R.id.progressBar);

        reference = FirebaseDatabase.getInstance().getReference().child("Notice");

        deleteNoticeRecycler.setLayoutManager(new LinearLayoutManager(this));
        deleteNoticeRecycler.setHasFixedSize(true);
        
        getNotice();
    }

    private void getNotice() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NoticeData data = dataSnapshot.getValue(NoticeData.class);
                    list.add(data);
                }
                adapter = new NoticeAdapter(RemoveNotice.this,list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                deleteNoticeRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RemoveNotice.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}