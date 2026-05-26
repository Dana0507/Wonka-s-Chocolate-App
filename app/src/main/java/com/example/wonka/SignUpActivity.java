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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    EditText username, password, confirmPassword, phone, email;
    Button signUpButton;
    SharedPreferences sp;
    String userk = "userkey";
    int user_id = 0;
    Boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, 0, 0, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        phone = findViewById(R.id.phone);
        signUpButton = findViewById(R.id.signUpButton);

        sp = getSharedPreferences("myfile", Context.MODE_PRIVATE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String confirm = confirmPassword.getText().toString().trim();
                String phoneNum = phone.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                if (TextUtils.isEmpty(user)) {
                    username.setError("Please enter your username");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Please enter your password");
                    return;
                }
                if (!pass.equals(confirm)) {
                    confirmPassword.setError("Passwords do not match");
                    return;
                }
                if (TextUtils.isEmpty(emailText)) {
                    email.setError("Please enter your email");
                    return;
                }
                if (TextUtils.isEmpty(phoneNum)) {
                    phone.setError("Please enter your phone number");
                    return;
                }

                String url = "http://10.0.2.2/wonka/signup.php";
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("username", user);
                    jsonBody.put("password", pass);
                    jsonBody.put("email", emailText);
                    jsonBody.put("phone", phoneNum);
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
                                    editor.putString("username", user);
                                    editor.putInt("user_id", userId);
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.commit();

                                    Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SignUpActivity.this, Homepage.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    if (message.toLowerCase().contains("username")) {
                                        username.setError(message);
                                    } else if (message.toLowerCase().contains("email")) {
                                        email.setError(message);
                                    } else if (message.toLowerCase().contains("phone")) {
                                        phone.setError(message);
                                    } else {
                                        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(SignUpActivity.this, "Response parse error", Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> Toast.makeText(SignUpActivity.this, "Network error", Toast.LENGTH_SHORT).show()
                );
                requestQueue.add(request);
            }
        });
    }
    public void goToLogin(View view) {
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
