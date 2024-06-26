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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateFacultyActivity extends AppCompatActivity {

    private ImageView updateFacultyImage;
    private EditText updateFacultyName, updateFacultyEmail, updateFacultyPost;
    private Button updateFacultyBtn, deleteFacultyBtn;

    private final int REQ=1;
    private Bitmap bitmap = null;
    private String name, email,post,image;

    private String uniqueKey,category;

    private ProgressDialog pd;
    private StorageReference storageReference;
    private DatabaseReference reference,dbRef;
    private String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty2);

        uniqueKey = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");

        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        post=getIntent().getStringExtra("post");
        image=getIntent().getStringExtra("image");

        updateFacultyName = findViewById(R.id.updateFacultyName);
        updateFacultyEmail = findViewById(R.id.updateFacultyEmail);
        updateFacultyPost = findViewById(R.id.updateFacultyPost);
        updateFacultyImage = findViewById(R.id.updateFacultyImage);
        updateFacultyBtn = findViewById(R.id.updateFacultyBtn);
        deleteFacultyBtn = findViewById(R.id.deleteFacultyBtn);

        reference = FirebaseDatabase.getInstance().getReference().child("Faculty");
        storageReference = FirebaseStorage.getInstance().getReference();


        try {
            Picasso.get().load(image).into(updateFacultyImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateFacultyName.setText(name);
        updateFacultyEmail.setText(email);
        updateFacultyPost.setText(post);

        updateFacultyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        updateFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = updateFacultyName.getText().toString();
                email = updateFacultyEmail.getText().toString();
                post = updateFacultyPost.getText().toString();
               checkValidation();
            }
        });

        deleteFacultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
    }

    private void checkValidation() {
        if (name.isEmpty()){
            updateFacultyName.setError("Can't be empty");
            updateFacultyName.requestFocus();
        } else if (email.isEmpty()) {
            updateFacultyEmail.setError("Can't be empty");
            updateFacultyEmail.requestFocus();
        }else if (post.isEmpty()) {
            updateFacultyPost.setError("Can't be empty");
            updateFacultyPost.requestFocus();
        }else if(bitmap == null){
            updateData(image);
        }else {
            uploadImage();
        }
    }
    private void updateData(String s){
        HashMap hp = new HashMap();
        hp.put("name",name);
        hp.put("email",email);
        hp.put("post",post);
        hp.put("image",s);

        reference.child(category).child(uniqueKey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateFacultyActivity.this, "Faculty updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateFacultyActivity.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateFacultyActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImg =baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Faculty").child(finalImg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(UpdateFacultyActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(UpdateFacultyActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteData() {
        reference.child(category).child(uniqueKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UpdateFacultyActivity.this, "Faculty removed Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateFacultyActivity.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateFacultyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();     
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
            updateFacultyImage.setImageBitmap(bitmap);
        }
    }

}