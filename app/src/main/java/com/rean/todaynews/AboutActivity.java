package com.rean.todaynews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutActivity extends AppCompatActivity {

    private TextView about_repo_link;
    private Toolbar toolbar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化控件
        about_repo_link = findViewById(R.id.about_repo_link);
        toolbar = findViewById(R.id.about_toolbar);

        // 设置toolbar
        toolbar.setNavigationOnClickListener(v -> finish());

        // 更新日志
        findViewById(R.id.about_update_log).setOnClickListener(v->{
            Toast.makeText(this, "功能开发中", Toast.LENGTH_SHORT).show();
        });

        // 设置链接
        about_repo_link.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Rean-Schwarze/Today-News")));
        });
        findViewById(R.id.about_check_update).setOnClickListener(v->{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Rean-Schwarze/Today-News/releases")));
        });
    }

}