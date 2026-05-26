package com.example.wonka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnLearn = findViewById(R.id.btnLearn);
        Button btnShopChocolate = findViewById(R.id.btnShopChocolate);
        Button btnCreateChocolate = findViewById(R.id.btnCreateChocolate);
        Button btnGoldenTicket = findViewById(R.id.btnGoldenTicket);

        btnLearn.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, AboutUs.class);
            startActivity(intent);
        });

        btnCreateChocolate.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, chooseShape.class);
            startActivity(intent);
        });


        btnGoldenTicket.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, goldenticket.class);
            startActivity(intent);
        });
        btnShopChocolate.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, ProductsActivity.class);
            startActivity(intent);
        });
    }
}