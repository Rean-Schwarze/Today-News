package com.rean.todaynews;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.rean.todaynews.db.CollectionDbHelper;
import com.rean.todaynews.db.HistoryDbHelper;
import com.rean.todaynews.pojo.CollectionInfo;
import com.rean.todaynews.pojo.News;
import com.rean.todaynews.pojo.UserInfo;

public class NewsDetailActivity extends AppCompatActivity {

    private News.DataDTO.ListDTO listDTO;
    private Toolbar toolbar;
    private WebView webView;
    private UserInfo loginUser;
    private boolean is_collected;
    private int userId;

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

        loginUser = UserInfo.getUserInfo();

        // 设置数据
        if(listDTO !=null){
            toolbar.setTitle(listDTO.getTitle());
            webView.loadUrl(listDTO.getUrl());

            // 添加历史记录
            userId = loginUser == null ? 10000 : loginUser.getId();
            HistoryDbHelper.getInstance(this).insertHistory(listDTO.getId(),userId, new Gson().toJson(listDTO));

            // 判断是否已收藏
            is_collected = CollectionDbHelper.getInstance(this).isCollected(listDTO.getId(),userId);
            onCollectionChange(toolbar,is_collected);
        }

        // 返回
        toolbar.setNavigationOnClickListener(v -> finish());

        // 收藏
        toolbar.getMenu().findItem(R.id.detail_collect).setOnMenuItemClickListener(item -> {
            if(is_collected){ // 已收藏时，取消收藏
                CollectionDbHelper.getInstance(this).deleteCollection(listDTO.getId(),userId);
                is_collected = false;
                onCollectionChange(toolbar, false);
            }
            else{ // 未收藏时，添加收藏
                if(loginUser==null){
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                    return false;
                }
                CollectionDbHelper.getInstance(this).insertCollection(listDTO.getId(),userId,new Gson().toJson(listDTO));
                is_collected = true;
                onCollectionChange(toolbar, true);
            }
            setResult(1005);
            return false;
        });
    }

    public void onCollectionChange(Toolbar toolbar, boolean is_collected){
        if(is_collected){
            toolbar.getMenu().findItem(R.id.detail_collect).setIcon(R.drawable.baseline_star_white_24);
        }
        else{
            toolbar.getMenu().findItem(R.id.detail_collect).setIcon(R.drawable.baseline_star_border_white_24);
        }
    }
}