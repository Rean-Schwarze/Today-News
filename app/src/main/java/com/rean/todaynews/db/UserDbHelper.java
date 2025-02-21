package com.rean.todaynews.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.rean.todaynews.pojo.UserInfo;
import com.rean.todaynews.util.Md5Util;

public class UserDbHelper extends SQLiteOpenHelper {
    private static UserDbHelper instance;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "user.db";

    public UserDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public UserDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 创建【单例】
    public static synchronized UserDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new UserDbHelper(context, DB_NAME, null, DB_VERSION);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user (user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "password TEXT," +
                "phone TEXT," +
                "avatar BLOB," +
                "userdesc TEXT," +
                "type INTEGER DEFAULT 0)"); // 0:普通用户 1:管理员
//        db.execSQL("UPDATE SQLITE_SEQUENCE SET user_id = 100000 WHERE name = 'user'");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 注册
     *
     * @param username 用户名
     * @param password 密码
     * @param phone    电话
     * @return int
     */
    public int register(String username, String password, String phone) {
        if(login(phone)!=null){
            return -1;
        }
        // 获取实例
        SQLiteDatabase db = this.getWritableDatabase();
        // 插入数据
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", Md5Util.getMD5String(password));
        values.put("phone", phone);
        String nullColumnHack = "values(null,?,?,?)";
        // 执行
        int res = (int) db.insert("user", nullColumnHack, values);
        db.close();
        return res;
    }

    /**
     * 登录
     *
     * @param phone    手机号
     * @return {@link UserInfo }
     */
    @SuppressLint({"Recycle", "Range"})
    public UserInfo login(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM user WHERE phone = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{phone});
        UserInfo userInfo = null;
        if(cursor.moveToNext()) {
            userInfo = new UserInfo();
            userInfo.setId(cursor.getInt(cursor.getColumnIndex("user_id")));
            userInfo.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            userInfo.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            userInfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
            userInfo.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            userInfo.setAvatar(cursor.getBlob(cursor.getColumnIndex("avatar")));
            userInfo.setUserdesc(cursor.getString(cursor.getColumnIndex("userdesc")));
        }
        cursor.close();
        db.close();
        return userInfo;
    }

    /**
     * 重置密码
     *
     * @param user_id 用户 id
     * @param newPwd  新 PWD
     * @return int
     */
    public int resetPwd(Integer user_id, String newPwd){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", Md5Util.getMD5String(newPwd));
        int res = db.update("user", values, "user_id = ?", new String[]{String.valueOf(user_id)});
        db.close();
        return res;
    }

    /**
     * 更新用户信息
     *
     * @param user_id  用户 ID
     * @param username 用户名
     * @param userdesc 用户简介
     * @return int
     */
    public int updateUserInfo(Integer user_id, String username, String userdesc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("userdesc", userdesc);
        int res = db.update("user", values, "user_id = ?", new String[]{String.valueOf(user_id)});
        db.close();
        return res;
    }

    /**
     * 更新用户头像
     *
     * @param user_id 用户 ID
     * @param avatar  头像（byte[]）
     * @return int
     */
    public int updateUserAvatar(Integer user_id, byte[] avatar){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("avatar", avatar);
        int res = db.update("user", values, "user_id = ?", new String[]{String.valueOf(user_id)});
        db.close();
        return res;
    }
}
