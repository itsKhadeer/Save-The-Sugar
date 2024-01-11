package com.example.deltaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    Button start_btn ;
    ImageView help_btn ;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        start_btn = findViewById(R.id.play_btn);

        start_btn.setOnClickListener(v -> {
            Intent intent = new Intent( HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });


    }
}