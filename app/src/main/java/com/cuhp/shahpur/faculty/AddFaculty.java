package com.cuhp.shahpur.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class AddFaculty extends AppCompatActivity {

    private ImageView addFacultyImage;
    private EditText addFacultyName, addFacultyEmail, addFacultyPost;
    private Spinner facultyCategory;
    private Button addFacultyBtn;
    private Bitmap bitmap = null;
    private final int REQ=1;
    private String category;
    private String name, email, post, downloadUrl ="";
    private ProgressDialog pd;
    private DatabaseReference reference, dbRef;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);

        addFacultyImage = findViewById(R.id.addFacultyImage);
        addFacultyName = findViewById(R.id.addFacultyName);
        addFacultyEmail = findViewById(R.id.addFacultyEmail);
        addFacultyPost = findViewById(R.id.addFacultyPost);
        facultyCategory = findViewById(R.id.facultyCategory);
        addFacultyBtn = findViewById(R.id.addFacultyBtn);

        reference = FirebaseDatabase.getInstance().getReference().child("Faculty");
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        String[] items = new String[]{"Select Category","Computer Science and Informatics","Mathematics","Library Science"};

        facultyCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));

        facultyCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = facultyCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        addFacultyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        name = addFacultyName.getText().toString();
        email = addFacultyEmail.getText().toString();
        post = addFacultyPost.getText().toString();
        if (name.isEmpty()){
            addFacultyName.setError("Can't be empty");
            addFacultyName.requestFocus();
        }else if (email.isEmpty()) {
            addFacultyEmail.setError("Can't be empty");
            addFacultyEmail.requestFocus();
        } else if (post.isEmpty()) {
            addFacultyPost.setError("Can't be empty");
            addFacultyPost.requestFocus();
        }else if (category.equals("Select Category")) {
            Toast.makeText(this, "Provide Faculty's Category", Toast.LENGTH_SHORT).show();
        }else if (bitmap == null) {
            pd.setMessage("Updating Faculty Details");
            pd.show();
            uploadData();
        }
        else {
            pd.setMessage("Updating Faculty Details");
            pd.show();
            uploadImage();
        }
    }

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImg =baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Faculty").child(finalImg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(AddFaculty.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(AddFaculty.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void uploadData() {
        dbRef =reference.child(category);
        final String uniqueKey = dbRef.push().getKey();

        FacultyData facultyData = new FacultyData(name,email,post,downloadUrl,uniqueKey);

        dbRef.child(uniqueKey).setValue(facultyData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(AddFaculty.this, "Faculty details successfully added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddFaculty.this, "ERROR 404 'Something went wrong'", Toast.LENGTH_SHORT).show();
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
            addFacultyImage.setImageBitmap(bitmap);
        }
    }

}