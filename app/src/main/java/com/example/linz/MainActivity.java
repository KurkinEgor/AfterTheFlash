package com.example.linz;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.linz.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //connecting binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        //Декорируем окно
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }
}