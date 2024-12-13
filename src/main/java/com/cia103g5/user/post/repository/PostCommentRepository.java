package com.cia103g5.user.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cia103g5.user.post.model.PostComment;

/**
 * 定義 PostCommentRepository 介面，繼承 JpaRepository 以支援基本的增刪查改功能
 */
public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {
	List<PostComment> findByPostNo(int postNo);
    // JpaRepository 提供基本的資料庫操作方法，不需要額外寫程式碼
}
