package com.rean.todaynews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rean.todaynews.adapter.CollectionListAdapter;
import com.rean.todaynews.db.CollectionDbHelper;
import com.rean.todaynews.pojo.CollectionInfo;
import com.rean.todaynews.pojo.News;
import com.rean.todaynews.pojo.UserInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionListActivity extends AppCompatActivity{

    private static final int SELECT_MODE_CHECK = 0;
    private static final int SELECT_MODE_EDIT = 1;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CollectionListAdapter collectionListAdapter;
    private boolean editStatus = false, isSelectAll;
    private Button mBtnDelete;
    private TextView mSelectAll, mTvSelectNum;
    private int selected_cnt = 0;
    private MenuItem mBtnEditor;
    private int mEditMode = SELECT_MODE_CHECK;
    private LinearLayout mBottomLayout;

    private List<News.DataDTO.ListDTO> list = new ArrayList<>();
    private UserInfo loginUser;
    private int user_id;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_collection_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化控件
        toolbar = findViewById(R.id.collection_list_toolbar);
        recyclerView = findViewById(R.id.collection_list_recyclerView);
        mBtnDelete = findViewById(R.id.btn_delete);
        mSelectAll = findViewById(R.id.select_all);
        mBtnEditor = toolbar.getMenu().findItem(R.id.collection_edit);
        mBottomLayout = findViewById(R.id.collection_bottom_dialog);
        mTvSelectNum = findViewById(R.id.tv_select_num);

        // 设置toolbar
        toolbar.setNavigationOnClickListener(v -> finish());

        // 初始化适配器
        collectionListAdapter = new CollectionListAdapter(this);
        // 设置适配器
        recyclerView.setAdapter(collectionListAdapter);

        loginUser = UserInfo.getUserInfo();
        if(loginUser==null) user_id = 10000;
        else user_id = loginUser.getId();

        // 获取并设置数据
        getCollections();

        // toolbar 编辑按钮点击事件
        mBtnEditor.setOnMenuItemClickListener(item -> {
            updateEditMode(); // 更新编辑模式
            return false;
        });

        // 全选按钮点击事件
        mSelectAll.setOnClickListener(v -> {
            selectAllMain();
        });

        // 删除按钮点击事件
        mBtnDelete.setOnClickListener(v -> {
            deleteCollection(); // 删除选中新闻
        });

        // recyclerView 点击事件
        collectionListAdapter.setMOnNewsItemClickListener((dataDTO, position) -> {
            if(editStatus){ // 编辑模式，点击选择
                boolean isSelected = dataDTO.isSelected();
                if(!isSelected){
                    dataDTO.setSelected(true); // 选中
                    selected_cnt++;
                    if(selected_cnt == collectionListAdapter.getItemCount()){
                        isSelectAll = true;
                        mSelectAll.setText("取消全选");
                    }
                }
                else{
                    dataDTO.setSelected(false); // 取消选中
                    selected_cnt--;
                    isSelectAll = false;
                    mSelectAll.setText("全选");
                }
                setBtnBackground(selected_cnt); // 更新删除按钮状态
                mTvSelectNum.setText(String.valueOf(selected_cnt)); // 更新选中数量
                collectionListAdapter.notifyDataSetChanged(); // 刷新列表
            }
            else{ // 不是编辑模式，跳转
                // 跳转到详情页
                Intent intent = new Intent(this, NewsDetailActivity.class);
                intent.putExtra("dataDTO", dataDTO); // 对象需实现Serializable接口
                startActivityForResult(intent, 1005);
            }
        });
    }

    /**
     * 获取并设置收藏列表
     */
    private void getCollections() {
        List<CollectionInfo> collectionInfoList = CollectionDbHelper.getInstance(this).queryUserCollection(user_id);
        Gson gson = new Gson();
        list = new ArrayList<>();
        if (collectionInfoList != null && !collectionInfoList.isEmpty()) {
            for(CollectionInfo collectionInfo : collectionInfoList) {
                list.add(gson.fromJson(collectionInfo.getNews_json(), News.DataDTO.ListDTO.class));
            }
        }
        Collections.reverse(list);
        collectionListAdapter.setListData(list);
    }

    /**
     * 根据选择的数量是否为0来判断按钮的是否可点击.
     *
     * @param size 选择的数量
     */
    private void setBtnBackground(int size) {
        if (size != 0) {
            mBtnDelete.setBackgroundResource(R.drawable.button_shape);
            mBtnDelete.setEnabled(true);
        } else {
            mBtnDelete.setBackgroundResource(R.drawable.button_notclickable_shape);
            mBtnDelete.setEnabled(false);
        }
    }

    /**
     * updateEditMode 更新编辑模式
     */
    private void updateEditMode() {
        mEditMode = mEditMode == SELECT_MODE_CHECK ? SELECT_MODE_EDIT : SELECT_MODE_CHECK;
        if (mEditMode == SELECT_MODE_EDIT) {  // 正在编辑，图标变为取消
            mBtnEditor.setTitle("取消");
            mBtnEditor.setIcon(R.drawable.baseline_cancel_white_24);
            mBottomLayout.setVisibility(View.VISIBLE);
            editStatus = true;
        } else { // 取消编辑，图标变为编辑
            mBtnEditor.setTitle("编辑");
            mBtnEditor.setIcon(R.drawable.baseline_edit_white_24);
            mBottomLayout.setVisibility(View.GONE);
            editStatus = false;
            clearAll();
        }
        collectionListAdapter.setEditMode(mEditMode);
    }

    /**
     * 清除已选
     */
    private void clearAll() {
        mTvSelectNum.setText(String.valueOf(0));
        isSelectAll = false;
        mSelectAll.setText("全选");
        setBtnBackground(0);
    }

    /**
     * 全选和反选
     */
    @SuppressLint("NotifyDataSetChanged")
    private void selectAllMain() {
        if (collectionListAdapter == null) return;
        if (!isSelectAll) { // 当前没有全选，则全选
            for (int i = 0, j = collectionListAdapter.getItemCount(); i < j; i++) {
                collectionListAdapter.getListData().get(i).setSelected(true);
            }
            selected_cnt = collectionListAdapter.getItemCount();
            mBtnDelete.setEnabled(true);
            mSelectAll.setText("取消全选");
            isSelectAll = true;
        } else {
            for (int i = 0, j = collectionListAdapter.getItemCount(); i < j; i++) {
                collectionListAdapter.getListData().get(i).setSelected(false);
            }
            selected_cnt = 0;
            mBtnDelete.setEnabled(false);
            mSelectAll.setText("全选");
            isSelectAll = false;
        }
        collectionListAdapter.notifyDataSetChanged();
        setBtnBackground(selected_cnt);
        mTvSelectNum.setText(String.valueOf(selected_cnt));
    }

    /**
     * 删除
     */
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    private void deleteCollection() {
        if (selected_cnt == 0){
            mBtnDelete.setEnabled(false);
            return;
        }
        final AlertDialog builder = new AlertDialog.Builder(this)
                .create();
        builder.show();
        if (builder.getWindow() == null) return;
        builder.getWindow().setContentView(R.layout.pop_dialog);//设置弹出框加载的布局
        TextView msg = builder.findViewById(R.id.tv_msg);
        Button cancel = builder.findViewById(R.id.btn_cancel);
        Button sure = builder.findViewById(R.id.btn_sure);
        if (msg == null || cancel == null || sure == null) return;

        if (selected_cnt == 1) {
            msg.setText("删除后不可恢复，是否删除该条目？");
        } else {
            msg.setText("删除后不可恢复，是否删除" + selected_cnt + "个条目？");
        }
        cancel.setOnClickListener(v -> builder.dismiss());
        sure.setOnClickListener(v -> {
            for (int i = collectionListAdapter.getItemCount(), j =0 ; i > j; i--) {
                News.DataDTO.ListDTO news = collectionListAdapter.getListData().get(i-1);
                if (news.isSelected()) {
                    CollectionDbHelper.getInstance(this).deleteCollection(news.getId(),user_id);
                    selected_cnt--;
                }
            }
            getCollections();
            selected_cnt = 0;
            mTvSelectNum.setText(String.valueOf(0));
            setBtnBackground(selected_cnt);
            if (collectionListAdapter.getItemCount() == 0){
                mBottomLayout.setVisibility(View.GONE);
            }
            collectionListAdapter.notifyDataSetChanged();
            builder.dismiss();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1005){
            getCollections();
            selected_cnt = 0;
            mTvSelectNum.setText(String.valueOf(0));
            setBtnBackground(selected_cnt);
            if (collectionListAdapter.getItemCount() == 0){
                mBottomLayout.setVisibility(View.GONE);
            }
            collectionListAdapter.notifyDataSetChanged();
        }
    }
}