package top.lingyuzhao.soulquestionapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.core.splashscreen.SplashScreen;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        ImageView splashImage = findViewById(R.id.splash_image);
        Glide.with(this)
            .load("https://question.lingyuzhao.top/image/logo_text.webp")
            .into(splashImage);
            
        ProgressBar loadingProgress = findViewById(R.id.loading_progress);
        loadingProgress.setVisibility(View.VISIBLE);
        
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 3000);
    }
}