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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spark.submitbutton.SubmitButton;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditStudentDetails extends AppCompatActivity {

    private EditText name,mail,dob,roll,enroll,collegeid;
    private CircleImageView profile;
    private String mailIntent;
    private FirebaseFirestore db ;
    private SubmitButton editDetails;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_details);

        name=findViewById(R.id.enterFullNameEt);
        db=FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        mail=findViewById(R.id.enterGmail);
        dob=findViewById(R.id.enterDOB);
        roll=findViewById(R.id.enterRollNo);
        enroll=findViewById(R.id.enterEnroll);
        collegeid=findViewById(R.id.entercollegeId);
        profile=findViewById(R.id.profile_image);
        editDetails=findViewById(R.id.editAccount_button);

        Intent intent = getIntent();
         mailIntent = intent.getStringExtra("mail");

         loadDetails();

         profile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 chooseProfilePic();
             }
         });

         editDetails.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 uploadDetailsToDB();

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
            profile.setImageURI(imageUri);
            uploadPic();
        }
    }

    private void uploadPic() {

        String mailString = mail.getText().toString();
        String fullname = name.getText().toString();

        final ProgressDialog pd = new ProgressDialog(EditStudentDetails.this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/"+fullname+"_"+randomKey);
        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();

                                Map<String , Object> note = new HashMap<>();
                                note.put("profilepic",imageUrl);

                                db.collection("Users").document(mailString).update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.dismiss();
                                        Toast.makeText(EditStudentDetails.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

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
                double progressPercentage = (100.00* snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage((int) progressPercentage+"%");
            }
        });
    }

    private void uploadDetailsToDB() {

        String mailString = mail.getText().toString();
        String fullname = name.getText().toString();
        String dobString = dob.getText().toString();
        String collegeId = collegeid.getText().toString();
        String enrollNo = enroll.getText().toString();
        String rollNo = roll.getText().toString();

        if (TextUtils.isEmpty(mailString) || TextUtils.isEmpty(fullname) || TextUtils.isEmpty(dobString) || TextUtils.isEmpty(collegeId) || TextUtils.isEmpty(enrollNo) || TextUtils.isEmpty(rollNo)) {
            Toast.makeText(EditStudentDetails.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else {


            Map<String, Object> note = new HashMap<>();
            note.put("name", fullname);
            note.put("dob", dobString);
            note.put("collegecode", collegeId);
            note.put("enroll", enrollNo);
            note.put("roll", rollNo);
            note.put("mail", mailString);

            db.collection("Users").document(mailString).update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(EditStudentDetails.this,ExistingStudent.class));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }



    private void loadDetails() {
        DocumentReference documentReference = db.collection("Users").document(mailIntent);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                dob.setText(value.getString("dob"));
                mail.setText(value.getString("mail"));
                collegeid.setText(value.getString("collegecode"));
                enroll.setText(value.getString("enroll"));
                roll.setText(value.getString("roll"));
                name.setText(value.getString("name"));
                try {
                    Picasso.get().load(value.getString("profilepic")).placeholder(R.drawable.ic_default_profile1_blue).into(profile);
                }
                catch (Exception e) {
                }
            }
        });
    }
}