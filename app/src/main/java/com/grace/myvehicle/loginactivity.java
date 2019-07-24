package com.grace.myvehicle;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;


public class loginactivity extends AppCompatActivity {
    private FirebaseAuth auth;

    Button signin;
    EditText email;
    EditText password;
    //Button signup;
    MaterialDialog dialog;
    Preferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = new Preferences(this);

        signin = (Button)findViewById(R.id.signin);
        //signup=(Button)findViewById(R.id.signup);

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
                dialog = Utils.configureProgessDialog(loginactivity.this, "Login", "Please wait as we log you in..");
                JSONObject postData = new JSONObject();
                try {
                    postData.put("email", email.getText().toString());
                    postData.put("code", password1);

                    String url = Utils.BASE_URL + "auth";
                    new LoginService().execute(url, postData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
        /*signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginactivity.this, SignupActivity.class));

            }
        });*/
    }
    private class LoginService extends AsyncTask<String, Void, Pair<Integer, String> > {
        protected void onPreExecute(){
            dialog.show();
        }

        @Override
        protected Pair<Integer, String> doInBackground(String... strings) {
            NetworkHandler handler = new NetworkHandler(getApplicationContext());
            return handler.doPost(strings[0], strings[1]);
        }
        @Override
        protected void onPostExecute(Pair<Integer, String> result){
            dialog.dismiss();
            if(result != null && result.message != null){
                JsonParser jsonParser = new JsonParser();
                if (result.message == null || result.message.isEmpty())
                    return;
                Log.d("H", result.message);
                JsonObject jo = (JsonObject) jsonParser.parse(result.message).getAsJsonObject();
                if (result.code/100 != 2){
                    dialog = Utils.configureDialog(loginactivity.this, "Error", "Unable to login","OK", null);
                    dialog.show();
                    return;
                }
                if (!jo.has("id")){
                    Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                    return;
                }
                preferences.setUserId(jo.get("id").getAsInt());
                preferences.setIsLoggedin(true);

                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                finish();

                return;
            }else{
                dialog = Utils.configureDialog(loginactivity.this, "Connection error", "Connection error","OK", null);
                dialog.show();
            }
        }
    }
}