package com.example.wonka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.example.wonka.databinding.ActivityProductViewBinding;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Objects;


public class ProductViewActivity extends AppCompatActivity {
    ImageView productImage, favoriteButton;
    TextView productName, ingredientsList, quantityText, productPrice;
    Button addToCartButton;
    ImageButton plusBtn, minusBtn;
    int quantity = 1;
    boolean isFavorite = false;
    SharedPreferences sp;
    int userId = 0;
    int productId = 0;
    private AppBarConfiguration appBarConfiguration;
    private ActivityProductViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        sp = getSharedPreferences("myfile", Context.MODE_PRIVATE);
        userId = sp.getInt("user_id", -1);

        productImage = (ImageView) findViewById(R.id.productImage);
        productName = (TextView) findViewById(R.id.productName);
        productPrice = (TextView) findViewById(R.id.productPrice);
        ingredientsList = (TextView) findViewById(R.id.ingredientsList);
        addToCartButton = (Button) findViewById(R.id.addToCartButton);
        favoriteButton = (ImageView) findViewById(R.id.favoriteBtn);
        quantityText = (TextView) findViewById(R.id.quantityText);
        plusBtn = (ImageButton) findViewById(R.id.plusBtn);
        minusBtn = (ImageButton) findViewById(R.id.minusBtn);

        productId = getIntent().getIntExtra("product_id", 0);
        String name = getIntent().getStringExtra("product_name");
        String imagePath = getIntent().getStringExtra("product_image");
        double price = getIntent().getDoubleExtra("product_price", 4.00);
        loadFavoriteStatus(productId);

        productName.setText(name);
        productPrice.setText(String.valueOf(price));
        quantityText.setText(String.valueOf(quantity));
        Glide.with(this)
                .load(imagePath)
                .placeholder(R.drawable.wonkas_bar)
                .error(R.drawable.wonkas_bar)
                .into(productImage);

        loadIngredients(productId);

        plusBtn.setOnClickListener(v -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
        });

        minusBtn.setOnClickListener(v -> {
            if (quantity > 1) quantity--;
            quantityText.setText(String.valueOf(quantity));
        });

        addToCartButton.setOnClickListener(v -> {
            addToCart(name, productId, quantity);

        });

        favoriteButton.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            if (isFavorite) {
                favoriteButton.setImageResource(R.drawable.star_filled_icon);
                addToFavorites(name, productId);

            } else {
                favoriteButton.setImageResource(R.drawable.star_icon_border);
                removeFromFavorites(name, productId);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        if (id == R.id.action_favorites) {
            startActivity(new Intent(this, FavoritesListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadIngredients(int productId) {
        String url = "http://10.0.2.2/wonka/getIngredients.php?id=" + productId;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            sb.append("• ").append(jsonArray.getString(i)).append("\n");
                        }
                        ingredientsList.setText(sb.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load ingredients", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: "+ error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        queue.add(request);
    }

    private void addToCart(String name, int productId, int qty) {
        String url = "http://10.0.2.2/wonka/addToCart.php?user_id=" + userId + "&product_id=" + productId + "&quantity=" + qty;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            if (response.equals("1")) {
                Toast.makeText(this, name + " was successfully added to cart!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        queue.add(request);
    }

    private void loadFavoriteStatus(int productId) {
        String url = "http://10.0.2.2/wonka/checkFavorite.php?user_id=" + userId + "&product_id=" + productId;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    isFavorite = response.optBoolean("isFavorite", false);
                    updateFavoriteIcon(isFavorite);
                },
                error -> Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }


    private void addToFavorites(String name, int productId) {
        String url = "http://10.0.2.2/wonka/addFavorite.php?user_id=" + userId + "&product_id=" + productId;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("1")) {
                        Toast.makeText(this, name + " was added to favorites!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        queue.add(request);
    }

    private void removeFromFavorites(String name, int productId) {
        String url = "http://10.0.2.2/wonka/removeFavorite.php?user_id=" + userId + "&product_id=" + productId;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("1")) {
                        Toast.makeText(this, name + " was removed from favorites!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
    private void updateFavoriteIcon(boolean isFavorite) {
        if (isFavorite){
            favoriteButton.setImageResource(R.drawable.star_filled_icon);
        }
        else{
            favoriteButton.setImageResource(R.drawable.star_icon_border);
        }
    }
}
