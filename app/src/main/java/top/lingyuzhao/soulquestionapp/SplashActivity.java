package top.lingyuzhao.soulquestionapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.core.splashscreen.SplashScreen;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    private String currentVersion;

    private String getAppVersion() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.2"; // 默认版本号
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentVersion = getAppVersion();
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        ImageView splashImage = findViewById(R.id.splash_image);
        Glide.with(this)
            .load("https://question.lingyuzhao.top/image/logo_text.webp")
            .into(splashImage);
            
        ProgressBar loadingProgress = findViewById(R.id.loading_progress);
        loadingProgress.setVisibility(View.VISIBLE);
        
        checkVersionUpdate();
    }
    
    private void checkVersionUpdate() {
        new Thread(() -> {
            try {
                URL url = new URL("https://gitee.com/BeardedManZhao/SoulQuestion-Android/releases/download/vlatest/latestVersion.txt");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String latestVersion = reader.readLine();
                StringBuilder updateContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    updateContent.append(line).append("\n");
                }
                reader.close();
                
                if (latestVersion != null && !latestVersion.equals(currentVersion)) {
                    runOnUiThread(() -> showUpdateDialog(latestVersion, updateContent.toString()));
                } else {
                    proceedToMainActivity();
                }
            } catch (Exception e) {
                e.printStackTrace();
                proceedToMainActivity();
            }
        }).start();
    }
    
    private void showUpdateDialog(String version, String content) {
        new AlertDialog.Builder(this)
            .setTitle("发现新版本 " + version)
            .setMessage(content)
            .setPositiveButton("下载", (dialog, which) -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
                    Uri.parse("https://gitee.com/BeardedManZhao/SoulQuestion-Android/releases/download/vlatest/SoulQuestion.apk"));
                finish();
                startActivity(browserIntent);
            })
            .setNegativeButton("忽略", (dialog, which) -> proceedToMainActivity())
            .setCancelable(false)
            .show();
    }
    
    private void proceedToMainActivity() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 3000);
    }
}