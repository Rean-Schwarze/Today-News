package com.rean.todaynews;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BriefNews {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
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
