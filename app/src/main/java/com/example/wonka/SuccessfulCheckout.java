package com.example.wonka;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessfulCheckout extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successful_checkout);

        mediaPlayer = MediaPlayer.create(this, R.raw.yay_sound);
        mediaPlayer.start();

        Button continueBtn = findViewById(R.id.continue_btn);

        continueBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SuccessfulCheckout.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
