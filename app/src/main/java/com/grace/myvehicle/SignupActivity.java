package com.grace.myvehicle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.LENGTH_LONG;

public class SignupActivity extends AppCompatActivity {
    Button signup;
    EditText email,password,confirm_password;

    private FirebaseAuth auth;

    private DatabaseReference root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference (  "message");

        root = FirebaseDatabase.getInstance().getReference();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                //dataTextview.setText(value);
                Log.d("SIGNUP", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //dataTextview.setText("Error " + databaseError.toString());
                // Failed to read value
                Log.w("SIGNUP", "Failed to read value.",    databaseError.toException());
            }
        });

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);

        signup = (Button)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailstring = email.getText().toString();
                String passwordstring = password.getText().toString();
                String confirm_passstring = confirm_password.getText().toString();

                Toast.makeText(SignupActivity.this, "Authenticating", Toast.LENGTH_LONG).show();

                Log.d("Sign_Up", "onclick: " + emailstring + passwordstring);

                if (passwordstring.equals(confirm_passstring)) {


                    registerUser(emailstring, passwordstring);
                    Log.d("register_button_if", "onclick: " + emailstring + passwordstring);
                }
            }
        });
    }
    public void registerUser(String emailstring, String passwordstring) {
        final Task<AuthResult> authResultTask = auth.createUserWithEmailAndPassword(emailstring, passwordstring).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    String uid = user.getUid();

                    DatabaseReference userDB = root.child("Users").child(uid);


                    Intent intent = new Intent(SignupActivity.this, MapsActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("firebase error: ", task.getException().getMessage());
                    Intent intent = new Intent(SignupActivity.this, MapsActivity.class);
                    startActivity(intent);
                    Toast.makeText(SignupActivity.this, "Registration Failed "+task.getException().toString(),LENGTH_LONG).show();
                    finish();
                }

            }
        });
    }
}


