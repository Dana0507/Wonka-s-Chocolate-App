package com.example.wonka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class chooseType extends AppCompatActivity {

    ImageView btnBack, btnNext;
    ImageButton btnWhite, btnMilk, btnDark, btnStrawberry, btnCaramel, btnMatcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_type);

        // Arrows
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);

        // Chocolate buttons
        btnWhite = findViewById(R.id.btnWhite);
        btnMilk = findViewById(R.id.btnMilk);
        btnDark = findViewById(R.id.btnDark);
        btnStrawberry = findViewById(R.id.btnStrawberry);
        btnCaramel = findViewById(R.id.btnCaramel);
        btnMatcha = findViewById(R.id.btnMatcha);

        // Assign TAGS instead of switch-case
        btnWhite.setTag("White");
        btnMilk.setTag("Milk");
        btnDark.setTag("Dark");
        btnStrawberry.setTag("Strawberry");
        btnCaramel.setTag("Caramel");
        btnMatcha.setTag("Matcha");

        // Back button
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(chooseType.this, chooseShape.class));
        });

        // Next disabled
        btnNext.setOnClickListener(v ->
                Toast.makeText(this, "Choose a chocolate type first", Toast.LENGTH_SHORT).show()
        );

        // One single listener for all chocolate types
        View.OnClickListener chooseChoco = v -> {
            String chocoType = (String) v.getTag();

            // Save
            SharedPreferences prefs = getSharedPreferences("CHOSEN_DATA", MODE_PRIVATE);
            prefs.edit().putString("chocoType", chocoType).apply();

            Toast.makeText(this, "You chose " + chocoType + " chocolate", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(chooseType.this, chooseFills.class));
        };

        // Apply listener
        btnWhite.setOnClickListener(chooseChoco);
        btnMilk.setOnClickListener(chooseChoco);
        btnDark.setOnClickListener(chooseChoco);
        btnStrawberry.setOnClickListener(chooseChoco);
        btnCaramel.setOnClickListener(chooseChoco);
        btnMatcha.setOnClickListener(chooseChoco);
    }
}