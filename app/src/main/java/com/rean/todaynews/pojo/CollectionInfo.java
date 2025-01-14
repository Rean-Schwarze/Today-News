package com.rean.todaynews.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionInfo {
    private Integer collection_id;
    private Integer user_id;
    private String news_id;
    private String news_json;
    private String timestamp;
}
