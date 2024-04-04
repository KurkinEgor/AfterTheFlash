package com.example.linz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.linz.databinding.ActivityLevelTestBinding;

public class LevelTest extends AppCompatActivity {

    public ActivityLevelTestBinding binding;
    private static final String USER_NAME = "user_name";

    public int screenWidth;
    public int screenHeight;
    public float screenRatio;
    public float backgroundImageScale;
    public float scaleRatioForBackground;
    public float defScale;

    public int levelMapWidth = 3600;
    public int levelMapHeight = 1500;
    public int plateWidth = 120;
    public int plateHeight = 100;
    public float plateForY = 10;
    public float levelMapWidthTheoretical;
    public float levelMapHeightTheoretical;

    public void decoratingWindow(){
        //Декорируем окно
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    //фабричный метод приёма запросов
    public static Intent newIntent(Context context, String userName) {
        Intent intent = new Intent(context, LevelTest.class);
        intent.putExtra(USER_NAME, userName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLevelTestBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        decoratingWindow();

        binding.backgroundLevel.setImageResource(R.drawable.sand_background);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        screenRatio = (float)screenWidth/screenHeight;
        if((float)levelMapWidth/levelMapHeight>screenRatio){
            defScale = (levelMapWidthTheoretical-levelMapWidth)/screenWidth;
            levelMapHeightTheoretical = levelMapWidth / screenRatio;
            levelMapWidthTheoretical = levelMapWidth;
        } else {
            defScale = (levelMapHeightTheoretical-levelMapHeight)/screenWidth;
            levelMapWidthTheoretical = levelMapHeight * screenRatio;
            levelMapHeightTheoretical = levelMapHeight;
        }
        scaleRatioForBackground = defScale/1.5F*levelMapHeight/plateHeight/plateForY;
        backgroundImageScale = screenHeight / levelMapHeightTheoretical;
        //binding.backgroundLevel.setX(120*scaleRatioForBackground/1.5F);
        binding.backgroundLevel.setScaleX(scaleRatioForBackground);
        binding.backgroundLevel.setScaleY(scaleRatioForBackground);
    }
}