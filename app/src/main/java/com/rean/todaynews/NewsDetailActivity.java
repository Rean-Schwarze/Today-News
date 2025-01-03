package com.rean.todaynews;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.rean.todaynews.pojo.NewsBrief;

public class NewsDetailActivity extends AppCompatActivity {

    private NewsBrief.DataDTO dataDTO;
    private Toolbar toolbar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.news_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化控件
        toolbar = findViewById(R.id.news_detail_toolbar);
        webView = findViewById(R.id.news_detail_webView);

        // 获取传递的数据
        dataDTO = (NewsBrief.DataDTO) getIntent().getSerializableExtra("dataDTO");

        // 设置数据
        if(dataDTO!=null){
            toolbar.setTitle(dataDTO.getTitle());
        }

        // 返回
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}