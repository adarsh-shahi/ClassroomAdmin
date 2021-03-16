package com.example.campnewsadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.spark.submitbutton.SubmitButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateAccount extends AppCompatActivity {

    private EditText username , fullName , DOB , email , collegeId , enroll , roll;
    private CircleImageView profilePic;
    private Uri imageUri;
    private SubmitButton submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        username=findViewById(R.id.enterusernameEt);
        fullName=findViewById(R.id.enterFullNameEt);
        DOB=findViewById(R.id.enterDOB);
        email=findViewById(R.id.enterGmail);
        collegeId=findViewById(R.id.entercollegeId);
        enroll=findViewById(R.id.enterEnroll);
        roll=findViewById(R.id.enterRollNo);
        profilePic=findViewById(R.id.profile_image);

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
           // uploadPic();
        }
    }







}