package com.rean.todaynews;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.rean.todaynews.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private final String[] categories = {"娱乐", "军事", "教育", "文化", "健康", "财经", "体育", "汽车", "科技"};

    private TabLayout tab_layout;
    private ViewPager2 view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化控件
        tab_layout = findViewById(R.id.tab_layout);
        view_pager = findViewById(R.id.view_pager);
        view_pager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return TabFragment.newInstance(categories[position]);
            }

            @Override
            public int getItemCount() {
                return categories.length;
            }
        });

        // 设置tab_layout与view_pager联动
        TabLayoutMediator mediator = new TabLayoutMediator(tab_layout, view_pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(categories[position]);
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