package com.rean.todaynews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rean.todaynews.R;
import com.rean.todaynews.pojo.News;
import com.rean.todaynews.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.NewsViewHolder> {

    private static final int SELECT_MODE_CHECK = 0;
    int mEditMode = SELECT_MODE_CHECK;

    private List<News.DataDTO.ListDTO> mListData=new ArrayList<>();
    private Context mContext;

    @SuppressLint("NotifyDataSetChanged")
    public void setListData(List<News.DataDTO.ListDTO> listData) {
        this.mListData=listData;
        // 通知数据更新
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void appendListData(List<News.DataDTO.ListDTO> listData) {
        this.mListData.addAll(listData);
        // 通知数据更新
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }

    public List<News.DataDTO.ListDTO> getListData() {
        if(mListData==null){
            mListData=new ArrayList<>();
        }
        return mListData;
    }

    public CollectionListAdapter(Context context){
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
        News.DataDTO.ListDTO listDTO = mListData.get(position);
        holder.brief_news_title.setText(listDTO.getTitle());
        holder.brief_news_src.setText(listDTO.getSource());
        holder.brief_news_time.setText(DateUtil.getDurationToNow(listDTO.getCtime()));
        // 加载图片
        Glide.with(mContext).load(listDTO.getPicUrl()).error(R.mipmap.error_load).into(holder.brief_news_img);
        if (mEditMode == SELECT_MODE_CHECK) {
            holder.mCheckBox.setVisibility(View.GONE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
            if (listDTO.isSelected()) {
                holder.mCheckBox.setImageResource(R.mipmap.ic_checked);
            } else {
                holder.mCheckBox.setImageResource(R.mipmap.ic_uncheck);
            }
        }
        // 点击事件
        holder.itemView.setOnClickListener(v -> {
            if (mOnNewsItemClickListener!=null){
                mOnNewsItemClickListener.onNewsItemClick(listDTO, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mListData!=null){
            return mListData.size();
        }
        else return 0;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView brief_news_img, mCheckBox;
        TextView brief_news_title;
        TextView brief_news_src;
        TextView brief_news_time;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            brief_news_img=itemView.findViewById(R.id.brief_news_img);
            brief_news_title=itemView.findViewById(R.id.brief_news_title);
            brief_news_src=itemView.findViewById(R.id.brief_news_src);
            brief_news_time=itemView.findViewById(R.id.brief_news_time);
            mCheckBox = itemView.findViewById(R.id.check_box);
        }
    }

    @Getter
    @Setter
    private onNewsItemClickListener mOnNewsItemClickListener;

    public interface onNewsItemClickListener{
        void onNewsItemClick(News.DataDTO.ListDTO listDTO, int position);
    }
}
