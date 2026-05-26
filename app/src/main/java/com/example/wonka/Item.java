package com.example.wonka;

public class Item {
    private final int productId;
    private String name;
    private double price;
    private int quantity;
    private String imgPath;
    private String ingredients;
    private boolean isFavorite;

    public Item(int productId, String name, double price, int quantity, String imgPath, String ingredients) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imgPath = imgPath;
        this.ingredients = ingredients;
        this.isFavorite = false;
    }

    public Item(int productId, String name, double price, int quantity, String imgPath) {

        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imgPath = imgPath;
    }

    public int getProductId() { return productId; }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getImagePath() { return imgPath; }
    public String getIngredients() { return ingredients; }
    public boolean isFavorite() { return isFavorite; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setImageRes(String imgResId) { this.imgPath = imgPath; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    public void setFavorite(boolean favorite) { this.isFavorite = favorite; }

}

