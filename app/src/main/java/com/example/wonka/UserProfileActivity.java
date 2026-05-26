package com.example.wonka;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.bumptech.glide.Glide;
import com.example.wonka.databinding.UserProfileBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    // UI
    ImageView pfp;
    TextView usernameTv, emailTv, phoneTv, tv_ed_username;
    EditText editUsernameEd, editEmailEd, editPhoneEd;
    MaterialButton btnEditContact, btnSaveContact, btnEditPfp;
    RecyclerView favRecycler;
    LinearLayout username_ed_layout;

    // Favorites preview
    ArrayList<FavoriteItem> favoritesList = new ArrayList<>();
    FavoritesListAdapter favoritesAdapter;
    int userId;

    private AppBarConfiguration appBarConfiguration;
    private UserProfileBinding binding;
    Uri selectedImageUri = null;

    String BASE_URL = "http://10.0.2.2/wonka/";

    private static final int REQ_GALLERY = 100;
    private static final int REQ_CAMERA = 200;
    Uri cameraUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = UserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        SharedPreferences sp = getSharedPreferences("myfile", MODE_PRIVATE);
        userId = sp.getInt("user_id", -1);

        initViews();
        setListeners();

        loadUserInfo();
        loadFavorites();
    }


    private void initViews() {

        pfp = findViewById(R.id.pfp);

        usernameTv = findViewById(R.id.profile_username);
        emailTv = findViewById(R.id.user_email);
        phoneTv = findViewById(R.id.user_phone);
        tv_ed_username = findViewById(R.id.tv_ed_username);

        editUsernameEd = findViewById(R.id.editTextText);
        editEmailEd = findViewById(R.id.edit_user_email);
        editPhoneEd = findViewById(R.id.edit_user_phone);

        username_ed_layout = findViewById(R.id.username_ed_layout);

        btnEditContact = findViewById(R.id.btn_edit_contact);
        btnSaveContact = findViewById(R.id.btn_save_contact);
        btnEditPfp = findViewById(R.id.btn_edit_pfp);

        favRecycler = findViewById(R.id.recycler_favorites);
        favRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }


    private void setListeners() {

        btnEditPfp.setOnClickListener(v -> chooseImageDialog());

        btnEditContact.setOnClickListener(v -> enterEditMode());

        btnSaveContact.setOnClickListener(v -> saveUserEdits());

        findViewById(R.id.btn_view_all_favorites)
                .setOnClickListener(v -> startActivity(new Intent(this, FavoritesListActivity.class)));
    }


    // ---------------------------------------------------------
    // ENTER EDIT MODE
    // ---------------------------------------------------------
    private void enterEditMode() {

        // Hide static text
        emailTv.setVisibility(View.GONE);
        phoneTv.setVisibility(View.GONE);

        // Fill edit boxes
        editUsernameEd.setText(usernameTv.getText().toString());
        editEmailEd.setText(emailTv.getText().toString());
        editPhoneEd.setText(phoneTv.getText().toString());

        // Show editable fields
        username_ed_layout.setVisibility(View.VISIBLE);
        editEmailEd.setVisibility(View.VISIBLE);
        editPhoneEd.setVisibility(View.VISIBLE);
        editUsernameEd.setVisibility(View.VISIBLE);
        tv_ed_username.setVisibility(View.VISIBLE);

        btnEditContact.setVisibility(View.GONE);
        btnSaveContact.setVisibility(View.VISIBLE);
    }
    // LOAD USER INFO (From updated get_user.php)
    // ---------------------------------------------------------
    private void loadUserInfo() {

        String url = BASE_URL + "get_user.php";

        StringRequest req = new StringRequest(Request.Method.POST, url,
                res -> {
                    try {
                        JSONObject obj = new JSONObject(res);

                        if (!obj.getString("status").equals("success")) return;

                        JSONObject u = obj.getJSONObject("user");

                        usernameTv.setText(u.getString("username"));
                        emailTv.setText(u.getString("email"));
                        phoneTv.setText(u.getString("phone"));

                        editEmailEd.setText(u.getString("email"));
                        editPhoneEd.setText(u.getString("phone"));

                        // ------------------------------------------------------
                        // LOAD PROFILE PICTURE WITH FALLBACK
                        // ------------------------------------------------------
                        String imgURL = u.optString("profile_pic_url", "");

                        if (imgURL == null || imgURL.equals("null") || imgURL.trim().isEmpty()) {
                            // No image in database → use default
                            pfp.setImageResource(R.drawable.default_pfp);

                        } else {
                            // Try loading from server
                            Glide.with(this)
                                    .load(imgURL)
                                    .placeholder(R.drawable.default_pfp)
                                    .error(R.drawable.default_pfp) // fallback on broken link
                                    .into(pfp);
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Error loading user info", Toast.LENGTH_SHORT).show();
                        pfp.setImageResource(R.drawable.default_pfp); // final fallback
                    }
                },
                err -> {
                    Toast.makeText(this, "Network error user info", Toast.LENGTH_SHORT).show();
                    pfp.setImageResource(R.drawable.default_pfp);
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(userId));
                return map;
            }
        };

        Volley.newRequestQueue(this).add(req);
    }



    // ---------------------------------------------------------
    // LOAD FAVORITES PREVIEW
    // ---------------------------------------------------------
    private void loadFavorites() {

        String url = BASE_URL + "get_favorites.php?user_id=" + userId;

        StringRequest req = new StringRequest(Request.Method.GET, url,
                res -> {
                    try {
                        JSONObject obj = new JSONObject(res);
                        JSONArray arr = obj.getJSONArray("data");

                        favoritesList.clear();

                        for (int i = 0; i < arr.length(); i++) {

                            JSONObject p = arr.getJSONObject(i);

                            FavoriteItem f = new FavoriteItem();
                            f.setId(p.getInt("id"));
                            f.setName(p.getString("name"));
                            f.setPrice(p.getString("price"));
                            f.setImage(p.getString("image"));
                            f.setImagePath(p.getString("image_path"));
                            f.setHorizontal(true);

                            favoritesList.add(f);
                        }

                        favoritesAdapter = new FavoritesListAdapter(
                                UserProfileActivity.this,
                                favoritesList,
                                item -> {
                                    Intent i2 = new Intent(UserProfileActivity.this, ProductViewActivity.class);
                                    i2.putExtra("product_id", item.getId());
                                    startActivity(i2);
                                }
                        );

                        favRecycler.setAdapter(favoritesAdapter);

                    } catch (Exception e) {
                        Toast.makeText(this, "Error loading favorites", Toast.LENGTH_SHORT).show();
                    }
                },
                err -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(req);
    }


    // ---------------------------------------------------------
    // SAVE PROFILE (MULTIPART FOR IMAGE UPLOAD)
    // ---------------------------------------------------------
    private void saveUserEdits() {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(
                Request.Method.POST,
                BASE_URL + "update_profile.php",
                response -> {
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();

                    emailTv.setVisibility(View.VISIBLE);
                    phoneTv.setVisibility(View.VISIBLE);

                    username_ed_layout.setVisibility(View.GONE);
                    editEmailEd.setVisibility(View.GONE);
                    editPhoneEd.setVisibility(View.GONE);
                    editUsernameEd.setVisibility(View.GONE);
                    tv_ed_username.setVisibility(View.GONE);

                    btnSaveContact.setVisibility(View.GONE);
                    btnEditContact.setVisibility(View.VISIBLE);

                    loadUserInfo(); // refresh data after update
                },
                error -> Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> m = new HashMap<>();
                m.put("id", String.valueOf(userId));
                m.put("email", editEmailEd.getText().toString());
                m.put("phone", editPhoneEd.getText().toString());
                m.put("username", editUsernameEd.getText().toString());
                return m;
            }

            @Override
            public Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                if (selectedImageUri != null) {
                    try {
                        // Detect MIME type from Android
                        String mime = getContentResolver().getType(selectedImageUri);
                        String ext = "jpg"; // fallback

                        if (mime != null) {
                            if (mime.contains("png")) ext = "png";
                            else if (mime.contains("webp")) ext = "webp";
                            else if (mime.contains("jpeg")) ext = "jpg";
                        }

                        InputStream is = getContentResolver().openInputStream(selectedImageUri);
                        byte[] imgData = new byte[is.available()];
                        is.read(imgData);

                        String fileName = userId + "." + ext;

                        // YOUR DataPart only accepts 2 args
                        params.put("profile_pic", new DataPart(fileName, imgData));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return params;
            }


        };

        Volley.newRequestQueue(this).add(multipartRequest);
    }


    // ---------------------------------------------------------
    // IMAGE PICKING
    // ---------------------------------------------------------
    private void chooseImageDialog() {
        String[] options = {"Camera", "Gallery"};

        new AlertDialog.Builder(this)
                .setTitle("Choose Image")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) openCamera();
                    else openGallery();
                })
                .show();
    }

    private void openGallery() {
        Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pick, REQ_GALLERY);
    }

    private void openCamera() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    REQ_CAMERA
            );
            return;
        }

        ContentValues v = new ContentValues();
        v.put(MediaStore.Images.Media.TITLE, "New Image");
        cameraUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, v);

        Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cam.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(cam, REQ_CAMERA);
    }

    @Override
    protected void onActivityResult(int req, int result, @Nullable Intent data) {
        super.onActivityResult(req, result, data);

        if (result != Activity.RESULT_OK) return;

        if (req == REQ_GALLERY && data != null) {
            selectedImageUri = data.getData();
            pfp.setImageURI(selectedImageUri);

        } else if (req == REQ_CAMERA) {
            selectedImageUri = cameraUri;
            pfp.setImageURI(selectedImageUri);
        }
    }

    // ---------------------------------------------------------
    // MENU
    // ---------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_profile_menu, menu);
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
        if (id == R.id.action_favorites) {
            startActivity(new Intent(this, FavoritesListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
