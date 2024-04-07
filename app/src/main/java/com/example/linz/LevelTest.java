package com.example.linz;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import com.example.linz.databinding.ActivityLevelTestBinding;

public class LevelTest extends AppCompatActivity {

    public ActivityLevelTestBinding binding;
    private static final String USER_NAME = "user_name";

    public int screenWidth;
    public int screenHeight;
    public float screenRatio;

    public float scaleRatioForBackground;

    public int levelMapWidth = 3600; //TODO make automatically changing
    public int levelMapHeight = 1500;

    public float levelMapWidthTheoretical;
    public float levelMapHeightTheoretical;

    public float startActionGamepadPositionX;
    public float startActionGamepadPositionY;

    public float sinAngleGamepad;
    public float cosAngleGamepad;

    public int movingCondition;
    public float momentSpeedX;
    public float momentSpeedY;
    public float absolutSpeed = 15;

    //styles
    public int plateWidth = Style.mapPlateWidth;
    public int plateHeight = Style.mapPlateHeight;
    public float plateForY = Style.plateForY;
    public float gamepadAlpha = Style.gamepadAlpha;
    public float gamepadScale = Style.gamepadScale;
    public float gemepadActionButtonRadius = Style.gemepadActionButtonRadius;

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

    public void backgroundRender(){
        float defScale;
        binding.backgroundLevel.setImageResource(R.drawable.sand_background);

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
        //binding.backgroundLevel.setX(120*scaleRatioForBackground/1.5F);
        binding.backgroundLevel.setScaleX(scaleRatioForBackground);
        binding.backgroundLevel.setScaleY(scaleRatioForBackground);

    }

    public void movingListener(float xo, float yo){
        float momentSpeed;
        float hypotGamepad;
        xo = xo * gamepadScale;
        yo = yo * gamepadScale;
        hypotGamepad = (float)Math.hypot(xo, yo);
        sinAngleGamepad = yo / hypotGamepad;
        cosAngleGamepad = xo / hypotGamepad;
        if (Math.pow(xo, 2)+Math.pow(yo,2)>Math.pow(gemepadActionButtonRadius*gamepadScale, 2)) {
            binding.gamepadActive.setX(startActionGamepadPositionX + gemepadActionButtonRadius*gamepadScale*cosAngleGamepad);
            binding.gamepadActive.setY(startActionGamepadPositionY + gemepadActionButtonRadius*gamepadScale*sinAngleGamepad);
            momentSpeed = absolutSpeed;
        } else {
            binding.gamepadActive.setX(startActionGamepadPositionX + xo);
            binding.gamepadActive.setY(startActionGamepadPositionY + yo);
            momentSpeed = (float)(absolutSpeed * (Math.sqrt(Math.pow(xo, 2)+Math.pow(yo, 2)))/(gemepadActionButtonRadius*gamepadScale));
        }
        momentSpeedX = momentSpeed * - cosAngleGamepad;
        momentSpeedY = momentSpeed * - sinAngleGamepad;

        binding.backgroundLevel.setX(binding.backgroundLevel.getX()+momentSpeedX);
        binding.backgroundLevel.setY(binding.backgroundLevel.getY()+momentSpeedY);
    }

    public void toStart(){
        binding.gamepadActive.setX(startActionGamepadPositionX);
        binding.gamepadActive.setY(startActionGamepadPositionY);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void motionFromGamepad(){

        float gamepadPadding = (float)screenHeight/12;

        startActionGamepadPositionX = (float)-screenWidth/2 + gamepadPadding + (float)screenHeight/12;
        startActionGamepadPositionY = gamepadPadding + (float)screenHeight/6;

        binding.gamepad.setImageResource(R.drawable.gamepad_stand);
        binding.gamepad.setX(startActionGamepadPositionX);
        binding.gamepad.setY(startActionGamepadPositionY);
        binding.gamepad.setScaleY(gamepadScale);
        binding.gamepad.setScaleX(gamepadScale);
        binding.gamepad.setAlpha(gamepadAlpha);

        binding.gamepadActive.setImageResource(R.drawable.gamepad_activ);
        binding.gamepadActive.setX(startActionGamepadPositionX);
        binding.gamepadActive.setY(startActionGamepadPositionY);
        binding.gamepadActive.setScaleY(gamepadScale);
        binding.gamepadActive.setScaleX(gamepadScale);
        binding.gamepadActive.setAlpha(gamepadAlpha * 1.5F);


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLevelTestBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        decoratingWindow();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        screenRatio = (float)screenWidth/screenHeight;

        backgroundRender();
        motionFromGamepad();

        float realPosGamepadRatioX = (float)-screenWidth/2-gemepadActionButtonRadius*gamepadScale/2 ;
        float realPosGamepadRatioY = -startActionGamepadPositionY-(float)screenHeight/6-gemepadActionButtonRadius*gamepadScale/2;

        binding.gamepad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int actionEv = event.getAction();
                if (actionEv == MotionEvent.ACTION_DOWN){
                    if (Math.pow(event.getX()+realPosGamepadRatioX, 2) + Math.pow(event.getY()+realPosGamepadRatioY, 2) < Math.pow(2*gemepadActionButtonRadius*gamepadScale, 2)){
                        movingCondition = 1;
                    }
                }
                if (actionEv == MotionEvent.ACTION_MOVE && movingCondition == 1) {
                    movingListener(event.getX()+realPosGamepadRatioX, event.getY()+realPosGamepadRatioY);
                } else if (actionEv == MotionEvent.ACTION_UP) {
                    toStart();
                    movingCondition = 0;
                }
                return true;
            }
        });
    }
}