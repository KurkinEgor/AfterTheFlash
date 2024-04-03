package com.example.linz;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import com.example.linz.databinding.ActivityMainMenuBinding;
import java.util.Random;

public class MainMenu extends AppCompatActivity{

    public ActivityMainMenuBinding binding;
    private static final String USER_NAME = "user_name";
    public final String TAG = "MY_TAG";

    //initialization
    public int screenWidth;
    public double screenWidthRecycle;
    public int screenHeight;
    public float deltaImageSize;
    public float cloudsScale;

    //style
    public int imageWidth = Style.MainLoaderImageWidth;
    public int imageHeight = Style.MainLoaderImageHeight;
    public double widthRatio = Style.MainLoaderImageScreenRatio;
    public int buttonsPadding = Style.ButtonsMainMenuPadding;
    public int buttonsFontSize = Style.ButtonsMainMenuFontSize;
    public int cloudsMainMenuImageWidth = Style.cloudsMainMenuImageWidth;
    public int cloudsMainMenuImageStartPosition = Style.cloudsMainMenuImageStartPosition;
    public int cloudsMainMenuImageFinishPosition = Style.cloudsMainMenuImageFinishPosition;
    public int cloudsMainMenuImageBackward = Style.cloudsMainMenuImageBackward;
    public int cloudsStep = Style.cloudsMainMenuStep;
    public float buttonsMainMenuSettingScaleY = Style.ButtonsMainMenuSettingScaleY;
    public float buttonsMainMenuSettingScaleX = Style.ButtonsMainMenuSettingScaleX;
    public float LogoMainMenuImageScale = Style.LogoMainMenuImageScale;
    public int LogoMainMenuImageXPos = Style.LogoMainMenuImageXPos;
    public int LogoMainMenuImageYPos = Style.LogoMainMenuImageYPos;

    Random rand = new Random();
    public int wind_speed = rand.nextInt(70)+10;

    public void decoratingWindow(){
        //Декорируем окно
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    public void backgroundRender(){
        binding.skyImage.setImageResource(R.drawable.sky);
        binding.cloudsImage.setImageResource(R.drawable.clouds);
        binding.cityImage.setImageResource(R.drawable.city);
        if((screenWidthRecycle/screenHeight) > ((double)(imageWidth/imageHeight))){
            deltaImageSize = (float)screenWidthRecycle/imageWidth;
            binding.skyImage.setY((screenHeight - imageHeight * deltaImageSize)/2);
            binding.cloudsImage.setY((screenHeight - imageHeight * deltaImageSize)/2);
            binding.cityImage.setY((screenHeight - imageHeight * deltaImageSize)/2);
        } else {
            deltaImageSize = (float)screenHeight/imageHeight;
        }
        binding.skyImage.setX(0);
        binding.skyImage.setScaleX(deltaImageSize);
        binding.skyImage.setScaleY(deltaImageSize);
        binding.cloudsImage.setScaleX(deltaImageSize*cloudsMainMenuImageWidth/imageWidth);
        binding.cloudsImage.setScaleY(deltaImageSize*cloudsMainMenuImageWidth/imageWidth);
        cloudsScale = deltaImageSize*cloudsMainMenuImageWidth/imageWidth;
        binding.cloudsImage.setX(-cloudsMainMenuImageStartPosition*cloudsScale/2);
        binding.cityImage.setX(0);
        binding.cityImage.setScaleX(deltaImageSize);
        binding.cityImage.setScaleY(deltaImageSize);

        binding.logoImage.setImageResource(R.drawable.logo);
        binding.logoImage.setScaleY(LogoMainMenuImageScale);
        binding.logoImage.setScaleX(LogoMainMenuImageScale);
        binding.logoImage.setX(LogoMainMenuImageXPos);
        binding.logoImage.setY(LogoMainMenuImageYPos);
    }

    @SuppressLint("ResourceAsColor")
    public void buttonRender(){
        int buttonMinHeight = (screenHeight/2-buttonsPadding*5)/3;
        int buttonMinWidth = buttonMinHeight * 4;
        float buttonsPaddingLeft = (float)(buttonsPadding*4.5);

        binding.buttonMainMenuSingle.setX(buttonsPaddingLeft);
        binding.buttonMainMenuSingle.setY((float)screenHeight/2);
        binding.buttonMainMenuSingle.setMinimumHeight(buttonMinHeight);
        binding.buttonMainMenuSingle.setMinimumWidth(buttonMinWidth);
        binding.buttonMainMenuSingle.setTextSize(buttonsFontSize);
        binding.buttonMainMenuSingle.setText(R.string.MainMenuButtonSingle);

        binding.buttonMainMenuOnline.setX(buttonsPaddingLeft);
        binding.buttonMainMenuOnline.setY((float)screenHeight/2 + buttonsPadding + buttonMinHeight);
        binding.buttonMainMenuOnline.setMinimumHeight(buttonMinHeight);
        binding.buttonMainMenuOnline.setMinimumWidth(buttonMinWidth);
        binding.buttonMainMenuOnline.setTextSize(buttonsFontSize);
        binding.buttonMainMenuOnline.setText(R.string.MainMenuButtonOnline);

        binding.buttonMainMenuTutorial.setX(buttonsPaddingLeft);
        binding.buttonMainMenuTutorial.setY((float)screenHeight/2 + 2 *(buttonsPadding + buttonMinHeight));
        binding.buttonMainMenuTutorial.setMinimumHeight(buttonMinHeight);
        binding.buttonMainMenuTutorial.setMinimumWidth(buttonMinWidth);
        binding.buttonMainMenuTutorial.setTextSize(buttonsFontSize);
        binding.buttonMainMenuTutorial.setText(R.string.MainMenuButtonTutorial);


        binding.buttonMainMenuSettings.setBackgroundResource(R.drawable.settings);
        binding.buttonMainMenuSettings.setScaleY(buttonsMainMenuSettingScaleY);
        binding.buttonMainMenuSettings.setScaleX(buttonsMainMenuSettingScaleX);

        binding.buttonMainMenuSettings.setX((float)(screenWidthRecycle-(binding.buttonMainMenuSettings.getWidth()*binding.buttonMainMenuSettings.getScaleX())-buttonsPadding));
        binding.buttonMainMenuSettings.setX(0);
    }

    //фабричный метод приёма запросов
    public static Intent newIntent(Context context, String userName) {
        Intent intent = new Intent(context, MainMenu.class);
        intent.putExtra(USER_NAME, userName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //connecting binding
        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        decoratingWindow();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        screenWidthRecycle = screenWidth * (1+widthRatio);

        backgroundRender();
        buttonRender();

        Thread clouds_animation = new Thread(() -> {
            while (true) {
                try {
                    binding.cloudsImage.setX(binding.cloudsImage.getX() + cloudsStep);
                    Thread.sleep(wind_speed);
                    if (binding.cloudsImage.getX()>cloudsMainMenuImageFinishPosition*cloudsScale/2){
                        binding.cloudsImage.setX(binding.cloudsImage.getX()-cloudsMainMenuImageBackward*cloudsScale/2);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        clouds_animation.setDaemon(true);
        clouds_animation.start();
    }
}