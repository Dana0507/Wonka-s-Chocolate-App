package com.example.wonka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class chooseFoil extends AppCompatActivity {

    private static final String PREFS_NAME = "WonkaPrefs";
    private static final String FOIL_KEY = "selectedFoil";

    private ImageView chocoPreview;
    private Spinner colorSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_foil);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        chocoPreview = findViewById(R.id.chocoPreview);
        colorSpinner = findViewById(R.id.colorSpinner);
        ImageView btnNext = findViewById(R.id.btnNext);
        ImageView btnBack = findViewById(R.id.btnBack);

        // Load previous foil selection
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedFoil = prefs.getString(FOIL_KEY, "White"); // default White

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_selected_item, getResources().getStringArray(R.array.foil_colors)) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Inflate layout manually
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.spinner_selected_item, parent, false);
                }

                TextView text = convertView.findViewById(R.id.spinnerText);
                text.setText(getItem(position));

                // Set background color based on selected foil
                int bgColor = getColorHex(getItem(position));
                convertView.setBackgroundColor(bgColor);
                if (bgColor == 0xFFFFFFFF) {
                    text.setTextColor(0xFF000000); // black text if background is white
                } else {
                    text.setTextColor(0xFFFFFFFF); // white text otherwise
                }

                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // Inflate layout manually for dropdown
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.spinner_dropdown_item, parent, false);
                }

                TextView text = (TextView) convertView.findViewById(R.id.dropdownText);
                text.setText(getItem(position));

                // Optional: set background per color or leave white
                text.setBackgroundColor(0xFFFFFFFF);
                text.setTextColor(0xFF000000);

                return convertView;
            }
        };

        colorSpinner.setAdapter(adapter);

        // Set spinner to previous selection
        int spinnerPosition = adapter.getPosition(savedFoil);
        colorSpinner.setSelection(spinnerPosition);

        // Update image initially
        updateChocoPreview(savedFoil);

        // Spinner listener
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedColor = parent.getItemAtPosition(position).toString();
                updateChocoPreview(selectedColor);

                // Save selection
                prefs.edit().putString(FOIL_KEY, selectedColor).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Navigation buttons
        btnNext.setOnClickListener(v -> startActivity(new Intent(chooseFoil.this, chooseLabel.class)));
        btnBack.setOnClickListener(v -> startActivity(new Intent(chooseFoil.this, chooseFills.class)));
    }

    private void updateChocoPreview(String color) {
        int drawableId;
        switch (color.toLowerCase()) {
            case "pink":
                drawableId = R.drawable.pinkfoil;
                break;
            case "white":
                drawableId = R.drawable.whitefoil;
                break;
            case "yellow":
                drawableId = R.drawable.yellowfoil;
                break;
            case "green":
                drawableId = R.drawable.greenfoil;
                break;
            case "blue":
                drawableId = R.drawable.bluefoil;
                break;
            case "purple":
                drawableId = R.drawable.purplefoil;
                break;
            case "orange":
                drawableId = R.drawable.orangefoil;
                break;
            case "red":
                drawableId = R.drawable.redfoil;
                break;
            default:
                drawableId = R.drawable.whitefoil;
        }
        chocoPreview.setImageResource(drawableId);
    }
    private int getColorHex(String color) {
        switch (color.toLowerCase()) {
            case "pink": return   0xFFBC3C73; // pink
            case "white": return 0xFFFFFFFF;  // white
            case "yellow": return 0xFFDAC143; // yellow
            case "green": return 0xFFA9BA48;  // green
            case "blue": return 0xFF4889B7;
            case "purple": return 0xFF763377; // purple
            case "orange": return 0xFFE1A23C; // orange
            case "red": return 0xFFAC3832;    // red
            default: return 0xFFFFFFFF;       // default white
        }
    }
}