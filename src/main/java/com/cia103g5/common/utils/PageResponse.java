package com.cia103g5.common.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**##############################
 #                              #
 #     資料進行 頁數封裝           #
 #     @param <T>               #
 #                              #
 ###############################*/

@Getter
@Setter
public class PageResponse<T> {
    // 總紀錄數據
    private int totalCount;
    // 總頁數
    private int totalPage;
    // 資料
    private List<T> list;
    // 當前頁數
    private int currentPage;
    // 每頁資料量
    private int rows;

    // 建構子
    public PageResponse(int totalCount, int totalPage, List<T> list, int currentPage, int rows) {
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.list = list;
        this.currentPage = currentPage;
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "PageBean{ " +
                "totalCount=" + totalCount +
                "totalPage=" + totalPage +
                "list=" + list +
                "currentPage=" + currentPage +
                "rows" + rows + " }";
    }

}
