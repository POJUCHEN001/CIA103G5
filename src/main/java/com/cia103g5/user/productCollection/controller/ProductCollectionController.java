package com.cia103g5.user.productCollection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cia103g5.user.productCollection.model.ProductCollectionServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/productcollection")
public class ProductCollectionController {

    private final ProductCollectionServiceImpl productCollectionService;

    @Autowired
    public ProductCollectionController(ProductCollectionServiceImpl productCollectionService) {
        this.productCollectionService = productCollectionService;
    }



   
    @PostMapping("/add/{memId}/{prodNo}")
    @ResponseBody
    public ResponseEntity<String> addProductToCollection(@PathVariable Integer prodNo, HttpSession session) {
        
        Integer memId = (Integer) session.getAttribute("userId");
        if (memId == null) {
            return ResponseEntity.status(401).body("請先登入再收藏商品");
        }

       
        if (productCollectionService.existsByProdNoAndMemId(prodNo, memId)) {
            return ResponseEntity.badRequest().body("商品已在收藏中");
        }
        productCollectionService.addCollection(memId, prodNo);
        return ResponseEntity.ok("商品已加入收藏");
    }


    @PostMapping("/remove/{memId}/{prodNo}")
    @ResponseBody
    public ResponseEntity<String> removeProductFromCollection(@PathVariable Integer prodNo, HttpSession session) {
      
        Integer memId = (Integer) session.getAttribute("userId");
        if (memId == null) {
            return ResponseEntity.status(401).body("請先登入再操作");
        }

       
        if (!productCollectionService.existsByProdNoAndMemId(prodNo, memId)) {
            return ResponseEntity.badRequest().body("商品不在收藏中");
        }
        productCollectionService.removeCollection(memId, prodNo);
        return ResponseEntity.ok("商品已從收藏中移除");
    }
}