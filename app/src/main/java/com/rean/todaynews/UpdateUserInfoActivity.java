package com.rean.todaynews;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.rean.todaynews.db.UserDbHelper;
import com.rean.todaynews.pojo.UserInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UpdateUserInfoActivity extends AppCompatActivity {

    private static final int ALBUM_CODE = 1003;
    private ImageView update_avatar;
    private EditText update_username, update_desc;
    private UserInfo loginUser;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_user_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化控件
        update_avatar = findViewById(R.id.update_avatar);
        update_username = findViewById(R.id.update_username);
        update_desc = findViewById(R.id.update_desc);
        toolbar = findViewById(R.id.update_toolbar);

        // 设置toolbar
        toolbar.setNavigationOnClickListener(v -> finish());

        loginUser = UserInfo.getUserInfo();
        if (loginUser != null) {
            update_username.setText(loginUser.getUsername());
            update_desc.setText(loginUser.getUserdesc());
            if (loginUser.getAvatar() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(loginUser.getAvatar(), 0, loginUser.getAvatar().length);
                update_avatar.setImageBitmap(bitmap);
            }
        }

        // 更新用户头像
        update_avatar.setOnClickListener(v -> {
            // 从手机相册中获取图片需要动态申请权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ALBUM_CODE);
            } else {
                //如果已经获取权限，那么就直接拿
                takePhoto();
            }
        });

        // 更新用户信息
        findViewById(R.id.update_save_btn).setOnClickListener(v -> {
            String new_username = update_username.getText().toString();
            String new_desc = update_desc.getText().toString();
            if(!new_username.isEmpty()){
                int row = UserDbHelper.getInstance(this).updateUserInfo(loginUser.getId(), new_username, new_desc);
                if (row > 0) {
                    loginUser.setUsername(new_username);
                    loginUser.setUserdesc(new_desc);
                    UserInfo.setUserInfo(loginUser);
                    Toast.makeText(this, "修改资料成功！", Toast.LENGTH_SHORT).show();
                    setResult(1004);
                } else {
                    Toast.makeText(this, "修改资料失败！", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //权限申请回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ALBUM_CODE) {//获取照片
            takePhoto();
        }
    }


    private void takePhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        startActivityForResult(intent, ALBUM_CODE);
    }

    //从相册返回的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ALBUM_CODE) {
            if(data!=null){
                Uri uri = data.getData();
                if (uri != null) {
                    // Glide 加载图片
                    Glide.with(this).load(uri).into(update_avatar);
                    updateAvatar(uri);
                }
            }
        }
    }

    public void updateAvatar(Uri uri){
        try {
            byte[] avatar = getBytes(uri); // 转换为 byte[]
            int row = UserDbHelper.getInstance(this).updateUserAvatar(loginUser.getId(), avatar);
            if (row > 0) {
                loginUser.setAvatar(avatar);
                UserInfo.setUserInfo(loginUser);
                Toast.makeText(this, "修改头像成功！", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "修改头像失败！", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 转换为 byte[]
    public byte[] getBytes(Uri uri) throws IOException {
        // 转化为位图
        InputStream stream = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        assert stream != null;
        stream.close();

        // 创建输出字节流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            // 压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            return bos.toByteArray();
        }
        catch (Exception ignored){

        }
        finally {
            try {
                bitmap.recycle();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

}