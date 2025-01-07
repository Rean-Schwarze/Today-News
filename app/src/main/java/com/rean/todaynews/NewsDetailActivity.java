package com.rean.todaynews;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.rean.todaynews.db.HistoryDbHelper;
import com.rean.todaynews.pojo.News;

public class NewsDetailActivity extends AppCompatActivity {

    private News.DataDTO.ListDTO listDTO;
    private Toolbar toolbar;
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
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
        // 支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        // 扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        // 自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient());

        // 获取传递的数据
        listDTO = (News.DataDTO.ListDTO) getIntent().getSerializableExtra("dataDTO");

        // 设置数据
        if(listDTO !=null){
            toolbar.setTitle(listDTO.getTitle());
            webView.loadUrl(listDTO.getUrl());

            // 添加历史记录
            HistoryDbHelper.getInstance(this).insertHistory(listDTO.getId(),10000, new Gson().toJson(listDTO));
        }

        // 返回
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}