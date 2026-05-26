package com.example.wonka;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class goldenticket extends AppCompatActivity {

    EditText codeInput;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_goldenticket);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        codeInput = findViewById(R.id.codeInput);
        btnSubmit = findViewById(R.id.btnSubmitCode);

        btnSubmit.setOnClickListener(v -> {
            String code = codeInput.getText().toString().trim();
            if(code.isEmpty()){
                Toast.makeText(this, "Please enter a code", Toast.LENGTH_SHORT).show();
                return;
            }

            checkCode(code);
        });
    }

    private void checkCode(String code) {
        String url = "http://10.0.2.2/wonka/check_code.php"; // replace with your server URL

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String status = obj.getString("status");
                        String message = obj.getString("message");

                        if(status.equals("success")){
                            // Code is valid, go to goldenticket2
                            Intent intent = new Intent(goldenticket.this, goldenticket2.class);
                            startActivity(intent);
                        } else {
                            // Invalid code
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Response parse error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("code", code);
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}