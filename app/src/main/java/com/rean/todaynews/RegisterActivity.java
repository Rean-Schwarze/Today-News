package com.rean.todaynews;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.rean.todaynews.db.UserDbHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_username, et_password, et_phone, et_password_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化控件
        et_username = findViewById(R.id.register_et_username);
        et_password = findViewById(R.id.register_et_password);
        et_phone = findViewById(R.id.register_et_phone);
        et_password_confirm = findViewById(R.id.register_et_password_confirm);

        // 注册
        findViewById(R.id.register_btn).setOnClickListener(v -> {
             String username = et_username.getText().toString();
             String password = et_password.getText().toString();
             String phone = et_phone.getText().toString();
             String password_confirm = et_password_confirm.getText().toString();

             if(username.isEmpty() || password.isEmpty() || phone.isEmpty() || password_confirm.isEmpty()) {
                 // 提示用户填写完整信息
                 Toast.makeText(this, "信息尚未填写完毕！", Toast.LENGTH_SHORT).show();
             }
             else if(!phone.matches("^1[3456789]\\d{9}$")) {
                 // 提示用户输入的手机号格式不正确
                 Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
             }
             else if(!password.equals(password_confirm)) {
                 // 提示用户两次输入的密码不一致
                 Toast.makeText(this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
             }
             else {
                 // 注册
                 int row = UserDbHelper.getInstance(this).register(username, password, phone);
                 if(row>0){
                     Toast.makeText(this, "注册成功！请登录", Toast.LENGTH_SHORT).show();
                     finish(); // 注册成功后关闭注册界面
                 }
                 else {
                     Toast.makeText(this, "注册失败！手机号已被注册", Toast.LENGTH_SHORT).show();
                 }
             }
        });

        // 返回
        findViewById(R.id.register_toolbar).setOnClickListener(v -> finish());
    }
}