package com.rean.todaynews.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class News {


    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("result")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("curpage")
        private Integer curpage;
        @SerializedName("allnum")
        private Integer allnum;
        @SerializedName("newslist")
        private List<ListDTO> list;

        @NoArgsConstructor
        @Data
        public static class ListDTO implements Serializable {
            @SerializedName("id")
            private String id;
            @SerializedName("ctime")
            private String ctime;
            @SerializedName("title")
            private String title;
            @SerializedName("description")
            private String description;
            @SerializedName("source")
            private String source;
            @SerializedName("picUrl")
            private String picUrl;
            @SerializedName("url")
            private String url;
            @SerializedName("isSelected")
            private boolean isSelected;
        }
    }
}
