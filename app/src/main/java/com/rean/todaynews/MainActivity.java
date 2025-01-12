package com.rean.todaynews;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.rean.todaynews.pojo.TypeInfo;
import com.rean.todaynews.pojo.UserInfo;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final String typeUrl="https://apis.tianapi.com/channellist/index?key=4eaa1b03d3fd3599e76ad23768fde053";

    private List<TypeInfo.DataDTO.ListDTO> categories;

    private TabLayout tab_layout;
    private ViewPager2 view_pager;

    private NavigationView nav_view;
    private Toolbar toolbar;
    private DrawerLayout drawer_layout;
    private TextView nav_username;
    private TextView nav_user_phone;
    private ImageView nav_avatar;

    private UserInfo loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 强制设置为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
        categories=response.getData().getList().get(0);
        for(TypeInfo.DataDTO.ListDTO category:categories){
            if(category.getName().contains("新浪宠物")){
                category.setName("宠物");
                continue;
            }
            if(category.getName().contains("科学探索")){
                category.setName("科学");
                continue;
            }
            if(category.getName().contains("健康知识")){
                category.setName("健康");
                continue;
            }
            if(category.getName().contains("VR")){
                category.setName("VR");
                continue;
            }
            if(category.getName().contains("人工智能")){
                category.setName("AI");
                continue;
            }
            category.setName(category.getName().split("新闻")[0].split("资讯")[0]);
        }
        for(TypeInfo.DataDTO.ListDTO category:categories){
            if(category.getName().contains("垃圾分类")){
                categories.remove(category);
                break;
            }
        }
        Collections.reverse(categories);
    }

    private void initView() {
        // 初始化控件
        tab_layout = findViewById(R.id.tab_layout);
        view_pager = findViewById(R.id.view_pager);

        nav_view = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.main_bar_nav);
        drawer_layout = findViewById(R.id.drawer_layout);
        nav_username = nav_view.getHeaderView(0).findViewById(R.id.nav_username);
        nav_user_phone = nav_view.getHeaderView(0).findViewById(R.id.nav_user_phone);
        nav_avatar = nav_view.getHeaderView(0).findViewById(R.id.nav_avatar);

        view_pager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return TabFragment.newInstance(categories.get(position).getColid().toString());
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
                tab.setText(categories.get(position).getName());
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

        // nav_view 点击事件
        nav_view.setNavigationItemSelectedListener(item -> {
            if(item.getItemId()==R.id.nav_history){
                startActivity(new Intent(MainActivity.this,HistoryListActivity.class));
            }
//            else if(item.getItemId()==R.id.nav_resetPwd){
//                startActivity(new Intent(MainActivity.this,ResetPwdActivity.class));
//            }
            return true;
        });

        // nav_avatar 点击事件
        nav_avatar.setOnClickListener(v -> {
            if(loginUser==null){
                // 未登录，跳转到登录页面
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
            else{
                // 已登录，跳转到用户中心页面
                startActivity(new Intent(MainActivity.this,UserCenterActivity.class));
            }
        });

        // toolbar 点击事件
        toolbar.setOnClickListener(v -> drawer_layout.openDrawer(nav_view));
    }

    @Override
    protected void onResume() {
        super.onResume();

        loginUser = UserInfo.getUserInfo();
        if(loginUser!=null){
            nav_username.setText(loginUser.getUsername());
            nav_user_phone.setText(loginUser.getPhone());
        }
        else{
            nav_username.setText("未登录用户");
            nav_user_phone.setText("点击头像登录");
        }
    }
}