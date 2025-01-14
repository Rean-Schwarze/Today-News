package com.rean.todaynews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
        toolbar.setNavigationOnClickListener(v -> finish());

        // 设置用户数据
        loginUser = UserInfo.getUserInfo();
        if(loginUser!=null){
            tv_username.setText(loginUser.getUsername());
            if(loginUser.getUserdesc()!=null && !loginUser.getUserdesc().isEmpty()){
                tv_desc.setText(loginUser.getUserdesc());
            }
            else{
                tv_desc.setText("这个人很懒，什么都没有留下");
            }
            if(loginUser.getAvatar()!=null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(loginUser.getAvatar(), 0, loginUser.getAvatar().length);
                iv_avatar.setImageBitmap(bitmap);
            }
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

        // 修改资料
        findViewById(R.id.user_center_edit_info).setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateUserInfoActivity.class);
            startActivityForResult(intent,1004);
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

        // 我的消息
        findViewById(R.id.user_center_my_message).setOnClickListener(v -> {
            Toast.makeText(this, "功能开发中", Toast.LENGTH_SHORT).show();
        });

        // 我的收藏
        findViewById(R.id.user_center_my_collection).setOnClickListener(v -> {
            startActivity(new Intent(this,CollectionListActivity.class));
        });

        // 设置
        findViewById(R.id.user_center_setting).setOnClickListener(v -> {
            Toast.makeText(this, "功能开发中", Toast.LENGTH_SHORT).show();
        });

        // 反馈
        findViewById(R.id.user_center_feedback).setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Rean-Schwarze/Today-News/issues")));
        });

        // 隐私政策
        findViewById(R.id.user_center_privacy).setOnClickListener(v -> {
            Toast.makeText(this, "功能开发中", Toast.LENGTH_SHORT).show();
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
        else if(resultCode==1004){
            loginUser = UserInfo.getUserInfo();
            if(loginUser!=null){
                tv_username.setText(loginUser.getUsername());
                if(loginUser.getUserdesc()!=null && !loginUser.getUserdesc().isEmpty()){
                    tv_desc.setText(loginUser.getUserdesc());
                }
                else{
                    tv_desc.setText("这个人很懒，什么都没有留下");
                }
                if(loginUser.getAvatar()!=null){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(loginUser.getAvatar(), 0, loginUser.getAvatar().length);
                    iv_avatar.setImageBitmap(bitmap);
                }
            }
        }
    }
}