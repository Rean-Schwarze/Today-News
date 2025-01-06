package com.rean.todaynews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rean.todaynews.adapter.NewsListAdapter;
import com.rean.todaynews.pojo.News;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private NewsListAdapter mNewsListAdapter;
    private Handler mHandler = new Handler(Objects.requireNonNull(Looper.myLooper())){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                String responseData = (String) msg.obj;
                News news = new Gson().fromJson(responseData, News.class);
                if (news != null && news.getData() != null) {
                    if (mNewsListAdapter != null) {
                        if(page==1){
                            mNewsListAdapter.setListData(news.getData().getList());
                        }
                        else{
                            mNewsListAdapter.appendListData(news.getData().getList());
                        }
                    }
                    page++;
                } else {
                    Toast.makeText(getActivity(), "数据获取失败", Toast.LENGTH_SHORT).show();
                }
            }
            dialog.dismiss();
        }
    };

    private AlertDialog.Builder loadingDialog;
    private Dialog dialog;

    private String oriUrl="https://apis.tianapi.com/allnews/index?key=4eaa1b03d3fd3599e76ad23768fde053&num=10";

    private static final String ARG_PARAM = "typeId";
    private String typeId;
    private Integer page;

    public TabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Parameter 1.
     * @return A new instance of fragment TabFragment.
     */
    public static TabFragment newInstance(String param) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            typeId = getArguments().getString(ARG_PARAM);
            page = 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tab, container, false);

        // 初始化RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);

        // 设置到达顶部和底部监听
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) { // 向下滑动到底部
                    getHttpData(oriUrl + "&col=" + typeId + "&page=" + page);
                } else if (!recyclerView.canScrollVertically(-1)) { // 向上滑动到顶部
                    page = 1;
                    getHttpData(oriUrl + "&col=" + typeId + "&page=" + page);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        };
        recyclerView.addOnScrollListener(onScrollListener);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 初始化适配器
        mNewsListAdapter = new NewsListAdapter(getActivity());
        // 设置适配器
        recyclerView.setAdapter(mNewsListAdapter);

        // 设置加载框
        loadingDialog = new AlertDialog.Builder(getActivity());
//        loadingDialog.setCancelable(false);
        loadingDialog.setView(R.layout.loading_dialog);

        getHttpData(oriUrl+"&col="+typeId+"&page="+page);

        // recyclerView设置点击事件
        mNewsListAdapter.setMOnNewsItemClickListener((dataDTO, position) -> {
            // 跳转到详情页
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra("dataDTO", dataDTO); // 对象需实现Serializable接口
            startActivity(intent);
        });
    }

    private void getHttpData(String url){
        // 显示加载框
        dialog = loadingDialog.show();
        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        // 创建Request对象
        Request request = new Request.Builder().url(url).get().build();
        // 创建Call对象
        Call call = client.newCall(request);
        // 异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("-------------", "onFailure: "+e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String responseData = response.body().string();
//                Log.d("--------------", "onResponse: " + responseData);
                // 避免在耗时操作中更新UI
                Message message = new Message();
                message.what = 100;
                message.obj = responseData;
                mHandler.sendMessage(message);
            }
        });
    }
}