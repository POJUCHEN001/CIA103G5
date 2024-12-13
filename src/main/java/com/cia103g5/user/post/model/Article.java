package com.cia103g5.user.post.model;

//Article 類別是一個簡單的資料模型，通常被稱為 POJO（Plain Old Java Object），用於儲存和傳遞文章的資料。
public class Article {// 定義 Article 類別，用來表示一篇文章的基本資料
    private String title;// 定義私有欄位 title，儲存文章的標題
    private String content;// 定義私有欄位 content，儲存文章的內容
//將 title 和 content 設為私有欄位，符合封裝（encapsulation）原則，這樣可以限制外部對欄位的直接存取，並保護資料的一致性。
//    私有欄位需要透過 getter 和 setter 方法來讀取或修改值，這樣可以在設定值時進行控制和檢查。
    public Article(String title, String content) {// // 帶有參數的建構子，用於在建立物件時設置初始值
        this.title = title;// 初始化 title 欄位
        this.content = content;// 初始化 content 欄位
    }
    //提供一個帶參數的建構子，允許在建立 Article 物件時直接設定 title 和 content 的初始值。
    //使用 this.title = title 和 this.content = content 將傳入的參數值賦予對應的欄位。
    //		這樣的建構子使 Article 類別更具靈活性，方便在不同場景下創建預設值的文章物件。

    public String getTitle() {
        return title;
    }
    
    //Article 類別是 POJO，因為它是一個簡單的資料模型，用於儲存文章的基本資料

    public void setTitle(String title) {
        this.title = title;
    }
    //getTitle() 方法提供 title 欄位的讀取功能，允許外部獲取文章的標題。
    //setTitle(String title) 方法提供 title 欄位的修改功能，允許外部設置新的標題值。
    //使用 getter 和 setter 方法可以控制資料的存取，方便日後進行驗證或資料處理。
    
    
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

//getContent() 方法提供 content 欄位的讀取功能，允許外部獲取文章的內容。
//setContent(String content) 方法提供 content 欄位的修改功能，允許外部設置新的內容值。
//同樣，使用 getter 和 setter 方法可以控制欄位的存取，確保類別的欄位在被存取或修改時可以進行必要的控制。
//