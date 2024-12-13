package com.cia103g5.user.post.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "post_like") // 對應資料庫中的 post_like 表
public class PostLike {

    @EmbeddedId
    private FavPostKey id; // 使用獨立的 FavPostKey 類別作為複合主鍵

    @ManyToOne
    @JoinColumn(name = "post_no", referencedColumnName = "post_no", insertable = false, updatable = false) // 正確映射到 Post 類中的 post_no
    private Post post;

    @Column(name = "added_time", nullable = false)
    private Timestamp addedTime;

    // Getters and Setters
    public FavPostKey getId() {
        return id;
    }

    public void setId(FavPostKey id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Timestamp getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(Timestamp addedTime) {
        this.addedTime = addedTime;
    }
}
