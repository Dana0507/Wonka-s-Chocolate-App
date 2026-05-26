package com.example.wonka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private Context context;
    private List<Item> list;
    private List<String> imageUrls;
    private int userId;

    public ProductsAdapter(Context context, List<Item> list, List<String> imageUrls) {
        this.context = context;
        this.list = list;
        this.imageUrls = imageUrls;

        SharedPreferences sp = context.getSharedPreferences("myfile", Context.MODE_PRIVATE);
        userId = sp.getInt("user_id", -1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView parent;
        ImageView image;
        TextView title, price, favBtn;

        public ViewHolder(View view) {
            super(view);

            parent = view.findViewById(R.id.card_item);
            image = view.findViewById(R.id.item_image);
            title = view.findViewById(R.id.item_title);
            price = view.findViewById(R.id.item_price);
            favBtn = view.findViewById(R.id.btn_favorite_toggle);
        }
    }

    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductsAdapter.ViewHolder holder, int position) {

        Item item = list.get(position);

        holder.title.setText(item.getName());
        holder.price.setText("$" + item.getPrice());

        // GET IMAGE URL BY POSITION
        String imageUrl = imageUrls.get(position);

        // LOAD USING GLIDE
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.default_pfp)
                .into(holder.image);

        // CLICK → CartViewActivity
       /* holder.parent.setOnClickListener(v -> {
            Intent intent = new Intent(context, CartViewActivity.class);
            intent.putExtra("product_id", item.getProductId());
            context.startActivity(intent);
        });*/

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductViewActivity.class);

            intent.putExtra("product_id", item.getProductId());
            intent.putExtra("product_name", item.getName());
            intent.putExtra("product_price", item.getPrice());
            intent.putExtra("product_image", imageUrls.get(position));
            intent.putExtra("product_quantity", item.getQuantity());
            intent.putExtra("product_is_favorite", item.isFavorite());

            context.startActivity(intent);
        });


        // FAVORITE ICON DISPLAY
        updateFavIcon(holder.favBtn, item.isFavorite());

        // FAVORITE TOGGLE
        holder.favBtn.setOnClickListener(v -> {
            boolean newState = !item.isFavorite();
            item.setFavorite(newState);
            updateFavIcon(holder.favBtn, newState);

            if (newState) addFavorite(item.getProductId());
            else removeFavorite(item.getProductId());
        });
    }

    private void updateFavIcon(TextView favBtn, boolean isFav) {
        favBtn.setText(isFav ? "★" : "☆");
        favBtn.setTextColor(isFav ? Color.parseColor("#FFD700") : Color.GRAY);
    }

    private void addFavorite(int productId) {
        String url = "http://10.0.2.2/wonka/add_favorite.php";

        StringRequest req = new StringRequest(Request.Method.POST, url,
                r -> {},
                e -> {}
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(userId));
                map.put("product_id", String.valueOf(productId));
                return map;
            }
        };

        Volley.newRequestQueue(context).add(req);
    }

    private void removeFavorite(int productId) {
        String url = "http://10.0.2.2/wonka/remove_favorite.php";

        StringRequest req = new StringRequest(Request.Method.POST, url,
                r -> {},
                e -> {}
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(userId));
                map.put("product_id", String.valueOf(productId));
                return map;
            }
        };

        Volley.newRequestQueue(context).add(req);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
