package com.cia103g5.user;

import lombok.Getter;

/**###################################################
 #                                                   #
 #  身份識別（建議 使用）                               #
 #                                                   #
 # 用法：                                             #
 #  繼承 Identifiable 並實作                           #
 #    public UserIdentityEnum getIdentity() {        #
 #      return UserIdentityEnum.ADMIN;               #
 #          }                                        #
 #                                                   #
 ####################################################*/

@Getter
public enum UserIdentityEnum {

    /**
     * 管理員
     */
    ADMIN("ADMIN"),

    /**
     * 占卜師
     */
    FORTUNE_TELLER("FORTUNE_TELLER"),

    /**
     * 用戶
     */
    MEMBER("MEMBER");

    // 獲取名稱
    private final String name;

    // 構造器
    UserIdentityEnum(String name) {
        this.name = name;
    }


}
