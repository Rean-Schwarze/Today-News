package com.rean.todaynews;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final String typeUrl="https://www.mxnzp.com/api/news/types/v2?app_id=ngeorpqtkeijibqu&app_secret=ZWJlWXFzc21KNjYzVG9iakdBT3cydz09";

    private List<TypeInfo.DataDTO> categories;

    private TabLayout tab_layout;
    private ViewPager2 view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化数据
        getHttpData();

        // 初始化控件
        initView();
    }

    private void getHttpData(){
        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        // 创建Request对象
        Request request = new Request.Builder().url(typeUrl).get().build();
        // 创建Call对象
        Call call = client.newCall(request);
        // 同步请求
        try (Response response = call.execute()){
            if(response.body()!=null){
                String responseData = response.body().string();
                parseJson(responseData);
            }
            else {
                Log.d("MainActivity", "getHttpData: response.body() is null");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseJson(String responseData) {
        Gson gson = new Gson();
        TypeInfo response=gson.fromJson(responseData, TypeInfo.class);
        categories=response.getData();
    }

    private void initView() {
        // 初始化控件
        tab_layout = findViewById(R.id.tab_layout);
        view_pager = findViewById(R.id.view_pager);
        view_pager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return TabFragment.newInstance(categories.get(position).getTypeId().toString());
            }

            @Override
            public int getItemCount() {
                return categories.size();
            }
        });

        // 设置tab_layout与view_pager联动
        TabLayoutMediator mediator = new TabLayoutMediator(tab_layout, view_pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(categories.get(position).getTypeName());
            }
        });
        mediator.attach();


        // tab_layout 点击事件
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view_pager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}