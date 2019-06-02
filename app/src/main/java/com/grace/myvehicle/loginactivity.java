package com.grace.myvehicle;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class loginactivity extends AppCompatActivity {
    private FirebaseAuth auth;

    Button signin;
    EditText email;
    EditText password;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);


        signin = (Button)findViewById(R.id.signin);
        signup=(Button)findViewById(R.id.signup);

        auth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(loginactivity.this, "Clicked", Toast.LENGTH_LONG).show();

                String email1 = email.getText().toString();
                String password1 = password.getText().toString();
                if (email1.isEmpty() || password1.isEmpty()) {
                    Toast.makeText(loginactivity.this, "Please fill in the email and password", Toast.LENGTH_LONG).show();
                }
                else{
                    auth.signInWithEmailAndPassword(email1, password1).addOnCompleteListener(loginactivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(loginactivity.this, "Welcome ", Toast.LENGTH_LONG ).show();
                                startActivity(new Intent(loginactivity.this, MapsActivity.class));
                            }else {
                                Toast.makeText( loginactivity.this, "Incorrect email or password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginactivity.this, SignupActivity.class));

            }
        });
    }
}