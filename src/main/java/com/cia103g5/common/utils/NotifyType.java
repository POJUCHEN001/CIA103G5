package com.cia103g5.common.utils;


import lombok.Getter;

@Getter
public enum NotifyType {

    SYSTEM_UPDATE("系統更新"),
    MAINTENANCE_NOTICE("系統維護通知"),
    TERMS_UPDATED("服務條款更新"),
    ANNOUNCEMENT("公告通知"),
    PROMOTION("推廣或促銷活動"),

    RESERVE("預約"),

    ORDER("訂單");


    private final String description;
    NotifyType(String description) {
        this.description = description;
    }

}
