package com.example.wonka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    EditText firstNameEd, lastNameEd, phoneEd, addressEd;
    RadioGroup orderTypeGroup;
    RadioButton pickupBtn, deliveryBtn;
    Spinner citySpinner;
    CheckBox cashOnDelivery;
    TextView totalTv;
    Button confirmBtn, cancelBtn;

    String totalAmount = "0";
    int userId = -1; // loaded from SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        // ---------------- LOAD USER_ID FROM SHARED PREF ----------------
        SharedPreferences sp = getSharedPreferences("myfile", MODE_PRIVATE);
        userId = sp.getInt("user_id", -1);  // <-- THIS IS YOUR USER SESSION

        if (userId == -1) {
            Toast.makeText(this, "Error: No user logged in", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // ---------------- FIND VIEWS ----------------
        firstNameEd = findViewById(R.id.first_name_ed);
        lastNameEd = findViewById(R.id.last_name_ed);
        phoneEd = findViewById(R.id.phone_ed);
        addressEd = findViewById(R.id.address_ed);

        orderTypeGroup = findViewById(R.id.order_type_group);
        pickupBtn = findViewById(R.id.radio_pickup);
        deliveryBtn = findViewById(R.id.radio_delivery);
        citySpinner = findViewById(R.id.spinner);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Beirut", "Tripoli", "Saida", "Tyre", "Jounieh", "Zahle", "Baabda"}
        );
        citySpinner.setAdapter(cityAdapter);

        cashOnDelivery = findViewById(R.id.checkBox);
        totalTv = findViewById(R.id.tv_total);
        confirmBtn = findViewById(R.id.checkout_btn);
        cancelBtn = findViewById(R.id.cancel_order_button);

        // ---------------- GET TOTAL AMOUNT ----------------
        if (getIntent() != null) {
            totalAmount = getIntent().getStringExtra("total");
            if (totalAmount == null) totalAmount = "0";
        }
        totalTv.setText("$" + totalAmount);

        // ---------------- CANCEL ORDER ----------------
        cancelBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cancel Order")
                    .setMessage("Are you sure you want to cancel your order?")
                    .setPositiveButton("Yes", (d, w) -> {
                        startActivity(new Intent(this, CartViewActivity.class));
                        finish();
                    })
                    .setNegativeButton("No", (d, w) -> d.dismiss())
                    .create().show();
        });

        // ---------------- CONFIRM CHECKOUT ----------------
        confirmBtn.setOnClickListener(v -> {
            if (!validateInputs()) return;
            sendOrderToServer();
        });
    }

    private boolean validateInputs() {

        if (isEmpty(firstNameEd)) {
            firstNameEd.setError("First name is required");
            return false;
        }
        if (isEmpty(lastNameEd)) {
            lastNameEd.setError("Last name is required");
            return false;
        }
        if (isEmpty(phoneEd)) {
            phoneEd.setError("Phone number is required");
            return false;
        }
        if (orderTypeGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select an order type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!cashOnDelivery.isChecked()) {
            Toast.makeText(this, "Please select a payment option", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (citySpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a city", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isEmpty(addressEd)) {
            addressEd.setError("Address is required");
            return false;
        }

        return true;
    }

    private boolean isEmpty(EditText ed) {
        return TextUtils.isEmpty(ed.getText().toString().trim());
    }

    // ---------------------- SEND ORDER TO SERVER ----------------------
    private void sendOrderToServer() {

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Placing order...");
        pd.show();

        String url = "http://10.0.2.2/wonka/add_order.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    pd.dismiss();
                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(this, SuccessfulCheckout.class);
                    intent.putExtra("total", totalAmount);
                    startActivity(intent);
                    finish();
                },
                error -> {
                    pd.dismiss();
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();

                String orderType =
                        pickupBtn.isChecked() ? "pickup" : "delivery";

                map.put("user_id", String.valueOf(userId));
                map.put("shipping_address",
                        citySpinner.getSelectedItem().toString() + " - " + addressEd.getText().toString());
                map.put("payment_method", "Cash on Delivery");
                map.put("total_amount", totalAmount);
                map.put("order_type", orderType);

                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
