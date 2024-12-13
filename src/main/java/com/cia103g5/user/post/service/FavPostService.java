package com.cia103g5.user.post.service;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cia103g5.user.post.model.FavPost;
import com.cia103g5.user.post.model.FavPostKey;
import com.cia103g5.user.post.repository.FavPostRepository;

@Service
public class FavPostService {

    @Autowired
    private FavPostRepository favPostRepository;

    /**
     * 收藏或取消收藏文章
     *
     * @param memId  會員編號
     * @param postNo 文章編號
     */
    public void toggleFavorite(int memId, int postNo) {
        // 使用複合主鍵來操作
        FavPostKey key = new FavPostKey(memId, postNo);
        Optional<FavPost> existingFavPost = favPostRepository.findById(key);

        if (existingFavPost.isPresent()) {
            // 如果已存在，刪除該收藏
            favPostRepository.deleteById(key);
        } else {
            // 如果不存在，新增收藏
            FavPost favPost = new FavPost();
            favPost.setId(key);
            favPost.setAddedTime(new Timestamp(System.currentTimeMillis()));
            favPostRepository.save(favPost);
        }
    }

    /**
     * 檢查是否已收藏某篇文章
     *
     * @param memId  會員編號
     * @param postNo 文章編號
     * @return 是否已收藏
     */
    public boolean isFavorited(int memId, int postNo) {
        FavPostKey key = new FavPostKey(memId, postNo);
        return favPostRepository.existsById(key);
    }
}
