package com.example.campnewsadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spark.submitbutton.SubmitButton;

public class LoginActivity extends AppCompatActivity {

    private EditText adminEtID;
    private EditText adminEtPass;
    private SubmitButton login;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        adminEtID=findViewById(R.id.enterId);
        adminEtPass=findViewById(R.id.enterPassword);
        login=findViewById(R.id.login_button);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginID = adminEtID.getText().toString().trim();
                String password = adminEtPass.getText().toString().trim();

                if(TextUtils.isEmpty(loginID) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Please enter above credentials to login", Toast.LENGTH_LONG).show();
                }
                else {
                    if(loginID.equals("admin1") && password.equals("aissmsadmin1")){
                        String email = "adarshshahi2404@gmail.com";
                        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(LoginActivity.this,"You are logged in successfully",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    String message=task.getException().toString();
                                    Toast.makeText(LoginActivity.this,"Error: "+message,Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}