package com.example.campnewsadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spark.submitbutton.SubmitButton;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateAccount extends AppCompatActivity {

    private EditText fullName , DOB , email , collegeId , enroll , roll , pass1 , pass2;
    private CircleImageView profilePic;
    private Uri imageUri;
    private SubmitButton submitButton;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userId;
    private boolean selectPic=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        userId=user.getUid();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        fullName=findViewById(R.id.enterFullNameEt);
        DOB=findViewById(R.id.enterDOB);
        email=findViewById(R.id.enterGmail);
        collegeId=findViewById(R.id.entercollegeId);
        enroll=findViewById(R.id.enterEnroll);
        roll=findViewById(R.id.enterRollNo);
        profilePic=findViewById(R.id.profile_image);
        submitButton=findViewById(R.id.createAccount_button);
        pass1=findViewById(R.id.enterPass1);
        pass2=findViewById(R.id.enterPass2);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProfilePic();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDetailsToDB();
            }
        });
    }

    private void uploadDetailsToDB() {
        String mail = email.getText().toString();
        String fullname = fullName.getText().toString();
        String dob = DOB.getText().toString();
        String collegeid = collegeId.getText().toString();
        String enrollNo = enroll.getText().toString();
        String rollNo = roll.getText().toString();
        String password1 = pass1.getText().toString();
        String password2 = pass2.getText().toString();
        imageUri = Uri.parse("android.resource://com.example.campnewsadmin/drawable/edit_image_logo");

        if(TextUtils.isEmpty(mail) || TextUtils.isEmpty(fullname) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(collegeid) || TextUtils.isEmpty(enrollNo) || TextUtils.isEmpty(rollNo) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)){
            Toast.makeText(CreateAccount.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }
        else {
            if (!password1.equals(password2)) {
                Toast.makeText(CreateAccount.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                pass1.requestFocus();
            } else {

                final ProgressDialog pd = new ProgressDialog(CreateAccount.this);
                pd.setTitle("Creating New Account...");
                pd.show();

                final String randomKey = UUID.randomUUID().toString();
                StorageReference riversRef = storageReference.child("images/" + fullname + "_" + randomKey);
                riversRef.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();

                                        Map<String, Object> note = new HashMap<>();
                                        note.put("name", fullname);
                                        note.put("dob", dob);
                                        note.put("collegecode", collegeid);
                                        note.put("enroll", enrollNo);
                                        note.put("roll", rollNo);
                                        note.put("profilepic", imageUrl);
                                        note.put("userid", userId);
                                        note.put("mail", mail);
                                        note.put("password",password1);


                                        db.collection("Users").document(mail).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                pd.dismiss();
                                                startActivity(new Intent(CreateAccount.this, MainActivity.class));

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercentage = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage((int) progressPercentage + "%");
                    }
                });
            }
        }
    }

    private void chooseProfilePic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();
            profilePic.setImageURI(imageUri);
           selectPic=true;
        }
    }







}