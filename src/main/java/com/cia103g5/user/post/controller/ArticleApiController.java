package com.cia103g5.user.post.controller;

//Post 是文章的模型類別，代表一篇文章。
//PostRepository 是用來與資料庫互動、管理 Post 資料的類別。

import java.util.List;//List 用於存放多筆 Post 物件。

import org.springframework.http.ResponseEntity;//ResponseEntity 用於封裝 HTTP 回應。
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
//GetMapping 和 PostMapping 分別標示 GET 和 POST 請求的處理方法。
import org.springframework.web.bind.annotation.RequestMapping;//RequestMapping 設定基礎路徑。
import org.springframework.web.bind.annotation.RequestParam;//RequestParam 用於從請求中取得參數。
import org.springframework.web.bind.annotation.RestController;//RestController 標示類別為 REST API 控制器。

import com.cia103g5.user.post.model.Post;
import com.cia103g5.user.post.repository.PostRepository;

@RestController
//使用 @RestController 註解，將 ArticleApiController 標示為 REST API 的控制器。這樣所有方法的回應都會自動轉為 JSON 格式。

@RequestMapping("/api") // @RequestMapping("/api") 設定這個控制器的基礎路徑為 /api，表示所有路由會以 /api 作為開頭。
class ArticleApiController {

    private final PostRepository postRepository;
//宣告 postRepository 屬性，類型為 PostRepository。此屬性負責操作 Post 資料。

    public ArticleApiController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
//定義建構子，並注入 PostRepository，允許控制器在內部使用 postRepository 來操作資料庫。
    @PostMapping("/articles")
    public ResponseEntity<?> createArticle(
        @RequestParam("title") String title,
        @RequestParam("author") String author,
        @RequestParam("content") String content
    ) 
//使用 @PostMapping("/articles") 設定此方法處理 /api/articles 路徑的 POST 請求，用於新增文章。
//方法參數使用 @RequestParam 來接收來自請求的參數，包括 title、author 和 content。
    
    
    {
        Post post = new Post();
        post.setTitle(title);
        post.setAuthorName(author);
        post.setContent(content);
//建立新的 Post 物件 post，並設定其標題、作者名稱和內容。這些資料來自請求的參數。
        postRepository.save(post);
//使用 postRepository.save(post) 將 post 物件儲存至資料庫中。        
        return ResponseEntity.ok("文章已成功儲存");
//回傳 ResponseEntity.ok("文章已成功儲存")，表示儲存成功，並返回成功訊息給客戶端。
    }

    @GetMapping("/articles")
//使用 @GetMapping("/articles") 註解，設定此方法處理 /api/articles 的 GET 請求，目的是取得所有文章。    
    public ResponseEntity<List<Post>> getAllArticles() {
        List<Post> articles = postRepository.findAll();
        System.out.println("Fetched articles: " + articles);
        return ResponseEntity.ok(articles);
// 呼叫 postRepository.findAll() 來查詢資料庫中所有的文章，並將結果存放在 articles 變數中。
//打印出文章列表以方便除錯。
// 使用 ResponseEntity.ok(articles) 回傳查詢結果（articles）給客戶端。        
    }
}
