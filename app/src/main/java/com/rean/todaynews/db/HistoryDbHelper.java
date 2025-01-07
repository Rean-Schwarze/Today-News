package com.rean.todaynews.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.rean.todaynews.pojo.HistoryInfo;

import java.util.ArrayList;
import java.util.List;

public class HistoryDbHelper extends SQLiteOpenHelper {
    private static HistoryDbHelper instance;
    private static final String DB_NAME = "history.db";
    private static final int DB_VERSION = 1;

    public HistoryDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public HistoryDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 创建【单例】
    public static synchronized HistoryDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new HistoryDbHelper(context, DB_NAME, null, DB_VERSION);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS history (history_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "news_id TEXT, " +
                "user_id INTEGER, " +
                "news_json TEXT," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }


    /**
     * 添加历史记录
     *
     * @param news_id   新闻 ID
     * @param user_id   用户 ID
     * @param news_json 新闻 JSON
     */
    public void insertHistory(String news_id, Integer user_id, String news_json) {
        // 判断是否浏览过
        if(isHistory(news_id, user_id)){ // 浏览过，更新时间戳
            updateHistory(news_id, user_id);
            return;
        }
        // 获取实例
        SQLiteDatabase db = this.getWritableDatabase();
        // 插入数据
        ContentValues values = new ContentValues();
        values.put("news_id", news_id);
        values.put("user_id", user_id);
        values.put("news_json", news_json);
        String nullColumnHack = "values(null,?,?,?)";
        // 执行
        db.insert("history", nullColumnHack, values);
        db.close();
    }

    /**
     * 查询用户历史记录
     *
     * @param user_id 用户 ID
     * @return {@link List }<{@link HistoryInfo }>
     */
    @SuppressLint({"Range", "Recycle"})
    public List<HistoryInfo> queryUserHistory(Integer user_id){
        List<HistoryInfo> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql;
        Cursor cursor;
        if(user_id == null){ // 未登录访客
            sql = "SELECT * FROM history WHERE user_id = ?";
            cursor= db.rawQuery(sql, null);
        }
        else{ // 登陆用户
            sql = "SELECT * FROM history WHERE user_id = ?";
            cursor = db.rawQuery(sql, new String[]{user_id.toString()});
        }
        while(cursor.moveToNext()){
            HistoryInfo historyInfo = new HistoryInfo();
            historyInfo.setHistory_id(cursor.getInt(cursor.getColumnIndex("history_id")));
            historyInfo.setNews_id(cursor.getString(cursor.getColumnIndex("news_id")));
            historyInfo.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
            historyInfo.setNews_json(cursor.getString(cursor.getColumnIndex("news_json")));
            historyInfo.setTimestamp(cursor.getString(cursor.getColumnIndex("timestamp")));
            list.add(historyInfo);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 查询用户浏览历史是否存在
     *
     * @param news_id 新闻 ID
     * @param user_id 用户 ID
     * @return boolean
     */
    @SuppressLint("Recycle")
    public boolean isHistory(String news_id, Integer user_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql= "SELECT * FROM history WHERE user_id = ? and news_id = ?";
        Cursor cursor;
        if(user_id == null){
            return true; // 未登录用户不记录
        }
        else{
            cursor = db.rawQuery(sql, new String[]{user_id.toString(), news_id});
        }
        boolean res = cursor.moveToNext();
        cursor.close();
        db.close();
        return res;
    }

    public void updateHistory(String news_id, Integer user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql;
        if(user_id == null){
            return; // 未登录用户不记录
        }
        else{
            sql = "UPDATE history SET timestamp = CURRENT_TIMESTAMP WHERE user_id = ? and news_id = ?";
        }
        db.execSQL(sql, new String[]{user_id.toString(), news_id});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
