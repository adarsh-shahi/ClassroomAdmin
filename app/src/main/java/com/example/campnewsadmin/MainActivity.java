package com.example.campnewsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUserId;
    private TextView createAccountTv;
    private TextView existingAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser();

        createAccountTv=findViewById(R.id.createAccount);
        existingAccount=findViewById(R.id.existingAccount);

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