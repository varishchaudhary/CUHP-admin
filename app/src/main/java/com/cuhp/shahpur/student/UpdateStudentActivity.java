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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cuhp.shahpur.R;
import com.cuhp.shahpur.faculty.UpdateFaculty;
import com.cuhp.shahpur.faculty.UpdateFacultyActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateStudentActivity extends AppCompatActivity {

    private ImageView updateStudentImage;
    private EditText updateStudentName, updateStudentEmail, updateStudentFatherName, updateStudentRegn;
    private Button updateStudentBtn, deleteStudentBtn;

    private final int REQ=1;
    private Bitmap bitmap = null;
    private String name, email,fatherName,registrationNumber,image;

    private String uniqueKey,category,year;

    private ProgressDialog pd;
    private StorageReference storageReference;
    private DatabaseReference reference,dbRef;
    private String downloadUrl;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);

        uniqueKey = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");
        year = getIntent().getStringExtra("year");

        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        fatherName=getIntent().getStringExtra("fatherName");
        registrationNumber=getIntent().getStringExtra("registrationNumber");
        image=getIntent().getStringExtra("image");

        updateStudentName = findViewById(R.id.updateStudentName);
        updateStudentRegn = findViewById(R.id.updateStudentRegn);
        updateStudentFatherName = findViewById(R.id.updateStudentFatherName);
        updateStudentEmail = findViewById(R.id.updateStudentEmail);
        updateStudentImage = findViewById(R.id.updateStudentImage);
        updateStudentBtn = findViewById(R.id.updateStudentBtn);
        deleteStudentBtn = findViewById(R.id.deleteStudentBtn);

        reference = FirebaseDatabase.getInstance().getReference().child("Student");
        storageReference = FirebaseStorage.getInstance().getReference();

        try {
            Picasso.get().load(image).into(updateStudentImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateStudentName.setText(name);
        updateStudentRegn.setText(registrationNumber);
        updateStudentFatherName.setText(fatherName);
        updateStudentEmail.setText(email);

        updateStudentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        updateStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = updateStudentName.getText().toString();
                registrationNumber = updateStudentRegn.getText().toString();
                fatherName = updateStudentFatherName.getText().toString();
                email = updateStudentEmail.getText().toString();
                checkValidation();
            }
        });

        deleteStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
    }
    private void checkValidation() {
        if (name.isEmpty()){
            updateStudentName.setError("Can't be empty");
            updateStudentName.requestFocus();
        } else if (registrationNumber.isEmpty()) {
            updateStudentRegn.setError("Can't be empty");
            updateStudentRegn.requestFocus();
        }else if (fatherName.isEmpty()) {
            updateStudentFatherName.setError("Can't be empty");
            updateStudentFatherName.requestFocus();
        }else if (email.isEmpty()) {
            updateStudentEmail.setError("Can't be empty");
            updateStudentEmail.requestFocus();
        }else if(bitmap == null){
            updateData(image);
        }else {
            uploadImage();
        }
    }
    private void updateData(String s){
        HashMap hp = new HashMap();
        hp.put("name",name);
        hp.put("registrationNumber",registrationNumber);
        hp.put("fatherName",fatherName);
        hp.put("email",email);
        hp.put("image",s);

        reference.child(category).child(year).child(uniqueKey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateStudentActivity.this, "Student updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateStudentActivity.this, UpdateStudents.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateStudentActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImg =baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Student").child(category).child(finalImg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(UpdateStudentActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    updateData(downloadUrl);
                                }
                            });
                        }
                    });
                }
                else {
                    pd.dismiss();
                    Toast.makeText(UpdateStudentActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void deleteData() {
        reference.child(category).child(year).child(uniqueKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UpdateStudentActivity.this, "Student removed Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateStudentActivity.this,UpdateStudents.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateStudentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
            updateStudentImage.setImageBitmap(bitmap);
        }
    }
}