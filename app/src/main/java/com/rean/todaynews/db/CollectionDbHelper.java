package com.rean.todaynews.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.rean.todaynews.pojo.CollectionInfo;

import java.util.ArrayList;
import java.util.List;

public class CollectionDbHelper extends SQLiteOpenHelper {
    private static CollectionDbHelper instance;
    private static final String DB_NAME = "collection.db";
    private static final int DB_VERSION = 1;

    public CollectionDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public CollectionDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 创建【单例】
    public static synchronized CollectionDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CollectionDbHelper(context, DB_NAME, null, DB_VERSION);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS collection (collection_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "news_id TEXT, " +
                "user_id INTEGER, " +
                "news_json TEXT," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }


    /**
     * 添加收藏
     *
     * @param news_id   新闻 ID
     * @param user_id   用户 ID
     * @param news_json 新闻 JSON
     */
    public void insertCollection(String news_id, Integer user_id, String news_json) {
        // 获取实例
        SQLiteDatabase db = this.getWritableDatabase();
        // 插入数据
        ContentValues values = new ContentValues();
        values.put("news_id", news_id);
        values.put("user_id", user_id);
        values.put("news_json", news_json);
        String nullColumnHack = "values(null,?,?,?)";
        // 执行
        db.insert("collection", nullColumnHack, values);
        db.close();
    }

    /**
     * 查询用户全部收藏新闻
     *
     * @param user_id 用户 ID
     * @return {@link List }<{@link CollectionInfo }>
     */
    @SuppressLint({"Range", "Recycle"})
    public List<CollectionInfo> queryUserCollection(Integer user_id){
        List<CollectionInfo> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql;
        Cursor cursor;
        if(user_id == null){ // 未登录访客
            sql = "SELECT * FROM collection WHERE user_id = ?";
            cursor= db.rawQuery(sql, null);
        }
        else{ // 登陆用户
            sql = "SELECT * FROM collection WHERE user_id = ?";
            cursor = db.rawQuery(sql, new String[]{user_id.toString()});
        }
        while(cursor.moveToNext()){
            CollectionInfo collectionInfo = new CollectionInfo();
            collectionInfo.setCollection_id(cursor.getInt(cursor.getColumnIndex("collection_id")));
            collectionInfo.setNews_id(cursor.getString(cursor.getColumnIndex("news_id")));
            collectionInfo.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
            collectionInfo.setNews_json(cursor.getString(cursor.getColumnIndex("news_json")));
            collectionInfo.setTimestamp(cursor.getString(cursor.getColumnIndex("timestamp")));
            list.add(collectionInfo);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 查询用户是否收藏过某新闻
     *
     * @param news_id 新闻 ID
     * @param user_id 用户 ID
     * @return boolean
     */
    @SuppressLint("Recycle")
    public boolean isCollected(String news_id, Integer user_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql= "SELECT * FROM collection WHERE user_id = ? and news_id = ?";
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

    /**
     * 移除收藏新闻
     *
     * @param news_id 新闻 ID
     * @param user_id 用户 ID
     * @return int
     */
    @SuppressLint("Recycle")
    public int deleteCollection(String news_id, Integer user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(user_id == null){
            db.close();
            return 0; // 未登录用户不记录
        }
        else{
            values.put("news_id", news_id);
            values.put("user_id", user_id);
            int res = db.delete("collection", "news_id = ? and user_id = ?",
                    new String[]{news_id, user_id.toString()});
            db.close();
            return res;
        }
    }

    public void updateCollection(String news_id, Integer user_id){
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
