package com.cia103g5.user.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cia103g5.user.post.model.Post;
import com.cia103g5.user.post.model.PostCategory;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
//	// JpaRepository 已經提供基本的資料庫操作方法，不需要額外實作
	 List<Post> findByPostCategory(PostCategory postCategory);
	 List<Post> findByPostCategory_CategoryName(String categoryName);
	 List<Post> findByPostCategoryOrderByPostTimeDesc(PostCategory postCategory);

	 @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
	    List<Post> searchByKeyword(@Param("keyword") String keyword);
	 List<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword);
	 
}
