package com.cia103g5.user.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cia103g5.user.post.model.PostCategory;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Integer> {
    Optional<PostCategory> findByCategoryName(String categoryName);
}
