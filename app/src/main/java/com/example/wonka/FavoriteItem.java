package com.example.wonka;

public class FavoriteItem {
    private int id;
    private String name;
    private String price;
    private String image;
    private String image_path;

    private boolean horizontal; // used by adapter

    // Getters & setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getImagePath() { return image_path; }
    public void setImagePath(String image_path) { this.image_path = image_path; }

    public boolean isHorizontal() { return horizontal; }
    public void setHorizontal(boolean horizontal) { this.horizontal = horizontal; }
}
