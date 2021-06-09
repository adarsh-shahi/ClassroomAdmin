package com.example.campnewsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spark.submitbutton.SubmitButton;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUserId;
    private TextView createAccountTv ,createTeacherAccount;
    private TextView existingAccount ,existingTeacherAccount;
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser();

        createAccountTv=findViewById(R.id.createAccount);
        existingAccount=findViewById(R.id.existingAccount);
        createTeacherAccount = findViewById(R.id.createTeacherAccount);
        existingTeacherAccount = findViewById(R.id.existingTeacherAccount);
        floatingActionButton = findViewById(R.id.adminLogout);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        createAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CreateAccount.class));
            }
        });

        existingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ExistingStudent.class));
            }
        });

        createTeacherAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateTecherAccount.class));
            }
        });

        existingTeacherAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ExistingTeacherAccount.class));
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUserId==null){
            startActivity( new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
    }
}