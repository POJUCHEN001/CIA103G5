package com.cia103g5.user.post.model;

import com.cia103g5.user.member.model.MemberVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


/**
 *
 * 這張已修正
 *
 */


@Getter
@Setter
@ToString
@Entity
@Table(name = "post") // 對應資料庫中的 post 表格
public class PostVO {

    @Id
    @Column(name = "post_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主鍵自動生成
    private int postNo; // 文章的唯一識別碼

    @Column(name = "user_type", columnDefinition = "TINYINT")
    private Integer userType; // 對應資料庫中的 TINYINT 欄位

    @ManyToOne
    @JoinColumn(name = "mem_id")
    private MemberVO memberVO;


    @ManyToOne
    @JoinColumn(name = "category_no")
    private PostCategoryVO postCategoryVO;

    @Column(name = "title")
    private String title; // 文章標題

    @Column(name = "author_name")
    private String authorName; // 作者名稱

    @Column(name = "post_time")
    private Date postTime; // 發文時間

    @Column(name = "mod_time")
    private Date modTime; // 最後修改時間

    @Column(name = "content", columnDefinition = "TEXT")
    private String content; // 文章內容

    private Integer view; // 瀏覽次數

    private Integer reply; // 按讚次數



}
