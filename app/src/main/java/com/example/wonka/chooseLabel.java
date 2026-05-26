package com.example.wonka;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class chooseLabel extends AppCompatActivity {

    private EditText inputChocoName, inputMadeBy, inputPrice;
    private ImageView chocoPreview, btnBack, btnNext;
    private Button btnPickLabel, btnSaveChocolate;
    private FrameLayout progressOverlay;
    private Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_label);

        inputPrice = findViewById(R.id.inputPrice);
        inputChocoName = findViewById(R.id.inputChocoName);
        inputMadeBy = findViewById(R.id.inputMadeBy);
        chocoPreview = findViewById(R.id.chocoPreview);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        btnPickLabel = findViewById(R.id.btnPickLabel);
        btnSaveChocolate = findViewById(R.id.btnSaveChocolate);
        progressOverlay = findViewById(R.id.progressOverlay);

        btnBack.setOnClickListener(v -> startActivity(new Intent(this, chooseFoil.class)));

        btnNext.setOnClickListener(v -> {
            if(validateFields()) {
                startActivity(new Intent(this, Homepage.class));
            }
        });

        ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        chocoPreview.setImageURI(selectedImageUri);
                    }
                }
        );

        btnPickLabel.setOnClickListener(v -> {
            String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                    Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;

            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permission}, 101);
            } else {
                openGallery(pickImageLauncher);
            }
        });

        btnSaveChocolate.setOnClickListener(v -> {
            if(validateFields()) {
                progressOverlay.setVisibility(FrameLayout.VISIBLE);
                btnSaveChocolate.setEnabled(false);

                String name = inputChocoName.getText().toString().trim();
                String madeBy = inputMadeBy.getText().toString().trim();
                double price = Double.parseDouble(inputPrice.getText().toString().trim());
                JSONArray ingredients = buildIngredients();

                saveChocolateToServer(name, madeBy, price, selectedImageUri, ingredients);

                new Handler().postDelayed(() -> {
                    progressOverlay.setVisibility(FrameLayout.GONE);
                    btnSaveChocolate.setEnabled(true);
                }, 1500);
            }
        });
    }

    private boolean validateFields() {
        if(inputChocoName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter chocolate name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(inputMadeBy.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(inputPrice.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter price", Toast.LENGTH_SHORT).show();
            return false;
        }
        try { Double.parseDouble(inputPrice.getText().toString().trim()); }
        catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(selectedImageUri == null) {
            Toast.makeText(this, "Pick a label image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private JSONArray buildIngredients() {
        JSONArray ingredients = new JSONArray();
        String[] defaultIngredients = {"Sugar", "Cocoa Butter", "Whole Milk Powder", "Cocoa Mass", "Soy Lecithin"};
        for(String ing : defaultIngredients) ingredients.put(ing);

        SharedPreferences prefs = getSharedPreferences("CHOSEN_DATA", MODE_PRIVATE);
        String chocoType = prefs.getString("chocoType", null);
        if(chocoType != null) ingredients.put(chocoType);

        SharedPreferences fillsPrefs = getSharedPreferences("WonkaPrefs", MODE_PRIVATE);
        Set<String> fills = fillsPrefs.getStringSet("selectedFills", new HashSet<>());
        for(String fill : fills) ingredients.put(fill);

        return ingredients;
    }

    private void saveChocolateToServer(String name, String madeBy, double price, Uri imageUri, JSONArray ingredients) {
        String url = "http://10.0.2.2/wonka/save_chocolate.php";

        VolleyMultipartRequest request = new VolleyMultipartRequest(
                Request.Method.POST,
                url,
                response -> Toast.makeText(this, "Chocolate saved!", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Upload failed!", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("madeBy", madeBy);
                params.put("price", String.valueOf(price));
                params.put("ingredients", ingredients.toString());
                return params;
            }

            @Override
            public Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if(imageUri != null) {
                    try {
                        InputStream iStream = getContentResolver().openInputStream(imageUri);
                        byte[] inputData = getBytes(iStream);
                        params.put("image", new DataPart(System.currentTimeMillis() + ".jpg", inputData));
                    } catch (Exception e) { e.printStackTrace(); }
                }
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void openGallery(ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        launcher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this, "Permission granted! Pick your image again.", Toast.LENGTH_SHORT).show();
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024; byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) byteBuffer.write(buffer, 0, len);
        return byteBuffer.toByteArray();
    }
}