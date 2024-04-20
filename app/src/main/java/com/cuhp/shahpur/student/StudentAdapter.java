package com.cuhp.shahpur.student;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cuhp.shahpur.R;
import com.cuhp.shahpur.faculty.FacultyAdapter;
import com.cuhp.shahpur.faculty.FacultyData;
import com.cuhp.shahpur.faculty.UpdateFacultyActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewAdapter> {
    private List<StudentData> list;
    private Context context;
    private String category,year;

    public StudentAdapter(List<StudentData> list, Context context, String category,String year) {
        this.list = list;
        this.context = context;
        this.category = category;
        this.year = year;
    }

    @NonNull
    @Override
    public StudentViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_data_layout, parent, false);
        return new StudentAdapter.StudentViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewAdapter holder, int position) {
        StudentData item = list.get(position);
        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.registrationNumber.setText(item.getRegistrationNumber());
        try {
            Picasso.get().load(item.getImage()).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateStudentActivity.class);
                intent.putExtra("name",item.getName());
                intent.putExtra("email",item.getEmail());
                intent.putExtra("fatherName",item.getFatherName());
                intent.putExtra("registrationNumber",item.getRegistrationNumber());
                intent.putExtra("image",item.getImage());
                intent.putExtra("key",item.getKey());
                intent.putExtra("category",category);
                intent.putExtra("year",year);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StudentViewAdapter extends RecyclerView.ViewHolder {

        private TextView name, email, registrationNumber;
        private ImageView imageView;
        private Button update;

        public StudentViewAdapter(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.studentName);
            email = itemView.findViewById(R.id.studentEmail);
            registrationNumber = itemView.findViewById(R.id.studentRegn);
            imageView = itemView.findViewById(R.id.studentImage);
            update = itemView.findViewById(R.id.studentUpdate);
        }
    }
}
