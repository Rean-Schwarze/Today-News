package com.rean.todaynews.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class NewsDetail {

    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("id")
        private Integer id;
        @SerializedName("items")
        private List<ItemsDTO> items;

        @NoArgsConstructor
        @Data
        public static class ItemsDTO {
            @SerializedName("type")
            private String type;
            @SerializedName("content")
            private String content;
            @SerializedName("imageUrl")
            private String imageUrl;
            @SerializedName("videoUrl")
            private Object videoUrl;
        }
    }
}
