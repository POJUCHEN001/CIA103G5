package com.cia103g5.user.post.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cia103g5.user.ft.model.FtRepository;
import com.cia103g5.user.member.model.MemberRepository;
import com.cia103g5.user.member.model.MemberVO;
import com.cia103g5.user.post.model.Post;
import com.cia103g5.user.post.model.PostCategory;
import com.cia103g5.user.post.model.PostComment;
import com.cia103g5.user.post.model.PostPic;
import com.cia103g5.user.post.model.PostReport;
import com.cia103g5.user.post.repository.PostCategoryRepository;
import com.cia103g5.user.post.repository.PostCommentRepository;
import com.cia103g5.user.post.repository.PostPicRepository;
import com.cia103g5.user.post.repository.PostReportRepository;
import com.cia103g5.user.post.repository.PostRepository;
import com.cia103g5.user.post.service.FavPostService;
import com.cia103g5.user.post.service.PostService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/post") // 設定 API 路徑前綴為 /post
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostPicRepository postPicRepository;

    @Autowired
    private PostCategoryRepository postCategoryRepository;

    @Autowired
    private FavPostService favPostService;

    @Autowired
    private PostService postService;
    
	@Autowired
	private PostCommentRepository postCommentRepository;
	
	@Autowired
	private PostReportRepository postReportRepository;
	
	@Autowired
	private FtRepository ftRepository;
	
	@Autowired
	private MemberRepository memberRepository;


    // 發表新文章
    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post, HttpSession session) {
    	Integer memberId = (Integer) session.getAttribute("memberId");
    	
        try {
            // 預設 memId 和 userType
            post.setMemId(memberId);
            
			Optional<MemberVO> member = memberRepository.findById(memberId);
			if (member.isPresent()) {
				post.setAuthorName(member.get().getNickname());
			} else {
				post.setAuthorName(member.get().getName());	
			}
            
        	if (ftRepository.findFtIdByMemId(memberId).isPresent()) {
                post.setUserType((byte) 1);
        	} else {
                post.setUserType((byte) 0);
        	}

            // 檢查分類是否存在或創建新分類
            if (post.getPostCategory() != null && post.getPostCategory().getCategoryName() != null) {
                PostCategory category = postCategoryRepository
                        .findByCategoryName(post.getPostCategory().getCategoryName())
                        .orElseGet(() -> {
                            PostCategory newCategory = new PostCategory();
                            newCategory.setCategoryName(post.getPostCategory().getCategoryName());
                            return postCategoryRepository.save(newCategory);
                        });
                post.setPostCategory(category);
            }

            // 清理 HTML 標籤並限制字數
            String cleanContent = post.getContent() != null ? Jsoup.parse(post.getContent()).text() : "";
            if (cleanContent.length() > 30) {
                cleanContent = cleanContent.substring(0, 30) + "...";
            }
            post.setContent(cleanContent);

            LocalDateTime currentTime = LocalDateTime.now();
            // 設定發文時間            
            post.setPostTime(currentTime);
            
            // 設定修改文章時間，預設值為上面的發文時間
            post.setModTime(currentTime);
            
            // 設定文章status, 發文時預設為1
            post.setStatus((byte) 1);

            // 保存文章
            Post savedPost = postRepository.save(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

 // 上傳與文章相關的圖片
    @PostMapping("/posts/{postNo}/uploadPic")
    public ResponseEntity<String> uploadPic(@PathVariable("postNo") int postNo,
                                            @RequestParam("pic") MultipartFile file) {
        try {
            // 查詢文章是否存在
            Optional<Post> postOptional = postRepository.findById(postNo);
            if (!postOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("文章不存在，無法上傳圖片。");
            }

            // 確認檔案是否為空
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("請提供有效的圖片檔案。");
            }

            // 檢查圖片大小 (以 byte 為單位)
            long maxSizeInBytes = 1024 * 1024 * 16; // 16 MB，適合 longblob
            if (file.getSize() > maxSizeInBytes) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("圖片大小超出限制，請上傳小於 16 MB 的圖片。");
            }

            // 創建並保存圖片
            PostPic postPic = new PostPic();
            postPic.setPostNo(postNo);
            postPic.setPic(file.getBytes());
            postPicRepository.save(postPic);

            return ResponseEntity.ok("圖片上傳成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("圖片處理失敗！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("圖片上傳失敗！");
        }
    }


    // 根據文章編號查詢文章資訊
    @GetMapping("/posts/{postNo}")
    public ResponseEntity<Post> getPostById(@PathVariable("postNo") int postNo) {
        Optional<Post> post = postRepository.findById(postNo);
        if (post.isPresent()) {
            return ResponseEntity.ok(post.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 更新文章資訊
    @PutMapping("/posts/{postNo}")
    public ResponseEntity<String> updatePost(@PathVariable("postNo") int postNo, 
                                             @RequestBody Post updatedPost) {
        try {
            Optional<Post> existingPost = postRepository.findById(postNo);
            if (existingPost.isPresent()) {
                Post post = existingPost.get();
                post.setTitle(updatedPost.getTitle());

                // 清理內容並限制字數
                String cleanContent = updatedPost.getContent() != null ? Jsoup.parse(updatedPost.getContent()).text() : "";
                if (cleanContent.length() > 100) {
                    cleanContent = cleanContent.substring(0, 100) + "...";
                }
                post.setContent(cleanContent);

                post.setAuthorName(updatedPost.getAuthorName());
                post.setModTime(LocalDateTime.now());

                // 更新分類
                if (updatedPost.getPostCategory() != null && updatedPost.getPostCategory().getCategoryName() != null) {
                    PostCategory category = postCategoryRepository
                            .findByCategoryName(updatedPost.getPostCategory().getCategoryName())
                            .orElseGet(() -> {
                                PostCategory newCategory = new PostCategory();
                                newCategory.setCategoryName(updatedPost.getPostCategory().getCategoryName());
                                return postCategoryRepository.save(newCategory);
                            });
                    post.setPostCategory(category);
                }

                postRepository.save(post);
                return ResponseEntity.ok("文章更新成功！");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到此文章，無法更新！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新文章失敗！");
        }
    }

    // 刪除指定文章
    @DeleteMapping("/posts/{postNo}")
    public ResponseEntity<String> deletePost(@PathVariable("postNo") int postNo) {
        try {
            Optional<Post> existingPost = postRepository.findById(postNo);
            if (existingPost.isPresent()) {
            	List<PostPic> pics = postPicRepository.findByPostNo(postNo);
            	if (!pics.isEmpty()) {
            		postPicRepository.deleteAll(pics);
            	}
            	
            	List<PostComment> comments = postCommentRepository.findByPostNo(postNo);
            	if (!comments.isEmpty()) {
            		postCommentRepository.deleteAll(comments);
            	}
            	
            	List<PostReport> reports = postReportRepository.findByPostNo(postNo);
            	if (!reports.isEmpty()) {
            		postReportRepository.deleteAll(reports);
            	}
            	
                postRepository.deleteById(postNo);
                return ResponseEntity.ok("文章已刪除！");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到此文章，無法刪除！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("刪除文章失敗！");
        }
    }

    // 互動功能postComment
    @PostMapping("/favorite/{postNo}")
    public ResponseEntity<Void> toggleFavorite(@PathVariable int postNo) {
        favPostService.toggleFavorite(6, postNo); // 預設 memId:6
        return ResponseEntity.ok().build();
    }

    @PostMapping("/like/{postNo}")
    public ResponseEntity<Void> toggleLike(@PathVariable int postNo) {
        postService.toggleLike(6, postNo); // 預設 memId:6
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike/{postNo}")
    public ResponseEntity<Void> toggleDislike(@PathVariable int postNo) {
        postService.toggleDislike(6, postNo); // 預設 memId:6
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/comment/{postNo}")
    public ResponseEntity<Void> postComment(@PathVariable int postNo, @RequestBody PostComment newComment, HttpSession session) {
        Integer memId =(Integer)session.getAttribute("memberId");
    	postService.postComment(memId, postNo, newComment.getCommentText()); // 預設 memId:6
        return ResponseEntity.ok().build();
    }
    
    // 刪除指定留言
    @DeleteMapping("/comment/{commentNo}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentNo") int commentNo) {
        try {
            Optional<PostComment> existingComment = postCommentRepository.findById(commentNo);
            if (existingComment.isPresent()) {
            	postCommentRepository.deleteById(commentNo);
                return ResponseEntity.ok("留言已刪除！");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到此留言，無法刪除！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("留言文章失敗！");
        }
    }
    @PostMapping("/report")
    public ResponseEntity<Void> reportPost(@RequestBody PostReport report) {
        report.setMemId(6); // 預設會員 ID
        report.setReportedTime(LocalDateTime.now()); // 設定檢舉時間
        report.setStatus(0); // 狀態設為未處理
        report.setCompletedTime(LocalDateTime.now()); // 設定完成時間（可後續更新）

        postReportRepository.save(report); // 保存檢舉資料
        return ResponseEntity.ok().build();
    }

}
