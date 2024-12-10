package com.cia103g5.user.productCollection.controller;

import com.cia103g5.user.productCollection.model.ProductCollectionVO;
import com.cia103g5.user.productCollection.model.ProductCollectionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-collection")
public class ProductCollectionController {

    private final ProductCollectionService productCollectionService;

    @Autowired
    public ProductCollectionController(ProductCollectionService productCollectionService) {
        this.productCollectionService = productCollectionService;
    }

    @GetMapping("/product-collection")
    public String showProductCollectionPage(Model model, Integer memId) {
      
        List<ProductCollectionVO> collections = productCollectionService.findByMemId(memId);

      
        model.addAttribute("collections", collections);
        return "product-collection"; 
    }
}
