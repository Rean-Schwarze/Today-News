package com.rean.todaynews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rean.todaynews.adapter.NewsListAdapter;

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
                BriefNews briefNews = new Gson().fromJson(responseData, BriefNews.class);
                if (briefNews != null && briefNews.getData() != null) {
                    if (mNewsListAdapter != null) {
                        mNewsListAdapter.setListData(briefNews.getData());
                    }
                } else {
                    Toast.makeText(getActivity(), "数据获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private String oriUrl="https://www.mxnzp.com/api/news/list/v2?app_id=ngeorpqtkeijibqu&app_secret=ZWJlWXFzc21KNjYzVG9iakdBT3cydz09";

    private static final String ARG_PARAM = "typeId";
    private String typeId;


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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tab, container, false);

        // 初始化RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 初始化适配器
        mNewsListAdapter = new NewsListAdapter(getActivity());
        // 设置适配器
        recyclerView.setAdapter(mNewsListAdapter);

        getHttpData(oriUrl+"&typeId="+typeId+"&page=1");
    }

    private void getHttpData(String url){
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