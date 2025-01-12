package com.rean.todaynews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class LoginActivity extends AppCompatActivity {

    private EditText et_phone, et_password;
    private CheckBox cb_remember_password;
    private SharedPreferences mSharedPreferences;
    private boolean isRememberPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        // 是否记住密码
        isRememberPassword = mSharedPreferences.getBoolean("remember_password", false);
        if (isRememberPassword) {
            et_phone.setText(mSharedPreferences.getString("phone", ""));
            et_password.setText(mSharedPreferences.getString("password", ""));
            cb_remember_password.setChecked(true);
        }

        // 初始化控件
        et_phone = findViewById(R.id.login_et_phone);
        et_password = findViewById(R.id.login_et_password);
        cb_remember_password = findViewById(R.id.login_cb_remember_password);

        // 点击注册
        findViewById(R.id.login_register_btn).setOnClickListener(v -> {
            // 跳转到注册页面
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // 点击登录
        findViewById(R.id.login_btn).setOnClickListener(v -> {
            String phone = et_phone.getText().toString();
            String password = et_password.getText().toString();
            if (phone.isEmpty() || password.isEmpty()) {
                // 提示用户输入完整信息
                Toast.makeText(this, "请输入手机号和密码", Toast.LENGTH_SHORT).show();
            }
            else {
                UserInfo loginUser = UserDbHelper.getInstance(this).login(phone);
                // 检查用户信息是否正确
                if(loginUser!=null){
                    if (phone.equals(loginUser.getPhone()) && loginUser.getPassword().equals(Md5Util.getMD5String(password))) {
                        // 登录成功，保存用户信息
                        @SuppressLint("CommitPrefEdits")
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putBoolean("isRememberPassword", true);
                        editor.putString("phone", phone);
                        editor.putString("password", password);
                        editor.apply();
                        UserInfo.setUserInfo(loginUser);
                        finish();
                    }
                    else{
                        Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 点击记住密码
        cb_remember_password.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isRememberPassword = isChecked;
        });
    }
}