package com.example.wonka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wonka.databinding.ProductsBinding;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class ProductsActivity extends AppCompatActivity {
    SharedPreferences sp;
    int userId = 0;
    RecyclerView recyclerView;
    private AppBarConfiguration appBarConfiguration;
    private ProductsBinding binding;

    ArrayList<Item> products = new ArrayList<>();
    ArrayList<String> imageUrls = new ArrayList<>();

    HashSet<Integer> favoriteIds = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);


        recyclerView = findViewById(R.id.products_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sp = getSharedPreferences("myfile", MODE_PRIVATE);
        userId = sp.getInt("user_id", -1);

        loadFavorites();
    }

    private void loadFavorites() {
        String url = "http://10.0.2.2/wonka/get_favorites.php?user_id=" + userId;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                res -> {
                    try {
                        JSONArray arr = res.getJSONArray("data");
                        favoriteIds.clear();

                        for (int i = 0; i < arr.length(); i++) {
                            favoriteIds.add(arr.getJSONObject(i).getInt("id"));
                        }

                        loadProducts();

                    } catch (Exception e) {
                        loadProducts();
                    }
                },
                err -> loadProducts()
        );

        Volley.newRequestQueue(this).add(req);
    }

    private void loadProducts() {

        String url = "http://10.0.2.2/wonka/get_products.php";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                res -> {
                    try {
                        JSONArray arr = res.getJSONArray("products");

                        products.clear();
                        imageUrls.clear();

                        for (int i = 0; i < arr.length(); i++) {
                            var obj = arr.getJSONObject(i);

                            int id = obj.getInt("id");
                            String name = obj.getString("name");
                            double price = obj.getDouble("price");

                            String path = obj.getString("img_path");
                            String fullUrl = "http://10.0.2.2/wonka/" + path;

                            String ingredients = obj.getJSONArray("ingredients").toString();

                            // Item.java constructor
                            Item item = new Item(id, name, price, 1, path, ingredients);

                            item.setFavorite(favoriteIds.contains(id));

                            products.add(item);
                            imageUrls.add(fullUrl);
                        }

                        recyclerView.setAdapter(new ProductsAdapter(this, products, imageUrls));

                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing products", Toast.LENGTH_SHORT).show();
                    }
                },
                err -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(req);
    }

    // ---------------------------------------------------------
    // MENU
    // ---------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            startActivity(new Intent(this, CartViewActivity.class));
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

}
