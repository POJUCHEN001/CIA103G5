package com.cia103g5.user.post.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cia103g5.user.post.model.Post;
import com.cia103g5.user.post.model.PostComment;
import com.cia103g5.user.post.repository.PostCommentRepository;
import com.cia103g5.user.post.repository.PostRepository;

@Service
public class PostService {
	
    @Autowired
    private final PostRepository postRepository;
    
    @Autowired
    private PostCommentRepository postCommentRepository;
    
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    // 搜尋文章的具體實現
    public List<Post> searchByKeyword(String keyword) {
        // 調用 Repository 的查詢方法
        return postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
    }

    public List<Post> searchPosts(String keyword) {
        return postRepository.searchByKeyword(keyword);
    }
    
	// <Member ID, >
	final static int NO_COMMENT = 0;
	final static int LIKE = 1;
	final static int DISLIKE = 2;
	final static Map<Integer, Integer> mLikeMap= new HashMap<>();
	
    // 更新按讚數量
    public void updateLikes(int postNo, boolean isLike) {
        Post post = postRepository.findById(postNo).orElseThrow(() -> new RuntimeException("Post not found"));
        int currentLikes = post.getTotalLikes();
        post.setTotalLikes(isLike ? currentLikes + 1 : currentLikes - 1);
        postRepository.save(post);
    }

    // 更新倒讚數量
    public void updateDislikes(int postNo, boolean isDislike) {
        Post post = postRepository.findById(postNo).orElseThrow(() -> new RuntimeException("Post not found"));
        int currentDislikes = post.getTotalDislikes();
        post.setTotalDislikes(isDislike ? currentDislikes + 1 : currentDislikes - 1);
        postRepository.save(post);
    }
    
    public void toggleLike(int memId, int postNo) {
    	try {
    		updateLikeDislike(memId, postRepository.findById(postNo), LIKE);
    	} catch (RuntimeException e) {
    		throw new RuntimeException("找不到指定的文章，PostNo: " + postNo);
    	}
    }

    public void toggleDislike(int memId, int postNo) {
    	try {
    		updateLikeDislike(memId, postRepository.findById(postNo), DISLIKE);
    	} catch (RuntimeException e) {
    		throw new RuntimeException("找不到指定的文章，PostNo: " + postNo);
    	}
    }
    
	private void updateLikeDislike(int memId, Optional<Post> optionalPost, int clicked) {
        if (optionalPost.isPresent()) {
    		final int oldPref = mLikeMap.getOrDefault(memId, NO_COMMENT);
    		final Post post = optionalPost.get(); 
    		
    		switch (oldPref) {
    			case NO_COMMENT:
    				if (clicked == LIKE) {
    					post.setTotalLikes(post.getTotalLikes() + 1);
    					mLikeMap.put(memId, LIKE);
    				} else {
    					post.setTotalDislikes(post.getTotalDislikes() + 1);
    					mLikeMap.put(memId, DISLIKE);
    				}
    				postRepository.save(post);
    				break;
    			case LIKE:
    				if (clicked == LIKE) {
    					post.setTotalLikes(post.getTotalLikes() - 1);
    					mLikeMap.put(memId, NO_COMMENT);
    				} else {
  						post.setTotalLikes(post.getTotalLikes() - 1);
   						post.setTotalDislikes(post.getTotalDislikes() + 1);
    					mLikeMap.put(memId, DISLIKE);
    				}
    				postRepository.save(post);
    				break;
    			case DISLIKE:
    				if (clicked == LIKE) {
    					post.setTotalLikes(post.getTotalLikes() + 1);
    					post.setTotalDislikes(post.getTotalDislikes() - 1);
    					mLikeMap.put(memId, LIKE);
    				} else {
   						post.setTotalDislikes(post.getTotalDislikes() - 1);
    					mLikeMap.put(memId, NO_COMMENT);
    				}
    				postRepository.save(post);
    				break;
    			default:
    				System.out.println("updateLikeDislike error");
    		}
        } else {
        	throw new RuntimeException("updateLikeDislike fail");
        }
	}
	
    public void postComment(int memId, int postNo, String commentText) {
    	try {
    		System.out.println("postComment: memId:" + memId + ", postNo:" + postNo + ", commentText:" + commentText);
    		PostComment comment = new PostComment();
    		comment.setPostNo(postNo);
    		comment.setMemId(memId);
    		comment.setCommentText(commentText);
    		comment.setCreatedTime(LocalDateTime.now());
    		postCommentRepository.save(comment);
    	} catch (RuntimeException e) {
    		throw new RuntimeException("新增留言失敗，PostNo: " + postNo);
    	}
    }
}
