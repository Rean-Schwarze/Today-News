package com.rean.todaynews;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rean.todaynews.adapter.NewsListAdapter;
import com.rean.todaynews.db.HistoryDbHelper;
import com.rean.todaynews.pojo.HistoryInfo;
import com.rean.todaynews.pojo.News;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private NewsListAdapter newsListAdapter;

    private List<News.DataDTO.ListDTO> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.history_list_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化控件
        toolbar = findViewById(R.id.history_list_toolbar);
        recyclerView = findViewById(R.id.history_list_recyclerView);

        // 设置toolbar
        toolbar.setOnClickListener(v -> finish());

        // 初始化适配器
        newsListAdapter = new NewsListAdapter(this);
        // 设置适配器
        recyclerView.setAdapter(newsListAdapter);

        // 获取并设置数据
        List<HistoryInfo> historyInfoList = HistoryDbHelper.getInstance(this).queryUserHistory(10000);
        Gson gson = new Gson();
        if (historyInfoList != null && !historyInfoList.isEmpty()) {
            for(HistoryInfo historyInfo : historyInfoList) {
                list.add(gson.fromJson(historyInfo.getNews_json(), News.DataDTO.ListDTO.class));
            }
        }
        Collections.reverse(list);
        newsListAdapter.setListData(list);

        // recyclerView 点击事件
        newsListAdapter.setMOnNewsItemClickListener((dataDTO, position) -> {
            // 跳转到详情页
            Intent intent = new Intent(this, NewsDetailActivity.class);
            intent.putExtra("dataDTO", dataDTO); // 对象需实现Serializable接口
            startActivity(intent);
        });
    }
}