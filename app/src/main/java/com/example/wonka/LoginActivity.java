package com.example.wonka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button loginButton;
    SharedPreferences sp;
    String userk = "userkey";
    int user_id = 0;
    Boolean isLoggedIn = false;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        sp = getSharedPreferences("myfile", Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);

        if (sp.getBoolean("isLoggedIn", false)) {
            Intent i = new Intent(LoginActivity.this, Homepage.class);
            startActivity(i);
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(username.getText())) {
                    username.setError("Please enter your username");
                }
                else if (TextUtils.isEmpty(password.getText())) {
                    password.setError("Please enter your password");
                }
                else {
                    String url = "http://10.0.2.2/wonka/login.php";
                    String usernameText = username.getText().toString().trim();
                    String passwordText = password.getText().toString().trim();

                    JSONObject jsonBody = new JSONObject();
                    try {
                        jsonBody.put("username", usernameText);
                        jsonBody.put("password", passwordText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonBody,
                            response -> {
                                try {
                                    String status = response.getString("status");
                                    String message = response.getString("message");

                                    if (status.equals("success")) {
                                        int userId = response.getInt("user_id");
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("userk", usernameText);
                                        editor.putInt("user_id", userId);
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.commit();

                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LoginActivity.this, Homepage.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    else {
                                        if (message.toLowerCase().contains("username")) {
                                            username.setError(message);
                                        } else if (message.toLowerCase().contains("password")) {
                                            password.setError(message);
                                        } else {
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Response parse error", Toast.LENGTH_SHORT).show();
                                }
                            },
                            error -> Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show()
                    );
                    requestQueue.add(request);
                }
            }
        });
    }

    public void goToSignUp(View view) {
        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(i);
        finish();
    }

}