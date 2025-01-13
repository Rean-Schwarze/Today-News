package com.rean.todaynews;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.rean.todaynews.pojo.UserInfo;

public class UserCenterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tv_username, tv_desc;
    private ImageView iv_avatar;

    private UserInfo loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_center);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化控件
        toolbar = findViewById(R.id.user_center_toolbar);
        tv_username = findViewById(R.id.user_center_username);
        tv_desc = findViewById(R.id.user_center_desc);
        iv_avatar = findViewById(R.id.user_center_avatar);

        // 设置toolbar
        toolbar.setOnClickListener(v -> finish());

        // 设置用户数据
        loginUser = UserInfo.getUserInfo();
        if(loginUser!=null){
            tv_username.setText(loginUser.getUsername());
            if(loginUser.getUserdesc()!=null){
                tv_desc.setText(loginUser.getUserdesc());
            }
            else{
                tv_desc.setText("这个人很懒，什么都没有留下");
            }
//            iv_avatar.setImageResource(loginUser.getAvatar());
        }

        // 切换账号
        findViewById(R.id.user_center_switch_account).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("确定要切换账号吗？")
                    .setNegativeButton("取消", (dialog, which) -> {

                    })
                    .setPositiveButton("确定", (dialog, which) -> {
                        UserInfo.clearUserInfo(); // 清除用户信息
                        finish(); // 关闭当前页面
                        startActivity(new Intent(this,LoginActivity.class));
                    })
                    .show();
        });

        // 修改密码
        findViewById(R.id.user_center_reset_pwd).setOnClickListener(v -> {
            Intent intent = new Intent(this, ResetPwdActivity.class);
            startActivityForResult(intent,1002);
        });

        // 退出登录
        findViewById(R.id.user_center_exit).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("确定要退出登录吗？")
                    .setNegativeButton("取消", (dialog, which) -> {

                    })
                    .setPositiveButton("确定", (dialog, which) -> {
                        UserInfo.clearUserInfo(); // 清除用户信息
                        finish(); // 关闭当前页面
                    })
                    .show();
        });

        // 关于我们
        findViewById(R.id.user_center_about_us).setOnClickListener(v -> {
            startActivity(new Intent(this,AboutActivity.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1002){
            this.finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}