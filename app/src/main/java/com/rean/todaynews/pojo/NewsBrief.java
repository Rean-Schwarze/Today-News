package com.rean.todaynews.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class NewsBrief {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO implements Serializable {
        @JsonProperty("title")
        private String title;
        @JsonProperty("imgList")
        private List<String> imgList;
        @JsonProperty("source")
        private String source;
        @JsonProperty("newsId")
        private String newsId;
        @JsonProperty("digest")
        private String digest;
        @JsonProperty("postTime")
        private String postTime;
        @JsonProperty("videoList")
        private List<String> videoList;
    }
}
