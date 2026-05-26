package com.example.wonka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FavoritesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(FavoriteItem item);
    }

    private static final int TYPE_HORIZONTAL = 0;
    private static final int TYPE_VERTICAL = 1;

    private List<FavoriteItem> list;
    private OnItemClickListener listener;
    private Context context;

    private static final String BASE_URL = "http://10.0.2.2/wonka/";

    public FavoritesListAdapter(Context context, List<FavoriteItem> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isHorizontal() ? TYPE_HORIZONTAL : TYPE_VERTICAL;
    }

    // ---------------- VIEW HOLDERS ----------------

    public static class HorizontalViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public HorizontalViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.fav_item_image);
            title = view.findViewById(R.id.fav_item_title);
        }

        public void bind(FavoriteItem item, OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    public static class VerticalViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, price;

        public VerticalViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.item_image);
            title = view.findViewById(R.id.item_title);
            price = view.findViewById(R.id.item_price);
        }

        public void bind(FavoriteItem item, OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    // ---------------- ON CREATE ----------------

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HORIZONTAL) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_favorite, parent, false);
            return new HorizontalViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_favorite_vertical, parent, false);
            return new VerticalViewHolder(view);
        }
    }

    // ---------------- ON BIND ----------------

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        FavoriteItem item = list.get(position);
        String imageUrl = BASE_URL + item.getImagePath();

        if (holder instanceof HorizontalViewHolder) {
            HorizontalViewHolder h = (HorizontalViewHolder) holder;

            h.title.setText(item.getName());

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.default_pfp)
                    .into(h.image);

            h.bind(item, listener);

        } else {
            VerticalViewHolder v = (VerticalViewHolder) holder;

            v.title.setText(item.getName());
            v.price.setText("$" + item.getPrice());

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.default_pfp)
                    .into(v.image);

            v.bind(item, listener);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
