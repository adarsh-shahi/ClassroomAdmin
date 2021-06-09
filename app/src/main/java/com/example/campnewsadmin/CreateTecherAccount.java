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

public class CreateTecherAccount extends AppCompatActivity {

    private EditText nameEt, mailEt, idEt, subjectEt, passEt, pass1Et;
    private SubmitButton createAccountBtn;
    private CircleImageView teacherProfile;

    private Uri imageUri;
    private boolean selectPic=false;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_techer_account);

        nameEt = findViewById(R.id.enterTeacherFullNameEt);
        mailEt = findViewById(R.id.enterTeacherMail);
        idEt = findViewById(R.id.enterTeachercollegeId);
        subjectEt = findViewById(R.id.enterSubject);
        passEt = findViewById(R.id.enterTeacherPass1);
        pass1Et = findViewById(R.id.enterTeacherPass2);
        createAccountBtn = findViewById(R.id.createTeacherAccount_button);
        teacherProfile = findViewById(R.id.teacher_profile_image);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        userId=user.getUid();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        teacherProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProfilePic();
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEt.getText().toString().trim();
                String mail = mailEt.getText().toString().trim();
                String id = idEt.getText().toString().trim();
                String subject = subjectEt.getText().toString().trim();
                String pass = passEt.getText().toString().trim();
                String pass1 = pass1Et.getText().toString().trim();
                imageUri = Uri.parse("android.resource://com.example.campnewsadmin/drawable/edit_image_logo");

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(id) || TextUtils.isEmpty(subject) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(pass1) )
                    Toast.makeText(CreateTecherAccount.this, "Please enter all the credentials", Toast.LENGTH_LONG).show();
                else if(! (pass.equals(pass1))) {
                    Toast.makeText(CreateTecherAccount.this, "Passwords didn't match", Toast.LENGTH_SHORT).show();
                    passEt.findFocus();
                }
                else{

                    final ProgressDialog pd = new ProgressDialog(CreateTecherAccount.this);
                    pd.setTitle("Creating New Account...");
                    pd.show();

                    final String randomKey = UUID.randomUUID().toString();
                    StorageReference riversRef = storageReference.child("images/" + name + "_" + randomKey);
                    riversRef.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();

                                            Map<String, Object> note = new HashMap<>();
                                            note.put("name", name);
                                            note.put("subject", subject);
                                            note.put("collegecode", id);
                                            note.put("profilepic", imageUrl);
                                            note.put("userid", userId);
                                            note.put("mail", mail);
                                            note.put("password",pass);


                                            db.collection("Teachers").document(mail).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    pd.dismiss();
                                                    startActivity(new Intent(CreateTecherAccount.this, MainActivity.class));

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
        });
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
            teacherProfile.setImageURI(imageUri);
            selectPic=true;
        }
    }
}