package com.example.wonka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashSet;
import java.util.Set;

public class chooseFills extends AppCompatActivity {

    // Keys for SharedPreferences
    private static final String PREFS_NAME = "WonkaPrefs";
    private static final String FILLS_KEY = "selectedFills";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_fills);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get all checkboxes
        CheckBox checkWaffers = findViewById(R.id.checkWaffers);
        CheckBox checkTiramisu = findViewById(R.id.checkTiramisu);
        CheckBox checkPistachio = findViewById(R.id.checkPistachio);
        CheckBox checkPeanut = findViewById(R.id.checkPeanut);
        CheckBox checkPretzels = findViewById(R.id.checkPretzels);
        CheckBox checkMarshmallows = findViewById(R.id.checkMarshmallows);
        CheckBox checkHazelnut = findViewById(R.id.checkHazelnut);
        CheckBox checkGummy = findViewById(R.id.checkGummy);
        CheckBox checkCottonCandy = findViewById(R.id.checkCottonCandy);
        CheckBox checkCaramell = findViewById(R.id.checkCaramell);
        CheckBox checkCoffee = findViewById(R.id.checkCoffee);
        CheckBox checkBaklava = findViewById(R.id.checkBaklava);

        CheckBox[] checkBoxes = {checkWaffers, checkTiramisu, checkPistachio, checkPeanut,
                checkPretzels, checkMarshmallows, checkHazelnut, checkGummy, checkCottonCandy,
                checkCaramell, checkCoffee, checkBaklava};

        // Load previous selections if any
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> savedFills = prefs.getStringSet(FILLS_KEY, new HashSet<>());
        for (CheckBox cb : checkBoxes) {
            if (savedFills.contains(cb.getText().toString())) {
                cb.setChecked(true);
            }
        }

        ImageView btnNext = findViewById(R.id.btnNext);
        ImageView btnBack = findViewById(R.id.btnBack);

        // Next button -> chooseFoil
        btnNext.setOnClickListener(v -> {
            saveSelectedFills(checkBoxes);
            startActivity(new Intent(chooseFills.this, chooseFoil.class));
        });

        // Back button -> chooseType
        btnBack.setOnClickListener(v -> {
            saveSelectedFills(checkBoxes);
            startActivity(new Intent(chooseFills.this, chooseType.class));
        });
    }

    private void saveSelectedFills(CheckBox[] checkBoxes) {
        Set<String> selectedFills = new HashSet<>();
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) {
                selectedFills.add(cb.getText().toString());
            }
        }
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putStringSet(FILLS_KEY, selectedFills).apply();
    }
}