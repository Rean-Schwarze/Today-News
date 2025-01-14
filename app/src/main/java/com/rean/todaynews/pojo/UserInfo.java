package com.rean.todaynews.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Integer id;
    private String username;
    private String password;
    private String phone;
    private byte[] avatar;
    private String userdesc;
    private Integer type; // 0:普通用户 1:管理员

    @Setter
    @Getter
    public static UserInfo userInfo;

    public static void clearUserInfo() {
        userInfo = null;
    }
}
