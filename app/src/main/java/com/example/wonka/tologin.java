package com.example.wonka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class tologin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tologin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ✅ Get buttons
        Button loginBtn = findViewById(R.id.buttonLogin);
        Button signupBtn = findViewById(R.id.buttonSignup);

        // ✅ Go to LoginActivity
        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(tologin.this, LoginActivity.class);
            startActivity(intent);
        });

        // ✅ Go to SignUpActivity
        signupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(tologin.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}