package com.example.wonka;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.navigation.ui.AppBarConfiguration;
import com.example.wonka.databinding.ActivityCartViewBinding;

public class CartViewActivity extends AppCompatActivity {
    TextView totalAmount;
    Button checkoutButton;
    RecyclerView cartRecyclerView;
    CartAdapter adapter;
    List<Item> cartList = new ArrayList<>();
    private AppBarConfiguration appBarConfiguration;
    private ActivityCartViewBinding binding;
    SharedPreferences sp;
    int userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);


        sp = getSharedPreferences("myfile", Context.MODE_PRIVATE);
        checkoutButton = (Button) findViewById(R.id.checkoutButton);
        checkoutButton.setVisibility(View.INVISIBLE);
        userId = sp.getInt("user_id", -1);
        totalAmount = findViewById(R.id.totalAmount);

        cartRecyclerView = (RecyclerView) findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(cartRecyclerView);

        adapter = new CartAdapter(cartList, userId, updated -> {
            updateTotalAmount();

        });
        cartRecyclerView.setAdapter(adapter);
        loadCartFromDB();
        checkoutButton.setOnClickListener(v->{
            String total = totalAmount.getText().toString().replace("$", "");
            if(!total.equals("0.00")){
                Intent i = new Intent(this, CheckoutActivity.class);
                i.putExtra("total", total);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_shop) {
            startActivity(new Intent(this, ProductsActivity.class));
            return true;
        }
        if (id == R.id.action_customize) {
            startActivity(new Intent(this, chooseShape.class));
            return true;
        }
        if (id == R.id.action_scanTicket) {
            startActivity(new Intent(this, goldenticket.class));
            return true;
        }
        if (id == R.id.action_profile) {
            startActivity(new Intent(this, UserProfileActivity.class));
            return true;
        }
        if (id == R.id.action_favorites) {
            startActivity(new Intent(this, FavoritesListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadCartFromDB() {
        String url = "http://10.0.2.2/wonka/getCart.php?user_id="+userId;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("CART_RESPONSE", response.toString());
                    try {
                        cartList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            String fullImageUrl = "http://10.0.2.2/wonka/" + obj.getString("img_path");

                            cartList.add(new Item(
                                    obj.getInt("id"),
                                    obj.getString("name"),
                                    obj.getDouble("price"),
                                    obj.getInt("quantity"),
                                    fullImageUrl
                            ));
                        }
                        adapter.notifyDataSetChanged();

                        if (adapter.getItemCount() > 0) {
                            checkoutButton.setVisibility(View.VISIBLE);
                            updateTotalAmount();
                        } else {
                            checkoutButton.setVisibility(View.INVISIBLE);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(getApplicationContext(),"Error:"+error.toString(),Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }
    private void updateTotalAmount() {
        double total = 0;
        for (Item item : cartList) {
            total += item.getPrice() * item.getQuantity();
        }
        totalAmount.setText(String.format("$%.2f", total));
    }

}