package com.example.linz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import com.example.linz.databinding.ActivityMainLoaderBinding;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainLoader extends AppCompatActivity{

    public ActivityMainLoaderBinding binding;
    public final String TAG = "MY_TAG";

    //initialization
    public float cloudsScale;
    public int screenWidth;
    public double screenWidthRecycle;
    public int screenHeight;
    public float deltaImageSize;
    public int timeProgressBar = 25;

    //styles
    public int imageWidth = Style.MainLoaderImageWidth;
    public int imageHeight = Style.MainLoaderImageHeight;
    public double widthRatio = Style.MainLoaderImageScreenRatio;
    public int progressBarMainWidth = Style.LoaderProgressBarMainWidth;
    public int progressBarMainHeight = Style.LoaderProgressBarMainHeight;
    public int progressBarMainVerticalAlign = Style.LoaderProgressBarMainVerticalAlign;
    public int cloudsMainMenuImageWidth = Style.cloudsMainMenuImageWidth;
    public int cloudsMainMenuImageStartPosition = Style.cloudsMainMenuImageStartPosition;
    public int cloudsMainMenuImageFinishPosition = Style.cloudsMainMenuImageFinishPosition;
    public int cloudsMainMenuImageBackward = Style.cloudsMainMenuImageBackward;
    public int cloudsStep = Style.cloudsMainMenuStep;
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
        binding.imageView6.setImageResource(R.drawable.sky);
        binding.cloudsImage.setImageResource(R.drawable.clouds);
        binding.imageView10.setImageResource(R.drawable.city);
        if((screenWidthRecycle/screenHeight) > ((double)(imageWidth/imageHeight))){
            deltaImageSize = (float)screenWidthRecycle/imageWidth;
            binding.imageView6.setY((screenHeight - imageHeight * deltaImageSize)/2);
            binding.cloudsImage.setY((screenHeight - imageHeight * deltaImageSize)/2);
            binding.imageView10.setY((screenHeight - imageHeight * deltaImageSize)/2);
        } else {
            deltaImageSize = (float)screenHeight/imageHeight;
        }
        binding.imageView6.setX(0);
        binding.imageView6.setScaleX(deltaImageSize);
        binding.imageView6.setScaleY(deltaImageSize);
        binding.cloudsImage.setScaleX(deltaImageSize*cloudsMainMenuImageWidth/imageWidth);
        binding.cloudsImage.setScaleY(deltaImageSize*cloudsMainMenuImageWidth/imageWidth);
        cloudsScale = deltaImageSize*cloudsMainMenuImageWidth/imageWidth;
        binding.cloudsImage.setX(-cloudsMainMenuImageStartPosition*cloudsScale/2);
        binding.imageView10.setX(0);
        binding.imageView10.setScaleX(deltaImageSize);
        binding.imageView10.setScaleY(deltaImageSize);

        binding.logoImage.setImageResource(R.drawable.logo);
        binding.logoImage.setScaleY(LogoMainMenuImageScale);
        binding.logoImage.setScaleX(LogoMainMenuImageScale);
        binding.logoImage.setX(LogoMainMenuImageXPos);
        binding.logoImage.setY(LogoMainMenuImageYPos);

    }

    private void launchMainMenuFromLoader(){
        Intent intent = MainMenu.newIntent(MainLoader.this, "George");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ActivityTheme);
        //connecting binding
        binding = ActivityMainLoaderBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        decoratingWindow();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        screenWidthRecycle = screenWidth * (1+widthRatio);

        backgroundRender();

        binding.loadBarMainLoader.setMinimumWidth(progressBarMainWidth);
        binding.loadBarMainLoader.setMinimumHeight(progressBarMainHeight);
        binding.loadBarMainLoader.setX(((float)screenWidthRecycle/2)-(float)progressBarMainWidth/2);
        binding.loadBarMainLoader.setY((float)screenHeight - progressBarMainVerticalAlign);
        binding.loadBarMainLoader.setVisibility(View.VISIBLE);
        binding.loadBarMainLoader.setProgress(0);

        final Timer progressBarTimer = new Timer(); //TODO Remake this one big mistake
        TimerTask timerTaskProgressBar = new TimerTask() {
            int counter = 0;
            @Override
            public void run() {
                counter++;
                binding.loadBarMainLoader.setProgress(counter);
                if (counter == 100){
                    progressBarTimer.cancel();
                    launchMainMenuFromLoader();
                }
            }
        };

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
        progressBarTimer.schedule(timerTaskProgressBar, 0, timeProgressBar);
    }
}