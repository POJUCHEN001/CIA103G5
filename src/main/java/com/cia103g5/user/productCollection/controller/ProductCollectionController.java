package com.cia103g5.user.productCollection.controller;

import com.cia103g5.user.productCollection.model.ProductCollectionVO;
import com.cia103g5.user.productCollection.model.ProductCollectionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productcollection")
public class ProductCollectionController {

    private final ProductCollectionService productCollectionService;

    @Autowired
    public ProductCollectionController(ProductCollectionService productCollectionService) {
        this.productCollectionService = productCollectionService;
    }


    @GetMapping("/{memId}")
    public List<ProductCollectionVO> getProductCollections(@PathVariable Integer memId) {
        return productCollectionService.findByMemId(memId);
    }


    @PostMapping("/{memId}/{prodNo}")
    public ProductCollectionVO addProductToCollection(@PathVariable Integer memId, @PathVariable Integer prodNo) {
        return productCollectionService.addCollection(memId, prodNo);
    }


    @DeleteMapping("/{memId}/{prodNo}")
    public void removeProductFromCollection(@PathVariable Integer memId, @PathVariable Integer prodNo) {
        productCollectionService.removeCollection(memId, prodNo);
    }
  }

