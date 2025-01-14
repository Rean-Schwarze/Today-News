package com.rean.todaynews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WelcomeActivity extends AppCompatActivity {

    private TextView tv_countdown;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 5000; // 设置倒计时时长，单位：毫秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化倒计时
        tv_countdown = findViewById(R.id.welcome_countdown);
        tv_countdown.setOnClickListener(v -> {
            if(countDownTimer!=null){
                countDownTimer.cancel();
                // 跳转到主页面
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        });
        startCountdown();
    }

    private void startCountdown(){
        countDownTimer =new CountDownTimer(timeLeftInMillis,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                tv_countdown.setText(secondsRemaining +" s");
            }

            @Override
            public void onFinish() {
                // 跳转到主页面
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}