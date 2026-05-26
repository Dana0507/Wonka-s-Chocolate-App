package com.example.wonka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wonka.databinding.FavoritesListBinding;
import com.example.wonka.databinding.FavoritesListContentBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class FavoritesListActivity extends AppCompatActivity {

    private FavoritesListContentBinding binding;
    private FavoritesListAdapter adapter;
    private ArrayList<FavoriteItem> favoriteItems;

    SharedPreferences sp;
    int userId = -1;
    private static final String BASE_URL = "http://10.0.2.2/wonka/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FavoritesListContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        sp = getSharedPreferences("myfile", Context.MODE_PRIVATE);
        userId = sp.getInt("user_id", -1);

        favoriteItems = new ArrayList<>();

        adapter = new FavoritesListAdapter(
                this,
                favoriteItems,
                item -> {
                    Intent intent = new Intent(FavoritesListActivity.this, ProductViewActivity.class);
                    intent.putExtra("product_id", item.getId());
                    startActivity(intent);
                }
        );

        binding.recyclerFavList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerFavList.setAdapter(adapter);

        loadFavorites();
    }

    private void loadFavorites() {

        String url = BASE_URL + "get_favorites.php?user_id=" + userId;

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                res -> {
                    try {
                        if (!res.getBoolean("success")) return;

                        JSONArray arr = res.getJSONArray("data");
                        favoriteItems.clear();

                        for (int i = 0; i < arr.length(); i++) {

                            JSONObject o = arr.getJSONObject(i);

                            FavoriteItem item = new FavoriteItem();
                            item.setId(o.getInt("id"));
                            item.setName(o.getString("name"));
                            item.setPrice(o.getString("price"));
                            item.setImage(o.getString("image"));
                            item.setImagePath(o.getString("image_path"));

                            // full-page → vertical list
                            item.setHorizontal(false);

                            favoriteItems.add(item);
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                err -> err.printStackTrace()
        );

        Volley.newRequestQueue(this).add(req);
    }

    // ---------------------------------------------------------
    // MENU
    // ---------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favoriteslist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_shop) {
            startActivity(new Intent(this, ProductsActivity.class));
            return true;
        }
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

        return super.onOptionsItemSelected(item);
    }

}
