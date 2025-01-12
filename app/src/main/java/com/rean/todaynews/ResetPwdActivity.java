package com.rean.todaynews;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.rean.todaynews.db.UserDbHelper;
import com.rean.todaynews.pojo.UserInfo;
import com.rean.todaynews.util.Md5Util;

public class ResetPwdActivity extends AppCompatActivity {

    private EditText reset_pwd_old_pwd, reset_pwd_new_pwd, reset_pwd_confirm_pwd;
    private UserInfo loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_pwd);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化控件
        reset_pwd_new_pwd = findViewById(R.id.reset_pwd_new_pwd);
        reset_pwd_old_pwd = findViewById(R.id.reset_pwd_old_pwd);
        reset_pwd_confirm_pwd = findViewById(R.id.reset_pwd_confirm_pwd);

        // 设置toolbar
        findViewById(R.id.reset_pwd_toolbar).setOnClickListener(v->finish());

        // 点击修改密码
        findViewById(R.id.reset_pwd_btn).setOnClickListener(v->{
            String old_pwd = reset_pwd_old_pwd.getText().toString();
            String new_pwd = reset_pwd_new_pwd.getText().toString();
            String confirm_pwd = reset_pwd_confirm_pwd.getText().toString();
            loginUser = UserInfo.getUserInfo();
            if(old_pwd.isEmpty() || new_pwd.isEmpty() || confirm_pwd.isEmpty()){
                Toast.makeText(this, "尚未输入完毕！", Toast.LENGTH_SHORT).show();
            }
            else if(!new_pwd.equals(confirm_pwd)){
                Toast.makeText(this, "两次输入的新密码不一致！", Toast.LENGTH_SHORT).show();
            }
            else if(loginUser!=null && loginUser.getPassword().equals(Md5Util.getMD5String(old_pwd))){
                int row = UserDbHelper.getInstance(this).resetPwd(loginUser.getId(), new_pwd);
                if (row>0){
                    Toast.makeText(this, "密码修改成功！请重新登陆", Toast.LENGTH_SHORT).show();
                    UserInfo.clearUserInfo();
                    setResult(1002); // 回传 Result
                    finish();
                }
                else{
                    Toast.makeText(this, "密码修改失败！", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "原密码错误！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}