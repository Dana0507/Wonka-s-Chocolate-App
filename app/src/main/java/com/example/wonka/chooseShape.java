package com.example.wonka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class chooseShape extends AppCompatActivity {

    RadioGroup shapeGroup;
    ImageView btnNext, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_shape);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        shapeGroup = findViewById(R.id.shapeRadioGroup);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        // When a shape is selected → show Toast and save it
        shapeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = findViewById(checkedId);

            if (selected != null) {
                String shape = selected.getText().toString();

                Toast.makeText(this, "You picked " + shape + " shape", Toast.LENGTH_SHORT).show();

                // Save to SharedPreferences for later use
                SharedPreferences prefs = getSharedPreferences("CHOSEN_DATA", MODE_PRIVATE);
                prefs.edit().putString("shape", shape).apply();
            }
        });

        // NEXT → chooseType.java
        btnNext.setOnClickListener(v -> {
            int id = shapeGroup.getCheckedRadioButtonId();
            if (id == -1) {
                Toast.makeText(this, "Please choose a shape first", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(chooseShape.this, chooseType.class);
            startActivity(intent);
        });

        // BACK → Homepage.java
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(chooseShape.this, Homepage.class);
            startActivity(intent);
        });
    }
}