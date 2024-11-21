package com.cia103g5.user.article.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "post") // 對應資料庫中的 post 表格
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主鍵自動生成
    private int postNo; // 文章的唯一識別碼

    private String title; // 文章標題
    private String authorName; // 作者名稱
    private String content; // 文章內容
    private LocalDateTime postTime; // 發文時間
    private LocalDateTime modAt; // 最後修改時間

    private int totalLikes; // 文章的按讚數
    private int totalDislikes; // 文章的點踩數
    private int favoritesCount; // 文章的收藏數量

    private byte userType; // 對應資料庫中的 TINYINT 欄位
    private int memId;     // 對應資料庫中的 INT 欄位

    @Transient // 標示 formattedModAt 欄位不會儲存到資料庫
    private String formattedModAt; // 格式化後的修改時間字串

    @ManyToOne // 多對一的關係，許多 Post 對應到一個 PostCategory
    @JoinColumn(name = "category_no") // 設定外鍵欄位名稱為 category_no
    private PostCategory postCategory; // 文章的分類

    @OneToMany(mappedBy = "postNo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostPic> postPics; // 儲存與此文章關聯的所有圖片
    
    private String preview;

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }


    // Getters 和 Setters

    public int getPostNo() { // 取得 postNo 的值
        return postNo;
    }

    public void setPostNo(int postNo) { // 設定 postNo 的值
        this.postNo = postNo;
    }

    public String getTitle() { // 取得 title 的值
        return title;
    }

    public void setTitle(String title) { // 設定 title 的值
        this.title = title;
    }

    public String getAuthorName() { // 取得 authorName 的值
        return authorName;
    }

    public void setAuthorName(String authorName) { // 設定 authorName 的值
        this.authorName = authorName;
    }

    public String getContent() { // 取得 content 的值
        return content;
    }

    public void setContent(String content) { // 設定 content 的值
        this.content = content;
    }

    public LocalDateTime getPostTime() { // 取得 postTime 的值
        return postTime;
    }

    public void setPostTime(LocalDateTime postTime) { // 設定 postTime 的值
        this.postTime = postTime;
    }

    public LocalDateTime getModAt() { // 取得 modAt 的值
        return modAt;
    }

    public void setModAt(LocalDateTime modAt) { // 設定 modAt 的值
        this.modAt = modAt;
    }

    public int getTotalLikes() { // 取得 totalLikes 的值
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) { // 設定 totalLikes 的值
        this.totalLikes = totalLikes;
    }

    public int getTotalDislikes() { // 取得 totalDislikes 的值
        return totalDislikes;
    }

    public void setTotalDislikes(int totalDislikes) { // 設定 totalDislikes 的值
        this.totalDislikes = totalDislikes;
    }

    public int getFavoritesCount() { // 取得 favoritesCount 的值
        return favoritesCount;
    }

    public void setFavoritesCount(int favoritesCount) { // 設定 favoritesCount 的值
        this.favoritesCount = favoritesCount;
    }

    public byte getUserType() { // 取得 userType 的值
        return userType;
    }

    public void setUserType(byte userType) { // 設定 userType 的值
        this.userType = userType;
    }

    public int getMemId() { // 取得 memId 的值
        return memId;
    }

    public void setMemId(int memId) { // 設定 memId 的值
        this.memId = memId;
    }

    public String getFormattedModAt() { // 取得格式化後的 modAt
        if (modAt == null) { // 如果 modAt 為空
            return ""; // 返回空字串
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 定義格式
        return modAt.format(formatter); // 以指定格式返回 modAt
    }

    public void setFormattedModAt(String formattedModAt) { // 設定 formattedModAt 的值
        this.formattedModAt = formattedModAt;
    }

    public PostCategory getPostCategory() { // 取得 postCategory 的值
        return postCategory;
    }

    public void setPostCategory(PostCategory postCategory) { // 設定 postCategory 的值
        this.postCategory = postCategory;
    }

    public List<PostPic> getPostPics() { // 取得與文章關聯的圖片列表
        return postPics;
    }

    public void setPostPics(List<PostPic> postPics) { // 設定與文章關聯的圖片列表
        this.postPics = postPics;
    }
}
