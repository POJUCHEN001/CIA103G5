package com.cia103g5.user.post.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// 將此類別標記為一個 JPA 實體（Entity），這樣它可以映射到資料庫的表格
@Entity
@Table(name = "post_category") // 指定對應的資料表名稱為 "post_category"
public class PostCategory {

    // 指定 categoryNo 欄位為主鍵
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 使用自動生成的主鍵策略（IDENTITY）
    private int categoryNo; // 主鍵欄位，對應資料庫的 category_no 欄位

    // 定義資料庫的欄位，用來存放類別名稱
    private String categoryName; // 類別名稱，例如 "塔羅板" 或 "星座板"

    // Getter 和 Setter 方法用於訪問和設置類別的屬性
    public int getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(int categoryNo) {
        this.categoryNo = categoryNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

//PostCategory 類別是 POJO，因為它的用途是代表資料庫中的實體，而不是用於在不同層之間傳遞資料（這是 DTO 的用途）。
//使用 Spring Data JPA 來與資料庫互動檔案中 spring-boot-starter-data-jpa 的依賴關係可以確認。
//這種方式會將 JDBC 操作抽象化，讓您可以透過 JPA（Java Persistence API）來使用 repository
//（例如 PostRepository 和 PostCategoryRepository）與資料庫進行互動。

//實體（Entity）標記與資料表對應：
//
//@Entity 是 JPA 的標註，用來標記這個類別是一個 JPA 實體，會被映射到資料庫中。
//@Table(name = "post_category") 指定此類別應對應的資料表為 post_category，這樣 JPA 能夠在操作時找到正確的資料表。
//主鍵（Primary Key）定義：
//
//@Id 標記 categoryNo 是這張表的主鍵。
//@GeneratedValue(strategy = GenerationType.IDENTITY) 指定主鍵的生成策略為 IDENTITY，意思是資料庫會自動生成主鍵（通常是自增的整數）。
//原理：這樣的設定讓 JPA 自動在新增記錄時分配主鍵的值，而不需要我們手動設定。
//屬性欄位：
//
//private String categoryName 定義了資料表中的另一個欄位，儲存類別名稱（如 "塔羅板"）。
//類別名稱的值可以通過 getter 和 setter 方法訪問和修改。
//原理：使用 getter 和 setter 可以實現封裝，這樣可以確保 categoryName 的值只能透過這些方法進行操作。
//
//使用這種方式是因為 JPA 能夠讓我們以物件的方式操作資料，而不需要撰寫繁瑣的 SQL 語法。此外，主鍵生成策略的使用能自動處理主鍵值的生成，減少開發的錯誤並提升效能。