package com.cuhp.shahpur.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cuhp.shahpur.R;
import com.cuhp.shahpur.faculty.AddFaculty;
import com.cuhp.shahpur.faculty.FacultyData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddStudent extends AppCompatActivity {

    private ImageView addStudentImage;
    private EditText addStudentName, addStudentEmail, addStudentFatherName, addStudentRegn;

    private Spinner studentCategory,studentYear;
    private Button addStudentBtn;
    private Bitmap bitmap = null;
    private final int REQ=1;
    private String category,year;
    private String name, email, fatherName, registrationNumber, downloadUrl ="";
    private ProgressDialog pd;
    private DatabaseReference reference, dbRef;
    private StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        addStudentImage = findViewById(R.id.addStudentImage);
        addStudentName = findViewById(R.id.addStudentName);
        addStudentFatherName = findViewById(R.id.addStudentFatherName);
        addStudentRegn = findViewById(R.id.addStudentRegn);
        addStudentEmail = findViewById(R.id.addStudentEmail);
        addStudentBtn = findViewById(R.id.addStudentBtn);
        studentCategory = findViewById(R.id.studentCategory);
        studentYear = findViewById(R.id.studentYear);

        pd = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Student");
        storageReference = FirebaseStorage.getInstance().getReference();

        String[] items = new String[]{"Select Category","Computer Science and Informatics","Mathematics","Library Science"};

        studentCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));

        studentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = studentCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        String[] item1 = new String[]{"Select Category","First Year","Final Year"};

        studentYear.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,item1));

        studentYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = studentYear.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        addStudentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        name = addStudentName.getText().toString();
        email = addStudentEmail.getText().toString();
        fatherName = addStudentFatherName.getText().toString();
        registrationNumber = addStudentRegn.getText().toString();

        if (name.isEmpty()){
            addStudentName.setError("Can't be empty");
            addStudentName.requestFocus();
        }else if (email.isEmpty()) {
            addStudentEmail.setError("Can't be empty");
            addStudentEmail.requestFocus();
        } else if (fatherName.isEmpty()) {
            addStudentFatherName.setError("Can't be empty");
            addStudentFatherName.requestFocus();
        } else if (registrationNumber.isEmpty()) {
            addStudentRegn.setError("Can't be empty");
            addStudentRegn.requestFocus();
        }else if (category.equals("Select Category")) {
            Toast.makeText(this, "Provide Student's Department", Toast.LENGTH_SHORT).show();
        }else if (year.equals("Select Category")) {
            Toast.makeText(this, "Provide Student's Category", Toast.LENGTH_SHORT).show();
        }else if (bitmap == null) {
            pd.setMessage("Updating student details");
            pd.show();
            uploadData();
        }
        else {
            pd.setMessage("Updating student details");
            pd.show();
            uploadImage();
        }
    }

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImg =baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Student").child(category).child(finalImg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(AddStudent.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                }
                else {
                    pd.dismiss();
                    Toast.makeText(AddStudent.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void uploadData() {
        dbRef =reference.child(category).child(year);
        final String uniqueKey = dbRef.push().getKey();

        StudentData studentData = new StudentData(name,registrationNumber,fatherName,email,downloadUrl,uniqueKey);

        dbRef.child(uniqueKey).setValue(studentData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(AddStudent.this, "Student details successfully added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddStudent.this, "ERROR 404 'Something went wrong'", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void openGallery(){
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            addStudentImage.setImageBitmap(bitmap);
        }
    }
}