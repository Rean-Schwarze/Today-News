package com.rean.todaynews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rean.todaynews.BriefNews;
import com.rean.todaynews.DateUtil;
import com.rean.todaynews.R;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {

    private List<BriefNews.DataDTO> mListData=new ArrayList<>();
    private Context mContext;

    @SuppressLint("NotifyDataSetChanged")
    public void setListData(List<BriefNews.DataDTO> listData) {
        this.mListData=listData;
        // 通知数据更新
        notifyDataSetChanged();
    }

    public NewsListAdapter(Context context){
        this.mContext=context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载布局文件
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, null);
        return new NewsViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        // 绑定数据
        BriefNews.DataDTO dataDTO = mListData.get(position);
        holder.brief_news_title.setText(dataDTO.getTitle());
        holder.brief_news_src.setText(dataDTO.getSource());
        holder.brief_news_time.setText(DateUtil.getDurationToNow(dataDTO.getPostTime()));
        // 加载图片
        Glide.with(mContext).load(dataDTO.getImgList().get(0)).into(holder.brief_news_img);
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView brief_news_img;
        TextView brief_news_title;
        TextView brief_news_src;
        TextView brief_news_time;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            brief_news_img=itemView.findViewById(R.id.brief_news_img);
            brief_news_title=itemView.findViewById(R.id.brief_news_title);
            brief_news_src=itemView.findViewById(R.id.brief_news_src);
            brief_news_time=itemView.findViewById(R.id.brief_news_time);
        }
    }
}
