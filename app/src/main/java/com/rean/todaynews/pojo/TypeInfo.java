package com.rean.todaynews.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TypeInfo {

    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("result")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("list")
        private List<List<ListDTO>> list;

        @NoArgsConstructor
        @Data
        public static class ListDTO {
            @SerializedName("colid")
            private Integer colid;
            @SerializedName("name")
            private String name;
            @SerializedName("nameid")
            private String nameid;
            @SerializedName("jianjie")
            private String jianjie;
        }
    }
}
