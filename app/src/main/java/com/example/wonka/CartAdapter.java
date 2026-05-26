package com.example.wonka;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    List<Item> cartList;
    CartUpdateListener listener;
    SharedPreferences sp;
    int userId = 0;

    public interface CartUpdateListener {
        void onCartUpdated(Item updatedItem);
    }

    public CartAdapter(List<Item> cartList, int userId, CartUpdateListener listener) {
        this.cartList = cartList;
        this.userId = userId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        Item item = cartList.get(position);

        holder.name.setText(item.getName());
        holder.price.setText(String.format("$%.2f", item.getPrice()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        Glide.with(holder.image.getContext())
                .load(item.getImagePath())
                .placeholder(R.drawable.wonkas_bar)
                .into(holder.image);


        holder.plus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            holder.quantity.setText(String.valueOf(item.getQuantity()));
            updateQuantityOnDB(v, item);
            listener.onCartUpdated(item);
        });

        holder.minus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                holder.quantity.setText(String.valueOf(item.getQuantity()));
                updateQuantityOnDB(v, item);
                listener.onCartUpdated(item);
            }
        });

        holder.remove.setOnClickListener(v -> {
            Item removed = cartList.get(position);
            removeFromDB(v, removed);
            cartList.remove(position);
            notifyItemRemoved(position);
            listener.onCartUpdated(removed);
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price, quantity;
        ImageButton plus, minus, remove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.itemImage);
            name = (TextView) itemView.findViewById(R.id.itemName);
            price = (TextView) itemView.findViewById(R.id.itemPrice);
            quantity = (TextView) itemView.findViewById(R.id.quantityText);
            plus = (ImageButton) itemView.findViewById(R.id.plusBtn);
            minus = (ImageButton) itemView.findViewById(R.id.minusBtn);
            remove = (ImageButton) itemView.findViewById(R.id.removeBtn);
        }
    }

    private void updateQuantityOnDB(View v, Item item) {
        String url = "http://10.0.2.2/wonka/updateCart.php?user_id="+userId
                + "&product_id=" + item.getProductId()
                + "&quantity=" + item.getQuantity();

        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        StringRequest req = new StringRequest(Request.Method.GET, url,
                response -> {},
                error -> {});
        queue.add(req);
    }

    private void removeFromDB(View v, Item item) {
        String url = "http://10.0.2.2/wonka/removeFromCart.php?user_id="+userId
                + "&product_id=" + item.getProductId();

        RequestQueue queue = Volley.newRequestQueue(v.getContext());
        queue.add(new StringRequest(Request.Method.GET, url, r->{}, e->{}));
    }

}

