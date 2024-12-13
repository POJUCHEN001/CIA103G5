package com.cia103g5.user.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cia103g5.user.post.model.FavPost;
import com.cia103g5.user.post.model.FavPostKey;

@Repository
public interface FavPostRepository extends JpaRepository<FavPost, FavPostKey> {

    // 根據複合主鍵查詢收藏紀錄
    Optional<FavPost> findById(FavPostKey id);

    // 刪除特定收藏紀錄
    void deleteById(FavPostKey id);

    // 檢查是否存在特定收藏
    boolean existsById(FavPostKey id);
}